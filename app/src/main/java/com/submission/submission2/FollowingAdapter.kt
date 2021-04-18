package com.submission.submission2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submission.submission2.databinding.FragmentlayoutBinding

class FollowingAdapter(private val datalist: ArrayList<UserData>) : RecyclerView.Adapter<FollowingAdapter.Holder>()
{
    private lateinit var maincontext: Context

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
                    Toast.makeText(maincontext, data.username, Toast.LENGTH_SHORT).show()
                    val intent = Intent(maincontext, UserDetail::class.java)
                    intent.putExtra(UserDetail.EXTRA_DATA, userdata)
                    maincontext.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingAdapter.Holder {
        val bind = FragmentlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: FollowingAdapter.Holder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}