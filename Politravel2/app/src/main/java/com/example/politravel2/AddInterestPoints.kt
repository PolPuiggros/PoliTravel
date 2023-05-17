package com.example.politravel2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.politravel.InterestPoint

class AddInterestPoints: AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.add_interest_points)
            val btnGoBack = findViewById<ImageButton>(R.id.btnGoBackAddInterestPoints)
            val editTxtInterestPointName = findViewById<EditText>(R.id.InterestPointNameEditText)
            val editTxtInterestPointCoordinates = findViewById<EditText>(R.id.InterestPointCoordinatesEditText)
            val btnAddPoint = findViewById<Button>(R.id.ButtonAddPoint)
            val btnSaveAndExit = findViewById<Button>(R.id.BtnInterestPointsSaveAndExit)
            val listViewPoints = findViewById<ListView>(R.id.listViewPoints)

            val intent = getIntent()
            var listInterestPoints : ArrayList<InterestPoint> = intent.getSerializableExtra(
                NewPaquet.REQUEST_CODE_INTEREST_POINTS_STRING) as ArrayList<InterestPoint>


            val adapter = ListViewPointsAdapter(this, R.layout.interest_points_adapter, listInterestPoints)
            listViewPoints.adapter = adapter


            btnAddPoint.setOnClickListener{
                if (!emptyFields(editTxtInterestPointName, editTxtInterestPointCoordinates) && Utilties.correctCoordinates(editTxtInterestPointCoordinates)){
                    val interestPointName = editTxtInterestPointName.text
                    val latitudeAndLongitude = editTxtInterestPointCoordinates.text
                    val latitude = latitudeAndLongitude.split(",")[0].toFloat()
                    val longitude = latitudeAndLongitude.split(",")[1].toFloat()

                    reestablishEditTexts(editTxtInterestPointName, editTxtInterestPointCoordinates)
                    listInterestPoints.add(InterestPoint(interestPointName.toString(), latitude,longitude))
                    adapter.notifyDataSetChanged()
                }
            }
            btnGoBack.setOnClickListener{
                setResult(RESULT_CANCELED)
                finish()
            }

            btnSaveAndExit.setOnClickListener() {
                val resultIntent = Intent(this, NewPaquet::class.java)
                resultIntent.putExtra(NewPaquet.REQUEST_CODE_INTEREST_POINTS_STRING, listInterestPoints)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

        }
    }
    private fun reestablishEditTexts(editTextName: EditText, editTextCoord: EditText) {
        editTextName.setText("")
        editTextCoord.setText("")
    }

    fun emptyFields(pointName: EditText, pointCoord: EditText): Boolean{

        if (pointName.text.isNullOrEmpty() || pointCoord.text.isNullOrEmpty()) return true
        return false
    }
