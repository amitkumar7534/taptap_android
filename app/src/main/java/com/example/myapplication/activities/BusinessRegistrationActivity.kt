package com.example.myapplication.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.myapplication.api.response.CategoryResponse
import com.yookoo.discoveraddis.databinding.ActivityBusinessRegistrationBinding
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.FileUtils
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.yookoo.discoveraddis.R
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

class BusinessRegistrationActivity : BindingActivity <ActivityBusinessRegistrationBinding> () {
    val latlng:String=""
    var lat:String=""
    var lng:String=""
    var mPhotoFile: File? = null
    var formatedDate2:String=""
    val PERMISSION_CODE = 200
    var imgPath: String = ""
    val REQUEST_TAKE_PHOTO = 101
    val REQUEST_GALLERY_PHOTO = 201

    val categoryList=ArrayList<CategoryResponse>()

    private val businesscategory = arrayOf(
        "Choose Business Category",
        "Dealership",
        "Content Production",
        "Delivery",
        "Therapy",
        "Call Center",
        "Stationary",
        "Hardware Store",
        "Pastry",
        "Orphanage",
        "Real Estate",
        "Betting",
        "Marketing",
        "Mineral",
        "Logistics",
        "Gym",
        "Hotel",
        "Air BnB",
        "Property Developer",
        "Airport",
        "Restaurant",
        "Fast Food",
        "Technology",
        "Shop",
        "Printing",
        "Telecommunication",
        "Fashion Shop",
        "Gift Shop",
        "Gallery Shop",
        "Guest House",
        "Cafeteria",
        "Coffee Shop",
        "Butchery",
        "Mall",
        "Construction Company",
        "ATM",
        "Bank",
        "Foreign Exchange",
        "Event Host",
        "Beauty Salon",
        "Makeup House",
        "Hospital",
        "Clinic",
        "Pharmacy",
        "Pension",
        "Station",
        "Townhouse",
        "Import Export",
        "Manufacturer",
        "Courier",
        "Legal Firm",
        "Business Consultant",
        "Cleaning Company",
        "Spaza Shop",
        "Super Market",
        "Hyper Market",
        "Spare Part",
        "Agents|Delala",
        "Liquore Store",
        "Online Store",
        "Interior Designer",
        "Fashion Designers",
        "Content Developer",
        "Social Media Marketing",
        "Decore",
        "Photography",
        "Videography",
        "Photography and Videography",
        "Car Rental",
        "Post office",
        "Tour and Travel",
        "Laundry",
        "Open Market",
        "Book Store",
        "Furniture Store",
        "Church",
        "Mosque",
        "Tailor Shop",
        "Azmari Bet",
        "Shoe Maker",
        "Cinema",
        "Sport Club",
        "Museum",
        "Wild Life",
        "Natural Wonder",
        "Park",
        "Gallery",
        "Culture Center",
        "Resort",
        "Play Ground",
        "Embassy",
        "University",
        "College",
        "School",
        "Library",
        "Church",
        "Mosque",
        "Tavern",
        "Taxi Stand",
        "Bus Station",
        "Bus Stop",
        "Train Station",
        "Factory",
        "Night Club",
        "Laboratory",
        "Mall",
        "Supermarket",
        "Historical Site",
        "NGO",
        "Government Office",
        "Massage House",
        "Stadium",
        "Bakery",
        "Others"
    )

    val arraySpinner=ArrayList<String>()
        override fun getLayout(): Int {
        return R.layout.activity_business_registration
    }

    override fun onStart() {
        super.onStart()
        if(appSession().getFloat("lat")!=0.0F) {
            setAddress()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("cvf vf",appSession().getFloat("lat").toString())

        if(appSession().getFloat("lat")!=0.0F) {
            setAddress()
        }

        categoryList.add(CategoryResponse("Select category",R.drawable.hotel))
        categoryList.add(CategoryResponse("Hotels",         R.drawable.hotel))
        categoryList.add(CategoryResponse("BnB",            R.drawable.ic_bnb))
        categoryList.add(CategoryResponse("Mesuems",        R.drawable.ic_museums))
        categoryList.add(CategoryResponse("Parks",          R.drawable.park))
        categoryList.add(CategoryResponse("Hist Sites",     R.drawable.historic_site))
        categoryList.add(CategoryResponse("Galleries",      R.drawable.ic_sculpture))
        categoryList.add(CategoryResponse("Culture center", R.drawable.culture_dress))
        categoryList.add(CategoryResponse("Monuments",      R.drawable.fue_station))
        categoryList.add(CategoryResponse("Libraries",      R.drawable.ic_library))
        categoryList.add(CategoryResponse("African",        R.drawable.ic_library))
        categoryList.add(CategoryResponse("Rests",          R.drawable.restaurant))
        categoryList.add(CategoryResponse("Shops",          R.drawable.ic_shop))
        categoryList.add(CategoryResponse("Banks",          R.drawable.bank))
        categoryList.add(CategoryResponse("Apamts",         R.drawable.ic_apartment))
        categoryList.add(CategoryResponse("Car Rent",       R.drawable.ic_car_rent))
        categoryList.add(CategoryResponse("Post Office",    R.drawable.ic_post_office))
        categoryList.add(CategoryResponse("Tour n Tr.",     R.drawable.ic_tour_agents))
        categoryList.add(CategoryResponse("Airports",       R.drawable.ic_airport))
        categoryList.add(CategoryResponse("Laundry",        R.drawable.ic_laundry))
        categoryList.add(CategoryResponse("Open Mrkts",     R.drawable.ic_market_place))
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_api_keys))
        }

        for (i in categoryList.indices){
            arraySpinner.add(categoryList[i].name)
        }


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

        viewBinding.busregBtn.setOnClickListener {
            if(viewBinding.txtregBusinessName.text.toString()==""){
                showToast("Please enter business name")
            }else if(viewBinding.txtregBusinessDescription.text.toString()==""){
                showToast("Please enter business description")
            }else if(viewBinding.spinnerCategory.selectedItemPosition==0){
                showToast("Please select business category")
            }else if(viewBinding.txtregBusinessSpecialization.text.toString()==""){
                showToast("Please enter business specialization")
            }else if(viewBinding.txtregBusinessPhone.text.toString()==""){
                showToast("Please enter business phone")
            }else if(viewBinding.txtregBusinessCellphone.text.toString()==""){
                showToast("Please enter business cell phone")
            }else if(viewBinding.txtregBusinessAddress.text.toString()==""){
                showToast("Please enter business address")
            }else if(viewBinding.tvBusinessWord.text.toString()==""){
                showToast("Please enter business woreda")
            }else if(viewBinding.txtregBusinessHousenumber.text.toString()==""){
                showToast("Please enter business house number ")
            }else if(viewBinding.txtregBusinessEmail.text.toString()==""){
                showToast("Please enter business website ")
            }else if(viewBinding.txtregBusinessRegistrationNumber.text.toString()==""){
                showToast("Please enter business registration number")
            }else if(viewBinding.txtregBusinessWorkinghrs.text.toString()==""){
                showToast("Please enter working hours ")
            }else if(viewBinding.txtregBusinessGrade.text.toString()==""){
                showToast("Please enter business grade ")
            }else if(mPhotoFile==null){
                showToast("Please upload business logo")
            }else{
                createBusiness(
                   mPhotoFile,
                    Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),viewBinding.txtregBusinessName.text.toString(),
                    viewBinding.txtregBusinessPhone.text.toString(),viewBinding.txtregBusinessCellphone.text.toString(),
                    viewBinding.txtregBusinessAddress.text.toString(),viewBinding.spinnerCategory.selectedItem.toString(),viewBinding.txtregBusinessLatlng.text.toString(),
                    viewBinding.txtregBusinessWorkinghrs.text.toString(),viewBinding.txtregBusinessRegistrationNumber.text.toString(),viewBinding.tvBusinessWord.text.toString()+","+viewBinding.txtregBusinessEmail.text.toString(),viewBinding.tvCity.text.toString(),
                    viewBinding.tvRegion.text.toString(),viewBinding.tvZipcode.text.toString(),viewBinding.txtregBusinessDescription.text.toString()+","+viewBinding.txtregBusinessSpecialization.text.toString(),
                    viewBinding.txtregBusinessGrade.text.toString()
                )
            }
        }


        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, businesscategory
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewBinding.spinnerCategory.adapter = adapter

        viewBinding.txtregBusinessAddress.setOnClickListener {
          /*  val fields: List<Place.Field> = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).build(this)
            startActivityForResult(intent, 102)

           */
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
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data)
                Log.i("TAG", "Place: " + place.name + ", " + place.id + ", " + place.address)


                val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
                lat=place.latLng.latitude.toString()
                lng=place.latLng.longitude.toString()
                val addresses: List<Address>? = geocoder.getFromLocation(
                    place.latLng.latitude,
                    place.latLng.longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                Log.e("cvsdcvsd",addresses.toString())
                val address: String =
                    addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val city: String = addresses!![0].locality?:""
                val state: String = addresses!![0].adminArea?:""
                val country: String = addresses!![0].countryName?:""
                val postalCode: String = addresses!![0].postalCode?:""
                val knownName: String = addresses!![0].featureName?:""
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
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }else if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == -1) {
                if (mPhotoFile != null) {
                    loadGlideInCircle(mPhotoFile!!.absolutePath,viewBinding.busregImg,R.drawable.gallery)
                    imgPath= FileUtils.compressImage(
                        FileUtils.getRealPathFromUri(
                            this,
                            mPhotoFile!!.toUri()
                        )!!.absolutePath
                    ).toString()
                    mPhotoFile=File(imgPath)
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
                        loadGlideInCircle(selectedImage.toString(),viewBinding.busregImg,R.drawable.gallery)
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


  fun createBusiness(image: File?,
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
                       businessstar: String){
        baseViewModel.repository.createBusiness(apiListener(),image, device_id, businessname, businessphone, businesscellphone, businessaddress, businesscategory, latlng, operatinghrs, registrationnumebr, businessworeda, businesscity, businessregion, postalcode, businessspecializesin, businessstar).observe(this) {
            if (it.success){
                showToast(it.message)
                finish()
            }
        }
    }

    suspend fun getImage():File{
        val compressedImageFile = Compressor.compress(this@BusinessRegistrationActivity, mPhotoFile!!) {
            default(width = 640, format = Bitmap.CompressFormat.WEBP)
        }
        return compressedImageFile
    }


    fun setAddress(){
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        lat=appSession().getFloat("lat").toString()
        lng=appSession().getFloat("lng").toString()
        val addresses: List<Address>? = geocoder.getFromLocation(
            appSession().getFloat("lat").toDouble(), appSession().getFloat("lng").toDouble(),
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        Log.e("cvsdcvsd",addresses.toString())
        val address: String =
            addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        val city: String = addresses!![0].locality?:""
        val state: String = addresses!![0].adminArea?:""
        val country: String = addresses!![0].countryName?:""
        val postalCode: String = addresses!![0].postalCode?:""
        val knownName: String = addresses!![0].featureName?:""
        viewBinding.txtregBusinessAddress.setText(address)
        viewBinding.tvCity.setText(city)
        viewBinding.tvRegion.setText(state)
        viewBinding.tvZipcode.setText(postalCode)
        viewBinding.txtregBusinessLatlng.setText("$lat,$lng")
    }

}