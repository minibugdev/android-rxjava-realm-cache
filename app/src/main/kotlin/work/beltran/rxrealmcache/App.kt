package work.beltran.rxrealmcache

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration
import work.beltran.rxrealmcache.di.WeatherServiceModule

/**
 * Created by Miquel Beltran on 9/24/16
 * More on http://beltran.work
 */
class App : Application() {

    lateinit var component: WeatherComponent

    override fun onCreate() {
        super.onCreate()

        val realmConfig = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(realmConfig)

        component = DaggerWeatherComponent.builder()
            .weatherServiceModule(WeatherServiceModule(getString(R.string.base_url)))
            .build()
    }
}
