package com.example.politravel2

import android.app.Activity
import android.app.AlertDialog
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class EditPackage: AppCompatActivity() {
    private lateinit var imgPaquet: String
    private lateinit var listInterestPoints: ArrayList<InterestPoint>
    private lateinit var map: GoogleMap
    private lateinit var startingPointMarker: Marker
    private lateinit var endingPointMarker: Marker
    private lateinit var pointMarker: Marker
    private lateinit var paquet: Paquet

    private var listPointMarkers = mutableListOf<Marker>()

    private val getResultInterestPoints = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val listViewInterestPoints = findViewById<ListView>(R.id.editListViewInterestPointsPreview)
        if (it.resultCode == RESULT_OK){
            listInterestPoints = it.data?.getSerializableExtra(NewPaquet.REQUEST_CODE_INTEREST_POINTS_STRING) as ArrayList<InterestPoint>
            val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listInterestPoints)
            listViewInterestPoints.adapter = listAdapter

            updateMapPoints(listInterestPoints)
        }
        else{
            listInterestPoints
        }
    }
    private val getResultImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val imgViewPrev = findViewById<ImageView>(R.id.packageImgPreviewEdit)
        if (it.resultCode == RESULT_OK){
            imgPaquet = it.data?.getStringExtra(NewPaquet.REQUEST_CODE_IMAGE_STRING) as String
            Utilties.accedirImg(this, imgViewPrev, imgPaquet)
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_package)
        //PAQUET
        paquet = intent.getParcelableExtra("PAQUET")!!
        paquet.Id -1
        listInterestPoints = paquet.Interest_points
        createFragment()
        //SPINNER VALUES
        val unitValues = arrayOf("day/s", "week/s","month/s")
        //APP NAVBAR
        val btnGoBack = findViewById<ImageButton>(R.id.btnGoBackFromEditPackage)
        val btnDeletePackage = findViewById<ImageButton>(R.id.btnDeletePackage)
        //ACTIVITY ELEMENTS
        val packageName = findViewById<EditText>(R.id.EditTextPackageNameEdit)
        val duration = findViewById<EditText>(R.id.EditTextDurationEdit)
        val spinnerUnitats = findViewById<Spinner>(R.id.spinnerUnitatsEdit)
        //TRANSPORT BUTTONS
        val btnCotxe = findViewById<ImageButton>(R.id.editPackageBtnCar)
        val btnAvio = findViewById<ImageButton>(R.id.editPackageBtnAvio)
        val btnTren = findViewById<ImageButton>(R.id.editPackageBtnTrain)
        val btnBarco = findViewById<ImageButton>(R.id.editPackageBtnBoat)
        //PACKAGE IMAGE AND INTEREST POINTS
        val paquetImatgePreview = findViewById<ImageView>(R.id.packageImgPreviewEdit)
        val btnChoosePackageImg = findViewById<Button>(R.id.setAPackageImageEdit)
        val startingPointName = findViewById<EditText>(R.id.editPackageStartingPointNameEditText)
        val startingPointCoordinates = findViewById<EditText>(R.id.editPackageStartingPointCoordinatesEditText)
        val endingPointName = findViewById<EditText>(R.id.editPackageEndingPointNameEditText)
        val endingPointCoordinates = findViewById<EditText>(R.id.editPackageEndingPointCoordinatesEditText)
        val btnAddInterestPoints = findViewById<Button>(R.id.editBtnAddInterestPoints)
        val listViewInterestPoints = findViewById<ListView>(R.id.editListViewInterestPointsPreview)
        //SAVE AND EXIT
        val saveAndExit = findViewById<Button>(R.id.EditPackage)

        //FILL FIELDS
        packageName.setText(paquet.Name_package)
        val tripDuration = paquet.DurationOfTheTrip
        duration.setText(tripDuration.toString())
        var unitatPaquet = paquet.Unit
        if(spinnerUnitats != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, unitValues)
            spinnerUnitats.adapter = adapter
            when(unitatPaquet){
                'D' -> spinnerUnitats.setSelection(0)
                'W' -> spinnerUnitats.setSelection(1)
                'M' -> spinnerUnitats.setSelection(2)
            }
        }
        var lastBtn = btnCotxe
        when (paquet.Mean_of_transport){
            "Cotxe" -> {
                btnCotxe.setImageResource(R.drawable.car_green)
                lastBtn = btnCotxe
            }
            "Avio" -> {
                btnAvio.setImageResource(R.drawable.plane_green)
                lastBtn = btnAvio
            }
            "Tren" -> {
                btnTren.setImageResource(R.drawable.train_green)
                lastBtn = btnAvio
            }
            "Barco" -> {
                btnBarco.setImageResource(R.drawable.boat_green)
                lastBtn = btnBarco
            }
        }
        imgPaquet = paquet.Img
        Utilties.accedirImg(this, paquetImatgePreview, paquet.Img)
        startingPointName.setText(paquet.Start_point)
        startingPointCoordinates.setText(Utilties.joinCoordinates(paquet.Start_point_latitude, paquet.Start_point_longitude))
        endingPointName.setText(paquet.Ending_point)
        endingPointCoordinates.setText(Utilties.joinCoordinates(paquet.Ending_point_latitude, paquet.Ending_point_longitude))

        val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listInterestPoints)
        listViewInterestPoints.adapter = listAdapter

        var meanOfTransport = paquet.Mean_of_transport

        spinnerUnitats.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when(parent.getItemAtPosition(position).toString()){
                    "day/s" -> unitatPaquet = 'D'
                    "week/s" -> unitatPaquet = 'W'
                    "month/s" -> unitatPaquet = 'M'
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnGoBack.setOnClickListener{
            finish()
        }
        btnDeletePackage.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete the package?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    val paquetsList = Utilties.getPaquets(this)
                    val positionToDelete = positionToSavePaquet(paquetsList, paquet)
                    paquetsList.removeAt(positionToDelete)
                    Utilties.writePaquets(this, paquetsList)
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        btnCotxe.setOnClickListener(){
            buttonStyle(lastBtn, btnCotxe)
            meanOfTransport = "Cotxe"
            lastBtn = btnCotxe
        }
        btnAvio.setOnClickListener(){
            buttonStyle(lastBtn, btnAvio)
            meanOfTransport = "Avio"
            lastBtn = btnAvio
        }
        btnTren.setOnClickListener(){
            buttonStyle(lastBtn, btnTren)
            meanOfTransport = "Tren"
            lastBtn = btnTren
        }
        btnBarco.setOnClickListener(){
            buttonStyle(lastBtn, btnBarco)
            meanOfTransport = "Barco"
            lastBtn = btnBarco
        }
        btnChoosePackageImg.setOnClickListener(){
            val intent = Intent(this, SelectAPackageImage::class.java)
            intent.getStringExtra(NewPaquet.REQUEST_CODE_IMAGE_STRING)
            getResultImage.launch(intent)
        }
        startingPointCoordinates.setOnFocusChangeListener(){ view, hasFocus ->
            if (!hasFocus) {
                if (!startingPointCoordinates.text.isNullOrEmpty() && Utilties.correctCoordinates(startingPointCoordinates)){
                    var textStartingPointCoordinetes = ""
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
                    var textEndingPointCoordinetes = ""
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
            intent.putExtra(NewPaquet.REQUEST_CODE_INTEREST_POINTS_STRING, listInterestPoints)
            getResultInterestPoints.launch(intent)
        }
        saveAndExit.setOnClickListener{
            if (allFieldsFilled(packageName, duration, startingPointName, startingPointCoordinates, endingPointName, endingPointCoordinates))
            {
                if (Utilties.correctCoordinates(startingPointCoordinates) && Utilties.correctCoordinates(endingPointCoordinates))
                {
                    val paquetsList = Utilties.getPaquets(this)
                    val positionToSave = positionToSavePaquet(paquetsList, paquet)
                    val id = positionToSave +1

                    val name = packageName.text.toString()
                    val startingPointNameText = startingPointName.text.toString()
                    val endingPointNameText = endingPointName.text.toString()

                    val startingPointLattitude = startingPointCoordinates.text.split(",")[0].toFloat()
                    val startingPointLongitude = startingPointCoordinates.text.split(",")[1].toFloat()

                    val endingPointLattitude = endingPointCoordinates.text.split(",")[0].toFloat()
                    val endingPointLongitude = endingPointCoordinates.text.split(",")[1].toFloat()

                    val tripDurationvalue = duration.text.toString().toInt()


                    val newPaquetEdited = Paquet(
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
                        tripDurationvalue,
                        unitatPaquet,
                        listInterestPoints
                    )
                    paquetsList[positionToSave] = newPaquetEdited
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
            R.id.editPackageBtnCar -> actualBtn.setImageResource(R.drawable.car_green)
            R.id.editPackageBtnAvio -> actualBtn.setImageResource(R.drawable.plane_green)
            R.id.editPackageBtnTrain -> actualBtn.setImageResource(R.drawable.train_green)
            R.id.editPackageBtnBoat -> actualBtn.setImageResource(R.drawable.boat_green)
        }
        unselect(lastBtn)
    }
    private fun unselect(lastBtn: ImageButton) {
        when(lastBtn.id){
            R.id.editPackageBtnCar -> lastBtn.setImageResource(R.drawable.car_unselected)
            R.id.editPackageBtnAvio -> lastBtn.setImageResource(R.drawable.plane_unselected)
            R.id.editPackageBtnTrain -> lastBtn.setImageResource(R.drawable.train_unselected)
            R.id.editPackageBtnBoat -> lastBtn.setImageResource(R.drawable.boat_unselected)
        }
    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapaEditPackage) as SupportMapFragment
        mapFragment.getMapAsync(callback)
    }
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val startingPoint = LatLng(paquet.Start_point_latitude.toDouble(), paquet.Start_point_longitude.toDouble())
        val endingPoint = LatLng(paquet.Ending_point_latitude.toDouble(), paquet.Ending_point_longitude.toDouble())
        startingPointMarker = map.addMarker(MarkerOptions().position(startingPoint))
        startingPointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder))
        endingPointMarker = map.addMarker(MarkerOptions().position(endingPoint))
        endingPointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag))

        for (interestPoint: InterestPoint in listInterestPoints){
            val point = LatLng(interestPoint.Interest_point_latitude.toDouble(), interestPoint.Interest_point_longitude.toDouble())
            pointMarker = map.addMarker(MarkerOptions().position(point))
            pointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_gray))
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 12.2F))
        googleMap.uiSettings.isZoomControlsEnabled = true
    }
    private fun positionToSavePaquet(paquets: MutableList<Paquet>, paquetObert: Paquet): Int{
        var paquetAtrobar: Paquet
        val actualId = paquetObert.Id
        var trobat = false
        var i = 0
        do {
            paquetAtrobar = paquets[i]
            if (paquetAtrobar.Id == actualId) {
                trobat = true
            }
            else{
                i++
            }
        } while (i < paquets.size && !trobat)
        return i
    }
}