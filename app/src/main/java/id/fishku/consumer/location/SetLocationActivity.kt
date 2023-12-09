package id.fishku.consumer.location

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.databinding.ActivitySetLocationBinding
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class SetLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySetLocationBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Initialize the map asynchronously
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Other initialization code
        val btnFindMyLocation: Button = findViewById(R.id.btn_location)
        btnFindMyLocation.setOnClickListener {
            findMyLocation()
        }

        val btnNavigateToDashboard: Button = findViewById(R.id.btn_save_adress)
        btnNavigateToDashboard.setOnClickListener {
            saveAndShowLocation()
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

        // Now that mMap is initialized, you can perform any map-related operations
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        // Load saved address after map is ready
        loadSavedAddress()

        mMap.setOnMapClickListener { latLng ->
            val address = getAddressFromLocation(latLng)
            binding.fullAdress.setText(address)
        }
    }

    private fun getAddressFromLocation(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                return address.getAddressLine(0) ?: ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ""
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

                        // Get the address from the current location and display it
                        val address = getAddressFromLocation(currentLatLng)
                        binding.fullAdress.setText(address)
                    }
                }
        } else {
            // Jika izin tidak diberikan, minta izin.
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun moveCameraToAddress(address: String) {
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (!addresses.isNullOrEmpty()) {
                val location = addresses[0]
                val latLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                Toast.makeText(this, "Alamat tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Terjadi kesalahan saat mencari alamat", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAddressToSharedPreferences(address: String) {
        val editor = sharedPreferences.edit()
        editor.putString("saved_address", address)
        editor.apply()
    }

    private fun saveAndShowLocation() {
        val address = binding.fullAdress.text.toString().trim()

        if (address.isNotEmpty()) {
            saveAddressToSharedPreferences(address)
            moveCameraToAddress(address)
            showMarkerAtAddress(address)
        } else {
            Toast.makeText(this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSavedAddress() {
        val savedAddress = sharedPreferences.getString("saved_address", "")
        if (!savedAddress.isNullOrBlank()) {
            binding.fullAdress.setText(savedAddress)
            moveCameraToAddress(savedAddress)
        }
    }

    private fun showMarkerAtAddress(address: String) {
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (!addresses.isNullOrEmpty()) {
                val location = addresses[0]
                val latLng = LatLng(location.latitude, location.longitude)

                // Hapus marker sebelumnya jika ada
                mMap.clear()

                // Tambahkan marker baru di titik koordinat alamat
                mMap.addMarker(MarkerOptions().position(latLng).title(address))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                Toast.makeText(this, "Alamat tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Terjadi kesalahan saat mencari alamat", Toast.LENGTH_SHORT).show()
        }
    }
}