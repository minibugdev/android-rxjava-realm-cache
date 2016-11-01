package work.beltran.rxrealmcache

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.textViewName
import kotlinx.android.synthetic.main.activity_main.textViewTemp
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import work.beltran.rxrealmcache.api.WeatherService
import work.beltran.rxrealmcache.api.model.WeatherResponse
import work.beltran.rxrealmcache.realm.WeatherRealm
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = "MainActivity"

        private val FIELD_CITY_NAME = "name"
        private val CITY_NAME = "Bangkok"
    }

    @Inject internal lateinit var service: WeatherService

    private lateinit var realmUI: Realm
    private lateinit var subscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).component.inject(this)

        realmUI = Realm.getDefaultInstance()

        // Request API data on IO Scheduler
        var observable = service.getWeather(CITY_NAME, getString(R.string.api_key))

            // One second delay for demo purposes
            .delay(1L, java.util.concurrent.TimeUnit.SECONDS)

            // Write to Realm on Computation scheduler
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { this.writeToRealm(it) }

            // Read results in Android Main Thread (UI)
            .observeOn(AndroidSchedulers.mainThread())
            .map { this.readFromRealm(it) }


        // Read any cached results
        val cachedWeather = readFromRealm(CITY_NAME)
        if (cachedWeather != null) {
            // Merge with the observable from API
            observable = observable
                .mergeWith(Observable.just(cachedWeather))
                .distinct()
        }

        // Subscription happens on Main Thread
        subscription = observable.subscribe(
            { this.display(it) },
            { this.processError(it) }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.unsubscribe()
        realmUI.close()
    }

    private fun readFromRealm(name: String): WeatherRealm? {
        return findInRealm(realmUI, name)
    }

    private fun writeToRealm(weatherResponse: WeatherResponse): String {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { transactionRealm ->
            var weatherRealm: WeatherRealm? = findInRealm(transactionRealm, weatherResponse.name)
            if (weatherRealm == null) {
                weatherRealm = transactionRealm.createObject(WeatherRealm::class.java, weatherResponse.name)
            }
            weatherRealm!!.temp = weatherResponse.main?.temp
        }
        realm.close()
        return weatherResponse.name
    }

    private fun findInRealm(realm: Realm, name: String): WeatherRealm? {
        return realm.where(WeatherRealm::class.java)
            .equalTo(FIELD_CITY_NAME, name)
            .findFirst()
    }

    private fun processError(e: Throwable) {
        Log.e(TAG, e.message, e)
    }

    private fun display(weatherRealm: WeatherRealm?) {
        weatherRealm?.let {
            Log.d(TAG, "City: " + it.name + ", Temp: " + it.temp)
            textViewName.text = it.name
            textViewTemp.text = it.temp.toString()
        }
    }
}
