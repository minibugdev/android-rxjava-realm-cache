package work.beltran.rxrealmcache

import dagger.Component
import work.beltran.rxrealmcache.di.WeatherServiceModule
import javax.inject.Singleton

/**
 * Created by Miquel Beltran on 9/24/16
 * More on http://beltran.work
 */
@Singleton
@Component(modules = arrayOf(WeatherServiceModule::class))
interface WeatherComponent {
    fun inject(activity: MainActivity)
}