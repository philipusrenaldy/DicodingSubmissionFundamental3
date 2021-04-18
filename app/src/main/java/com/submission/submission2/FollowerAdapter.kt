package com.submission.submission2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.submission2.databinding.FragmentlayoutBinding

class FollowerAdapter(private val dataList: ArrayList<DataUser>) :
    RecyclerView.Adapter<FollowerAdapter.Holder>()
{
    private lateinit var mainContext: Context

    inner class Holder(private val bind: FragmentlayoutBinding) : RecyclerView.ViewHolder(bind.root) {
        fun bind(dataUser: DataUser) {
            with(bind) {
                Glide.with(itemView.context)
                    .load(dataUser.avatar)
                    .into(avatar)
                username.text = dataUser.username
                name.text = dataUser.name
                location.text = dataUser.location
                itemView.setOnClickListener {
                    val userData = DataUser(
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
                    val intent = Intent(mainContext, UserDetail::class.java)
                    intent.putExtra(UserDetail.EXTRA_DATA, userData)
                    mainContext.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerAdapter.Holder {
        val bind = FragmentlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: FollowerAdapter.Holder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}