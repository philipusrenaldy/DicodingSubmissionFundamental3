package com.submission.consumerapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.consumerapp.databinding.FragmentlayoutBinding

class FollowerAdapter(private val dataList: ArrayList<DataUser>) : RecyclerView.Adapter<FollowerAdapter.Holder>()
{
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
                    val intent = Intent(itemView.context, UserDetail::class.java)
                    intent.putExtra(UserDetail.EXTRA_DATA, userData)
                    itemView.context.startActivity(intent)
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