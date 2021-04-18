package com.submission.submission2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.submission.submission2.databinding.ActivityMainBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    private var userlist: ArrayList<DataUser> = ArrayList()
    private lateinit var homeadapter: MainAdapter
    private val token = BuildConfig.GITHUB_TOKEN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        rvAction()
        fundUser()
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this, UserFavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return true
        }
    }

    private fun rvAction() {
        homeadapter = MainAdapter(userlist)
        homeadapter.notifyDataSetChanged()
        bind.loading.visibility = View.VISIBLE
        bind.recycler.layoutManager = LinearLayoutManager(this)
        bind.recycler.setHasFixedSize(true)
        bind.recycler.addItemDecoration(
                DividerItemDecoration(
                        bind.recycler.context,
                        DividerItemDecoration.VERTICAL
                )
        )
    }

    private fun getData() {
        bind.loading.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", token)
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d(TAG, result)
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getDetail(username)
                    }
                    bind.loading.visibility = View.INVISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                bind.loading.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fundUser() {
        bind.searchcontent.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    userlist.clear()
                    dataSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun dataSearch(id: String) {
        bind.loading.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", token)
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                bind.loading.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                bind.loading.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getDetail(id: String) {
        bind.loading.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", token)
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                bind.loading.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val id = jsonObject.getInt("id")
                    val username = jsonObject.getString("login").toString()
                    val name = jsonObject.getString("name").toString()
                    val avatar = jsonObject.getString("avatar_url").toString()
                    val company = jsonObject.getString("company").toString()
                    val location = jsonObject.getString("location").toString()
                    val repository = jsonObject.getString("public_repos")
                    val followers = jsonObject.getString("followers")
                    val following = jsonObject.getString("following")
                    userlist.add(
                            DataUser(
                                    id,
                                    username,
                                    name,
                                    avatar,
                                    company,
                                    location,
                                    repository,
                                    followers,
                                    following
                            )
                    )
                    rvList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                bind.loading.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun rvList() {
        bind.recycler.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = MainAdapter(userlist)
        bind.recycler.adapter = homeadapter

        listDataAdapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUser: DataUser) {
                userSelect(dataUser)
            }
        })
    }

    private fun userSelect(dataUser: DataUser) {
        DataUser(
                dataUser.id,
                dataUser.username,
                dataUser.name,
                dataUser.avatar,
                dataUser.company,
                dataUser.location,
                dataUser.repository,
                dataUser.followers,
                dataUser.following
        )
        val intent = Intent(this@MainActivity,UserDetail::class.java)
        intent.putExtra(UserDetail.EXTRA_DATA, dataUser)
        this.startActivity(intent)
        this@MainActivity.startActivity(intent)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}