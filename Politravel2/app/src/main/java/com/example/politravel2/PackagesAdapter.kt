package com.example.politravel2

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PackagesAdapter (private val context: Context, private var paquets: MutableList<Paquet>):
    RecyclerView.Adapter<PackagesAdapter.PackagesViewHolder>(), View.OnClickListener, View.OnLongClickListener {
    private val layout = R.layout.item_adapter
    private var clickListener: View.OnClickListener? = null
    private var longClickListener: View.OnLongClickListener? = null


    class PackagesViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        var nomPaquet: TextView = view.findViewById(R.id.PackageNameCard)
        var imgPaquet: ImageView = view.findViewById(R.id.ImageViewCardList)
        var metodeTransport: ImageView = view.findViewById(R.id.transportIconCard)
        var duracio: TextView = view.findViewById(R.id.durationTextCard)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return PackagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackagesViewHolder, position: Int) {
        val paquet = paquets[position]
        bindPaquet(holder, paquet)

    }override fun getItemCount() = paquets.size

    @SuppressLint("SetTextI18n")
    fun bindPaquet(holder: PackagesViewHolder, paquet: Paquet)
    {
        val metodeTransport = paquet.Mean_of_transport
        val duracio = paquet.DurationOfTheTrip
        val unitat = paquet.Unit

        holder.nomPaquet.text = paquet.Name_package
        Utilties.accedirImg(context ,holder.imgPaquet, paquet.Img)
        if (duracio == 1){
            when (unitat) {
                'D' -> holder.duracio.text = paquet.DurationOfTheTrip.toString() + " day"
                'W' -> holder.duracio.text = paquet.DurationOfTheTrip.toString() + " week"
                'M' -> holder.duracio.text = paquet.DurationOfTheTrip.toString() + " month"
            }
        }
        else {
            when (unitat) {
                'D' -> holder.duracio.text = paquet.DurationOfTheTrip.toString() + " days"
                'W' -> holder.duracio.text = paquet.DurationOfTheTrip.toString() + " weeks"
                'M' -> holder.duracio.text = paquet.DurationOfTheTrip.toString() + " months"
            }
        }

        when(metodeTransport){
            "Cotxe" -> holder.metodeTransport.setImageResource(R.drawable.car)
            "Avio" -> holder.metodeTransport.setImageResource(R.drawable.plane)
            "Tren" -> holder.metodeTransport.setImageResource(R.drawable.train)
            "Barco" -> holder.metodeTransport.setImageResource(R.drawable.boat)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(paquetsFiltered: MutableList<Paquet>){
        this.paquets = paquetsFiltered
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: View.OnClickListener)
    {
        clickListener = listener
    }
    fun setOnLongClickListener(listener: View.OnLongClickListener)
    {
        longClickListener = listener
    }

    override fun onLongClick(view: View?): Boolean {
        longClickListener?.onLongClick(view)
        return true
    }

    override fun onClick(view: View?)
    {
        clickListener?.onClick(view)
    }

}