package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.activities.BusinessDetailActivity
import com.example.myapplication.api.response.business.Data
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.RvTopBusinessBinding

class TopBusinessAdapter (val mContext: Context) :
    RecyclerView.Adapter<TopBusinessAdapter.ViewHolder>(){

    var list=ArrayList<Data>()
        set(value) {
            field=value
        }
    inner class ViewHolder(val binding: RvTopBusinessBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RvTopBusinessBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rv_top_business, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(mContext)
            .load(list[position].logo)
            .placeholder(R.drawable.discoveraddislogo_new)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.binding.ivGallery)



        holder.binding.root.setOnClickListener {
            /* val intent=Intent(mContext,UpdateBusinessActivity::class.java)
             intent.putExtra("data",list[position])
             mContext.startActivity(intent)*/
           // callbacks.onClick(position,list[position].id.toString(),list[position].businessphone.toString())
            val intent= Intent(mContext, BusinessDetailActivity::class.java)
            intent.putExtra("data",list[position])
            mContext.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}