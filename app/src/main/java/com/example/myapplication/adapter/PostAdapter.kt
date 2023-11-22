package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devs.readmoreoption.ReadMoreOption
import com.example.myapplication.activities.PostDetailActivity
import com.example.myapplication.api.response.post.Data
import com.like.LikeButton
import com.like.OnLikeListener
import com.yookoo.discoveraddis.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit


class PostAdapter(val mContext: Context, var click: onthreeDots, val data: ArrayList<Data>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    var count = 0

    inner class ViewHolder(val binding: com.yookoo.discoveraddis.databinding.RvPostBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<com.yookoo.discoveraddis.databinding.RvPostBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rv_post, parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.binding.bookedmenuPicImg.setOnClickListener {
            //   click.DotClick(position,data[position],"view")
            val intent = Intent(mContext, PostDetailActivity::class.java)
            intent.putExtra("data", data[position])
            mContext.startActivity(intent)
        }
        holder.binding.viewPost.setOnClickListener {
            //   click.DotClick(position,data[position],"view")
            val intent = Intent(mContext, PostDetailActivity::class.java)
            intent.putExtra("data", data[position])
            mContext.startActivity(intent)
        }

        holder.binding.userName.setOnClickListener {
            //   click.DotClick(position,data[position],"view")
            val intent = Intent(mContext, ProfileActivity::class.java)
            intent.putExtra("id", data[position].user_id.toString())
            mContext.startActivity(intent)
        }

//        holder.binding.bookedmenuPicImg.setOnTouchListener(ImageMatrixTouchHandler(mContext))


//        if (position == 0) {
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            params.setMargins(0, 120, 0, 0)
//            holder.binding.rvShade.setLayoutParams(params)
//        }


//        if (position == 0){
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            params.setMargins(0, 120, 0, 0)
//            holder.binding.rvShade.setLayoutParams(params)
//
//        }else{
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            params.setMargins(0, 0, 0, 0)
//            holder.binding.rvShade.setLayoutParams(params)
//
//        }

        /* holder.binding.commentCount.setOnClickListener {
             Toast.makeText(mContext,"Coming Soon",Toast.LENGTH_SHORT).show()
         }*/
        Glide.with(mContext)
            .load(data[position].post_image)
            /*.addListener(object: RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let {
                        val width = resource.intrinsicWidth
                        val height = resource.intrinsicHeight


                        if (height>500){
                            holder.binding.bookedmenuPicImg.layoutParams.height = 500
                        }else{
                            holder.binding.bookedmenuPicImg.layoutParams.height = LayoutParams.WRAP_CONTENT

                        }
                        Log.e("EWcfewc","$width $height")
                        //use these sizes whatever needed for
                      //  imageView.setImageDrawable(resource)
                    }
                    return false                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }


            })*/
            .into(holder.binding.bookedmenuPicImg)


        click.imageClick(position,holder.binding.bookedmenuPicImg)


        val displayMetrics = DisplayMetrics()
        (mContext as Activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels


        Glide.with(mContext)
            .load(data[position].user_image)
            .placeholder(R.drawable.discoveraddislogo_new)
            .into(holder.binding.userImage)
        if (data[position].post_type == "post") {
            holder.binding.productPrice.visibility = View.GONE
        } else {
            holder.binding.productPrice.visibility = View.VISIBLE

        }
        holder.binding.likeButton.isLiked = data[position].is_liked == "1"
        count = data[position].favorites_count
        holder.binding.bookedmenuNameInc.text = data[position].post_title
        holder.binding.decription.text = data[position].description

        if(data[position].description.toString().length!! > 70) { //assuming you want to show the [Read more] for more than 50 characters
            holder.binding.readMore.setVisibility(View.VISIBLE)
        } else { //if there are no enough characters to show the read more text
            holder.binding.readMore.setVisibility(View.GONE); //hide the read more textview
        }

        holder.binding.readMore.setOnClickListener {
            if(holder.binding.readMore.getText().toString().equals("Read More")) { //you should set the text of tvReadMore textview to [Read more] in the beginning
                holder.binding.decription.setMaxLines(Integer.MAX_VALUE); //set the maximum line to Maximum integer
                holder.binding.readMore.setText("Show Less")
            } else {
                holder.binding.decription.setMaxLines(2); //set the maximum line to 3
                holder.binding.readMore.setText("Read More")
            }
        }


//        click.settext(position, holder.binding.decription,data[position].description)

        holder.binding.userName.text = data[position].name
        holder.binding.productPrice.text = "Price - " + data[position].price.toString() + " Br"
        holder.binding.likeCount.text = "Like ($count)"
        holder.binding.viewCount.text = "View (" + data[position].review_count.toString() + ")"
        holder.binding.commentCount.text =
            "Comment (" + data[position].comment_count.toString() + ")"
        holder.binding.postTime.text = covertTimeToText(data[position].created_at)


        holder.binding.userImage.setOnClickListener {
            val intent = Intent(mContext, ProfileActivity::class.java)
            intent.putExtra("id", data[position].user_id.toString())
            mContext.startActivity(intent)
        }

        holder.binding.likeButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                count = data[position].favorites_count
                count += 1
                holder.binding.likeCount.text = "Like ($count)"
                click.DotClick(position, data[position], "like")

            }

            override fun unLiked(likeButton: LikeButton) {
                if (count >= 1) {
                    count -= 1
                    holder.binding.likeCount.text = "Like ($count)"
                    click.DotClick(position, data[position], "like")

                }
            }
        })
//
//        holder.binding.bookedmenuNameInc.setText(data[position].vehicle_name)
//        holder.binding.arrivingusernameInc.setText("Insurance type - "+data[position].insurance_type)
//        holder.binding.bookerarrivaldatetimeInc.setText("Vehicle Plate no -"+data[position].plate_number)
//
//
//        holder.binding.editDeleteView.visibility= View.VISIBLE
//
//        holder.binding.rlMain.setOnClickListener {
//            /* val intent=Intent(mContext,UpdateBusinessActivity::class.java)
//             intent.putExtra("data",list[position])
//             mContext.startActivity(intent)*/
//            val intent= Intent(mContext, CarDetailActivity::class.java)
//            intent.putExtra("data",data[position])
//            mContext.startActivity(intent)
//        }
//
//        holder.binding.edit.setOnClickListener {
//            val intent= Intent(mContext, UpdateCarActivity::class.java)
//            intent.putExtra("data",data[position])
//            mContext.startActivity(intent)
//            /* val intent=Intent(mContext,BusinessDetailActivity::class.java)
//             intent.putExtra("data",list[position])
//             mContext.startActivity(intent)*/
//        }
//        holder.binding.delete.setOnClickListener {
//            callbacks.onClick(position,data[position].id.toString())
//        }

        holder.binding.threeDots.setOnClickListener {
            click.DotClick(position, data[position], "dot")
        }

        holder.binding.llComment.setOnClickListener {
            click.DotClick(position, data[position], "comment")
        }
    }

    override fun getItemCount(): Int {
        Log.e("fdfdsfds", data.size.toString())
        return data.size
    }

    fun covertTimeToText(dataDate: String?): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = mContext.getString(R.string.ago)
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second " + mContext.getString(R.string.seconds) + " $suffix"
            } else if (minute < 60) {
                convTime = "$minute " + mContext.getString(R.string.minutes) + " $suffix"
            } else if (hour < 24) {
                convTime = "$hour " + mContext.getString(R.string.hours) + " $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " " + mContext.getString(R.string.years) + " " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " " + mContext.getString(R.string.month) + " " + suffix
                } else {
                    (day / 7).toString() + " " + mContext.getString(R.string.weak) + " " + suffix
                }
            } else if (day < 7) {
                convTime = "$day " + mContext.getString(R.string.days) + " $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message.toString())
        }
        return convTime
    }

}

interface onthreeDots {
    fun DotClick(postion: Int, data: Data, type: String)
    fun imageClick(postion: Int, imageview: ImageView)
    fun settext(postion: Int, imageview: TextView, description: String)
}