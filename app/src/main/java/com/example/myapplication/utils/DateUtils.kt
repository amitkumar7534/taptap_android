package com.example.myapplication.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/*
 @RequiresApi(Build.VERSION_CODES.N)
 fun currentYear(): String = SimpleDateFormat("MMMM YYYY").format(Date().time)
*/

fun dateFormat(dateString: String?=""): String {
 try {
  val utc = TimeZone.getDefault()
  val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  sourceFormat.timeZone =TimeZone.getDefault()

  val destFormat = SimpleDateFormat("MMM d, yyyy HH:mm:ss")
  destFormat.timeZone = TimeZone.getDefault()


  val convertedDate = sourceFormat.parse(dateString)
  return destFormat.format(convertedDate)

 } catch (e: ParseException) {
  e.printStackTrace()
  return ""
 } catch (t:Throwable){
  return ""
 }

}
