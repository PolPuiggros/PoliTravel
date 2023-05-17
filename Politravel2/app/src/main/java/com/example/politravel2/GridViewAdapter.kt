package com.example.politravel2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

internal class GridViewAdapter(/*private val imagesList: List<GridViewModal>*/private val imagesList: List<Int>, private val context: Context): BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var image: ImageView

    private val inflater = LayoutInflater.from(context) //2
    private var selectedPosition = -1 //2

    override fun getCount(): Int {
        return imagesList.size
    }

    override fun getItem(position: Int): Any? {
        return imagesList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
        //return 0;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.gridview_image, parent, false)
        val image = view.findViewById<ImageView>(R.id.idImage)
        val resourceId = imagesList[position % imagesList.size] // Obtenir l'ID de recurs de la imatge corresponent a la posició actual

        image.setImageDrawable(ContextCompat.getDrawable(context, resourceId))
        image.isSelected = selectedPosition == position

        image.setOnClickListener {
            selectedPosition = position
            (context as SelectAPackageImage).selectedImage = selectedPosition
            notifyDataSetChanged()

            for (i in 0 until parent!!.childCount) {
                val childView = parent.getChildAt(i)
                val childImage = childView.findViewById<ImageView>(R.id.idImage)
                childImage.isSelected = childView == view
            }
        }

        return view
    }

}