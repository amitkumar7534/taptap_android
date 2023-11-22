package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.activities.AllBusinessesActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ItemCategoryBinding

class CategoryAdapter(val mContext:Context, var newList: Array<String>, val newImage: Array<Int>) :RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    var list=ArrayList<String>()
        set(value) {
            field=value
        }
    inner class ViewHolder(val binding: ItemCategoryBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCategoryBinding>(LayoutInflater.from(parent.context),
            R.layout.item_category, parent, false)
        return ViewHolder(binding)
    }

    fun filterList(filterlist: ArrayList<String>) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.dhotels.setImageResource(newImage[position])
        holder.binding.tvCategory.text=list[position]

        holder.binding.root.setOnClickListener {
            val intent=Intent(mContext, AllBusinessesActivity::class.java)
            intent.putExtra("category",list[position])
            intent.putExtra("isSearchHide",true)
            mContext.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}