package com.example.myapplication.api.response
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationResponse {

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("count")
    @Expose
    var count: Int? = null


    class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("sender_id")
        @Expose
        var senderId: Int? = null

        @SerializedName("receiver_id")
        @Expose
        var receiverId: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("post_id")
        @Expose
        var post_id: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("count_status")
        @Expose
        var countStatus: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }

}