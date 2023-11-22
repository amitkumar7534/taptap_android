package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yookoo.discoveraddis.R
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        @SuppressLint("MissingInflatedId", "LocalSuppress") val tv =
            findViewById<TextView>(R.id.hello)
        val url = "http://197.156.78.113:8080/ClientApi-1.0/esbrestapi/ping"
        val jsonObjectRequest = JsonObjectRequest(url, null, { response ->
            try {
                val userId = response.getInt("userId")
                val id = response.getInt("id")
                val title = response.getString("title")
                val completed = response.getBoolean("completed")
                tv.text = """
                     $userId
                     $id
                     $title
                     $completed
                     """.trimIndent()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            tv.text = response.toString()
        }) { tv.text = "Error" }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }
}