package com.example.politravel2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.politravel.InterestPoint
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class NewPaquet: AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_IMAGE_STRING = "img"
        const val REQUEST_CODE_INTEREST_POINTS_STRING = "list"
    }
    private var  listInterestPoints =  arrayListOf<InterestPoint>()
    private lateinit var imgPaquet: String
    private var textStartingPointCoordinetes = ""
    private var textEndingPointCoordinetes = ""
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var startingPointMarker: Marker
    private lateinit var endingPointMarker: Marker
    private lateinit var pointMarker: Marker

    private var listPointMarkers = mutableListOf<Marker>()

    private val getResultInterestPoints = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val listViewInterestPoints = findViewById<ListView>(R.id.listViewInterestPointsPreview)
        if (it.resultCode == RESULT_OK){
            listInterestPoints = it.data?.getSerializableExtra(REQUEST_CODE_INTEREST_POINTS_STRING) as ArrayList<InterestPoint>
            val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listInterestPoints)
            listViewInterestPoints.adapter = listAdapter

            updateMapPoints(listInterestPoints)
        }
        else{
            listInterestPoints;
        }
    }

    private fun updateMapPoints(listInterestPoints: ArrayList<InterestPoint>) {

        if (listPointMarkers.size != 0){
            for (punt: Marker in listPointMarkers){
                punt.remove()
            }
        }
        for (interestPoint: InterestPoint in listInterestPoints){
            val point = LatLng(interestPoint.Interest_point_latitude.toDouble(), interestPoint.Interest_point_longitude.toDouble())
            pointMarker = map.addMarker(MarkerOptions().position(point))
            pointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_gray))
            listPointMarkers.add(pointMarker)
        }

    }

    private val getResultImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val imgViewPrev = findViewById<ImageView>(R.id.packageImgPreview)
        if (it.resultCode == RESULT_OK){
            imgPaquet = it.data?.getStringExtra(REQUEST_CODE_IMAGE_STRING) as String
            Utilties.accedirImg(this, imgViewPrev, imgPaquet)
        }
        else{
            imgViewPrev.setImageResource(R.drawable.logo_white)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_pckge)
        //SPINNER VALUES
        val unitValues = arrayOf("day/s", "week/s","month/s")
        //APP NAVBAR
        val btnGoBack = findViewById<ImageButton>(R.id.btnGoBackFromNewPackage)
        //ACTIVITY ELEMENTS
        val packageName = findViewById<EditText>(R.id.newPackageName)
        val duration = findViewById<EditText>(R.id.EditTextDuration)
        val spinnerUnitats = findViewById<Spinner>(R.id.spinnerUnitats)
        //TRANSPORT BUTTONS
        val btnCotxe = findViewById<ImageButton>(R.id.newPackageBtnCar)
        val btnAvio = findViewById<ImageButton>(R.id.newPackageBtnAvio)
        val btnTren = findViewById<ImageButton>(R.id.newPackageBtnTrain)
        val btnBarco = findViewById<ImageButton>(R.id.newPackageBtnBoat)
        //PACKAGE IMAGE AND INTEREST POINTS
        val btnChoosePackageImg = findViewById<Button>(R.id.setAPackageImage)
        val startingPointName = findViewById<EditText>(R.id.NewPackageStartingPointNameEditText)
        val startingPointCoordinates = findViewById<EditText>(R.id.NewPackageStartingPointCoordinatesEditText)
        val endingPointName = findViewById<EditText>(R.id.NewPackageEndingPointNameEditText)
        val endingPointCoordinates = findViewById<EditText>(R.id.NewPackageEndingPointCoordinatesEditText)
        val btnAddInterestPoints = findViewById<Button>(R.id.BtnAddInterestPoints)

        //MAP
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapaNewPackage) as SupportMapFragment
        mapFragment.getMapAsync(callback)


        //BUTTON CREATE
        val btnCreateNewPackage = findViewById<Button>(R.id.createNewPackage)

        //IMPLEMENTATION
        if(spinnerUnitats != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, unitValues)
            spinnerUnitats.adapter = adapter
        }
        var lastBtn = btnCotxe
        var meanOfTransport = "Cotxe"
        var spinnerValue = 'D'



        imgPaquet = filesDir.absolutePath + "/defaultImage/logo_white.png"

        btnChoosePackageImg.setOnClickListener {
            val intent = Intent(this, SelectAPackageImage::class.java)
            intent.getStringExtra(REQUEST_CODE_IMAGE_STRING)
            getResultImage.launch(intent)
        }

        startingPointCoordinates.setOnFocusChangeListener(){ view, hasFocus ->
            if (!hasFocus) {
                if (!startingPointCoordinates.text.isNullOrEmpty() && Utilties.correctCoordinates(startingPointCoordinates)){
                    try {
                        startingPointMarker.remove()
                    } catch (e : Exception ){

                    }finally {
                        textStartingPointCoordinetes = startingPointCoordinates.text.toString()
                        val lat = textStartingPointCoordinetes.split(",")[0].toDouble()
                        val lon = textStartingPointCoordinetes.split(",")[1].toDouble()
                        val startingPoint = LatLng(lat, lon)
                        startingPointMarker = map.addMarker(MarkerOptions().position(startingPoint))
                        startingPointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder))
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 12.2F))
                    }
                }
            }
        }
        endingPointCoordinates.setOnFocusChangeListener(){ view, hasFocus ->
            if (!hasFocus) {
                if (!endingPointCoordinates.text.isNullOrEmpty() && Utilties.correctCoordinates(endingPointCoordinates)){
                    try {
                        endingPointMarker.remove()
                    } catch (e : Exception ){

                    }finally {
                        textEndingPointCoordinetes = endingPointCoordinates.text.toString()
                        val lat = textEndingPointCoordinetes.split(",")[0].toDouble()
                        val lon = textEndingPointCoordinetes.split(",")[1].toDouble()
                        val endingPoint = LatLng(lat, lon)
                        endingPointMarker = map.addMarker(MarkerOptions().position(endingPoint))
                        endingPointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
                    }
                }
            }
        }

        btnAddInterestPoints.setOnClickListener{
            val intent = Intent(this, AddInterestPoints::class.java)
            intent.putExtra(REQUEST_CODE_INTEREST_POINTS_STRING, listInterestPoints)
            getResultInterestPoints.launch(intent)
        }
        btnCotxe.setOnClickListener{
            buttonStyle(lastBtn, btnCotxe)
            meanOfTransport = "Cotxe"
            lastBtn = btnCotxe
        }
        btnAvio.setOnClickListener{
            buttonStyle(lastBtn, btnAvio)
            meanOfTransport = "Avio"
            lastBtn = btnAvio
        }
        btnTren.setOnClickListener{
            buttonStyle(lastBtn, btnTren)
            meanOfTransport = "Tren"
            lastBtn = btnTren
        }
        btnBarco.setOnClickListener{
            buttonStyle(lastBtn, btnBarco)
            meanOfTransport = "Barco"
            lastBtn = btnBarco
        }
        btnGoBack.setOnClickListener{
            finish()
        }
        spinnerUnitats.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when(parent.getItemAtPosition(position).toString()){
                    "day/s" -> spinnerValue = 'D'
                    "week/s" -> spinnerValue = 'W'
                    "month/s" -> spinnerValue = 'M'
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        btnCreateNewPackage.setOnClickListener{
            if (allFieldsFilled(packageName, duration, startingPointName, startingPointCoordinates, endingPointName, endingPointCoordinates))
            {
                if (Utilties.correctCoordinates(startingPointCoordinates) && Utilties.correctCoordinates(endingPointCoordinates))
                {
                    val paquetsList = Utilties.getPaquets(this)

                    val id = paquetsList.last().Id +1
                    val name = packageName.text.toString()
                    val startingPointNameText = startingPointName.text.toString()
                    val endingPointNameText = endingPointName.text.toString()

                    val startingPointLattitude = startingPointCoordinates.text.split(",")[0].toFloat()
                    val startingPointLongitude = startingPointCoordinates.text.split(",")[1].toFloat()

                    val endingPointLattitude = endingPointCoordinates.text.split(",")[0].toFloat()
                    val endingPointLongitude = endingPointCoordinates.text.split(",")[1].toFloat()

                    val tripDuration = duration.text.toString().toInt()

                    val newPaquet = Paquet(
                       id,
                       name,
                       imgPaquet,
                       meanOfTransport,
                       startingPointNameText,
                       startingPointLattitude,
                       startingPointLongitude,
                       endingPointNameText,
                       endingPointLattitude,
                       endingPointLongitude,
                       tripDuration,
                       spinnerValue,
                       listInterestPoints
                    )
                    paquetsList.add(newPaquet)
                    Utilties.writePaquets(this, paquetsList)
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                else{
                    Toast.makeText( this,"Wrong coordinates format", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun allFieldsFilled(packageName: EditText, duration: EditText, startingPointName: EditText, startingPointCoordinates: EditText, endingPointName: EditText, endingPointCoordinates: EditText): Boolean
    {
        var allFieldsFilled = true
        if (packageName.text.isNullOrEmpty()) {
            Toast.makeText( this,"Package name is empty", Toast.LENGTH_SHORT).show()
            allFieldsFilled = false
        } else if (duration.text.isNullOrEmpty()) {
            Toast.makeText( this,"The duration of the route is empty", Toast.LENGTH_SHORT).show()
            allFieldsFilled = false
        } else if (startingPointName.text.isNullOrEmpty()) {
            Toast.makeText( this,"The starting point name of the route is empty", Toast.LENGTH_SHORT).show()
            allFieldsFilled = false
        } else if (startingPointCoordinates.text.isNullOrEmpty()) {
            Toast.makeText( this,"The starting point coordinates are empty", Toast.LENGTH_SHORT).show()
            allFieldsFilled = false
        }else if (endingPointName.text.isNullOrEmpty()) {
            Toast.makeText( this,"The ending point name of the route is empty", Toast.LENGTH_SHORT).show()
            allFieldsFilled = false
        } else if (endingPointCoordinates.text.isNullOrEmpty()) {
            Toast.makeText( this,"The ending point coordinates are empty", Toast.LENGTH_SHORT).show()
            allFieldsFilled = false
        }
        return allFieldsFilled
    }


    fun buttonStyle(lastBtn: ImageButton, actualBtn: ImageButton){
        when(actualBtn.id){
            R.id.newPackageBtnCar -> actualBtn.setImageResource(R.drawable.car_green)
            R.id.newPackageBtnAvio -> actualBtn.setImageResource(R.drawable.plane_green)
            R.id.newPackageBtnTrain -> actualBtn.setImageResource(R.drawable.train_green)
            R.id.newPackageBtnBoat -> actualBtn.setImageResource(R.drawable.boat_green)
        }
        unselect(lastBtn)
    }

    private fun unselect(lastBtn: ImageButton) {
        when(lastBtn.id){
            R.id.newPackageBtnCar -> lastBtn.setImageResource(R.drawable.car_unselected)
            R.id.newPackageBtnAvio -> lastBtn.setImageResource(R.drawable.plane_unselected)
            R.id.newPackageBtnTrain -> lastBtn.setImageResource(R.drawable.train_unselected)
            R.id.newPackageBtnBoat -> lastBtn.setImageResource(R.drawable.boat_unselected)
        }
    }
    /*
    private fun createFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapaNewPackage) as SupportMapFragment
        mapFragment.getMapAsync(callback)
    }*/
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        var startingPoint = LatLng(0.0, 0.0)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 12.2F))
        googleMap.uiSettings.isZoomControlsEnabled = true
    }
    fun refreshMap(mapFragment: SupportMapFragment, points: List<Double>){
    }
}