package org.cadnusdevs.sandroid.jwcongregationasignment.service

import com.google.gson.JsonObject
import org.cadnusdevs.sandroid.jwcongregationasignment.GoogleMapConfig
import org.osmdroid.util.GeoPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class GeoLocationService {

    interface NominatimOSM {

        @GET("/maps/api/geocode/json")
        fun search(
            @Query("address") address: String?,

            //// to resolve the 'not found' error below, create a file on location:
            //// app\app\src\main\java\org\cadnusdevs\sandroid\jwcongregationasignment\GoogleMapConfig.kt
            //// using this format below and passing your google map api key

            /*
            package org.cadnusdevs.sandroid.jwcongregationasignment
            class GoogleMapConfig {
                companion object{
                    const val  key = ""
                }
            }
            */
            @Query("key") apyKey: String = GoogleMapConfig.key,
            ): Call<JsonObject>

        companion object {
            const val url = "https://maps.google.com"
        }
    }
    fun getLocationByAddress(street: String, houseNumber: String, neighborHood: String, callback: (geo: GeoPoint?)-> Unit)  {
        val address = "$street, $houseNumber - $neighborHood "

        val retrofit = Retrofit.Builder()
            .baseUrl(NominatimOSM.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NominatimOSM::class.java)
        val call = service.search(address)
        call?.enqueue(object: Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.
                        getAsJsonArray("results")?.get(0)?.asJsonObject
                        ?.get("geometry")?.asJsonObject?.get("location")?.asJsonObject
                    val lat = data?.get("lat")?.asDouble
                    val long = data?.get("lng")?.asDouble
                    val location = if(lat!= null && long != null) GeoPoint(lat, long) else null
                    callback.invoke(location)
                }else{
                    callback.invoke(null)
                }

            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }
}