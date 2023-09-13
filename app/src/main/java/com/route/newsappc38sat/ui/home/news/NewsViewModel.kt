package com.route.newsappc38sat.ui.home.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.route.api.ApiManager
import com.route.api.model.newsResponse.News
import com.route.api.model.newsResponse.NewsResponse
import com.route.api.model.sourcesResponse.Source
import com.route.api.model.sourcesResponse.SourcesResponse
import com.route.newsappc38sat.ui.ViewError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    val shouldShowLoading = MutableLiveData<Boolean>()
    val sourcesLiveData = MutableLiveData<List<Source?>?>()
    val newsLiveData = MutableLiveData<List<News?>?>()
    val errorLiveData = MutableLiveData<ViewError>()
    fun getNewsSources() {
        //     shouldShowLoading.value =true
//        shouldShowLoading.set(true)
//        shouldShowLoading.get()
        shouldShowLoading.postValue(true)
        ApiManager.getApis()
            .getSources()
            .enqueue(object : Callback<SourcesResponse> {
                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    shouldShowLoading.postValue(false)
                    errorLiveData.postValue(
                        ViewError(
                            throwable = t
                        ) {
                            getNewsSources()
                        }
                    )
                }

                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    shouldShowLoading.postValue(false)
                    if (response.isSuccessful) {
                        sourcesLiveData.postValue(response.body()?.sources)
                    } else {
                        val errorBodyJsonString = response.errorBody()?.string()
                        val response =
                            Gson().fromJson(errorBodyJsonString, SourcesResponse::class.java)
                        errorLiveData.postValue(ViewError(
                            message = response.message
                        ) {
                            getNewsSources()
                        })
                    }

                }
            })
    }

    fun getNews(sourceId: String, pageSize: Int, page: Int) {
        shouldShowLoading.postValue(true)
        ApiManager
            .getApis()
            .getNews(sources = sourceId ?: "", pageSize = pageSize, page = page)
            .enqueue(object : Callback<NewsResponse> {
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    shouldShowLoading.postValue(false)
                    errorLiveData.postValue(
                        ViewError(
                            throwable = t
                        ) { getNews(sourceId,pageSize,page) }
                    )
                }

                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    shouldShowLoading.postValue(false)
                    if (response.isSuccessful) {
                        // show news
                        newsLiveData.postValue(response.body()?.articles)
                        return
                    }
                    val responseJsonError = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(responseJsonError, NewsResponse::class.java)
                    errorLiveData.postValue(
                        ViewError(
                            message = errorResponse.message
                        ) { getNews(sourceId,pageSize,page) }
                    )
                }
            })
    }


}