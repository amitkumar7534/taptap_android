package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.activities.BusinessDetailActivity
import com.example.myapplication.activities.UpdateBusinessActivity
import com.yookoo.discoveraddis.R
import com.example.myapplication.api.response.business.Data
import com.example.myapplication.api.response.business.DistanceResponse
import com.yookoo.discoveraddis.databinding.BusinessLayoutBinding
import com.example.myapplication.ui.callback.ViewClickCallback

class BusinessesAdapter(val mContext:Context,val distance:Boolean,val distacdeList: ArrayList<DistanceResponse>, val callbacks: ViewClickCallback) :RecyclerView.Adapter<BusinessesAdapter.ViewHolder>(){
    var list=ArrayList<Data>()
        set(value) {
            field=value
        }
    inner class ViewHolder(val binding: BusinessLayoutBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): ViewHolder {
        val binding = DataBindingUtil.inflate<BusinessLayoutBinding>(LayoutInflater.from(parent.context),
            R.layout.business_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (distance && distacdeList[position].distance.toFloat()>10.0){
            holder.binding.root.visibility=View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)

        }else{
            holder.binding.root.visibility=View.VISIBLE
            holder.itemView.layoutParams =
                RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        }

        Glide.with(mContext)
            .load(list[position].logo)
            .override(100,100)
            .into(holder.binding.bookedmenuPicImg)

        holder.binding.bookedmenuNameInc.text=list[position].businessname
        holder.binding.tvView.text=list[position].review_count

        holder.binding.totalarrivingUsersInc.setText(distacdeList[position].distance+ " km")
        holder.binding.arrivingusernameInc.setText(distacdeList[position].travel_time + " driving time ")
        holder.binding.bookerarrivaldatetimeInc.setText(distacdeList[position].walk_time+ " Walking time ")

//        Log.e("device id main",Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID))
//        Log.e("device id api",list[0].device_id.toString())

        if (list[position].device_id.equals(Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID))){
            Log.e("Ccdcdc","if")
           holder.binding.delete.visibility=View.GONE
           holder.binding.btnBookAcceptMenuInc.visibility=View.GONE
        }else{
            Log.e("Ccdcdc","else")
            holder.binding.btnBookAcceptMenuInc.visibility=View.GONE

            holder.binding.delete.visibility=View.GONE
        }

        holder.binding.rlMain.setOnClickListener {
           /* val intent=Intent(mContext,UpdateBusinessActivity::class.java)
            intent.putExtra("data",list[position])
            mContext.startActivity(intent)*/
          //  callbacks.onClick(position,list[position].id.toString(),list[position].businessphone.toString())
            val intent=Intent(mContext, BusinessDetailActivity::class.java)
            intent.putExtra("data",list[position])
            mContext.startActivity(intent)
        }

        holder.binding.btnBookAcceptMenuInc.setOnClickListener {
            val intent=Intent(mContext, UpdateBusinessActivity::class.java)
            intent.putExtra("data",list[position])
            mContext.startActivity(intent)
           /* val intent=Intent(mContext,BusinessDetailActivity::class.java)
            intent.putExtra("data",list[position])
            mContext.startActivity(intent)*/
        }

        holder.binding.delete.setOnClickListener {
            callbacks.onClick(position,list[position].id.toString(),"delete")
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}