package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.activities.MyPostActivity
import com.example.myapplication.api.response.post.Data
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ItemImageBinding


class OwnPostAdapter(val mContext: Context,val data: ArrayList<Data>,val id:String) :
    RecyclerView.Adapter<OwnPostAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_image, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(mContext).load(data[position].post_image).into(holder.binding.ivGallery)

        holder.binding.root.setOnClickListener {
            val intent =Intent(mContext, MyPostActivity::class.java)
            intent.putExtra("pos", position)
            intent.putExtra("id",id)
            mContext.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return data.size
    }


}