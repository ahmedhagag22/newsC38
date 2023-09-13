package com.route.newsappc38sat.ui.home.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.new_app.ui.category.CategoryDataClass
import com.example.new_app.ui.newsDetails.NewsDetailsActivity
import com.google.android.material.tabs.TabLayout
import com.route.api.model.newsResponse.News
import com.route.api.model.sourcesResponse.Source
import com.route.newsappc38sat.databinding.FragmentNewsBinding
import com.route.newsappc38sat.ui.ViewError
import com.route.newsappc38sat.ui.showMessage

class NewsFragment : Fragment() {
    lateinit var viewBinding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel
    var pageSize = 20
    var curranPage = 1
    var isLoading = false
    lateinit var sourceObj: Source
    lateinit var category:CategoryDataClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentNewsBinding.inflate(
            inflater,
            container, false
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        viewModel.getNewsSources()
    }

    private fun initObservers() {

        viewModel.sourcesLiveData.observe(viewLifecycleOwner) { sources ->
            bindTabs(sources)
            getNews()
        }
        viewModel.newsLiveData.observe(viewLifecycleOwner) {
            adapter.bindNews(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
    }

    fun getNews() {
        viewModel.getNews(sourceId = sourceObj.id ?: "", pageSize = pageSize, page = curranPage)
        isLoading = false
    }

    val adapter = NewsAdapter()
    private fun initViews() {
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewBinding.recyclerView.adapter = adapter
        //pageSize on the Screen
        viewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var layoutManager = recyclerView.layoutManager as LinearLayoutManager
                var lastVisibleItemCount = layoutManager.findLastVisibleItemPosition()
                var totalItemCount = layoutManager.itemCount
                var visibleThreshold = 3
                if (!isLoading && totalItemCount - lastVisibleItemCount <= visibleThreshold) {
                    isLoading = true
                    curranPage++
                    getNews()
                }


            }
        })
        adapter.onNewsClick = object : NewsAdapter.OnNewsClick {
            override fun onItemClick(news: News?) {
                var intent = Intent(requireContext(), NewsDetailsActivity::class.java)
                intent.putExtra("news", news)
                startActivity(intent)
            }

        }
    }

    private fun bindTabs(sources: List<Source?>?) {
        if (sources == null) return
        sources.forEach { source ->
            val tab = viewBinding.tabLayout.newTab()
            tab.text = source?.name
            tab.tag = source
            viewBinding.tabLayout.addTab(tab)
            // margin for tab
            var layoutParams = LinearLayout.LayoutParams(tab.view.layoutParams)
            layoutParams.marginEnd = 12
            layoutParams.marginStart = 12
            layoutParams.topMargin = 18
            tab.view.layoutParams = layoutParams
        }
        viewBinding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    tab.text -> source name
                    val source = tab?.tag as Source
                    sourceObj = source
                    source.id?.let { viewModel.getNews(it, pageSize, curranPage) }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val source = tab?.tag as Source
                    sourceObj = source
                    source.id?.let { viewModel.getNews(it, pageSize, curranPage) }


                }
            }
        )
        viewBinding.tabLayout.getTabAt(0)?.select()


    }

    fun handleError(viewError: ViewError) {
        showMessage(message = viewError.message ?: viewError.throwable?.localizedMessage
        ?: "Something went wrong",
            posActionName = "try again",
            posAction = { dialogInterface, i ->
                dialogInterface.dismiss()
                viewError.onTryAgainClickListener?.onTryAgainClick()
            }, negActionName = "cancel",
            negAction = { dialogInterface, i ->
                dialogInterface.dismiss()
            })
    }
    companion object {
        fun getInstance(category: CategoryDataClass): NewsFragment {
            var newNewFragment = NewsFragment()
            newNewFragment.category=category
            return newNewFragment
        }
    }

}
