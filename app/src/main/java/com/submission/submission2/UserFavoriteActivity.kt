package com.submission.submission2

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.submission2.databinding.ActivityUserFavoriteBinding
import com.submission.submission2.db.DBContract.favColumns.Companion.CONTENT_URI
import com.submission.submission2.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: UserFavoriteAdapter
    private lateinit var binding: ActivityUserFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBarTitle()

        binding.favRecycler.layoutManager = LinearLayoutManager(this)
        binding.favRecycler.setHasFixedSize(true)
        adapter = UserFavoriteAdapter(this)
        binding.favRecycler.adapter= adapter

        val handlerThread = HandlerThread("ObserveData")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val contentObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFav()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, contentObserver)

        if (savedInstanceState == null){
            loadFav()
        }
        else{
            val dataList = savedInstanceState.getParcelableArrayList<DataUser>(EXTRA_STATE)
            if (dataList != null) {
                adapter.favUser = dataList
            }
        }
    }

    private fun loadFav() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.loadingFavorite.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO){
                val cursor = contentResolver?.query(CONTENT_URI, null,null,null,null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredNotes.await()
            binding.loadingFavorite.visibility = View.INVISIBLE
            if (favData.size > 0){
                adapter.favUser = favData
            }
            else{
                adapter.favUser = ArrayList()
                Toast.makeText(this@UserFavoriteActivity,"Favorite Empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setActionBarTitle() {
        if (supportActionBar != null) {
            supportActionBar?.title = "User's Favorite"
        }
    }

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
}