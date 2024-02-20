package com.example.jobquestapp.api

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.example.jobquestapp.api.*
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class api_resource {

    suspend fun logIn(login: String, password: String): LoginResponse {
        val apiUrl = "http://79.174.94.169:8100/log_in/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"login\":\"$login\",\"password\":\"$password\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), LoginResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing login data ", e)
                throw e
            }
        }
    }

    suspend fun Sign_in(login: String, password: String): Sign_in_Response {
        val apiUrl = "http://79.174.94.169:8100/sign_in/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"login\":\"$login\",\"password\":\"$password\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), Sign_in_Response::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing login data ", e)
                throw e
            }
        }
    }

    suspend fun getAllVacancy(): List<VacancyItem> {
        val apiUrl = "http://79.174.94.169:8100/vacancies/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val vacancyResponse = gson.fromJson(response.toString(), VacancyResponse::class.java)

                vacancyResponse.vacancies.map { vacancy ->
                    VacancyItem(
                        vacancy.vacancy_name,
                        vacancy.vacancy_title,
                        vacancy.vacancy_salary,
                        vacancy.date_publish,
                        vacancy.id,
                        vacancy.company_name,
                        vacancy.company_city,
                        )
                }
            } catch (e: Exception) {
                Log.e("VacancyError", "Error fetching or parsing vacancy data", e)
                throw e
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun create_applications(profile_id: Int, vacancy_id: Int): Application_create {
        val apiUrl = "http://79.174.94.169:8100/create_application/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                val text = "Отправлено"
                // Получение текущей даты
                val currentDate = LocalDate.now()

// Форматирование даты в строку
                val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"profile_id\":\"$profile_id\",\"vacancy_id\":\"$vacancy_id\",\"status_vac\":\"$text\",\"date_applies\":\"$formattedDate\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), Application_create::class.java)
            } catch (e: Exception) {
                Log.e("ApplicationError", "Error fetching or parsing login data ", e)
                throw e
            }
        }
    }


    suspend fun get_applications_id(profileId: Int): List<VacancyResponse_id> {
        val apiUrl = "http://79.174.94.169:8100/get_applications_id/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonInputString = "{\"profile_id\":\"$profileId\"}"
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val vacancyArray = gson.fromJson(response.toString(), Array<VacancyResponse_id>::class.java)

                vacancyArray.toList()
            } catch (e: FileNotFoundException) {
                Log.e("666", "File not found: ${e.message}")
                emptyList()
            } catch (e: Exception) {
                Log.e("666", "Error: ${e.message}")
                emptyList()
            }
        }
    }

    data class message_response(
        val message: String
    )
    suspend fun delete_applications(application_id: Int): message_response {
        val apiUrl = "http://79.174.94.169:8100/get_applications_delete/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с id заявки
                val jsonInputString = "{\"application_id\":\"$application_id\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                // Получаем ответ от сервера
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                // Преобразуем ответ в объект message_response
                val gson = Gson()
                gson.fromJson(response, message_response::class.java)
            } catch (e: Exception) {
                Log.e("DeleteError", "Error fetching or parsing data", e)
                throw e
            }
        }
    }


    suspend fun create_profile(name: String, lastname: String, email: String, number_phone: String, about_me: String, user_id: Int): ProfileResponse {
        val apiUrl = "http://79.174.94.169:8100/create_profile/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"name\":\"$name\",\"lastname\":\"$lastname\", \"email\":\"$email\", \"number_phone\":\"$number_phone\", \"about_me\":\"$about_me\", \"user_id\":\"$user_id\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), ProfileResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing data ", e)
                throw e
            }
        }
    }


}