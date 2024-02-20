package com.example.jobquestapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.jobquestapp.R
import com.example.jobquestapp.Vacancy_page
import com.example.jobquestapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class navigation_home : Fragment() {

    companion object {
        fun newInstance() = navigation_home()
    }

    private lateinit var viewModel: NavigationHomeViewModel
    private lateinit var VacancyContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navigation_home, container, false)
        VacancyContainer = view.findViewById(R.id.VacancyContainer)
        return view

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NavigationHomeViewModel::class.java)

        val all_data = api_resource()
        val sharedPreferences = context?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val sh = sharedPreferences?.getString("profile_id", "")
        val profile_id = sh?.toIntOrNull() ?: 0
        if (profile_id != 0)
        {
            lifecycleScope.launch {
                try {
                    val vacancyList = all_data.get_applications_id(profile_id)
                    vacancyList?.let { list ->
                        for (vacancyItem in list) {
                            // Создайте блок вакансии и добавьте его в контейнер

                            val vacancyBlock = createVacancyBlock(vacancyItem.vacancy_name, vacancyItem.id, vacancyItem.vacancy_title, vacancyItem.vacancy_calary, vacancyItem.date_publish, vacancyItem.company_name, vacancyItem.company_city, vacancyItem.application.status_vac, vacancyItem.application.application_id)
                            VacancyContainer.addView(vacancyBlock)
                        }
                        // Обработка результата
                    } ?: Log.e("VacancyError", "Vacancy list is null")
                } catch (e: Exception) {
                    Log.e("VacancyError", "Error in navigation_home", e)
                    // Обработка ошибки
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createVacancyBlock(title: String, id: Int, text: String, salary: String, date: String, company_name: String, company_city: String, status_vac: String, application_id: Int): LinearLayout {
        val VacancyBlock = LinearLayout(requireContext())
        val padding_in_layout= resources.getDimensionPixelOffset(R.dimen.layout_padding)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)
        VacancyBlock.layoutParams = layoutParams

        VacancyBlock.orientation = LinearLayout.VERTICAL

        // Стилизация LinearLayout в котором присоеденил блок rounded_background с белым цветом и закругленными углами
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_view)
        VacancyBlock.background = backgroundDrawable

        // Стилизация текста
        val titleTextView = TextView(requireContext())
        titleTextView.text = title
        titleTextView.setTextAppearance(R.style.VacancyTitle)
        // Установка отступа для текста внутри блока
        val paddingInPixels = resources.getDimensionPixelOffset(R.dimen.text_padding)
        titleTextView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

        val company_nameTextView = TextView(requireContext())
        company_nameTextView.text = company_name
        company_nameTextView.setTextAppearance(R.style.Vacancy_company_name)
        company_nameTextView.setPadding(paddingInPixels, 0, 0, 0)

        val company_cityTextView = TextView(requireContext())
        company_cityTextView.text = company_city
        company_cityTextView.setTextAppearance(R.style.Vacancy_company_city)
        company_cityTextView.setPadding(paddingInPixels, 10, 0, 0)

        val calaryTextView = TextView(requireContext())
        calaryTextView.text = "З/п - ${salary}"
        calaryTextView.setTextAppearance(R.style.VacancyCalary)
        val paddingInPixels_calary = resources.getDimensionPixelOffset(R.dimen.text_padding)
        calaryTextView.setPadding(paddingInPixels_calary, 80, paddingInPixels_calary, paddingInPixels_calary)

        val statustextView = TextView(requireContext())
        statustextView.text = "Статус - ${status_vac}"

        statustextView.setPadding(paddingInPixels_calary, 80, 0, 0)

        when (status_vac) {
            "Отправлено" -> {
                statustextView.setTextAppearance(R.style.status_text)
                statustextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_light))
            }
            "Рассматривается" -> {
                statustextView.setTextAppearance(R.style.status_text)
                statustextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
            }
            "Принято" -> {
                statustextView.setTextAppearance(R.style.status_text)
                statustextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                statustextView.text = "Пожалуйста, свяжитесь с организацией, ваш статус - $status_vac"
            }
            "Отклонено" -> {
                statustextView.setTextAppearance(R.style.status_text)
                statustextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        }

        // Парсинг строки в объект LocalDate
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(date, formatter)

        // Получение года, месяца и дня из объекта LocalDate

        val month = date.month.getDisplayName(java.time.format.TextStyle.FULL, Locale("ru"))

        val day = date.dayOfMonth

        val dateTextView = TextView(requireContext())
        dateTextView.text = "Опубликовано - $day  $month"
        dateTextView.setTextAppearance(R.style.VacancyDate)
        val paddingInPixels_date = resources.getDimensionPixelOffset(R.dimen.text_padding)
        dateTextView.setPadding(paddingInPixels_date, paddingInPixels_date, paddingInPixels_date, paddingInPixels_date)


        //Стилизация кнопки
        val news_button = Button(requireContext())
        news_button.text = "Убрать отклик"
        val backgroundButton = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button)
        news_button.background = backgroundButton


        VacancyBlock.setOnClickListener{
            val intent = Intent(requireContext(), Vacancy_page::class.java)
            intent.putExtra("news_title", title)
            intent.putExtra("text", text)
            intent.putExtra("company_name", company_name)
            intent.putExtra("date", "${day}  ${month}")
            intent.putExtra("salary", salary)
            intent.putExtra("city", company_city)
            intent.putExtra("id", id.toString())
            startActivity(intent)
        }


        news_button.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(300)
                .withEndAction {
                    // Возвращение к обычному размеру после анимации
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                    val sharedPreferences = context?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    val log = sharedPreferences?.getString("profile", "")

                    if (log == "false"){
                        Toast.makeText(requireContext(), "Создайте профиль", Toast.LENGTH_SHORT).show()
                    } else {
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {


                                val data = api_resource()
                                val result = data.delete_applications(application_id)
                                Log.e("ID", id.toString())
                                // Проверка на успешный код ответа сервера
                                if (result != null) {
                                    activity?.runOnUiThread {
                                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                                    }
                                    Log.d("CreateApplication", "Application created successfully: ${result.message}")
                                } else {
                                    // Обработка случая, когда result равен null
                                    Log.e("CreateApplication", "Failed to create application - result is null ")

                                }
                            } catch (e: Exception) {
                                // Обработка исключений
                                Log.e("CreateApplication", "Error during creating application", e)
                                e.printStackTrace()
                            }
                        }

                    }
                }
                .start()

        }
        VacancyBlock.addView(titleTextView)
        VacancyBlock.addView(company_nameTextView)
        VacancyBlock.addView(company_cityTextView)
        VacancyBlock.addView(calaryTextView)
        VacancyBlock.addView(statustextView)
        VacancyBlock.addView(dateTextView)
        VacancyBlock.addView(news_button)

        return VacancyBlock
    }

}