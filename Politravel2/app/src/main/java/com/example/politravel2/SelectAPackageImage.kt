package com.example.politravel2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SelectAPackageImage: AppCompatActivity(){
    lateinit var imagesGRV: GridView
    var selectedImage = -1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_a_package_image)

        imagesGRV = findViewById(R.id.GridViewImages)
        val btnChoose = findViewById<Button>(R.id.btnChoose)
        val btnGoback = findViewById<ImageButton>(R.id.btnGoBackSelectImage)

        val imagesList = listOf(
            R.drawable.island,
            R.drawable.africa,
            R.drawable.city,
            R.drawable.mountains_everest,
            R.drawable.sidney,
            R.drawable.xina,
            R.drawable.italy_south,
            R.drawable.india,
            R.drawable.london_bridge,
            R.drawable.newyork,
            R.drawable.barcelona,
            R.drawable.italy,
            R.drawable.poland,
            R.drawable.france
        )


        val itemAdapter = GridViewAdapter(imagesList = imagesList, this)
        imagesGRV.adapter = itemAdapter

        imagesGRV.setOnItemClickListener { parent, view, position, id ->
            val selectedView = parent.getChildAt(position)
            selectedImage = imagesList[position]
        }

        btnGoback.setOnClickListener{
            finish();
        }
        btnChoose.setOnClickListener {
            if (selectedImage >= 0){
                var resources = resources
                val selected = imagesList[selectedImage]
                val nameSelectedImg = resources.getResourceEntryName(selected)
                val resultIntent = Intent()
                resultIntent.putExtra(NewPaquet.REQUEST_CODE_IMAGE_STRING, nameSelectedImg)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

        }

    }

}