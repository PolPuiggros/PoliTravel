package com.example.politravel2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.politravel.InterestPoint

class ListViewPointsAdapter(context: Context, var layout: Int,private var points: MutableList<InterestPoint>):
    ArrayAdapter<InterestPoint>(context, layout, points) {

    override fun getView(positon: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(getContext()).inflate(layout, parent, false)
        }
        bindPoint(view, points[positon])

        val btnDelete = view.findViewById<ImageButton>(R.id.BtnDeletePoint)

        btnDelete.setOnClickListener{
            points.removeAt(positon)
            notifyDataSetChanged()
        }
        return view
    }

    private fun bindPoint(view: View, interestPoint: InterestPoint)
    {
        val pointName = view.findViewById<TextView>(R.id.pointName)
        pointName.setText(interestPoint.Interest_point_name)
    }
}