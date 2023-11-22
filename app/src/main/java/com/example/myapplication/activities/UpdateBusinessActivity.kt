package com.example.myapplication.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.myapplication.api.response.business.Data
import com.yookoo.discoveraddis.databinding.ActivityUpdateBusinessBinding
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.FileUtils
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.yookoo.discoveraddis.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

class UpdateBusinessActivity : BindingActivity<ActivityUpdateBusinessBinding>() {
    val latlng: String = ""
    var lat: String = ""
    var lng: String = ""
    var mPhotoFile: File? = null
    var formatedDate2: String = ""
    val PERMISSION_CODE = 200
    var imgPath: String = ""
    val REQUEST_TAKE_PHOTO = 101
    val REQUEST_GALLERY_PHOTO = 201
    lateinit var  data:Data

    override fun getLayout(): Int {
        return R.layout.activity_update_business
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_api_keys))
        }

        data = intent.getSerializableExtra("data") as Data


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
        viewBinding.txtregBusinessName.setText(data.businessname)
        viewBinding.txtregBusinessDescription.setText(data.businessspecializesin.split(",").get(0)?:"")
        if (data.businessspecializesin.split(",").size>1) {
            viewBinding.txtregBusinessSpecialization.setText(
                data.businessspecializesin.split(",").get(1) ?: ""
            )
        }else{
            viewBinding.txtregBusinessSpecialization.setText(data.businessspecializesin.split(",").get(0))

        }
        viewBinding.txtregBusinessPhone.setText(data.businessphone)
        viewBinding.txtregBusinessCellphone.setText(data.businesscellphone)
        viewBinding.txtregBusinessAddress.setText(data.businessaddress)
        viewBinding.txtregBusinessHousenumber.setText("102")
        viewBinding.txtregBusinessEmail.setText(data.registrationnumebr)
        viewBinding.txtregBusinessWorkinghrs.setText(data.operatinghrs)
        viewBinding.tvCity.setText(data.businesscity)
        viewBinding.tvRegion.setText(data.businessregion)
        viewBinding.tvZipcode.setText(data.postalcode)
        viewBinding.txtregBusinessGrade.setText(data.businessstar)
        viewBinding.tvBusinessWord.setText(data.businessworeda)
        viewBinding.spinnerCategory.setText(data.businesscategory)
        viewBinding.txtregBusinessRegistrationNumber.setText(data.registrationnumebr)
        viewBinding.txtregBusinessLatlng.setText(data.latlng)

        if(data.device_id==Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)){
            viewBinding.busregBtn.visibility=View.VISIBLE
        }else{
            viewBinding.busregBtn.visibility=View.VISIBLE
        }

        Glide.with(this).load(data.logo).placeholder(R.drawable.loading)
            .into(viewBinding.busregImg)


        viewBinding.busregBtn.setOnClickListener {
            if (viewBinding.txtregBusinessName.text.toString() == "") {
                showToast("Please enter business name")
            } else if (viewBinding.txtregBusinessDescription.text.toString() == "") {
                showToast("Please enter business description")
            } else if (viewBinding.txtregBusinessSpecialization.text.toString() == "") {
                showToast("Please enter business specialization")
            } else if (viewBinding.txtregBusinessPhone.text.toString() == "") {
                showToast("Please enter business phone")
            } else if (viewBinding.txtregBusinessCellphone.text.toString() == "") {
                showToast("Please enter business cell phone")
            } else if (viewBinding.txtregBusinessAddress.text.toString() == "") {
                showToast("Please enter business address")
            } else if (viewBinding.txtregBusinessHousenumber.text.toString() == "") {
                showToast("Please enter business house number")
            } else if (viewBinding.txtregBusinessEmail.text.toString() == "") {
                showToast("Please enter business website")
            } else if (viewBinding.txtregBusinessWorkinghrs.text.toString() == "") {
                showToast("Please enter working hours")
            } else if (viewBinding.txtregBusinessGrade.text.toString() == "") {
                showToast("Please enter business grade")
            } else{
                if (mPhotoFile == null) {
                    createBusinessWithoutPhoto(
                        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
                        viewBinding.txtregBusinessName.text.toString(),
                        viewBinding.txtregBusinessPhone.text.toString(),
                        viewBinding.txtregBusinessCellphone.text.toString(),
                        viewBinding.txtregBusinessAddress.text.toString(),
                        viewBinding.spinnerCategory.text.toString(),
                        viewBinding.txtregBusinessLatlng.text.toString(),
                        viewBinding.txtregBusinessWorkinghrs.text.toString(),
                        viewBinding.txtregBusinessRegistrationNumber.text.toString(),
                        viewBinding.tvBusinessWord.text.toString(),
                        viewBinding.tvCity.text.toString(),
                        viewBinding.tvRegion.text.toString(),
                        viewBinding.tvZipcode.text.toString(),
                        viewBinding.txtregBusinessDescription.text.toString()+","+viewBinding.txtregBusinessSpecialization.text.toString(),
                        viewBinding.txtregBusinessGrade.text.toString()
                    )
                } else {
                    createBusiness(
                        mPhotoFile,
                        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
                        viewBinding.txtregBusinessName.text.toString(),
                        viewBinding.txtregBusinessPhone.text.toString(),
                        viewBinding.txtregBusinessCellphone.text.toString(),
                        viewBinding.txtregBusinessAddress.text.toString(),
                        viewBinding.spinnerCategory.text.toString(),
                        viewBinding.txtregBusinessLatlng.text.toString(),
                        viewBinding.txtregBusinessWorkinghrs.text.toString(),
                        viewBinding.txtregBusinessRegistrationNumber.text.toString(),
                        viewBinding.tvBusinessWord.text.toString(),
                        viewBinding.tvCity.text.toString(),
                        viewBinding.tvRegion.text.toString(),
                        viewBinding.tvZipcode.text.toString(),
                        viewBinding.txtregBusinessDescription.text.toString()+","+viewBinding.txtregBusinessSpecialization.text.toString(),
                        viewBinding.txtregBusinessGrade.text.toString()
                    )
                }

            }

        }


        viewBinding.txtregBusinessAddress.setOnClickListener {
            val fields: List<Place.Field> = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).build(this)
            startActivityForResult(intent, 102)
        }

        viewBinding!!.ivBack.setOnClickListener { onBackPressed() }
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
        if (requestCode == 102) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data)
                Log.i("TAG", "Place: " + place.name + ", " + place.id + ", " + place.address)


                val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
                lat = place.latLng.latitude.toString()
                lng = place.latLng.longitude.toString()
                val addresses: List<Address>? = geocoder.getFromLocation(
                    place.latLng.latitude,
                    place.latLng.longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                val address: String =
                    addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val city: String = addresses!![0].locality
                val state: String = addresses!![0].adminArea
                val country: String = addresses!![0].countryName
                val postalCode: String = addresses!![0].postalCode
                val knownName: String = addresses!![0].featureName
                viewBinding.txtregBusinessAddress.setText(address.split(",").get(0))
                viewBinding.tvCity.setText(city)
                viewBinding.tvRegion.setText(state)
                viewBinding.tvZipcode.setText(postalCode)
                viewBinding.txtregBusinessLatlng.setText("$lat,$lng")


                // do query with address
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data)
                Toast.makeText(
                    this,
                    "Error: " + status.statusMessage,
                    Toast.LENGTH_LONG
                ).show()
                Log.i("TAG", status.statusMessage.toString())
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
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


    fun createBusiness(
        image: File?,
        device_id: String,
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
        businessregion: String,
        postalcode: String,
        businessspecializesin: String,
        businessstar: String
    ) {
        baseViewModel.repository.updateBusiness(
            apiListener(),
            data.id.toString(),
            image,
            device_id,
            businessname,
            businessphone,
            businesscellphone,
            businessaddress,
            businesscategory,
            latlng,
            operatinghrs,
            registrationnumebr,
            businessworeda,
            businesscity,
            businessregion,
            postalcode,
            businessspecializesin,
            businessstar
        ).observe(this) {
            if (it.success) {
                showToast(it.message)
//                val intent=Intent(this,AllBusinessesActivity::class.java)
//                intent.putExtra("isSearchHide",false)
//                startActivity(intent)
                finish()
            }
        }
    }
    fun createBusinessWithoutPhoto(
        device_id: String,
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
        businessregion: String,
        postalcode: String,
        businessspecializesin: String,
        businessstar: String
    ) {
        baseViewModel.repository.updateBusinessWithoutPhoto(
            apiListener(),
            data.id.toString(),
            device_id,
            businessname,
            businessphone,
            businesscellphone,
            businessaddress,
            businesscategory,
            latlng,
            operatinghrs,
            registrationnumebr,
            businessworeda,
            businesscity,
            businessregion,
            postalcode,
            businessspecializesin,
            businessstar
        ).observe(this) {
            if (it.success) {
                showToast(it.message)
//                val intent=Intent(this,AllBusinessesActivity::class.java)
//                intent.putExtra("isSearchHide",false)
//                startActivity(intent)
                finish()
            }
        }
    }

}