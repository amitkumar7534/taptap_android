package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.activities.NotificationDetail
import com.example.myapplication.api.response.NotificationResponse
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.RvNotificationBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class NotificationAdapter(
    val mContext: Context,
    private val data: List<NotificationResponse.Datum>,var delete: DeleteNotification) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RvNotificationBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rv_notification, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if(data.get(position).title.equals("New Like")){
            Glide.with(mContext)
                .load(R.drawable.ic_notify_like)
                .into(holder.binding.whiteBulb)

        }else if(data.get(position).title.equals("New Comment")){
            Glide.with(mContext)
                .load(R.drawable.ic_notify_comment)
                .into(holder.binding.whiteBulb)
        }else{
            if (data.get(position).status.equals("unread")){
                Glide.with(mContext)
                    .load(R.drawable.ic_bulb_white)
                    .into(holder.binding.whiteBulb)
            }else{
                Glide.with(mContext)
                    .load(R.drawable.ic_black_bulb)
                    .into(holder.binding.whiteBulb)
            }

        }



        if (data.get(position).status.equals("unread")){

            holder.binding.notificationTitle.setText(data[position].title)
            holder.binding.message.setText(data[position].message)
            holder.binding.date.setText(dateFormat(data[position].createdAt.toString()))

            holder.binding.relativeView.setBackgroundResource(R.color.colortxtgreen)
            holder.binding.notificationTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            holder.binding.message.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            holder.binding.date.setTextColor(ContextCompat.getColor(mContext, R.color.white))

        }else{


            holder.binding.notificationTitle.setText(data[position].title)
            holder.binding.message.setText(data[position].message)
//            holder.binding.date.setText(dateFormat(data[position].createdAt.toString()))
            holder.binding.date.text=covertTimeToText(dateFormat(data[position].createdAt.toString()))
            holder.binding.relativeView.setBackgroundResource(R.color.light_grey)
            holder.binding.notificationTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            holder.binding.message.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            holder.binding.date.setTextColor(ContextCompat.getColor(mContext, R.color.black))

        }

        holder.binding.Delete.setOnClickListener {
            delete.delete(position,data.get(position).id.toString())
        }

        holder.itemView.setOnClickListener {
            if (!data.get(position).post_id.toString().equals("null")){
                if (!data.get(position).post_id.toString().equals("0")){
                    val intent = Intent(mContext, NotificationDetail::class.java)
                    intent.putExtra("postid",data.get(position).post_id.toString())
                    mContext.startActivity(intent)
                }
            }
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



    fun dateFormat(dateString: String?=""): String {
        try {
            val utc = TimeZone.getDefault()
            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            sourceFormat.timeZone = TimeZone.getDefault()

            val destFormat = SimpleDateFormat("MMM d, yyyy HH:mm:ss")
            destFormat.timeZone = TimeZone.getDefault()


            val convertedDate = sourceFormat.parse(dateString)
            return destFormat.format(convertedDate)

        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        } catch (t:Throwable){
            return ""
        }

    }

    interface DeleteNotification{
        fun delete(position: Int, Id: String)
    }

}