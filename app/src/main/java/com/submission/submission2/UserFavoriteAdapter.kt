package com.submission.submission2

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.submission2.databinding.LayoutBinding

class UserFavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<UserFavoriteAdapter.Holder>() {
    var favUser = ArrayList<DataUser>()
    set(favUser) {
        if (favUser.size > 0){
            this.favUser.clear()
        }
        this.favUser.addAll(favUser)
        notifyDataSetChanged()
    }

    inner class Holder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bind = LayoutBinding.bind(itemView)
        fun bind(dataUser: DataUser) {
            with(bind) {
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
                    val intent = Intent(activity, UserDetail::class.java)
                    intent.putExtra(UserDetail.EXTRA_DATA, userdata)
                    activity.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFavoriteAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: UserFavoriteAdapter.Holder, position: Int) {
        holder.bind(favUser[position])
    }

    override fun getItemCount(): Int {
        return favUser.size
    }

}