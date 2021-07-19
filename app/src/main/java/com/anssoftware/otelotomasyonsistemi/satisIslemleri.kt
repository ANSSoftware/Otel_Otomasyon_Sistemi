package com.anssoftware.otelotomasyonsistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class satisIslemleri : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satis_islemleri)
    }

    fun onClickOdaServisi(view: View) {
        val intent = Intent(applicationContext, odaServisi::class.java)
        startActivity(intent)
    }

    fun onClickOzelHizmetler(view: View) {
        val intent = Intent(applicationContext, ozelHizmetler::class.java)
        startActivity(intent)
    }

    fun onClickOyunAlanlari(view: View) {
        val intent = Intent(applicationContext, oyunAlanlari::class.java)
        startActivity(intent)
    }

}