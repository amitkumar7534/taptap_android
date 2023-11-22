package com.example.myapplication.activities

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.example.myapplication.api.response.CarData
import com.example.myapplication.api.response.business.Data
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.FileUtils
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityAddCarBinding
import com.yookoo.discoveraddis.databinding.ActivityUpdateCarBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects

class UpdateCarActivity: BindingActivity<ActivityUpdateCarBinding>() {
    override fun getLayout(): Int {
      return  R.layout.activity_update_car
    }

    lateinit var  data:CarData
    var mPhotoFile: File? = null
    var formatedDate2: String = ""
    val PERMISSION_CODE = 200
    var imgPath: String = ""
    val REQUEST_TAKE_PHOTO = 101
    val REQUEST_GALLERY_PHOTO = 201


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = intent.getSerializableExtra("data") as CarData


        Glide.with(this).load(data.vehicle_photo).placeholder(R.drawable.loading)
            .into(viewBinding.busregImg)


        viewBinding.carName.setText(data.vehicle_name)
        viewBinding.vehicleCode.setText(data.vehicle_code)
        viewBinding.ownername.setText(data.owner_name)
        viewBinding.ownerCell.setText(data.owner_phone.toString())
        viewBinding.chassisNumber.setText(data.chasis_number)
        viewBinding.plateNumber.setText(data.plate_number)

        viewBinding.insurer.setText(data.insurer)
        viewBinding.insuranceNumber.setText(data.insurance_number)
        viewBinding.insuranceType.setText(data.insurance_type)
        viewBinding.insuranceDate.setText(data.insured_date)

        Setup()
    }


    fun Setup(){

        viewBinding.busregImg.setOnClickListener {
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

        viewBinding.insuranceDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox


                var month = monthOfYear + 1

                viewBinding.insuranceDate.setText(year.toString() + "-" +month +"-"+dayOfMonth)
            }, year, month, day)
            dpd.show()
        }

        viewBinding.busregBtn.setOnClickListener {
            if (viewBinding.carName.text.toString() == "") {
                showToast("Please Enter Car Name")
            } else if (viewBinding.vehicleCode.text.toString() == "") {
                showToast("Please Enter Vehicle Code")
            } else if (viewBinding.ownername.text.toString() == "") {
                showToast("Please enter owner name")
            } else if (viewBinding.ownerCell.text.toString() == "") {
                showToast("Please enter Owner Cellphone")
            } else if (viewBinding.chassisNumber.text.toString() == "") {
                showToast("Please enter Chasis Number")
            } else if (viewBinding.plateNumber.text.toString() == "") {
                showToast("Please enter plate  number")
            } else if (viewBinding.insurer.text.toString() == "") {
                showToast("Please enter insurer value")
            } else if (viewBinding.insuranceNumber.text.toString() == "") {
                showToast("Please enter Insurance number")
            } else if (viewBinding.insuranceType.text.toString() == "") {
                showToast("Please enter Insurance type")
            } else if (viewBinding.insuranceDate.text.toString() == "") {
                showToast("Please enter Insurance date")
            } else {

                if (mPhotoFile != null){
                    UpdateCar(
                        mPhotoFile,
                        data.id.toString(),
                        viewBinding.carName.text.toString(),
                        viewBinding.vehicleCode.text.toString(),
                        viewBinding.ownername.text.toString(),
                        viewBinding.ownerCell.text.toString(),
                        viewBinding.chassisNumber.text.toString(),
                        viewBinding.plateNumber.text.toString(),
                        viewBinding.insurer.text.toString(),
                        viewBinding.insuranceNumber.text.toString(),
                        viewBinding.insuranceType.text.toString(),
                        viewBinding.insuranceDate.text.toString(),
                    )

                }else{
                    UpdateWithoutPicture(
                        data.id.toString(),
                        viewBinding.carName.text.toString(),
                        viewBinding.vehicleCode.text.toString(),
                        viewBinding.ownername.text.toString(),
                        viewBinding.ownerCell.text.toString(),
                        viewBinding.chassisNumber.text.toString(),
                        viewBinding.plateNumber.text.toString(),
                        viewBinding.insurer.text.toString(),
                        viewBinding.insuranceNumber.text.toString(),
                        viewBinding.insuranceType.text.toString(),
                        viewBinding.insuranceDate.text.toString(),
                    )
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
                    "com.example.myapplication" + ".provider", photoFile
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
                    loadGlideInCircle(
                        mPhotoFile!!.absolutePath,
                        viewBinding.busregImg,
                        R.drawable.gallery
                    )
                    imgPath = FileUtils.compressImage(
                        FileUtils.getRealPathFromUri(
                            this,
                            mPhotoFile!!.toUri()
                        )!!.absolutePath
                    ).toString ()
                    mPhotoFile = File(imgPath)
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
                        loadGlideInCircle(
                            selectedImage.toString(),
                            viewBinding.busregImg,
                            R.drawable.gallery
                        )
                        imgPath = FileUtils.compressImage(
                            FileUtils.getRealPathFromUri(
                                this,
                                selectedImage
                            )!!.absolutePath
                        ).toString()
                        mPhotoFile = File(imgPath)


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


    fun UpdateCar(
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
        insuranc_type: String,
        insurance_date: String,
    ) {
        baseViewModel.repository.UpdateCARBusiness(
            apiListener(), image,car_id,
            businessname, businessphone, businesscellphone, businessaddress,
            businesscategory, latlng, operatinghrs, registrationnumebr, insuranc_type, insurance_date
        ).observe(this) {
            if (it.success) {
                showToast(it.message)
                finish()
            }
        }
    }
    fun UpdateWithoutPicture(
        car_id: String,
        businessname: String,
        businessphone: String,
        businesscellphone: String,
        businessaddress: String,
        businesscategory: String,
        latlng: String,
        operatinghrs: String,
        registrationnumebr: String,
        insuranc_type: String,
        insurance_date: String,
    ) {
        baseViewModel.repository.UpdateCARWithoutPic(
            apiListener(),car_id.toString(),
            businessname, businessphone, businesscellphone, businessaddress,
            businesscategory, latlng, operatinghrs, registrationnumebr, insuranc_type, insurance_date
        ).observe(this) {
            if (it.success) {
                showToast(it.message)
                finish()
            }
        }
    }


}