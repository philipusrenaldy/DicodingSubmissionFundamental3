package com.submission.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.consumerapp.databinding.ActivityMainBinding
import com.submission.consumerapp.db.DBContract.favColumns.Companion.CONTENT_URI
import com.submission.consumerapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserFavoriteAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBarTitle()

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.setHasFixedSize(true)
        adapter = UserFavoriteAdapter(this)
        binding.recycler.adapter= adapter

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
            binding.loading.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO){
                val cursor = contentResolver?.query(CONTENT_URI, null,null,null,null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredNotes.await()
            binding.loading.visibility = View.INVISIBLE
            if (favData.size > 0){
                adapter.favUser = favData
            }
            else{
                adapter.favUser = ArrayList()
                Toast.makeText(this@MainActivity,"Favorite Empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.favUser)
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