package com.submission.submission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.submission.submission2.databinding.ActivityDetailBinding

class UserDetail : AppCompatActivity() {

    private lateinit var bind: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setData()
        viewPage()
    }

    private fun viewPage() {
        val pageAdapter = PageSelectorAdapter(this)
        val blankPage: ViewPager2 = findViewById(R.id.vpFragmentPage)
        blankPage.adapter = pageAdapter
        val tabs: TabLayout = findViewById(R.id.tabsSelection)
        TabLayoutMediator(tabs, blankPage) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0F
    }

    private fun setData() {
        val userdata = intent.getParcelableExtra<UserData>(EXTRA_DATA) as UserData
        userdata.name?.let { setActionBarTitle(it) }
        bind.tvDetailName.text = userdata.name
        bind.tvDetailUsername.text = userdata.username
        bind.tvDetailCompany.text = getString(R.string.company, userdata.company)
        bind.tvDetailLocation.text = getString(R.string.location, userdata.location)
        bind.tvRepository.text = getString(R.string.repository, userdata.repository)
        Glide.with(this)
            .load(userdata.avatar)
            .into(bind.ivDetailAvatar)
    }

    private fun setActionBarTitle(it: String) {
        if(supportActionBar != null){
            this.title = it
        }
    }

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
    }
}