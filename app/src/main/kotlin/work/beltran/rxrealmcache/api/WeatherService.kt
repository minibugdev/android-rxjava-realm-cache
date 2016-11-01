package work.beltran.rxrealmcache.api

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import work.beltran.rxrealmcache.api.model.WeatherResponse

/**
 * Created by Miquel Beltran on 9/24/16
 * More on http://beltran.work
 */
interface WeatherService {
    @GET("weather?units=metric")
    fun getWeather(@Query("q") city: String, @Query("appid") apiKey: String): Observable<WeatherResponse>
}
