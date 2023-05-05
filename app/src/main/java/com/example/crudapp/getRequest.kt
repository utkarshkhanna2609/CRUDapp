package com.example.crudapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class getRequest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getrequest)
        val url = URL("http://192.168.0.143/analysedDevelopers/v1/getuser.php")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStreamReader = InputStreamReader(connection.inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val response = StringBuilder()

            var inputLine: String?
            while (bufferedReader.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            bufferedReader.close()
            val userData=StringBuilder()
            val jsonResponse = JSONObject(response.toString())
            val success = jsonResponse.getInt("success")
            val users = jsonResponse.getJSONArray("users")
            if (success == 1) {
                for (i in 0 until users.length()) {
                    val user = users.getJSONObject(i)
                    val name = user.getString("name")
                    val realname = user.getString("realname")
                    val rating = user.getInt("rating")
                    val position = user.getString("position")
                    userData.append("Name: $name\nReal Name: $realname\nRating: $rating\nPosition: $position\n\n")
                    // Do something with the user data
                    val userDataTextView = findViewById<TextView>(R.id.user_data)
                    userDataTextView.text = userData.toString()
                }
            } else {
                val message = jsonResponse.getString("message")
                // Handle error message
            }
        } else {
            // Handle connection error
        }
    }
}
