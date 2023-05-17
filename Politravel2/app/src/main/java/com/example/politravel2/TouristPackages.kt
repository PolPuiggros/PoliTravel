package com.example.politravel2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.politravel.DetailActivity

class TouristPackages: AppCompatActivity()
{
    private lateinit var adapter: PackagesAdapter
    private val getResultFromActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val paquetsUpdated = Utilties.getPaquets(this)
            refrescarLlista(paquetsUpdated)
        }
    }
    private var paquetsFiltereds = mutableListOf<Paquet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.llistat_screen)
        val fab = findViewById<ImageButton>(R.id.FAB)
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setQuery("", false);
        searchView.clearFocus();

        var paquets = Utilties.getPaquets(this)
        refrescarLlista(paquets)


        fab.setOnClickListener() {
            val intent = Intent(this, NewPaquet::class.java)
            getResultFromActivity.launch(intent)
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                filterPackages(query, paquets, adapter)
                return true
            }
        })
    }


    fun filterPackages(query: String?, paquets: MutableList<Paquet>, adapter: PackagesAdapter) {
        var nameLower: String
        paquetsFiltereds.clear()
        for (paquet in paquets){
            nameLower = paquet.Name_package.lowercase()
            if (nameLower.contains(query!!.lowercase())){
                paquetsFiltereds.add(paquet)
            }
        }
        if (paquetsFiltereds.isEmpty()){
            Toast.makeText(this, "No packages found", Toast.LENGTH_LONG).show()
        }
        else{
            adapter.setFilteredList(paquetsFiltereds)
        }
    }

    override fun onRestart() {
        super.onRestart()
        val searchview = findViewById<SearchView>(R.id.searchView)
        searchview.setQuery("", false)
        searchview.clearFocus()
    }
    fun refrescarLlista(paquets: MutableList<Paquet>)
    {
        adapter = PackagesAdapter(this, paquets)
        val lstViewPackages = findViewById<RecyclerView>(R.id.Rcycler_list)
        lstViewPackages.hasFixedSize()
        lstViewPackages.layoutManager = LinearLayoutManager(this)
        lstViewPackages.adapter = adapter

        adapter.setOnClickListener()
        {
            val intent = Intent(this, DetailActivity::class.java)
            val touristPackage: Paquet
            if (paquetsFiltereds.isEmpty()) {
                touristPackage = paquets[lstViewPackages.getChildAdapterPosition(it)]
                intent.putExtra("PAQUET", touristPackage)
                startActivity(intent)
            } else {
                touristPackage = paquetsFiltereds[lstViewPackages.getChildAdapterPosition(it)]
                intent.putExtra("PAQUET", touristPackage)
                startActivity(intent)
            }
        }


        adapter.setOnLongClickListener()
        {
            val intent = Intent(this, EditPackage::class.java)
            val touristPackage: Paquet
            if (paquetsFiltereds.isEmpty()) {
                touristPackage = paquets[lstViewPackages.getChildAdapterPosition(it)]
                intent.putExtra("PAQUET", touristPackage)
                getResultFromActivity.launch(intent)
            } else {
                touristPackage = paquetsFiltereds[lstViewPackages.getChildAdapterPosition(it)]
                intent.putExtra("PAQUET", touristPackage)
                getResultFromActivity.launch(intent)
            }
            return@setOnLongClickListener true
        }

    }
}