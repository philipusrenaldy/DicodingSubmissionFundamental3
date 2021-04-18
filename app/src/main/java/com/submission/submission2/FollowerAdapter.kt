package com.submission.submission2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submission.submission2.databinding.FragmentlayoutBinding

class FollowerAdapter(private val datalist: ArrayList<UserData>) :
    RecyclerView.Adapter<FollowerAdapter.Holder>()
{
    private lateinit var mainContext: Context

    inner class Holder(private val bind: FragmentlayoutBinding) : RecyclerView.ViewHolder(bind.root) {
        fun bind(data: UserData) {
            with(bind) {
                Glide.with(itemView.context)
                    .load(data.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(avatar)
                username.text = data.username
                name.text = data.name
                location.text = data.location
                itemView.setOnClickListener {
                    val userData = UserData(
                        data.username,
                        data.name,
                        data.avatar,
                        data.company,
                        data.location,
                        data.repository,
                        data.followers,
                        data.following
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
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}