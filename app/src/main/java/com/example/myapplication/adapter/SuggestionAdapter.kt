package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.IvSuggestionBinding

class SuggestionAdapter (val mContext: Context) : RecyclerView.Adapter<SuggestionAdapter.ViewHolder>(){
    var list=ArrayList<String>()
        set(value) {
            field=value
        }
    inner class ViewHolder(val binding: IvSuggestionBinding): RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<IvSuggestionBinding>(
            LayoutInflater.from(parent.context),
            R.layout.iv_suggestion, parent, false)
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