package com.anssoftware.otelotomasyonsistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class musteriIslemleri : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musteri_islemleri)

    }

    fun onClickYeniMusteriEkle(view: View) {
        val intent = Intent(applicationContext, yeniMusteriEkle::class.java)
        startActivity(intent)
    }

    fun onClickMusteriKaydiSil(view: View) {
        val intent = Intent(applicationContext, musteriKaydiSil::class.java)
        startActivity(intent)
    }







}