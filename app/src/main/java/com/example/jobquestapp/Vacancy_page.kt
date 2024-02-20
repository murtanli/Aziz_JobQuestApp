package com.example.jobquestapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.jobquestapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Vacancy_page : AppCompatActivity() {

    private lateinit var vacancy_title: TextView
    private lateinit var vacancy_salary: TextView
    private lateinit var company_name: TextView
    private lateinit var button_otclick: Button
    private lateinit var company_city: TextView
    private lateinit var vacancy_text: TextView
    private lateinit var pole_date: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacancy_page)

        vacancy_title = findViewById(R.id.Vacancy_title)
        vacancy_salary = findViewById(R.id.Vacancy_salary)
        company_name = findViewById(R.id.Company_name)
        company_city = findViewById(R.id.Company_city)
        vacancy_text = findViewById(R.id.Vacancy_text)
        button_otclick = findViewById(R.id.button_otclick)
        pole_date = findViewById(R.id.date)

        val title = intent.getStringExtra("news_title")
        val salary = intent.getStringExtra("salary")
        val name = intent.getStringExtra("company_name")
        val text = intent.getStringExtra("text")
        val city = intent.getStringExtra("city")
        val id = intent.getStringExtra("id")
        val date = intent.getStringExtra("date")

        val id_int = id?.toIntOrNull() ?: 0
        vacancy_title.text = title
        vacancy_salary.text = salary
        company_name.text = name
        company_city.text = city
        vacancy_text.text = text
        pole_date.text = "Опубликовано - $date"

        button_otclick.setOnClickListener {
            val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val log = sharedPreferences?.getString("profile", "")

            if (log == "false"){
                Toast.makeText(this, "Создайте профиль", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val sh = sharedPreferences?.getString("profile_id", "")
                        val profile_id = sh?.toIntOrNull() ?: 0

                        val data = api_resource()
                        val result = data.create_applications(profile_id, id_int)

                        // Проверка на успешный код ответа сервера
                        if (result != null) {
                            runOnUiThread {
                                Toast.makeText(this@Vacancy_page, result.message, Toast.LENGTH_SHORT).show()
                            }
                            Log.d("CreateApplication", "Application created successfully: ${result.message}")
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("CreateApplication", "Failed to create application - result is null")
                        }
                    } catch (e: Exception) {
                        // Обработка исключений
                        Log.e("CreateApplication", "Error during creating application", e)
                        e.printStackTrace()
                    }
                }

            }
        }
    }
}