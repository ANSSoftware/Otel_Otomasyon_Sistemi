package com.anssoftware.otelotomasyonsistemi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_musteri_bilgileri_gir.*

class musteriBilgileriGir : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    var tag=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musteri_bilgileri_gir)

        db = FirebaseFirestore.getInstance()

        var intent = intent
         tag = intent.getStringExtra("yeniMusteriEkleTag").toString()
    }

    fun onClickMusteriyiSistemeKaydet(view: View) {
        if(musteri_adi.text.toString()==""||
                musteri_adi.text.toString()==""||
                musteri_soyadi.text.toString()==""||
                musteri_tc.text.toString()==""||
                musteri_telefon.text.toString()==""||
                musteri_adresi.text.toString()==""||
                musteri_eposta.text.toString()=="")
                {
                    Toast.makeText(this, "LÜTFEN TÜM BİLGİLERİ EKSİKSİZ GİRİNİZ", Toast.LENGTH_SHORT).show()
                }else{
                        kayitFireBaseAktarma()
                     }
    }

    fun kayitFireBaseAktarma() {
        val postMapMusteri = hashMapOf<String, Any>()
        postMapMusteri.put("Adi", musteri_adi.text.toString())
        postMapMusteri.put("Soyadi", musteri_soyadi.text.toString())
        postMapMusteri.put("TcKimlikNo", musteri_tc.text.toString())
        postMapMusteri.put("TelefonNo", musteri_telefon.text.toString())
        postMapMusteri.put("Adres", musteri_adresi.text.toString())
        postMapMusteri.put("Email", musteri_eposta.text.toString())
        postMapMusteri.put("tag", tag)
        postMapMusteri.put("bakiye",0)
        db.collection("Musteriler").add(postMapMusteri).addOnCompleteListener { task ->

            if (task.isComplete && task.isSuccessful) {
                Toast.makeText(this, "MÜŞTERİ KAYDI TAMAMLANDI", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
        }
    }

}