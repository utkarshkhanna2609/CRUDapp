package com.example.crudapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

const val ROOT_URL: String = "http://your_ip_address/folder_with_php_files/"
const val URL_REGISTER: String = ROOT_URL + "registeruser.php"

class MainActivity : AppCompatActivity() {

    private lateinit var nameEdt: EditText
    private lateinit var realNameEdt: EditText
    private lateinit var ratingEdt: EditText
    private lateinit var positionEdt: EditText
    private lateinit var button: Button
    private lateinit var buttonGet: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEdt = findViewById(R.id.name_edit_text)
        realNameEdt = findViewById(R.id.real_name_edit_text)
        ratingEdt = findViewById(R.id.rating_edit_text)
        positionEdt = findViewById(R.id.position_edit_text)
        button = findViewById(R.id.button)
        buttonGet=findViewById(R.id.get_button)
        buttonGet.setOnClickListener {
            val intent= Intent(this, getRequest::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = nameEdt.text.toString()
        val realname = realNameEdt.text.toString()
        val rating = ratingEdt.text.toString()
        val position = positionEdt.text.toString()

        val stringRequest = object : StringRequest(
            Method.POST,
            URL_REGISTER,
            Response.Listener<String> { response ->
                val json = JSONObject(response)
                Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name
                params["realname"] = realname
                params["rating"] = rating
                params["position"] = position
                return params
            }
        }

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest)
    }
}
