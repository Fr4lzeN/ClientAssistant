package com.example.businesshub.data.data_source

import com.example.businesshub.core.Constants
import com.example.businesshub.data.data_source.DTO.GetUserDTO
import com.example.businesshub.data.data_source.DTO.CurrentUserDTO
import com.example.businesshub.data.data_source.DTO.SignUpDTO
import com.example.businesshub.data.data_source.DTO.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    companion object{
        @JvmStatic val BASE_URL = "https://parseapi.back4app.com"
    }

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "X-Parse-Revocable-Session: 1",
        "Content-Type: application/json",
        )
    @POST("/users")
    suspend fun signUp(@Body user: UserDTO): Response<SignUpDTO>

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "X-Parse-Revocable-Session: 1",
    )
    @GET("/login")
     suspend fun signIn(@Query("username")username: String, @Query("password")password: String) : Response<CurrentUserDTO>


    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
    )
    @GET("/users/{userId}")
    suspend fun getUser(@Path("userId")userId: String) : Response<GetUserDTO>

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
    )
    @POST("/logout")
    suspend fun logOut(@Header("X-Parse-Session-Token")token: String)


}