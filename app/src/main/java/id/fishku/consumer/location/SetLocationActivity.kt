package id.fishku.consumer.location

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.dashboard.DashboardFragment
import id.fishku.consumer.databinding.ActivitySetLocationBinding

@AndroidEntryPoint
class SetLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySetLocationBinding

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //get location dengan button
        val btnFindMyLocation: Button = findViewById(R.id.btn_location)
        btnFindMyLocation.setOnClickListener {
            findMyLocation()
        }

        val btnNavigateToDashboard: Button = findViewById(R.id.btn_save_adress)
        btnNavigateToDashboard.setOnClickListener {
            navigateToDashboardFragment()
        }

        setUpAction()
        hideActionBar()

    }

    private fun setUpAction() {
        binding.toolbarSetLocation.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

//        findMyLocation()
    }

    //tambahan
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                findMyLocation()
            }
        }
//
//    //getlocation
//    private fun getMyLocation() {
//        if (ContextCompat.checkSelfPermission(
//                this.applicationContext,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            mMap.isMyLocationEnabled = true
//        } else {
//            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }

    //get Location
    private fun findMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            mMap.isMyLocationEnabled = true

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    }
                }
        } else {
            // Jika izin tidak diberikan, minta izin.
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    //get address dan balik ke dashboard

    //tambahan intent dashboard belum ada function save adress
    private fun navigateToDashboardFragment() {
        val dashboardFragment = DashboardFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.action_navigation_dashboard_to_setLocationActivity, dashboardFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}