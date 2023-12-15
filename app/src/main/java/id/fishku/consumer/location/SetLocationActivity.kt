package id.fishku.consumer.location

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
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

    private var currentLatLng: LatLng? = null

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

        val btnSaveAddress: Button = findViewById(R.id.btn_save_adress)
        btnSaveAddress.setOnClickListener {
            saveAndShowLocation()
        }

        val btnResetAddress: Button = findViewById(R.id.btn_reset_address)
        btnResetAddress.setOnClickListener {
            resetAddress()
        }

        val savedLatLng = loadSavedMarkerCoordinate()
        Log.d(
            "SavedLatLng",
            "Latitude: ${savedLatLng?.latitude}, Longitude: ${savedLatLng?.longitude}"
        )


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

        // Load saved address and marker coordinate after map is ready
        loadSavedAddress()

        val savedLatLng = loadSavedMarkerCoordinate()
        if (savedLatLng != null) {
            showMarkerAtLatLng(savedLatLng)
        }

        mMap.setOnMapClickListener { latLng ->
            val address = getAddressFromLocation(latLng)
            binding.fullAdress.setText(address)

            // Save LatLng to SharedPreferences when the map is clicked
            saveMarkerCoordinateToSharedPreferences(latLng)
        }
    }

    private fun resetAddress() {
        // Hapus alamat yang tersimpan dari SharedPreferences
        sharedPreferences.edit().remove("saved_address").apply()

        // Hapus koordinat marker yang tersimpan dari SharedPreferences
        sharedPreferences.edit().remove("saved_marker_latitude").apply()
        sharedPreferences.edit().remove("saved_marker_longitude").apply()

        // Bersihkan teks pada fullAdress
        binding.fullAdress.text = null

        // Hapus marker pada peta
        mMap.clear()

        // Tampilkan pesan notifikasi bahwa alamat telah direset
        Toast.makeText(this, "Alamat telah direset", Toast.LENGTH_SHORT).show()
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
                        currentLatLng = LatLng(location.latitude, location.longitude)

                        // Dapatkan alamat dari lokasi saat ini dan tampilkan
                        val address = getAddressFromLocation(currentLatLng!!)
                        binding.fullAdress.setText(address)

                        // Pindahkan kamera ke lokasi saat ini
                        moveCameraToLocation(currentLatLng!!)
                    }
                }
        } else {
            // Jika izin tidak diberikan, minta izin.
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun moveCameraToLocation(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun saveMarkerCoordinateToSharedPreferences(latLng: LatLng) {
        val editor = sharedPreferences.edit()
        editor.putString("saved_marker_latitude", latLng.latitude.toString())
        editor.putString("saved_marker_longitude", latLng.longitude.toString())
        editor.apply()
    }

    private fun loadSavedMarkerCoordinate(): LatLng? {
        val latitudeStr = sharedPreferences.getString("saved_marker_latitude", null)
        val longitudeStr = sharedPreferences.getString("saved_marker_longitude", null)

        if (!latitudeStr.isNullOrBlank() && !longitudeStr.isNullOrBlank()) {
            return LatLng(latitudeStr.toDouble(), longitudeStr.toDouble())
        }

        return null
    }

    private fun saveAddressToSharedPreferences(address: String) {
        val editor = sharedPreferences.edit()
        editor.putString("saved_address", address)
        editor.apply()
    }

    private fun saveAndShowLocation() {
        val address = binding.fullAdress.text.toString().trim()

        if (address.isNotEmpty()) {
            val location = getLocationFromAddress(address)
            if (location != null) {
                // Save address to SharedPreferences
                saveAddressToSharedPreferences(address)

                // Save LatLng to SharedPreferences
                saveMarkerCoordinateToSharedPreferences(location)

                // Move camera and show marker
                moveCameraToLocation(location)
                showMarkerAtLatLng(location)

                // Display a toast notification
                Toast.makeText(this, "Alamat berhasil disimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Alamat tidak valid, coba lagi", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Mohon masukkan alamat terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocationFromAddress(address: String): LatLng? {
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (!addresses.isNullOrEmpty()) {
                val location = addresses[0]
                return LatLng(location.latitude, location.longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Terjadi kesalahan saat mencari alamat", Toast.LENGTH_SHORT).show()
        }

        return null
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

    private fun showMarkerAtLatLng(latLng: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng).title("Saved Location"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun loadSavedAddress() {
        val savedAddress = sharedPreferences.getString("saved_address", "")
        if (!savedAddress.isNullOrBlank()) {
            binding.fullAdress.setText(savedAddress)
            moveCameraToAddress(savedAddress)
        }
    }
}