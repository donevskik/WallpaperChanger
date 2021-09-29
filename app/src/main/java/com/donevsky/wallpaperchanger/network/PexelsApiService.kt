package com.donevsky.wallpaperchanger.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

val BASE_URL = "https://api.pexels.com/v1/"

val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface PexelsApiService {
    @Headers("Authorization: 563492ad6f91700001000001ab88b12f27324e31942c0e67c56310de")
    @GET("search")
    suspend fun getWallpapersApi(@Query("query" )search: String, @Query("per_page") perPage: Int) : Response
}

object PexelsApi {
    val retrofitService: PexelsApiService by lazy {
        retrofit.create(PexelsApiService::class.java)
    }
}
