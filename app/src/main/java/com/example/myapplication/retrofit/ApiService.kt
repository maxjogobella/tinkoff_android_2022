package com.example.myapplication.retrofit

import com.example.myapplication.utils.Constants.RATING_ADULTS
import com.example.myapplication.utils.Constants.RATING_KIDS
import com.example.myapplication.models.Datum
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val API_KEY = "FPiI6D7bXIFc9IXJPL6J9z4JbZs3uJLN"
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_RATING = "rating"
    }

    @GET("v1/gifs/random")
    fun getGif(
        @Query(QUERY_PARAM_API_KEY) apiKey : String = API_KEY
    ) : Single<Datum>

    @GET("v1/gifs/random")
    fun getGifKids(
        @Query(QUERY_PARAM_API_KEY) apiKey : String = API_KEY,
        @Query(QUERY_PARAM_RATING) rating : String = RATING_KIDS
    ) : Single<Datum>

    @GET("v1/gifs/random")
    fun getGifAdults(
        @Query(QUERY_PARAM_API_KEY) apiKey : String = API_KEY,
        @Query(QUERY_PARAM_RATING) rating : String = RATING_ADULTS
    ) : Single<Datum>

}