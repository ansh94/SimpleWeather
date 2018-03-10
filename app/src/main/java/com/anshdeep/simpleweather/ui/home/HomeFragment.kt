package com.anshdeep.simpleweather.ui.home

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anshdeep.simpleweather.R
import com.anshdeep.simpleweather.databinding.FragmentHomeBinding
import com.anshdeep.simpleweather.model.WeatherResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.android.support.DaggerFragment
import java.text.DateFormat
import java.util.*
import javax.inject.Inject


/**
 * Created by ansh on 10/03/18.
 */
class HomeFragment : DaggerFragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var swipeContainer: SwipeRefreshLayout? = null

    private var weatherFont: Typeface? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    // FragmentHomeBinding class is generated at compile time so build the project first
    // lateinit modifier allows us to have non-null variables waiting for initialization
    private lateinit var binding: FragmentHomeBinding

    companion object {

        private const val TAG = "HomeFragment"

    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel

    private var checkPermission: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.actionBar?.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(HomeViewModel::class.java)

        binding.viewModel = viewModel
        binding.executePendingBindings()



        viewModel.getUpdatedWeather().observe(this,
                Observer<WeatherResult> { it?.let { updateUi(it) } })

        weatherFont = Typeface.createFromAsset(activity?.assets, "weathericons-regular-webfont.ttf")


        binding.weatherIcon.typeface = weatherFont

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        if (savedInstanceState == null) {
            checkLocation() //check whether location service is enable or not in your  phone
        }


        // Lookup the swipe container view
        swipeContainer = view?.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout
        // Setup refresh listener which triggers new data loading
        swipeContainer!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            if (isConnectedToInternet()) {
                viewModel.onRefresh(viewModel.getLat(), viewModel.getLon())

            } else {
                Snackbar.make(binding.root, "You are not connected to the internet", Snackbar.LENGTH_LONG).show()
                swipeContainer!!.isRefreshing = false
            }
        })


    }

    private fun isConnectedToInternet(): Boolean {
        val connManager = activity!!.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val ni = connManager.activeNetworkInfo
        return ni != null && ni.isConnected
    }

    private fun updateUi(weather: WeatherResult) {
        val df = DateFormat.getDateTimeInstance()

        Log.d(TAG, "updateui: ");
        binding.cityField.text = weather.name + ", " + weather.sys.country

        binding.updatedField.text = df.format(Date(weather.dt * 1000))
        binding.currentTemperatureField.text = String.format("%.2f", weather.main.temp) + "° F"

        val iconText = setWeatherIcon(weather.weather[0].id,
                weather.sys.sunrise * 1000,
                weather.sys.sunset * 1000)

        binding.weatherIcon.text = Html.fromHtml(iconText)

    }


    private fun setWeatherIcon(actualId: Int, sunrise: Long, sunset: Long): String {
        val id = actualId / 100
        var icon = ""
        if (actualId == 800) {
            val currentTime = Date().time
            icon = if (currentTime in sunrise..(sunset - 1)) {
                "&#xf00d;"
            } else {
                "&#xf02e;"
            }
        } else {
            when (id) {
                2 -> icon = "&#xf01e;"
                3 -> icon = "&#xf01c;"
                7 -> icon = "&#xf014;"
                8 -> icon = "&#xf013;"
                6 -> icon = "&#xf01b;"
                5 -> icon = "&#xf019;"
            }
        }
        return icon
    }

    private fun checkLocation() {
        if (ContextCompat.checkSelfPermission(activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 12)

            }
        } else {
            checkPermission = true

            // Permission has already been granted
            Log.d(TAG, "Permission already granted: ")


            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        latitude = location!!.latitude
                        longitude = location.longitude

                        if (isConnectedToInternet()) {
                            if (viewModel.getLat() == 0.0) {
                                Log.d(TAG, "lat: " + latitude);
                                viewModel.setLatAndLon(latitude, longitude)
                            }
                        } else {
                            Snackbar.make(binding.root, "You are not connected to the internet", Snackbar.LENGTH_LONG).show()
                        }


                    }


        }


    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            12 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "Permission granted: ")
                    checkLocation()

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "Permission denied: ")
                    Snackbar.make(binding.root, "Permission Denied", Snackbar.LENGTH_LONG).show()


                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.

            else -> {
                // Ignore all other requests.
            }
        }
    }
}