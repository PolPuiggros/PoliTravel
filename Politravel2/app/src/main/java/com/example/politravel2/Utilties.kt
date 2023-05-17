package com.example.politravel2

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.EditText
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Utilties {
    companion object {
        fun correctCoordinates(coordinates: EditText): Boolean {
            var correctCoordinates = false
            val cadena = coordinates.text;
            if (cadena.contains(",")){
                val latitude = cadena.split(",")[0]
                val longitude = cadena.split(",")[1]
                if (latitude.contains(".") && longitude.contains(".")){
                    correctCoordinates = true
                }
            }
            return correctCoordinates
        }

        fun getPaquets(context: Context): MutableList<Paquet> {
            val jsonFilePath = context.filesDir.toString() + "/json/paquetsTuristics.json"
            val jsonFile = FileReader(jsonFilePath)
            val listPaquetsType = object : TypeToken<MutableList<Paquet>>() {}.type
            return Gson().fromJson(jsonFile, listPaquetsType)
        }
        fun writePaquets(context: Context, paquets: MutableList<Paquet>) {
            val jsonFilePath = context.filesDir.toString() + "/json/paquetsTuristics.json"
            val jsonFile = FileWriter(jsonFilePath)
            var gson = Gson()
            var jsonElement = gson.toJson(paquets)
            jsonFile.write(jsonElement)
            jsonFile.close()
        }

        fun accedirImg(context: Context, imgView: ImageView, imgNom: String){
            val paquetImgPath = context.filesDir.absolutePath + "/img/" + imgNom + ".jpg"
            val bitmap = BitmapFactory.decodeFile(paquetImgPath)
            imgView.setImageBitmap(bitmap)
        }
        fun joinCoordinates(latitude: Float, longitude: Float): String{
            return "$latitude,$longitude"
        }
    }
}
