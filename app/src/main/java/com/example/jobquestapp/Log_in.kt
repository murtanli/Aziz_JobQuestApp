package com.example.jobquestapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.jobquestapp.api.api_resource


class Log_in : AppCompatActivity() {

    private lateinit var but_reg : TextView
    private lateinit var buttonLogin : TextView
    private lateinit var ErrorText : TextView
    private lateinit var login : EditText
    private lateinit var password : EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        but_reg = findViewById(R.id.textView2)
        buttonLogin = findViewById(R.id.buttonLogin)
        ErrorText = findViewById(R.id.textView3)
        login = findViewById(R.id.editTextLogin)
        password = findViewById(R.id.editTextPassword)

        ErrorText.text = ""

        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val log = sharedPreferences.getString("login", "")

        if (log == "true"){
            val intent = Intent(this@Log_in, MainActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val loginText = login?.text?.toString()
            val passwordText = password?.text?.toString()


            if (loginText.isNullOrBlank() || passwordText.isNullOrBlank()) {
                ErrorText.text = "Введите данные в поля"
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        // Вызываем функцию logIn для выполнения запроса
                        val data = api_resource()
                        val result = data.logIn(login.text.toString(), password.text.toString())

                        if (result != null) {
                            if (result.message == "Авторизация успешна" || result.message == "Авторизация успешна, создайте профиль !") {
                                // Если успешно авторизованы, выводим сообщение об успешной авторизации и обрабатываем данные
                                Log.d("LoginActivity", "Login successful")
                                //Log.d("LoginActivity", "User ID: ${result.user_data.user_id}")
                                ErrorText.text = result.message

                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                try {
                                    editor.putString("profile_id", result.profile_data.pk.toString())
                                    editor.putString("name", result.profile_data.name)
                                    editor.putString("surname", result.profile_data.lastname)
                                    editor.putString("about_me", result.profile_data.about_me)
                                    editor.putString("email", result.profile_data.email)
                                    editor.putString("number_phone", result.profile_data.number_phone)
                                    editor.putString("user_id", result.profile_data.user_id.toString())
                                    editor.putString("login", "true")
                                    editor.putString("profile", "true")
                                    editor.apply()
                                } catch(e: Exception) {
                                    val toast = Toast.makeText(applicationContext, "Создайте профиль !", Toast.LENGTH_LONG)
                                    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
                                    toast.show()
                                    editor.putString("user_id", result.user_id.toString())
                                    editor.putString("profile", "false")
                                    editor.putString("login", "true")
                                    editor.apply()
                                }

                                val intent = Intent(this@Log_in, MainActivity::class.java)
                                startActivity(intent)
                                //ErrorText.setTextColor(R.color.blue)

                            } else {
                                // Если произошла ошибка, выводим сообщение об ошибке
                                Log.e("LoginActivity", "Login failed")
                                ErrorText.text = result.message
                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("login", "false")
                                editor.apply()
                            }
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            ErrorText.text = "Ошибка в процессе авторизации $result.message"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        ErrorText.text = "Ошибка входа: Неправильный пароль или профиль не найден"
                        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("login", "false")
                        editor.apply()
                    }
                }
            }
        }

        but_reg.setOnClickListener {
            val intent = Intent(this, Sign_in::class.java)
            startActivity(intent)
        }

    }
}