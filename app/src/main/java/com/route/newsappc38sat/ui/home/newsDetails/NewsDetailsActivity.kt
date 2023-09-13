package com.example.new_app.ui.newsDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil


import com.route.api.model.newsResponse.News
import com.route.newsappc38sat.R
import com.route.newsappc38sat.databinding.ActivityNewsDetailsBinding

class NewsDetailsActivity : AppCompatActivity() {
    lateinit var viewBinding:ActivityNewsDetailsBinding
    private lateinit var news :News
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=DataBindingUtil.setContentView(this, R.layout.activity_news_details)
             news=((intent.getParcelableExtra("news") as? News)!!)
        viewBinding.newsData= news


    }
}