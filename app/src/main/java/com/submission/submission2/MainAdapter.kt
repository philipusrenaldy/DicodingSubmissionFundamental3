package com.submission.submission2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submission.submission2.databinding.LayoutBinding
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter (private var userlist: ArrayList<UserData>) :
    RecyclerView.Adapter<MainAdapter.Holder>(), Filterable
{
    private var userFilterList = ArrayList<UserData>()
    private lateinit var maincontext: Context
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val bind = LayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        maincontext = parent.context
        return Holder(bind)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(userlist[position])
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val search = constraint.toString()
                userFilterList = if (search.isEmpty()){
                    userlist
                } else {
                    val result = ArrayList<UserData>()
                    for (row in userFilterList) {
                        if ((row.username.toString().toLowerCase(Locale.ROOT)
                                .contains(search.toLowerCase(Locale.ROOT)))
                        ) {
                            result.add(
                                UserData(
                                    row.username,
                                    row.name,
                                    row.avatar,
                                    row.company,
                                    row.location,
                                    row.repository,
                                    row.followers,
                                    row.following
                                )
                            )
                        }
                    }
                    result
                }
                val filterresults = FilterResults()
                filterresults.values = userFilterList
                return filterresults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                userFilterList = results.values as ArrayList<UserData>
                notifyDataSetChanged()
            }
        }
    }

    inner class Holder(private val binding: LayoutBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(data: UserData) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(avatar)
                username.text = data.username
                name.text = data.name
                location.text = data.location
                itemView.setOnClickListener {
                    val userdata = UserData(
                        data.username,
                        data.name,
                        data.avatar,
                        data.company,
                        data.location,
                        data.repository,
                        data.followers,
                        data.following
                    )
                    val intent = Intent(maincontext,UserDetail::class.java)
                    intent.putExtra(UserDetail.EXTRA_DATA, userdata)
                    maincontext.startActivity(intent)
                }
            }
        }
    }
}