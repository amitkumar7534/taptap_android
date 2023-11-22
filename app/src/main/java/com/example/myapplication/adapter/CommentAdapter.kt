package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.RvCommentsBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit


class CommentAdapter(val mContext: Context,val data: ArrayList<com.example.myapplication.api.response.comment.Data>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

   var count=0
    inner class ViewHolder(val binding: RvCommentsBinding): RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RvCommentsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rv_comments, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {



        Glide.with(mContext)
            .load(data[position].user_image)
            .placeholder(R.drawable.discoveraddislogo_new)
            .into(holder.binding.userImage)




        holder.binding.userName.text = data[position].name
        holder.binding.userComment.text = data[position].comment
        holder.binding.time.text=covertTimeToText(data[position].created_at)

        holder.binding.userName.setOnClickListener {
            val intent = Intent(mContext, ProfileActivity::class.java)
            intent.putExtra("id", data[position].user_id.toString())
            mContext.startActivity(intent)
        }
        holder.binding.userImage.setOnClickListener {
            val intent = Intent(mContext, ProfileActivity::class.java)
            intent.putExtra("id", data[position].user_id.toString())
            mContext.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun covertTimeToText(dataDate: String?): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = mContext.getString(R.string.ago)
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateFormat.timeZone= TimeZone.getTimeZone("UTC")
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second "+ mContext.getString(R.string.seconds) +" $suffix"
            } else if (minute < 60) {
                convTime = "$minute "+ mContext.getString(R.string.minutes) +" $suffix"
            } else if (hour < 24) {
                convTime = "$hour "+ mContext.getString(R.string.hours) +" $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " "+ mContext.getString(R.string.years) + " " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " "+ mContext.getString(R.string.month) + " " + suffix
                } else {
                    (day / 7).toString() + " "+ mContext.getString(R.string.weak) + " " + suffix
                }
            } else if (day < 7) {
                convTime = "$day "+ mContext.getString(R.string.days) +" $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message.toString())
        }
        return convTime
    }

}


