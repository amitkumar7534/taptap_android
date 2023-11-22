package com.example.myapplication.utils



import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FileUtils {

    companion object {


        private const val EOF = -1
        private const val DEFAULT_BUFFER_SIZE = 1024 * 4


        @Throws(IOException::class)
        fun from(context: Context, uri: Uri): File? {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = getFileName(context, uri)
            val splitName = splitFileName(fileName)
            var tempFile = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(tempFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }
            out?.close()
            return tempFile
        }

        private fun splitFileName(fileName: String?): Array<String?> {
            var name = fileName
            var extension: String? = ""
            val i = fileName!!.lastIndexOf(".")
            if (i != -1) {
                name = fileName.substring(0, i)
                extension = fileName.substring(i)
            }
            return arrayOf(name, extension)
        }

        @SuppressLint("Range")
        private fun getFileName(
            context: Context,
            uri: Uri
        ): String? {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor =
                    context.contentResolver.query(uri, null, null, null, null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    cursor?.close()
                }
            }
            if (result == null) {
                result = uri.path
                val cut = result!!.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
            return result
        }

        private fun rename(file: File, newName: String?): File {
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

        @Throws(IOException::class)
        private fun copy(input: InputStream, output: OutputStream?): Long {
            var count: Long = 0
            var n: Int
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (EOF != input.read(buffer).also { n = it }) {
                output!!.write(buffer, 0, n)
                count += n.toLong()
            }
            return count
        }

        fun uploadImage(mFile: File): MultipartBody.Part {
            var fileName: String
            val newFile: File
            if (mFile.name.trim().contains("jpg") || mFile.name.trim()
                    .contains("png") || mFile.name.trim().contains("jpeg")
            ) {
                newFile = File(
                    Environment.getRootDirectory()
                        .toString() + "/Back2Life" + File.separator + "BACK_2_LIFE_IMG" + System.currentTimeMillis() + ".png"
                )
                mFile.renameTo(newFile)
                fileName = newFile.name
            } else {
                fileName = mFile.name
            }
            if (fileName.contains(" ")) fileName = fileName.replace(" ".toRegex(), "")
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mFile)
            return MultipartBody.Part.createFormData("image", fileName, requestFile)
        }


        /*   @SuppressLint("ResourceType")
           fun paymentMethods(context: Context):List<PaymentMethodRequest>{
               val imgRes = context.resources.obtainTypedArray(R.array.payment_method_images)
               val list = ArrayList<PaymentMethodRequest>()
               list.add(PaymentMethodRequest(imgRes.getResourceId(0, 0), 0, "Paypal"))
               list.add(PaymentMethodRequest(imgRes.getResourceId(1, 1), 1, "Google Pay"))
               list.add(PaymentMethodRequest(imgRes.getResourceId(2, 3), 2, "Apple Pay"))
               list.add(PaymentMethodRequest(imgRes.getResourceId(3, 3), 3, "Credit/Debit Card"))
               return list
           }*/


        fun isValidEmailId(email: String): Boolean {
            return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }

        fun isValidPassword(password: String?): Boolean {
            val pattern: Pattern
            val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            val matcher: Matcher = pattern.matcher(password)
            return matcher.matches()
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

        @JvmName("splitFileName1")
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


    }






}