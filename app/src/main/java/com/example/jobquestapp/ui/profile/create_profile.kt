package com.example.jobquestapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.jobquestapp.MainActivity
import com.example.jobquestapp.R
import com.example.jobquestapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class create_profile : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextLastname: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextNumber_phone: EditText
    private lateinit var editTextAbout_me: EditText
    private lateinit var buttonLogin: Button

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        editTextName = findViewById(R.id.editTextName)
        editTextLastname = findViewById(R.id.editTextLastname)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextNumber_phone = findViewById(R.id.editTextNumber_phone)
        editTextAbout_me = findViewById(R.id.editTextAbout_me)
        buttonLogin = findViewById(R.id.buttonLogin)


        buttonLogin.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    // Вызываем функцию logIn для выполнения запроса
                    val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    val data_id = sharedPreferences.getString("user_id", "")
                    val user_id = data_id?.toInt()
                    val data = api_resource()
                    val result = user_id?.let { it1 ->
                        data.create_profile(editTextName.text.toString(), editTextLastname.text.toString(), editTextEmail.text.toString(), editTextNumber_phone.text.toString(), editTextAbout_me.text.toString(),
                            it1
                        )
                    }

                    if (result != null) {
                        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val user_id = sharedPreferences.getString("user_id", "")
                        editor.putString("profile_id", result.profile_id.toString())
                        editor.putString("name", editTextName.text.toString())
                        editor.putString("surname", editTextLastname.text.toString())
                        editor.putString("about_me", editTextAbout_me.text.toString())
                        editor.putString("email", editTextEmail.text.toString())
                        editor.putString("number_phone", editTextNumber_phone.text.toString())
                        editor.putString("user_id", user_id)
                        // Если успешно авторизованы, выводим сообщение об успешной авторизации и обрабатываем данные
                        Log.d("LoginActivity", "Login successful")
                        Toast.makeText(this@create_profile, result.message, Toast.LENGTH_SHORT)

                        editor.putString("profile_id", result.profile_id.toString())
                        editor.putString("profile", "true")
                        editor.apply()
                        val intent = Intent(this@create_profile, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        // Обработка случая, когда result равен null
                        Log.e("LoginActivity", "Login failed - result is null")
                        if (result != null) {
                            Toast.makeText(this@create_profile, result.message, Toast.LENGTH_SHORT)
                        }
                    }
                } catch (e: Exception) {
                    // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                    Log.e("LoginActivity", "Error during login", e)
                    e.printStackTrace()
                    val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("login", "false")
                    editor.apply()
                }
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Закрыть текущую активность
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}