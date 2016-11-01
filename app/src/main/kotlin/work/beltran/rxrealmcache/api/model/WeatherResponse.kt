package work.beltran.rxrealmcache.api.model


class WeatherResponse(val id: Int, val name: String) {
    val coord: Coord? = null
    val weather: List<Weather> = listOf()
    val base: String? = null
    val main: Main? = null
    val wind: Wind? = null
    val clouds: Clouds? = null
    val dt: Int? = null
    val sys: Sys? = null
    val cod: Int? = null
}
