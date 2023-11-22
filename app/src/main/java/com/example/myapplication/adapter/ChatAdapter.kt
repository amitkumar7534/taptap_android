package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyjob.ui.chat.MessagesActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ItemChatBinding


class ChatAdapter(val mContext: Context) :RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    var list=ArrayList<String>()
        set(value) {
            field=value
        }
    inner class ViewHolder(val binding: ItemChatBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemChatBinding>(LayoutInflater.from(parent.context),
            R.layout.item_chat, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



/*
        Glide.with(mContext).load(list[position].wallpaper).placeholder(R.drawable.dummy_category)
            .into(holder.binding.ivWallpaper)*/



        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, MessagesActivity::class.java)
            mContext.startActivity(intent)
        }




    }

    override fun getItemCount(): Int {

             return 4

    }
}