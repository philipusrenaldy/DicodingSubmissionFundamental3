package com.submission.consumerapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.submission.consumerapp.databinding.FragmentFollowerBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowerFragment : Fragment() {

    private var datalist: ArrayList<DataUser> = ArrayList()
    private lateinit var bind: FragmentFollowerBinding
    private lateinit var adapter: FollowerAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        bind = FragmentFollowerBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowerAdapter(datalist)
        datalist.clear()
        val userdata = activity?.intent?.getParcelableExtra<DataUser>(EXTRA_DATA) as DataUser
        getData(userdata.username.toString())
    }

    private fun getData(id: String)
    {
        bind.loadingfollowers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$id/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                bind.loadingfollowers.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    bind.recyclerfollower.visibility = View.VISIBLE
                    val jsonArray = JSONArray(result)
                    if (jsonArray.length() == 0) {
                        bind.recyclerfollower.visibility = View.GONE
                    } else {
                        bind.recyclerfollower.visibility = View.VISIBLE
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val username: String = jsonObject.getString("login")
                            getDetail(username)
                        }
                    }

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                bind.loadingfollowers.visibility = View.INVISIBLE
                bind.recyclerfollower.visibility = View.GONE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getDetail(id: String) {
        bind.loadingfollowers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                bind.loadingfollowers.visibility = View.INVISIBLE
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
                    datalist.add(
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
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                bind.loadingfollowers.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun rvList() {
        bind.recyclerfollower.layoutManager = LinearLayoutManager(activity)
        val listDataAdapter = MainAdapter(datalist)
        bind.recyclerfollower.adapter = adapter
        listDataAdapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUser: DataUser) {
                //No Implementation
            }
        })
    }

    companion object {
        private val TAG = FollowerFragment::class.java.simpleName
        const val EXTRA_DATA = "EXTRA_DATA"
    }
}