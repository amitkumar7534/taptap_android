package com.example.myapplication.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.yookoo.discoveraddis.R
import java.io.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/*
* this is Common Methods class that are used in the app.
*
* In Kotlin, object is a special class that only has one instance. If you create a class with
* the object keyword instead of class, the Kotlin compiler makes the constructor private,
*  creates a static reference for the object, and initializes the reference in a static block.
*
* */
@SuppressLint("StaticFieldLeak")
object CommonMethods {

    const val PER_CAMERA = Manifest.permission.CAMERA
    const val PER_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val PER_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    lateinit var mContext: Context
    operator fun invoke(applicationContext: Context?) {
        mContext = applicationContext!!
    }

    fun addFragmentActivity(
        fragmentManager: FragmentManager?,
        fragment: Fragment,
        container: Int
    ) {
        fragmentManager!!.beginTransaction()
            .add(container, fragment)
            .commit()
    }

    fun replaceFragmentActivityWithoutStack(
        fragmentManager: FragmentManager?,
        fragment: Fragment, container: Int
    ) {
        fragmentManager!!.beginTransaction()
            .replace(container, fragment)
            .commit()
    }

    fun replaceFragmentScreenWithBackStack(
        fragmentManager: FragmentManager?,
        fragment: Fragment, container: Int
    ) {
        fragmentManager!!.beginTransaction()
            .replace(container, fragment)
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun phoneIsOnline(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    //this method return the phone is Connected with internet or Not
    fun phoneIsOnline(): String? {
        val cm =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnectedOrConnecting) {
            "Something went wrong. Please try again later."
        } else {
            "Internet connection not available!"
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme != null && uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            if (result != null) {
                val cut = result.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
        }
        return result
    }

    private fun splitFileName(fileName: String): Array<String>? {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    private fun rename(file: File, newName: String): File? {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight;
        val width = options.outWidth;
        var inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio
            } else {
                inSampleSize = widthRatio
            }

        }
        var totalPixels = width * height;
        var totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    object CompressFile {
        fun getCompressedImageFile(file: File): File? {
            return try {
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                if (getFileExt(file.name) == "png" || getFileExt(file.name) == "PNG") {
                    o.inSampleSize = 6
                } else {
                    o.inSampleSize = 6
                }
                var inputStream = FileInputStream(file)
                BitmapFactory.decodeStream(inputStream, null, o)
                inputStream.close()

                // The new size we want to scale to
                val REQUIRED_SIZE = 100

                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE
                ) {
                    scale *= 2
                }
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                inputStream = FileInputStream(file)
                var selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
                val ei = ExifInterface(file.absolutePath)
                val orientation: Int = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> selectedBitmap =
                        rotateImage(selectedBitmap, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> selectedBitmap =
                        rotateImage(selectedBitmap, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> selectedBitmap =
                        rotateImage(selectedBitmap, 270f)
                    ExifInterface.ORIENTATION_NORMAL -> {
                    }
                    else -> {
                    }
                }
                inputStream.close()


                // here i override the original image file
                val folder =
                    File(Environment.getExternalStorageDirectory().toString() + "/FolderName")
                var success = true
                if (!folder.exists()) {
                    success = folder.mkdir()
                }
                if (success) {
                    val newFile = File(File(folder.absolutePath), file.name)
                    if (newFile.exists()) {
                        newFile.delete()
                    }
                    val outputStream = FileOutputStream(newFile)
                    if (getFileExt(file.name) == "png" || getFileExt(file.name) == "PNG") {
                        selectedBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    } else {
                        selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                    newFile
                } else {
                    null
                }
            } catch (e: java.lang.Exception) {
                null
            }
        }

        fun getFileExt(fileName: String): String {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)
        }

        fun rotateImage(source: Bitmap?, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(
                source!!, 0, 0, source!!.width, source!!.height,
                matrix, true
            )
        }
    }


    fun compressImage(filePath: String?): String? {
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()

// by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
// you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

// max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 1024.0f
        val maxWidth = 1024.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

// width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

// setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

// inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

// this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
// load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

// check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val out: FileOutputStream
        val filename = getFilename()
        try {
            out = FileOutputStream(filename)

// write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    private fun getFilename(): String? {
        val file = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
// val file = File(Environment.getExternalStorageDirectory().path, ".spongy/Images")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
    }


    fun getRealPathFromUri(context: Context, uri: Uri?): File? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            val fileName: String = getFileName(context, uri)!!
            val splitName: Array<String> = splitFileName(fileName)!!
            var tempFile = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            val out = FileOutputStream(tempFile)
            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }
            out.close()
            return tempFile
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    private fun copy(input: InputStream, output: OutputStream) {
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (-1 != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
        }
    }

    fun changeImageOrientation(photoPath: String?, bitmap: Bitmap): Bitmap? {
        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(photoPath!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var orientation = 0
        if (ei != null) {
            orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
        }
        val rotatedBitmap: Bitmap
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                bitmap,
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                bitmap,
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                bitmap,
                270f
            )
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }


    fun getResizedBitmap(
        bitmapImage: Bitmap?,
        bitmapWidth: Int,
        bitmapHeight: Int,
    ): Bitmap? {
        return Bitmap.createScaledBitmap(bitmapImage!!, bitmapWidth, bitmapHeight, true)
    }

    fun createFileFromBitMap(bitmap: Bitmap): File? {
        val file: File
        val imageFileName =
            "FreeSpot App" + "-" + System.currentTimeMillis() + ".jpg"
        val myDirectory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Pictures"
            )
        } else {
            File(Environment.getExternalStorageDirectory(), "FreeSpot App")
        }
        if (!myDirectory.exists()) {
            myDirectory.mkdir()
        }
        file = File(myDirectory, imageFileName)
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        //write the bytes in file
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file
    }



    fun createImageFile(mContext: Context): File {
        val imageFileName = "Dive App" + "_" + System.currentTimeMillis()
        val storageDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Pictures"
            )
        } else {
            File(Environment.getExternalStorageDirectory(), "Dive App")
        }

        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        var image: File
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
            val newImageFileName = "Dive App" + "-" + System.currentTimeMillis() + ".jpg"
            image = File(storageDir, newImageFileName)
            try {
                image.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return image
    }

    fun checkPermission(
        context: Context?,
        permission: String?,
    ): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isReadStorageGranted(context: Context?): Boolean {
        return checkPermission(
            context,
            PER_READ_STORAGE
        )
    }

    //define functions for each permission
    fun isCameraGranted(context: Context?): Boolean {
        return checkPermission(
            context,
            PER_CAMERA
        )
    }



    fun logPrint(msg: String) {
        Log.e("AppNameLog", msg)
    }

    private lateinit var mAlertDialog: AlertDialog
    /*fun showBaseProgressDialog(context: Context) {
        try {
            val mAlertDialogBuilder = AlertDialog.Builder(context)
            mAlertDialogBuilder.setCancelable(false)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.view_progress_dialog, null)
            view.findViewById<ProgressBar>(R.id.progressBarPB).apply {
                indeterminateDrawable = MultiplePulseRing()
            }
            view.findViewById<TextView>(R.id.progressMessageTV).apply {
                text = ""
            }
            mAlertDialogBuilder.setView(view)
            mAlertDialog = mAlertDialogBuilder.create()
            mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            try {

                if (mAlertDialog.isShowing.not()) {
                    mAlertDialog.show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("appClosedHere", " AppClosed " + e.message)
            }

            val layoutParams = WindowManager.LayoutParams()
            val window = mAlertDialog.window
            window!!.setGravity(Gravity.CENTER)
            layoutParams.copyFrom(window.attributes)
            // show dialog on full screen
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = layoutParams
        } catch (e: Exception) {
        }
    }*/

    fun hideBaseProgressDialog() {
        if (this::mAlertDialog.isInitialized && mAlertDialog.isShowing) {
            try {
                mAlertDialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("appClosedHere", " AppClosed " + e.message)
            }
        }
    }

    fun glideImageInt(url: String, sContext: Context, imageView: ImageView) {
        Glide.with(sContext)
            .load(url)
            .placeholder(R.drawable.ic_business_location)
            .into(imageView)

    }



    fun EMailValidation(emailString: String?): Boolean {
        if (null == emailString || emailString.isEmpty()) {
            return false
        }
        val emailPattern: Pattern = Pattern
            .compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            )
        val emailMatcher: Matcher = emailPattern.matcher(emailString)
        return emailMatcher.matches()
    }

    // this method is used to validation in password field
    fun isValidPassword(password: String): Boolean {
        return Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_])(?=\\S+$).{10,20}$")
            .matcher(password).matches()
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}