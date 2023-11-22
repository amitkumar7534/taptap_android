package com.example.myapplication.api.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.api.networks.ApiUtils
import com.example.myapplication.api.response.ApiBaseResponse
import com.example.myapplication.api.response.CommonResponse
import com.example.myapplication.api.response.MyCarResponse
import com.example.myapplication.api.response.NotificationResponse
import com.example.myapplication.api.response.UpdateProfileResponse
import com.example.myapplication.api.response.business.BusinessResponse
import com.example.myapplication.api.response.comment.CommentResponse
import com.example.myapplication.api.response.comment.PostDetailResponse
import com.example.myapplication.api.response.post.PostResponse
import com.example.myapplication.api.response.profile.ProfileResponse
import com.example.myapplication.app.App
import com.example.myapplication.login.LoginResponse
import com.example.myapplication.login.OTPVerificationResponse
import com.example.myapplication.ui.callback.ApiListener
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File


class ApiRepository constructor(var context: Context) {

    private val compositeDisposable = CompositeDisposable()

    fun getAllBusiness(apiListener : ApiListener,category:String): LiveData<BusinessResponse> {
        var response= MutableLiveData<BusinessResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .getAllBusiness(category)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response
    }

    fun getTopBusiness(apiListener : ApiListener,category:String): LiveData<BusinessResponse> {
        var response= MutableLiveData<BusinessResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .getTopBusiness(category)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response
    }


    fun getOWNBusiness(apiListener : ApiListener,mobile:String,type:String): LiveData<BusinessResponse> {
        var response= MutableLiveData<BusinessResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .getOWNBusiness(mobile,type)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun getOWNCar(apiListener : ApiListener,mobile:String,type:String): LiveData<MyCarResponse> {
        var response= MutableLiveData<MyCarResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .getOWNCar(mobile,type)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun delete_account(apiListener : ApiListener,id:String): LiveData<CommonResponse> {
        var response= MutableLiveData<CommonResponse>()

        var hashMap = HashMap<String, String>()
        hashMap["_id"] = id.toString()

        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .delete_account(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun push_notification(apiListener : ApiListener,title:String,message:String,token:String): LiveData<CommonResponse> {
        var response= MutableLiveData<CommonResponse>()

        var hashMap = HashMap<String, String>()
        hashMap["title"] = title.toString()
        hashMap["message"] = message.toString()
        hashMap["device_token"] = token.toString()

        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .push_notification(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }


    fun login(apiListener : ApiListener,phone_no:String,name:String,category:String,fcm:String): LiveData<LoginResponse> {
        var response= MutableLiveData<LoginResponse>()

        var hashMap = HashMap<String, String>()
        hashMap["phone_no"] = phone_no.trim()
        hashMap["full_name"] = name.trim()
        hashMap["device_id"] = category.trim()
        hashMap["device_token"] = fcm.trim()

        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .login(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")
                        var obj = JSONObject((it as HttpException).response()?.errorBody()?.string().toString())
                        apiListener.progress(false)
                        apiListener.msg(obj.getString("message").toString())
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response
    }
    fun VerifyOTP(apiListener : ApiListener,id:String,otp:String): LiveData<OTPVerificationResponse> {
        var response= MutableLiveData<OTPVerificationResponse>()

        var hashMap = HashMap<String, String>()
        hashMap["_id"] = id.toString().trim()
        hashMap["otp"] = otp.toString().trim()

        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .otpverify(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")
                        var obj = JSONObject((it as HttpException).response()?.errorBody()?.string().toString())
                        apiListener.progress(false)
                        apiListener.msg(obj.getString("message").toString())
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response
    }
    fun getNotification(apiListener : ApiListener,id:String): LiveData<NotificationResponse> {
        var response= MutableLiveData<NotificationResponse>()

        var hashMap = HashMap<String, String>()
        hashMap["user_id"] = id
        if (ApiUtils.checkInternet(context)) {
         //   apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .notification(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                       /* Log.e("error", "${it.message}")
                        var obj = JSONObject((it as HttpException).response()?.errorBody()?.string().toString())
                        apiListener.progress(false)
                        apiListener.msg(obj.getString("message").toString())*/
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response
    }
    fun readAllNotification(apiListener : ApiListener,id:String): LiveData<CommonResponse> {
        var response= MutableLiveData<CommonResponse>()

        var hashMap = HashMap<String, String>()
        hashMap["user_id"] = id
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .readall(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")
                        var obj = JSONObject((it as HttpException).response()?.errorBody()?.string().toString())
                        apiListener.progress(false)
                        apiListener.msg(obj.getString("message").toString())
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response
    }


    fun updateProfile(apiListener : ApiListener,
                          image: File?,
                          username:String, userid:String):LiveData<UpdateProfileResponse> {
        var response=MutableLiveData<UpdateProfileResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)

            var body: MultipartBody.Part? = null
            if (image != null) {
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                body =
                    MultipartBody.Part.createFormData("profile_pic", "image.jpeg", requestFile)
            }else{
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
                body =
                    MultipartBody.Part.createFormData("profile_pic", "image.jpeg", requestFile)

            }

            compositeDisposable.add(
                App.apiInterface()!!
                    .updateUser(
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            username.toString()
                        ), body,
                        userid.toString(),
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun updateWithoutProfilePic(apiListener : ApiListener,
                          username:String, userid:String):LiveData<UpdateProfileResponse> {
        var response=MutableLiveData<UpdateProfileResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)


            compositeDisposable.add(
                App.apiInterface()!!
                    .updateUserWithoutpic(
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            username.toString()
                        ),
                        userid.toString(),
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun createCARBusiness(
        apiListener: ApiListener,
        image: File?,
        businessname: String,
        businessphone: String,
        businesscellphone: String,
        businessaddress: String,
        businesscategory: String,
        latlng: String,
        operatinghrs: String,
        registrationnumebr: String,
        businessworeda: String,
        businesscity: String,
    ):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)

            var body: MultipartBody.Part? = null
            if (image != null) {
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                body =
                    MultipartBody.Part.createFormData("vehicle_photo", "image.jpeg", requestFile)
            }else{
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
                body =
                    MultipartBody.Part.createFormData("vehicle_photo", "image.jpeg", requestFile)

            }

            compositeDisposable.add(
                App.apiInterface()!!
                    .createCarBusiness(
                        body,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessname),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessphone
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businesscellphone
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessaddress
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businesscategory
                        ),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), latlng),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), operatinghrs),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            registrationnumebr
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessworeda
                        ),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscity),
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }



    fun addPost(apiListener : ApiListener,
                          image: File?,
                post_title:String,
                post_type:String,
                description:String,
                price:String,user_id:String):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)

            var body: MultipartBody.Part? = null
            if (image != null) {
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                body =
                    MultipartBody.Part.createFormData("post_image", "image.jpeg", requestFile)
            }else{
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
                body =
                    MultipartBody.Part.createFormData("post_image", "image.jpeg", requestFile)

            }

            compositeDisposable.add(
                App.apiInterface()!!
                    .addPost(
                        body,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), post_title),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), post_type),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), price),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user_id),
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }



    fun updatePost(apiListener : ApiListener,
                   image: File?,
                   post_title:String,
                   post_type:String,
                   description:String,
                   price:String,user_id:String,id:String):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)

            var body: MultipartBody.Part? = null
            if (image != null) {
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                body =
                    MultipartBody.Part.createFormData("post_image", "image.jpeg", requestFile)
            }else{
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
                body =
                    MultipartBody.Part.createFormData("post_image", "image.jpeg", requestFile)

            }

            compositeDisposable.add(
                App.apiInterface()!!
                    .updatePost(
                        body,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), post_title),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), post_type),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), price),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user_id),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), id),
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }

    fun updatePostWithoutPhoto(apiListener : ApiListener,
                   post_title:String,
                   post_type:String,
                   description:String,
                   price:String,user_id:String,id:String):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)



            compositeDisposable.add(
                App.apiInterface()!!
                    .updatePostWithoutPhoto(
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), post_title),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), post_type),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), price),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user_id),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), id),
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun UpdateCARBusiness(
        apiListener: ApiListener,
        image: File?,
        car_id: String,
        businessname: String,
        businessphone: String,
        businesscellphone: String,
        businessaddress: String,
        businesscategory: String,
        latlng: String,
        operatinghrs: String,
        registrationnumebr: String,
        businessworeda: String,
        businesscity: String,
    ):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)

            var body: MultipartBody.Part? = null
            if (image != null) {
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                body =
                    MultipartBody.Part.createFormData("vehicle_photo", "image.jpeg", requestFile)
            }else{
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
                body =
                    MultipartBody.Part.createFormData("vehicle_photo", "image.jpeg", requestFile)

            }

            compositeDisposable.add(
                App.apiInterface()!!
                    .updateCarBusiness(
                        body, car_id.toString(),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessname),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessphone
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businesscellphone
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessaddress
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businesscategory
                        ),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), latlng),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), operatinghrs),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            registrationnumebr
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessworeda
                        ),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscity),
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun UpdateCARWithoutPic(
        apiListener: ApiListener,
        car_id: String,
        businessname: String,
        businessphone: String,
        businesscellphone: String,
        businessaddress: String,
        businesscategory: String,
        latlng: String,
        operatinghrs: String,
        registrationnumebr: String,
        businessworeda: String,
        businesscity: String,
    ):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .updateCarBusinessWithout_pic(
                        car_id.toString(),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessname),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessphone
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businesscellphone
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessaddress
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businesscategory
                        ),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), latlng),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), operatinghrs),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            registrationnumebr
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            businessworeda
                        ),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscity),
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }



    fun seaerchBusiness(apiListener : ApiListener,keyword:String): LiveData<BusinessResponse> {
        var response= MutableLiveData<BusinessResponse>()
        if (ApiUtils.checkInternet(context)) {
        //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .searchBusiness(keyword)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }



    fun deleteBusiness(apiListener : ApiListener,id:String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .deleteBusiness(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }


    fun logout(apiListener : ApiListener,id:String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .logout(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }


    fun getProfile(apiListener : ApiListener,id:String,current_user_id:String): LiveData<ProfileResponse> {

        var hashMap = HashMap<String, String>()
        hashMap["user_id"] = id.toString().trim()
        hashMap["current_user_id"] = current_user_id.toString().trim()

        var response= MutableLiveData<ProfileResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .getProfile(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun PrivateAccount(apiListener : ApiListener,id:String,current_user_id:String): MutableLiveData<CommonResponse> {

        var hashMap = HashMap<String, String>()
        hashMap["user_id"] = id.toString().trim()
        hashMap["private_mode"] = current_user_id
        var response= MutableLiveData<CommonResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .private_account(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }


    fun deleteCarBusiness(apiListener : ApiListener,id:String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .deleteCarBusiness(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }


    fun getPosts(apiListener : ApiListener,id:String,user_id: String): LiveData<PostResponse> {
        var response= MutableLiveData<PostResponse>()
        if (ApiUtils.checkInternet(context)) {
         //   apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .getPosts(id,user_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }


    fun deletePost(apiListener : ApiListener,id:String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .deletePost(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }

    fun getComment(apiListener : ApiListener,id:String): LiveData<CommentResponse> {
        var response= MutableLiveData<CommentResponse>()
        if (ApiUtils.checkInternet(context)) {
        //     apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .getComments(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response
    }

    fun postData(apiListener : ApiListener,post_id:String,user_id:String): LiveData<PostDetailResponse> {

        var hashMap = HashMap<String, String>()
        hashMap["post_id"] = post_id.toString()
        hashMap["user_id"] = user_id.toString()

        var response= MutableLiveData<PostDetailResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .getPostdata(hashMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")
                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun likePost(apiListener : ApiListener,id:String,uId:String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .likePost(id, uId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }



    fun hidePost(apiListener : ApiListener,id:String,uId:String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .hidePost(id, uId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }

    fun reportPost(apiListener : ApiListener,id:String,uId:String,message: String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .reportPost(id, uId,message)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun addComment(apiListener : ApiListener,id:String,uId:String,message: String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
           // apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .addComment(id, uId,message)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }
    fun viewPost(apiListener : ApiListener,id:String,uId:String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .viewPost(id, uId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }

    fun businessReview(apiListener : ApiListener,id:String,uId:String): LiveData<ApiBaseResponse> {
        var response= MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            //    apiListener.progress(true)
            compositeDisposable.add(
                App.apiInterface()!!
                    .businessReview(id, uId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.message}")

                        apiListener.progress(false)
                        apiListener.msg("Something went Wrong")
                    })
            )
        } else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }


    fun createBusiness(apiListener : ApiListener,
                               image: File?,
                               device_id:String,
                               businessname:String,
                               businessphone:String,
                               businesscellphone:String,
                               businessaddress:String,
                               businesscategory:String,
                               latlng:String,
                               operatinghrs:String,
                               registrationnumebr:String,
                               businessworeda:String,
                               businesscity:String,
                               businessregion:String,
                               postalcode:String,
                               businessspecializesin:String,
                               businessstar: String):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)

            var body: MultipartBody.Part? = null
            if (image != null) {
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                body =
                    MultipartBody.Part.createFormData("logo", "image.jpeg", requestFile)
            }else{
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
                body =
                    MultipartBody.Part.createFormData("logo", "image.jpeg", requestFile)

            }

            compositeDisposable.add(
                App.apiInterface()!!
                    .createBusiness(body,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), device_id),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessname),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessphone),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscellphone) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessaddress) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscategory) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), latlng) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), operatinghrs) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrationnumebr),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessworeda),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscity),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessregion),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), postalcode),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessspecializesin),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessstar)
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }



    fun updateBusiness(apiListener : ApiListener,
                       id:String,
                       image: File?,
                       device_id:String,
                       businessname:String,
                       businessphone:String,
                       businesscellphone:String,
                       businessaddress:String,
                       businesscategory:String,
                       latlng:String,
                       operatinghrs:String,
                       registrationnumebr:String,
                       businessworeda:String,
                       businesscity:String,
                       businessregion:String,
                       postalcode:String,
                       businessspecializesin:String,
                       businessstar: String):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)

            var body: MultipartBody.Part? = null
            if (image != null) {
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                body =
                    MultipartBody.Part.createFormData("logo", "image.jpeg", requestFile)
            }else{
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
                body =
                    MultipartBody.Part.createFormData("logo", "image.jpeg", requestFile)

            }

            compositeDisposable.add(
                App.headerInterface()!!
                    .updateBusiness(id,body,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), device_id),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessname),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessphone),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscellphone) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessaddress) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscategory) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), latlng) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), operatinghrs) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrationnumebr),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessworeda),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscity),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessregion),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), postalcode),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessspecializesin),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessstar)
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }



    fun updateBusinessWithoutPhoto(apiListener : ApiListener,
                       id:String,
                       device_id:String,
                       businessname:String,
                       businessphone:String,
                       businesscellphone:String,
                       businessaddress:String,
                       businesscategory:String,
                       latlng:String,
                       operatinghrs:String,
                       registrationnumebr:String,
                       businessworeda:String,
                       businesscity:String,
                       businessregion:String,
                       postalcode:String,
                       businessspecializesin:String,
                       businessstar: String):LiveData<ApiBaseResponse> {
        var response=MutableLiveData<ApiBaseResponse>()
        if (ApiUtils.checkInternet(context)) {
            apiListener.progress(true)

            /*var body: MultipartBody.Part? = null
            if (image != null) {
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                body =
                    MultipartBody.Part.createFormData("logo", "image.jpeg", requestFile)
            }else{
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
                body =
                    MultipartBody.Part.createFormData("logo", "image.jpeg", requestFile)

            }*/



            compositeDisposable.add(
                App.apiInterface()!!
                    .updateBusinessWithoutPhoto(id,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), device_id),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessname),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessphone),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscellphone) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessaddress) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscategory) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), latlng) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), operatinghrs) ,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrationnumebr),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessworeda),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businesscity),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessregion),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), postalcode),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessspecializesin),
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), businessstar)
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("subscribe", "$it")
                        apiListener.progress(false)
                        response.postValue(it)
                    }, {
                        Log.e("error", "${it.cause}")
                        apiListener.progress(false)
                        apiListener.msg(it.message.toString())
                    })
            )
        }else {
            apiListener.msg(App.getInstance().applicationContext.resources.getString(R.string.internet_connect))
        }
        return response

    }









}









