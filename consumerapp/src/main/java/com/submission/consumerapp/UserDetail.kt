package com.submission.consumerapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.submission.consumerapp.databinding.ActivityDetailBinding
import com.submission.consumerapp.db.DBContract
import com.submission.consumerapp.db.DBContract.favColumns.Companion.CONTENT_URI
import com.submission.consumerapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetail : AppCompatActivity(), View.OnClickListener {

    private lateinit var bind: ActivityDetailBinding
    private var isFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()
        setData()
        loadFavorite()
        viewPage()
        bind.btnFavorite.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        val userData = intent.getParcelableExtra<DataUser>(EXTRA_DATA) as DataUser
        if (v?.id == R.id.btnFavorite){
            val values = ContentValues()
            if (isFav) {
                isFav = false
            } else {
                isFav = true
                values.put(DBContract.favColumns.ID, userData.id)
                values.put(DBContract.favColumns.USERNAME, userData.username)
                values.put(DBContract.favColumns.NAME, userData.name)
                values.put(DBContract.favColumns.AVATAR, userData.avatar)
                values.put(DBContract.favColumns.COMPANY, userData.username)
                values.put(DBContract.favColumns.LOCATION, userData.company)
                values.put(DBContract.favColumns.REPOSITORY, userData.repository)
                contentResolver.insert(CONTENT_URI, values)
            }
        }
        loadFavorite()
    }

    private fun loadFavorite() {
        val userData = intent.getParcelableExtra<DataUser>(EXTRA_DATA) as DataUser
        GlobalScope.launch(Dispatchers.Main){
            val deferredFav = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favUser = deferredFav.await()
            favUser.forEach {
                if(it.username ==  userData.username)
                    isFav = true
            }
            chechFav()
        }
    }

    private fun chechFav() {
        if (isFav)
            bind.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        else
            bind.btnFavorite.setImageResource(R.drawable.ic_baseline_unfavorite_24)
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
        val userdata = intent.getParcelableExtra<DataUser>(EXTRA_DATA) as DataUser
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