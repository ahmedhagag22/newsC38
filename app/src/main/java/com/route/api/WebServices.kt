package com.route.api

import com.route.api.model.newsResponse.NewsResponse
import com.route.api.model.sourcesResponse.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//apiKey=5909ae28122a471d8b0c237d5989cb73
interface WebServices {
    @GET("v2/top-headlines/sources")
    fun getSources(
        @Query("apiKey") key: String = ApiConstants.apiKey,
        //@Query("category") category:String

    ): Call<SourcesResponse>

    @GET("v2/everything")
    fun getNews(
        @Query("apiKey") key: String = ApiConstants.apiKey,
        @Query("sources") sources: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): Call<NewsResponse>


}