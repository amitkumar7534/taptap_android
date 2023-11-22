package com.example.myapplication.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.myapplication.activities.BusinessesDashboardActivity
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.FileUtils
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityAddPostBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects


class AddPostFragment : BaseFragment<ActivityAddPostBinding>() {

    var mPhotoFile: File? = null
    var formatedDate2: String = ""
    val PERMISSION_CODE = 200
    var imgPath: String = ""
    val REQUEST_TAKE_PHOTO = 101
    val REQUEST_GALLERY_PHOTO = 201
    var type= "post"

    override fun getLayout(): Int {
        return R.layout.activity_add_post
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tBinding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = tBinding.radioBtn2
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                // Changes the textview's text to "Checked: example radiobutton text"
                type= "product"
                tBinding.etPrice.visibility=View.VISIBLE
                tBinding.tvPrice.visibility=View.VISIBLE
            }else{
                type= "post"

                tBinding.etPrice.visibility=View.GONE
                tBinding.tvPrice.visibility=View.GONE
            }
        })


        tBinding.ivUpload.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
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


        tBinding.submitButton.setOnClickListener {
            if (type== "post") {
                if (tBinding.titleName.text.toString().trim() == "") {
                    getBaseActivity().showToast("Please enter title")
                } else if (tBinding.etDescription.text.toString().trim() == "") {
                    getBaseActivity().showToast("Please enter description")
                }else if (mPhotoFile == null) {
                    showToast("Please upload image")
                } else{
                    createBusiness(mPhotoFile,tBinding.titleName.text.toString(),
                        type,tBinding.etDescription.text.toString(),"0",getBaseActivity().appSession().getString(
                            Constant.USER_ID).toString())
                }
            }else{
                if (tBinding.titleName.text.toString().trim() == "") {
                    getBaseActivity().showToast("Please enter title")
                }else if (tBinding.etPrice.text.toString().trim() == "") {
                    getBaseActivity().showToast("Please enter price")
                } else if (tBinding.etDescription.text.toString().trim() == "") {
                    getBaseActivity().showToast("Please enter description")
                }else if (mPhotoFile == null) {
                    showToast("Please upload image")
                } else{
                    createBusiness(mPhotoFile,tBinding.titleName.text.toString(),
                        type,tBinding.etDescription.text.toString(),tBinding.etPrice.text.toString() ,getBaseActivity().appSession().getString(
                            Constant.USER_ID).toString())
                }

            }
        }

    }



    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
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
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
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
                    Objects.requireNonNull(requireActivity()),
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
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }


    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)
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
                        tBinding.ivUpload,
                        R.drawable.gallery
                    )
                    imgPath = FileUtils.compressImage(
                        FileUtils.getRealPathFromUri(
                            requireActivity(),
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
                            tBinding.ivUpload,
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
    user_id:String
    ) {
        getBaseActivity().baseViewModel.repository.addPost(
            getBaseActivity().apiListener(), image,post_title, post_type, description, price,user_id
        ).observe(viewLifecycleOwner) {
            if (it.success) {

                getBaseActivity().showToast(it.message)
                val intent=Intent(requireContext(), BusinessesDashboardActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

}