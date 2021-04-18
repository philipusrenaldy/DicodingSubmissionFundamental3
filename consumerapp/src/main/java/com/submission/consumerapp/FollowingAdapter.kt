package com.submission.consumerapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submission.consumerapp.databinding.FragmentlayoutBinding

class FollowingAdapter(private val datalist: ArrayList<DataUser>) : RecyclerView.Adapter<FollowingAdapter.Holder>()
{
    private lateinit var maincontext: Context

    inner class Holder(private val bind: FragmentlayoutBinding) : RecyclerView.ViewHolder(bind.root) {
        fun bind(dataUser: DataUser) {
            with(bind) {
                Glide.with(itemView.context)
                    .load(dataUser.avatar)
                    .apply(RequestOptions().override(55, 55))
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
                    Toast.makeText(maincontext, dataUser.username, Toast.LENGTH_SHORT).show()
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