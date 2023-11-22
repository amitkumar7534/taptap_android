package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.activities.BusinessDetailActivity
import com.example.myapplication.activities.UpdateBusinessActivity
import com.example.myapplication.api.response.business.Data
import com.example.myapplication.ui.callback.ViewClickCallback
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.RvMyBusinessBinding

class MyBusinessAdapter(val mContext: Context,
    val data: List<Data>, val callbacks: ViewClickCallback) :
    RecyclerView.Adapter<MyBusinessAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: RvMyBusinessBinding): RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RvMyBusinessBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rv_my_business, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(mContext)
            .load(data[position].logo)
            .override(200,200)
            .placeholder(R.drawable.discoveraddislogo_new)
            .into(holder.binding.bookedmenuPicImg)

        holder.binding.bookedmenuNameInc.setText(data[position].businessname)
        holder.binding.arrivingusernameInc.setText(data[position].businessphone)
        holder.binding.bookerarrivaldatetimeInc.setText(data[position].businessspecializesin)

        holder.binding.rlMain.setOnClickListener {
            /* val intent=Intent(mContext,UpdateBusinessActivity::class.java)
             intent.putExtra("data",list[position])
             mContext.startActivity(intent)*/

            val intent= Intent(mContext, BusinessDetailActivity::class.java)
            intent.putExtra("data",data[position])
            mContext.startActivity(intent)
            callbacks.onClick(position,data[position].id.toString(),data[position].businessphone)

        }

        holder.binding.edit.setOnClickListener {
            val intent= Intent(mContext, UpdateBusinessActivity::class.java)
            intent.putExtra("data",data[position])
            mContext.startActivity(intent)
            /* val intent=Intent(mContext,BusinessDetailActivity::class.java)
             intent.putExtra("data",list[position])
             mContext.startActivity(intent)*/
        }

        holder.binding.delete.setOnClickListener {
            callbacks.onClick(position,data[position].id.toString(),"delete")
        }

    }


    override fun getItemCount(): Int {
        return data.size
    }
}