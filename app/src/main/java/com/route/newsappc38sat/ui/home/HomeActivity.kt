package com.route.newsappc38sat.ui.home

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.new_app.ui.category.CategoryDataClass
import com.example.new_app.ui.category.CategoryFragment

import com.route.newsappc38sat.R
import com.route.newsappc38sat.databinding.ActivityHomeBinding
import com.route.newsappc38sat.ui.home.news.NewsFragment
import com.route.newsappc38sat.ui.home.setting.SettingFragment


class HomeActivity : AppCompatActivity(), CategoryFragment.OnCategoryClickListener {
    override fun onCategoryClick(category: CategoryDataClass) {
        showCategoryDetilsFragment(category)
    }

    lateinit var viewBinding: ActivityHomeBinding

    val categoryFragment = CategoryFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        //السطر اللي هيشغل الكليك
        categoryFragment.onCategoryClickListener = this
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, categoryFragment)
            .commit()

        showToggleNavDrawer()

    }

    fun showCategoryDetilsFragment(category: CategoryDataClass) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, NewsFragment.getInstance(category))
            .commit()

    }

    fun showToggleNavDrawer() {
        var toggle = ActionBarDrawerToggle(
            this, viewBinding.myDrawerLayout, viewBinding.toolbar,
            R.string.nav_open, R.string.nav_close
        )
        viewBinding.myDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewBinding.navView.setNavigationItemSelectedListener { items ->
            when (items.itemId) {
                R.id.nav_category -> {
                    showCategoryFragment()
                }

                R.id.nav_setting -> {
                    showSettingsFragment()

                }
            }

            viewBinding.myDrawerLayout.closeDrawers()

            return@setNavigationItemSelectedListener true

        }

    }

    fun showCategoryFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, categoryFragment)
            .addToBackStack(null)
            .commit()
        viewBinding.txtTollbar.text = "News App"
    }

    fun showSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,SettingFragment())
            .addToBackStack(null)
            .commit()
        viewBinding.txtTollbar.text = "Settings"

    }

}