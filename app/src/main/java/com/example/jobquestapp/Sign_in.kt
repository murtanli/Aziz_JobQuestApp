package com.example.jobquestapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.jobquestapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.jobquestapp.api.api_resource

class Sign_in : AppCompatActivity() {

    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextPassword2: EditText
    private lateinit var textView6: TextView
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextPassword2 = findViewById(R.id.editTextPassword2)
        textView6 = findViewById(R.id.textView6)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {

            if (!editTextLogin.text.isNullOrEmpty() && !editTextPassword.text.isNullOrEmpty() && !editTextPassword2.text.isNullOrEmpty() && editTextPassword.text in editTextPassword2.text) {
                val loginText = editTextLogin?.text?.toString()
                val passwordText = editTextPassword?.text?.toString()
                GlobalScope.launch(Dispatchers.Main) {
                    try {

                        val data = api_resource()
                        val result = data.Sign_in(loginText.toString(), passwordText.toString())

                        if (result != null) {
                            val intent = Intent(this@Sign_in, Log_in::class.java)
                            startActivity(intent)
                            textView6.text = result.message

                            val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("profile", "false")
                            editor.putString("user_id", result.user_id)
                            editor.apply()

                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            textView6.text = "Ошибка в процессе авторизации $result.message"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        textView6.text = "Ошибка входа: Неправильный пароль или профиль уже существует"
                    }
                }
            } else {
                textView6.text = "Пустые поля ! либо пороли не совпадают"
            }
        }
    }
}