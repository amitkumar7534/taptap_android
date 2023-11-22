package com.easyjob.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.RvMessageBinding

class AdapterMessages (val mContext: Context) : RecyclerView.Adapter<AdapterMessages.ViewHolder>(){

    inner class ViewHolder(val binding: RvMessageBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): AdapterMessages.ViewHolder {
        val binding = DataBindingUtil.inflate<RvMessageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rv_message, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterMessages.ViewHolder, position: Int) {


    }
    override fun getItemCount(): Int {
        return 10
    }


}