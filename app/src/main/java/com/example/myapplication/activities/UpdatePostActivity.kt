package com.example.myapplication.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.myapplication.api.response.post.Data
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.FileUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityUpdatePostBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

class UpdatePostActivity : BindingActivity<ActivityUpdatePostBinding>() {

    var mPhotoFile: File? = null
    var formatedDate2: String = ""
    val PERMISSION_CODE = 200
    var imgPath: String = ""
    val REQUEST_TAKE_PHOTO = 101
    val REQUEST_GALLERY_PHOTO = 201
    var type= "post"
    override fun getLayout(): Int {
        return R.layout.activity_update_post
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data:Data=intent.getSerializableExtra("data") as Data

        if (data.post_type=="product"){
            viewBinding.radioBtn2.isChecked=true
            type= "product"
            viewBinding.etPrice.visibility= View.VISIBLE
            viewBinding.tvPrice.visibility= View.VISIBLE
        }else{
            viewBinding.radioBtn1.isChecked=true

            type= "post"
            viewBinding.etPrice.visibility= View.GONE
            viewBinding.tvPrice.visibility= View.GONE
        }

        viewBinding.etPrice.setText(data.price.toString())
        viewBinding.etDescription.setText(data.description)
        viewBinding.titleName.setText(data.post_title)

        if (!data.post_image.isNullOrEmpty()) {

            loadGlide(
                data.post_image.toString(),
                viewBinding.ivUpload,
                R.drawable.app_icon
            )
        }

        viewBinding.ivBack.setOnClickListener {
            onBackPressed()
        }

        viewBinding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = viewBinding.radioBtn2
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                // Changes the textview's text to "Checked: example radiobutton text"
                type= "product"
                viewBinding.etPrice.visibility= View.VISIBLE
                viewBinding.tvPrice.visibility= View.VISIBLE
            }else{
                type= "post"

                viewBinding.etPrice.visibility= View.GONE
                viewBinding.tvPrice.visibility= View.GONE
            }
        })


        viewBinding.ivUpload.setOnClickListener {
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


              /*  ImagePicker.with(this)
                    .crop(3f,4f)	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()*/
            }
        }


        viewBinding.submitButton.setOnClickListener {
            if (type== "post") {
                if (viewBinding.titleName.text.toString().trim() == "") {
                    showToast("Please enter title")
                } else if (viewBinding.etDescription.text.toString().trim() == "") {
                    showToast("Please enter description")
                }else if (mPhotoFile == null) {
                    createBusinessWithoutPhoto(viewBinding.titleName.text.toString(),
                        type,viewBinding.etDescription.text.toString(),viewBinding.etPrice.text.toString() ,appSession().getString(
                            Constant.USER_ID).toString(),data.id.toString())
                } else{
                    createBusiness(mPhotoFile,viewBinding.titleName.text.toString(),
                        type,viewBinding.etDescription.text.toString(),"0",appSession().getString(
                            Constant.USER_ID).toString(),data.id.toString())
                }
            }else{
                if (viewBinding.titleName.text.toString().trim() == "") {
                    showToast("Please enter title")
                }else if (viewBinding.etPrice.text.toString().trim() == "") {
                    showToast("Please enter price")
                } else if (viewBinding.etDescription.text.toString().trim() == "") {
                    showToast("Please enter description")
                }else if (mPhotoFile == null) {
                    createBusinessWithoutPhoto(viewBinding.titleName.text.toString(),
                        type,viewBinding.etDescription.text.toString(),viewBinding.etPrice.text.toString() ,appSession().getString(
                            Constant.USER_ID).toString(),data.id.toString())
                } else{
                    createBusiness(mPhotoFile,viewBinding.titleName.text.toString(),
                        type,viewBinding.etDescription.text.toString(),viewBinding.etPrice.text.toString() ,appSession().getString(
                            Constant.USER_ID).toString(),data.id.toString())
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
                );

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
            if (resultCode == -1) {
                if (mPhotoFile != null) {
                    loadGlide(
                        mPhotoFile!!.absolutePath,
                        viewBinding.ivUpload,
                        R.drawable.gallery
                    )
                    imgPath = FileUtils.compressImage(
                        FileUtils.getRealPathFromUri(
                            this,
                            mPhotoFile!!.toUri()
                        )!!.absolutePath
                    ).toString()
                    mPhotoFile = File(imgPath)
                    mPhotoFile!!.mkdirs()
                    //  imgPath=CompressFile.getCompressedImageFile(mPhotoFile!!, this)
                    /* uploadImageViewModel.upload_image(
                         this,
                         "userImage",
                         CompressFile.getCompressedImageFile(mPhotoFile!!, this)
                     )*/
                }
            }
        } else if (requestCode == REQUEST_GALLERY_PHOTO) {
            if (data != null) {
                val selectedImage = data.data
                try {

                    if (!selectedImage.toString().contains("video")) {
                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
                        loadGlide(
                            selectedImage.toString(),
                            viewBinding.ivUpload,
                            R.drawable.gallery
                        )
                        /* imgPath=FileUtils.compressImage(
                             FileUtils.getRealPathFromUri(
                                 this,
                                 selectedImage
                             )!!.absolutePath
                         ).toString()
                         mPhotoFile=File(imgPath)

                         mPhotoFile!!.mkdirs()*/


                        /*uploadImageViewModel.upload_image(
                            this,
                            "userImage",
                            CompressFile.getCompressedImageFile(mPhotoFile!!, this)
                        )*/
                    } else {
                        showToast("Please select valid image!")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }



    fun createBusiness(
        image: File?,
        post_title:String,
        post_type:String,
        description:String,
        price:String,
        user_id:String,
        id:String
    ) {
        baseViewModel.repository.updatePost(
            apiListener(), image,post_title, post_type, description, price,user_id,id
        ).observe(this) {
            if (it.success) {
                showToast(it.message)
                finish()
            }
        }
    }

    fun createBusinessWithoutPhoto(
        post_title:String,
        post_type:String,
        description:String,
        price:String,
        user_id:String,
        id:String
    ) {
        baseViewModel.repository.updatePostWithoutPhoto(
            apiListener(), post_title, post_type, description, price,user_id,id
        ).observe(this) {
            if (it.success) {
                showToast(it.message)
               /* val intent=Intent(this,BusinessesDashboardActivity::class.java)
                startActivity(intent)*/
                finish()
            }
        }
    }

}