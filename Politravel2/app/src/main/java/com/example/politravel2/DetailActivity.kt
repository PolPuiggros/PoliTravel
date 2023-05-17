package com.example.politravel

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.politravel2.Paquet
import com.example.politravel2.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class DetailActivity: AppCompatActivity(), OnMapReadyCallback
{
    private lateinit var map:GoogleMap
    private lateinit var paquet: Paquet

    private lateinit var pointMarker: Marker
    private lateinit var startingPointMarker: Marker
    private lateinit var endingPointMarker: Marker
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_package)
        paquet = intent.getParcelableExtra<Paquet>("PAQUET")!!
        val title = findViewById<TextView>(R.id.tittlePackageName)
        val buttonBack = findViewById<ImageButton>(R.id.btnGoBack)
        val imageDetail = findViewById<ImageView>(R.id.packageImage)
        val duration = findViewById<TextView>(R.id.durationPackageDetail)
        val transportMethod = findViewById<ImageView>(R.id.trasportIconDetail)
        val startingPoint = findViewById<TextView>(R.id.nameStartingPointDetail)
        val endingPoint = findViewById<TextView>(R.id.nameEndingPointDetail)
        val routePoints = findViewById<ListView>(R.id.ListViewRoutePoints)
        val firstRoutePointText = findViewById<TextView>(R.id.TextViewRoutePointsStartingPoint)
        val endingRoutePointText = findViewById<TextView>(R.id.TextViewRoutePointsEndingPoint)
        createFragment()


        fillFields(paquet!!, title, imageDetail, duration, transportMethod, startingPoint, endingPoint, routePoints, firstRoutePointText, endingRoutePointText)



        buttonBack.setOnClickListener{
            finish()
        }
    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(callback)
    }


    private val callback = OnMapReadyCallback { googleMap ->
        val startingPoint = LatLng(paquet!!.Start_point_latitude.toDouble(), paquet.Start_point_longitude.toDouble())
        val endingPoint = LatLng(paquet.Ending_point_latitude.toDouble(), paquet.Ending_point_longitude.toDouble())
        for (interestPoint: InterestPoint in paquet.Interest_points){
            val point = LatLng(interestPoint.Interest_point_latitude.toDouble(), interestPoint.Interest_point_longitude.toDouble())
            pointMarker = googleMap.addMarker(MarkerOptions().position(point))
            pointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_gray))
        }
        startingPointMarker = googleMap.addMarker(MarkerOptions().position(startingPoint))
        startingPointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder))
        endingPointMarker = googleMap.addMarker(MarkerOptions().position(endingPoint))
        endingPointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 12.2F))
        googleMap.uiSettings.isZoomControlsEnabled = true
    }


    private fun fillFields(paquet: Paquet, title: TextView, imageDetail: ImageView, duration: TextView, trasportMethod: ImageView, startingPoint: TextView, endingPoint: TextView, routePoints: ListView, firstRoutePoint: TextView, lastRoutePoint: TextView)
    {
        title.text = paquet.Name_package
        accedirImg(imageDetail, paquet.Img)
        durationFormat(duration, paquet)
        trasportDisplay(trasportMethod, paquet)
        startingPoint.text = paquet.Start_point
        endingPoint.text = paquet.Ending_point
        firstRoutePoint.text = paquet.Start_point
        lastRoutePoint.text = paquet.Ending_point

        val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, paquet.Interest_points)
        routePoints.adapter = listAdapter


    }

    private fun trasportDisplay(trasportMethod: ImageView, paquet: Paquet)
    {
        when(paquet.Mean_of_transport){
            "Cotxe" -> trasportMethod.setImageResource(R.drawable.car_green)
            "Avio" -> trasportMethod.setImageResource(R.drawable.plane_green)
            "Tren" -> trasportMethod.setImageResource(R.drawable.train_green)
            "Barco" -> trasportMethod.setImageResource(R.drawable.boat_green)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun durationFormat(duration: TextView, paquet: Paquet)
    {
        val duracio = paquet.DurationOfTheTrip
        val unitat = paquet.Unit
        if (duracio == 1){
            when (unitat) {
                'D' -> duration.text = paquet.DurationOfTheTrip.toString() + " day"
                'W' -> duration.text = paquet.DurationOfTheTrip.toString() + " week"
                'M' -> duration.text = paquet.DurationOfTheTrip.toString() + " month"
            }
        }
        else {
            when (unitat) {
                'D' -> duration.text = paquet.DurationOfTheTrip.toString() + " days"
                'W' -> duration.text = paquet.DurationOfTheTrip.toString() + " weeks"
                'M' -> duration.text = paquet.DurationOfTheTrip.toString() + " months"
            }
        }
    }
    private fun accedirImg(imgView: ImageView, imgNom: String){
        val paquetImgPath = this.filesDir.absolutePath + "/img/" + imgNom + ".jpg"
        val bitmap = BitmapFactory.decodeFile(paquetImgPath)
        imgView.setImageBitmap(bitmap)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

}