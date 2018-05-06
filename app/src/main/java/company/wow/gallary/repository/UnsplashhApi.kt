package company.wow.gallary.repository

import company.wow.gallary.model.UnsplashModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashhApi {
    @GET("photos/")
    fun photos(@Query("client_id") query: String,
               @Query("page") page: Int,
               @Query("per_page") perPage: Int): Observable<List<UnsplashModel>>

    companion object Factory {
        fun create(): UnsplashhApi {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.unsplash.com/")
                    .build()

            return retrofit.create(UnsplashhApi::class.java);
        }
    }
}