package com.example.crudapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class getRequest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getrequest)

        GetUserDataTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetUserDataTask : AsyncTask<Void, Void, String>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void): String {
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

                return response.toString()
            } else {
                return ""
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val userData = StringBuilder()
            try {
                val jsonResponse = result?.let { JSONObject(it) }
                val success = jsonResponse?.optInt("success")
                if (success == 1) {
                    val users = jsonResponse.getJSONArray("users")
                    for (i in 0 until users.length()) {
                        val user = users.getJSONObject(i)
                        val name = user.getString("name")
                        val realname = user.getString("realname")
                        val rating = user.getInt("rating")
                        val position = user.getString("position")
                        userData.append("Name: $name\nReal Name: $realname\nRating: $rating\nPosition: $position\n\n")

                    }
                } else {
                    val message = jsonResponse?.optString("message")
                    // Handle error message
                    Log.w("Uttu","$message")
                }
            } catch (_: JSONException) {

            }
            val userDataTextView = findViewById<TextView>(R.id.user_data)
            userDataTextView.text = userData.toString()
        }

    }
}
