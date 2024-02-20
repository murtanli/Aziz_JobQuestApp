package com.example.jobquestapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.jobquestapp.Log_in
import com.example.jobquestapp.MainActivity
import com.example.jobquestapp.R
import com.example.jobquestapp.Vacancy_page
import com.example.jobquestapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class navigation_profile : Fragment() {

    companion object {
        fun newInstance() = navigation_profile()
    }

    private lateinit var viewModel: NavigationProfileViewModel
    private lateinit var name_text: TextView
    private lateinit var number_phone_text: TextView
    private lateinit var email_text: TextView
    private lateinit var about_me_text: TextView
    private lateinit var button_exit: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navigation_profile, container, false)
        name_text = view.findViewById(R.id.name)
        email_text = view.findViewById(R.id.email_text)
        number_phone_text = view.findViewById(R.id.number_phone)
        about_me_text = view.findViewById(R.id.about_me_text)
        button_exit = view.findViewById(R.id.button_exit)
        return view
    }

    @SuppressLint("CommitPrefEdits")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NavigationProfileViewModel::class.java)

        val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val log = sharedPreferences.getString("profile", "")

        if (log == "false" ) {
            val intent = Intent(requireContext(), create_profile::class.java)
            startActivity(intent)
        } else {
            val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)


            val name = sharedPreferences.getString("name", "")
            val lastname = sharedPreferences.getString("surname", "")
            val about_me = sharedPreferences.getString("about_me", "")
            val email = sharedPreferences.getString("email", "")
            val number_phone = sharedPreferences.getString("number_phone", "")

            name_text.text = "${name} ${lastname}"
            email_text.text = " Email - $email"
            number_phone_text.text = "Номер телефона - $number_phone"
            about_me_text.text = about_me

            button_exit.setOnClickListener {
                val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.remove("name")
                editor?.remove("surname")
                editor?.remove("email")
                editor?.remove("about_me")
                editor?.remove("number_phone")
                editor?.remove("user_id")
                if (editor != null) {
                    editor.putString("login", "false")
                }
                if (editor != null) {
                    editor.apply()
                }


                val intent = Intent(requireContext(), Log_in::class.java)
                startActivity(intent)
            }
        }

    }





    @SuppressLint("ShowToast")
    private fun createblock_create_profile(): LinearLayout {
        // Создание блока с фоном
        val createBlock = LinearLayout(requireContext())
        val padding_in_layout = resources.getDimensionPixelOffset(R.dimen.layout_padding)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)
        createBlock.layoutParams = layoutParams
        createBlock.orientation = LinearLayout.VERTICAL
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.background_edit)
        createBlock.background = backgroundDrawable


        // Создание заголовка
        val title = TextView(requireContext())
        title.text = "Создание профиля"
        title.textSize = 20F
        title.setTextColor(requireContext().getColor(R.color.white))
        title.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        // Создание полей для ввода данных профиля
        val nameEditText = EditText(requireContext())
        nameEditText.setHint("Имя")
        nameEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        nameEditText.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        val lastnameEditText = EditText(requireContext())
        lastnameEditText.setHint("Фамилия")
        lastnameEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lastnameEditText.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        val emailEditText = EditText(requireContext())
        emailEditText.setHint("Email")
        emailEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        emailEditText.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        val numberPhoneEditText = EditText(requireContext())
        numberPhoneEditText.setHint("Номер телефона")
        numberPhoneEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        numberPhoneEditText.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        val aboutMeEditText = EditText(requireContext())
        aboutMeEditText.setHint("О себе")
        aboutMeEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        aboutMeEditText.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        val button = Button(requireContext())
        button.text = "Сохранить"
        val style_bitton = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button)
        button.background = style_bitton
        button.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        button.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        button.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(300)
                .withEndAction {
                    // Возвращение к обычному размеру после анимации
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()
            if (nameEditText.text.isEmpty() && lastnameEditText.text.isEmpty() && emailEditText.text.isEmpty() && numberPhoneEditText.text.isEmpty() && aboutMeEditText.text.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните поля", Toast.LENGTH_SHORT)
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        // Вызываем функцию logIn для выполнения запроса
                        val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val data_id = sharedPreferences.getString("user_id", "")
                        val user_id = data_id?.toInt()
                        val data = api_resource()
                        val result = user_id?.let { it1 ->
                            data.create_profile(nameEditText.text.toString(), lastnameEditText.text.toString(), emailEditText.text.toString(), numberPhoneEditText.text.toString(), aboutMeEditText.text.toString(),
                                it1
                            )
                        }

                        if (result != null) {
                            val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            val user_id = sharedPreferences.getString("user_id", "")
                            editor.putString("profile_id", result.profile_id.toString())
                            editor.putString("name", nameEditText.text.toString())
                            editor.putString("surname", lastnameEditText.text.toString())
                            editor.putString("about_me", aboutMeEditText.text.toString())
                            editor.putString("email", emailEditText.text.toString())
                            editor.putString("number_phone", numberPhoneEditText.text.toString())
                            editor.putString("user_id", user_id)
                            // Если успешно авторизованы, выводим сообщение об успешной авторизации и обрабатываем данные
                            Log.d("LoginActivity", "Login successful")
                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)

                            editor.putString("profile_id", result.profile_id.toString())
                            editor.putString("profile", "true")
                            editor.apply()
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)

                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            if (result != null) {
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)
                            }
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("login", "false")
                        editor.putString("profile_id", "")
                        editor.putString("user_id", "")
                        editor.apply()
                    }
                }
            }

        }

        createBlock.addView(nameEditText)
        createBlock.addView(lastnameEditText)
        createBlock.addView(emailEditText)
        createBlock.addView(numberPhoneEditText)
        createBlock.addView(aboutMeEditText)
        createBlock.addView(button)

        // Создание общего контейнера для заголовка и блока с полями
        val parentLayout = LinearLayout(requireContext())
        val parentLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        parentLayout.layoutParams = parentLayoutParams
        parentLayout.orientation = LinearLayout.VERTICAL
        parentLayout.addView(title)
        parentLayout.addView(createBlock)

        return parentLayout
    }



}