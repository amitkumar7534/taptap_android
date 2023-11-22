package com.example.myapplication.api.rest

import com.example.myapplication.api.response.ApiBaseResponse
import com.example.myapplication.api.response.CommonResponse
import com.example.myapplication.api.response.MyCarResponse
import com.example.myapplication.api.response.NotificationResponse
import com.example.myapplication.api.response.UpdateProfileResponse
import com.example.myapplication.api.response.business.BusinessResponse
import com.example.myapplication.api.response.category.CategoryResponse
import com.example.myapplication.api.response.comment.CommentResponse
import com.example.myapplication.api.response.comment.PostDetailResponse
import com.example.myapplication.api.response.post.PostResponse
import com.example.myapplication.api.response.profile.ProfileResponse
import com.example.myapplication.api.response.wallpaper.WallpaperResponse
import com.example.myapplication.login.LoginResponse
import com.example.myapplication.login.OTPVerificationResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RestApi {

    @FormUrlEncoded
    @POST("api/business/get")
    fun getAllBusiness(@Field("category") category: String): Observable<BusinessResponse>

    @FormUrlEncoded
    @POST("api/business/get")
    fun getTopBusiness(@Field("top") category: String): Observable<BusinessResponse>

    @GET("api/vehicle-business/list/{phoneno}/{type}")
    fun getOWNBusiness(@Path("phoneno") phoneno: String ,@Path("type") type: String )
    : Observable<BusinessResponse>

   @GET("api/vehicle-business/list/{phoneno}/{type}")
    fun getOWNCar(@Path("phoneno") phoneno: String ,@Path("type") type: String )
    : Observable<MyCarResponse>

    @POST("api/business/signin")
    fun login(@Body request: HashMap<String, String>)
            : Observable<LoginResponse>

    @POST("api/business/user/delete")
    fun delete_account(@Body request: HashMap<String, String>)
            : Observable<CommonResponse>

    @POST("api/send-notification-to-user")
    fun push_notification(@Body request: HashMap<String, String>)
            : Observable<CommonResponse>

    @POST("api/business/otp-verification")
    fun otpverify(@Body request: HashMap<String, String>)
            : Observable<OTPVerificationResponse>

    @POST("api/get-notifications")
    fun notification(@Body request: HashMap<String, String>)
            : Observable<NotificationResponse>

    @POST("api/read-notifications")
    fun readall(@Body request: HashMap<String, String>)
            : Observable<CommonResponse>

    @Multipart
    @POST("api/vehicle/add")
    fun createCarBusiness(
        @Part logo: MultipartBody.Part,
        @Part("vehicle_name") vehicle_name: RequestBody,
        @Part("vehicle_code") vehicle_code: RequestBody,
        @Part("owner_name") owner_name: RequestBody,
        @Part("owner_phone") owner_phone: RequestBody,
        @Part("chasis_number") chasis_number: RequestBody,
        @Part("plate_number") plate_number: RequestBody,
        @Part("insurer") insurer: RequestBody,
        @Part("insurance_number") insurance_number: RequestBody,
        @Part("insurance_type") insurance_type: RequestBody,
        @Part("insured_date") insured_date: RequestBody,
    ): Observable<ApiBaseResponse>


    @Multipart
    @POST("api/post/add")
    fun addPost(
        @Part logo: MultipartBody.Part,
        @Part("post_title") post_title: RequestBody,
        @Part("post_type") post_type: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("user_id") user_id: RequestBody,
    ): Observable<ApiBaseResponse>

    @Multipart
    @POST("api/post/add")
    fun updatePost(
        @Part logo: MultipartBody.Part,
        @Part("post_title") post_title: RequestBody,
        @Part("post_type") post_type: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("user_id") user_id: RequestBody,
        @Part("id") id: RequestBody,
    ): Observable<ApiBaseResponse>


    @Multipart
    @POST("api/post/add")
    fun updatePostWithoutPhoto(
        @Part("post_title") post_title: RequestBody,
        @Part("post_type") post_type: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("user_id") user_id: RequestBody,
        @Part("id") id: RequestBody,
    ): Observable<ApiBaseResponse>


    @Multipart
    @POST("api/vehicle/update/{id}")
    fun updateCarBusiness(
        @Part logo: MultipartBody.Part,
        @Path("id") id: String,
        @Part("vehicle_name") vehicle_name: RequestBody,
        @Part("vehicle_code") vehicle_code: RequestBody,
        @Part("owner_name") owner_name: RequestBody,
        @Part("owner_phone") owner_phone: RequestBody,
        @Part("chasis_number") chasis_number: RequestBody,
        @Part("plate_number") plate_number: RequestBody,
        @Part("insurer") insurer: RequestBody,
        @Part("insurance_number") insurance_number: RequestBody,
        @Part("insurance_type") insurance_type: RequestBody,
        @Part("insured_date") insured_date: RequestBody,
    ): Observable<ApiBaseResponse>

    @Multipart
    @POST("api/vehicle/update/{id}")
    fun updateCarBusinessWithout_pic(
        @Path("id") id: String,
        @Part("vehicle_name") vehicle_name: RequestBody,
        @Part("vehicle_code") vehicle_code: RequestBody,
        @Part("owner_name") owner_name: RequestBody,
        @Part("owner_phone") owner_phone: RequestBody,
        @Part("chasis_number") chasis_number: RequestBody,
        @Part("plate_number") plate_number: RequestBody,
        @Part("insurer") insurer: RequestBody,
        @Part("insurance_number") insurance_number: RequestBody,
        @Part("insurance_type") insurance_type: RequestBody,
        @Part("insured_date") insured_date: RequestBody,
    ): Observable<ApiBaseResponse>

    @Multipart
    @POST("api/business/user/update/{id}")
    fun updateUser(
        @Part("name") name: RequestBody,
        @Part logo: MultipartBody.Part,
        @Path("id") id: String,
    ): Observable<UpdateProfileResponse>

  @Multipart
    @POST("api/business/user/update/{id}")
    fun updateUserWithoutpic(
        @Part("name") name: RequestBody,
        @Path("id") id: String,
    ): Observable<UpdateProfileResponse>



    @Multipart
    @POST("api/business/create")
    fun createBusiness(
        @Part logo: MultipartBody.Part,
        @Part("device_id") device_id: RequestBody,
        @Part("businessname") businessname: RequestBody,
        @Part("businessphone") businessphone: RequestBody,
        @Part("businesscellphone") businesscellphone: RequestBody,
        @Part("businessaddress") businessaddress: RequestBody,
        @Part("businesscategory") businesscategory: RequestBody,
        @Part("latlng") latlng: RequestBody,
        @Part("operatinghrs") operatinghrs: RequestBody,
        @Part("registrationnumebr") registrationnumebr: RequestBody,
        @Part("businessworeda") businessworeda: RequestBody,
        @Part("businesscity") businesscity: RequestBody,
        @Part("businessregion") businessregion: RequestBody,
        @Part("postalcode") postalcode: RequestBody,
        @Part("businessspecializesin") businessspecializesin: RequestBody,
        @Part("businessstar") businessstar: RequestBody

    ): Observable<ApiBaseResponse>

    @Multipart
    @POST("api/business/update/{id}")
    fun updateBusiness(
        @Path("id") id: String,
        @Part logo: MultipartBody.Part,
        @Part("device_id") device_id: RequestBody,
        @Part("businessname") businessname: RequestBody,
        @Part("businessphone") businessphone: RequestBody,
        @Part("businesscellphone") businesscellphone: RequestBody,
        @Part("businessaddress") businessaddress: RequestBody,
        @Part("businesscategory") businesscategory: RequestBody,
        @Part("latlng") latlng: RequestBody,
        @Part("operatinghrs") operatinghrs: RequestBody,
        @Part("registrationnumebr") registrationnumebr: RequestBody,
        @Part("businessworeda") businessworeda: RequestBody,
        @Part("businesscity") businesscity: RequestBody,
        @Part("businessregion") businessregion: RequestBody,
        @Part("postalcode") postalcode: RequestBody,
        @Part("businessspecializesin") businessspecializesin: RequestBody,
        @Part("businessstar") businessstar: RequestBody
    ): Observable<ApiBaseResponse>


    @Multipart
    @POST("api/business/update/{id}")
    fun updateBusinessWithoutPhoto(
        @Path("id") id: String,
        @Part("device_id") device_id: RequestBody,
        @Part("businessname") businessname: RequestBody,
        @Part("businessphone") businessphone: RequestBody,
        @Part("businesscellphone") businesscellphone: RequestBody,
        @Part("businessaddress") businessaddress: RequestBody,
        @Part("businesscategory") businesscategory: RequestBody,
        @Part("latlng") latlng: RequestBody,
        @Part("operatinghrs") operatinghrs: RequestBody,
        @Part("registrationnumebr") registrationnumebr: RequestBody,
        @Part("businessworeda") businessworeda: RequestBody,
        @Part("businesscity") businesscity: RequestBody,
        @Part("businessregion") businessregion: RequestBody,
        @Part("postalcode") postalcode: RequestBody,
        @Part("businessspecializesin") businessspecializesin: RequestBody,
        @Part("businessstar") businessstar: RequestBody
    ): Observable<ApiBaseResponse>

    @FormUrlEncoded
    @POST("api/business/search")
    fun searchBusiness(@Field("keyword") keyword: String): Observable<BusinessResponse>

    @DELETE("api/business/{id}")
    fun deleteBusiness(@Path("id") id: String): Observable<ApiBaseResponse>

    @GET("api/logout/{id}")
    fun logout(@Path("id") id: String): Observable<ApiBaseResponse>

    @POST("api/business/user-detail")
    fun getProfile(@Body request: HashMap<String, String>,
    ): Observable<ProfileResponse>

    @POST("api/user/settings/account")
    fun private_account(@Body request: HashMap<String, String>,
    ): Observable<CommonResponse>

    @POST("api/post/get-post")
    fun getPostdata(@Body request: HashMap<String, String>,
    ): Observable<PostDetailResponse>

    @GET("api/vehicle/delete/{id}")
    fun deleteCarBusiness(@Path("id") id: String): Observable<ApiBaseResponse>

    @FormUrlEncoded
    @POST("api/posts")
    fun getPosts(@Field("page_no") page_no: String,@Field("user_id") user_id: String): Observable<PostResponse>

    @FormUrlEncoded
    @POST("api/post/delete")
    fun deletePost(@Field("post_id") post_id: String): Observable<ApiBaseResponse>

    @FormUrlEncoded
    @POST("api/post/comment")
    fun getComments(@Field("post_id") post_id: String): Observable<CommentResponse>

    @FormUrlEncoded
    @POST("api/post/like")
    fun likePost(@Field("post_id") post_id: String,@Field("user_id") user_id: String): Observable<ApiBaseResponse>

    @FormUrlEncoded
    @POST("api/post/hide-post")
    fun hidePost(@Field("post_id") post_id: String,@Field("user_id") user_id: String): Observable<ApiBaseResponse>

    @FormUrlEncoded
    @POST("api/post/report-post")
    fun reportPost(@Field("post_id") post_id: String,@Field("user_id") user_id: String,@Field("message") message: String): Observable<ApiBaseResponse>

    @FormUrlEncoded
    @POST("api/post/add-comment")
    fun addComment(@Field("post_id") post_id: String,@Field("user_id") user_id: String,@Field("comment") message: String): Observable<ApiBaseResponse>

    @FormUrlEncoded
    @POST("api/post/review")
    fun viewPost(@Field("post_id") post_id: String,@Field("user_id") user_id: String): Observable<ApiBaseResponse>

    @FormUrlEncoded
    @POST("api/business/review")
    fun businessReview(@Field("phone_number") phone_number: String,@Field("business_id") business_id: String): Observable<ApiBaseResponse>


}