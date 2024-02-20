package com.example.jobquestapp.api





data class applicationsList(
    val id: Int,
    val profile_id: Int,
    val vacancy_id: Int,
    val status_vac: String,
    val date_applies: String
)
data class LoginResponse(
    val message: String,
    val user_id: Int,
    val profile_data: UserData
)

data class UserData(
    val pk: Int,
    val name: String,
    val lastname: String,
    val email: String,
    val number_phone: String,
    val about_me: String,
    val user_id: Int,
)

data class Sign_in_Response(
    val message: String,
    val user_id: String
)

data class Application_create(
    val message: String,
    val application_id: String
)
data class VacancyResponse(
    val vacancies: List<VacancyItem>
)


data class VacancyItem(
    val vacancy_name: String,
    val vacancy_title: String,
    val vacancy_salary: String,
    val date_publish: String,
    val id: Int,
    val company_name: String,
    val company_city: String
)
data class VacancyResponse_id(
    val id: Int,
    val vacancy_name: String,
    val vacancy_title: String,
    val vacancy_calary: String,
    val date_publish: String,
    val company_name: String,
    val company_city: String,
    val application: Application
)

data class Application(
    val status_vac: String,
    val application_id: Int
)

data class ProfileResponse (
    val message: String,
    val profile_id: Int
)