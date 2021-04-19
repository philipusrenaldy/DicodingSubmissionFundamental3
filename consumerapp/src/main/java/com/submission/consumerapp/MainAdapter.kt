package com.submission.consumerapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submission.consumerapp.databinding.LayoutBinding
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter (private var userlist: ArrayList<DataUser>) :
    RecyclerView.Adapter<MainAdapter.Holder>(), Filterable
{
    private var userFilterList = ArrayList<DataUser>()
    private lateinit var maincontext: Context
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUser: DataUser)
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
                    val result = ArrayList<DataUser>()
                    for (row in userFilterList) {
                        if ((row.username.toString().toLowerCase(Locale.ROOT)
                                .contains(search.toLowerCase(Locale.ROOT)))
                        ) {
                            result.add(
                                DataUser(
                                        row.id,
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
                userFilterList = results.values as ArrayList<DataUser>
                notifyDataSetChanged()
            }
        }
    }

    inner class Holder(private val binding: LayoutBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(dataUser: DataUser) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(dataUser.avatar)
                    .into(avatar)
                username.text = dataUser.username
                name.text = dataUser.name
                location.text = dataUser.location
                itemView.setOnClickListener {
                    val userdata = DataUser(
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
                    val intent = Intent(maincontext,UserDetail::class.java)
                    intent.putExtra(UserDetail.EXTRA_DATA, userdata)
                    maincontext.startActivity(intent)
                }
            }
        }
    }
}