package work.beltran.rxrealmcache.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Miquel Beltran on 9/25/16
 * More on http://beltran.work
 */
open class WeatherRealm : RealmObject() {
    @PrimaryKey
    var name: String? = null
    var temp: Double? = null
}
