package com.anssoftware.otelotomasyonsistemi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class odaServisi : AppCompatActivity() {

    lateinit var odaServisi_satin_al_sp: Spinner
    var hizmetler = arrayListOf("LÜTFEN BİR SERVİS SEÇİNİZ")
    var secilenHizmet = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oda_servisi)
        listeOlustur()
        urun_satin_al_spfun()
    }

    fun urun_satin_al_spfun() { //Spinner bilgileri oluşturuldu
        odaServisi_satin_al_sp = findViewById(R.id.odaServisiSpinner) as Spinner
        odaServisi_satin_al_sp.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, hizmetler)
        odaServisi_satin_al_sp.onItemSelectedListener = object : AdapterView.OnItemClickListener,
                AdapterView.OnItemSelectedListener {
            override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                secilenHizmet = hizmetler[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun listeOlustur(){
        hizmetler.add("Hamburger")
        hizmetler.add("Pizza")
        hizmetler.add("Kola")
        hizmetler.add("Ayran")
        hizmetler.add("Tavuk Döner")
        hizmetler.add("Sahanda Yumurta")
        hizmetler.add("Kahvaltı Tabağı")
        hizmetler.add("Serpme Kahvaltı")
        hizmetler.add("Günün Çorbası")
        hizmetler.add("Meyve Suyu")
        hizmetler.add("Soda")

    }

    fun onClickOdaServisiSatisiTamamla(view: View) {}
}