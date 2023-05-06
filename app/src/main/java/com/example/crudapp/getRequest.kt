package com.example.crudapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class getRequest : AppCompatActivity() {

    private lateinit var userDataTextView: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getrequest)

        userDataTextView = findViewById(R.id.user_data)
        userDataTextView.text = "GET REQUEST"

        fetchData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = executeRequest()
                handleResponse(result)
            } catch (e: Exception) {
                Log.e("getRequest", "Error: ${e.message}")
            }
        }
    }

    private suspend fun executeRequest(): String {
        val url = URL("http://your_ip_address/folder_with_php_files/your_php_file_GET.php")
        val connection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStreamReader = InputStreamReader(connection.inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val response = StringBuilder()

            var inputLine: String?
            while (withContext(Dispatchers.IO) {
                    bufferedReader.readLine()
                }.also { inputLine = it } != null) {
                response.append(inputLine)
            }
            withContext(Dispatchers.IO) {
                bufferedReader.close()
            }

            return response.toString()
        } else {
            return ""
        }
    }

    private suspend fun handleResponse(result: String) {
        withContext(Dispatchers.Main) {
            Log.d("getRequest", "Response: $result")

            val userData = StringBuilder()
            try {
                val jsonResponse = JSONObject(result)
                val error = jsonResponse.getBoolean("error")
                if (!error) {
                    val users = jsonResponse.getJSONArray("users")
                    for (i in 0 until users.length()) {
                        val user = users.getJSONObject(i)
                        val name = user.getString("name")
                        val realname = user.getString("realname")
                        val rating = user.getString("rating")
                        val position = user.getString("position")
                        userData.append("Name: $name\nReal Name: $realname\nRating: $rating\nPosition: $position\n\n")
                    }
                    val userDataTextView = findViewById<TextView>(R.id.user_data)
                    userDataTextView.text = userData.toString()
                } else {
                    val message = jsonResponse.getString("message")
                    Log.w("getRequest", "Error message: $message")
                }
            } catch (e: JSONException) {
                Log.e("getRequest", "JSON error: ${e.message}")
            }
        }
    }

}

