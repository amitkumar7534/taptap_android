package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ItemChatBinding
import com.yookoo.discoveraddis.databinding.IvFollowRequestBinding

class FollowRequestAdapter (val mContext: Context) : RecyclerView.Adapter<FollowRequestAdapter.ViewHolder>(){
    var list=ArrayList<String>()
        set(value) {
            field=value
        }
    inner class ViewHolder(val binding: IvFollowRequestBinding): RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<IvFollowRequestBinding>(
            LayoutInflater.from(parent.context),
            R.layout.iv_follow_request, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        /*        Glide.with(mContext).load(list[position].wallpaper).placeholder(R.drawable.dummy_category)
                    .into(holder.binding.ivWallpaper)*/



    }

    override fun getItemCount(): Int {

        return 4

    }
}