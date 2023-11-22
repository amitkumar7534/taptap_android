package com.example.myapplication.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.FileUtils
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityProfileBinding
import com.yookoo.discoveraddis.databinding.ActivitySettingBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

class ProfileActivity : BindingActivity<ActivityProfileBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_profile
    }

    var mPhotoFile: File? = null
    val PERMISSION_CODE = 200
    var imgPath: String = ""
    val REQUEST_TAKE_PHOTO = 101
    val REQUEST_GALLERY_PHOTO = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.ivBack.setOnClickListener {
            finish()
        }

        viewBinding.userImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), PERMISSION_CODE
                )
            } else {
                showPictureDialog()
            }
        }


        Glide.with(this)
            .load(appSession().getString(Constant.USER_PICTURE).toString())
            .override(200, 200)
            .placeholder(R.drawable.iv_default_user)
            .into(viewBinding.userImage)

        viewBinding.edtName.setText(appSession().getString(Constant.USER_NAME).toString())
        viewBinding.edtPhone.setText(appSession().getString(Constant.USER_PHONE_NUMBER).toString())


        viewBinding.updateButton.setOnClickListener {
            updateProfile()
        }

    }

    fun updateProfile() {

        if (mPhotoFile == null){
            baseViewModel.repository.updateWithoutProfilePic(apiListener(),viewBinding.edtName.text.toString(),
                appSession().getString(Constant.USER_ID).toString()).observe(this) {
                hideProgress()
                if (it.success == true) {
                    showToast(it.message.toString())
                    appSession().save("login_value", true)
                    appSession().save(Constant.USER_NAME, it.data.full_name)
                    appSession().save(Constant.USER_PICTURE, it?.data?.profile_pic?.toString())
                    val intent = Intent(this, BusinessesDashboardActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    showToast(it.message.toString())
                }
            }

        }else{
            baseViewModel.repository.updateProfile(apiListener(),mPhotoFile,viewBinding.edtName.text.toString(),
                appSession().getString(Constant.USER_ID).toString()).observe(this) {
                hideProgress()
                if (it.success == true) {
                    showToast(it.message.toString())
                    appSession().save("login_value", true)
                    appSession().save(Constant.USER_NAME, it.data.full_name)
                    appSession().save(Constant.USER_PICTURE, it?.data?.profile_pic?.toString())
                    val intent = Intent(this, BusinessesDashboardActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    showToast(it.message.toString())
                }
            }

        }

    }


    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.select_action))
        val pictureDialogItems = arrayOf(
            getString(R.string.select_image_from_gallery),
            getString(R.string.select_image_from_camera)
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> dispatchGalleryIntent()
                1 -> dispatchTakePictureIntent()
            }
        }
        pictureDialog.show()
    }


    private fun dispatchGalleryIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(
            pickPhoto, REQUEST_GALLERY_PHOTO
        )
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            if (photoFile != null) {
//                val photoURI = FileProvider.getUriForFile(
//                    this, "${BuildConfig.APPLICATION_ID}.provider",
//                    photoFile
//                )

                val photoURI = FileProvider.getUriForFile(
                    Objects.requireNonNull(this),
                    "com.yookoo.discoveraddis" + ".provider", photoFile
                )

                mPhotoFile = photoFile

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent,
                    REQUEST_TAKE_PHOTO
                )
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }


    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = this.contentResolver.query(contentUri!!, proj, null, null, null)
//            if (BuildConfig.DEBUG && cursor == null) {
//                error("Assertion failed")
//            }
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (mPhotoFile != null) {
                Glide.with(this)
                    .load(mPhotoFile!!.absolutePath)
                    .placeholder(R.drawable.iv_default_user)
                    .apply(RequestOptions.circleCropTransform()).into(viewBinding.userImage)
                imgPath = FileUtils.compressImage(
                    FileUtils.getRealPathFromUri(
                        this,
                        mPhotoFile!!.toUri()
                    )!!.absolutePath
                ).toString()
                mPhotoFile = File(imgPath)
                mPhotoFile!!.mkdirs()


//                    uploadImageViewModel.upload_image(
//                        requireActivity(),
//                        Compressor(requireActivity()).compressToFile(mPhotoFile)
//                    )

                //  imgPath=CompressFile.getCompressedImageFile(mPhotoFile!!, this)
                /* uploadImageViewModel.upload_image(
                     this,
                     "userImage",
                     CompressFile.getCompressedImageFile(mPhotoFile!!, this)
                 )*/
            }
        } else if (requestCode == REQUEST_GALLERY_PHOTO) {
            if (data != null) {
                val selectedImage = data.data
                try {

                    if (!selectedImage.toString().contains("video")) {
                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)


//                        uploadImageViewModel.upload_image(
//                            requireActivity(),
//                            Compressor(requireActivity()).compressToFile(mPhotoFile)
//                        )

                        Glide.with(this)
                            .load(selectedImage.toString())
                            .placeholder(R.drawable.iv_default_user)
                            .apply(RequestOptions.circleCropTransform()).into(viewBinding.userImage)
                    } else {
                        showToast("Please select valid image!")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


}