package com.anssoftware.otelotomasyonsistemi

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_yeni_musteri_ekle.*

class yeniMusteriEkle : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    var tag=""
    var tagKullaniliyorMu=false
    var KayitliTagler = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yeni_musteri_ekle)

        db = FirebaseFirestore.getInstance()
        tagKayitliMi()
        getEtiketData()
    }


    fun getEtiketData(){
        var x=0
        var EsitlikVarMi=false
        db.collection("Etiketler").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                if(snapshot != null){
                    if(!snapshot.isEmpty){

                        val documents = snapshot.documents

                        for(document in documents){
                            val IslemTamamlandi = document.get("IslemTamamlandi") as Boolean
                            if(IslemTamamlandi==false &&x==0){
                                x++
                                tag = document.get("tag") as String
                                for(tagKontrol in KayitliTagler){
                                    if(tag==tagKontrol){
                                        EsitlikVarMi=true
                                    }
                                }
                                if(EsitlikVarMi){

                                    Toast.makeText(this, "BU NFC KARTI SİSTEME KAYITLIDIR LÜTFEN BAŞKA KART OKUTUNUZ", Toast.LENGTH_LONG).show()
                                    db.collection("Etiketler").document(document.id)
                                            .update("IslemTamamlandi", true)
                                    tagKullaniliyorMu=true
                                    kirmiziYap()
                                    musteri_bilgileri_gir.text="BAŞKA KART EKLEMEK İÇİN LÜTFEN BASINIZ"
                                    break
                                }else{
                                    db.collection("Etiketler").document(document.id)
                                            .update("IslemTamamlandi", true)
                                    tagKullaniliyorMu=false
                                    yesilYap()
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onClickMusteriBilgileriniGir(view: View) {
        if(tag==""){
            Toast.makeText(this,"LÜTFEN ÖNCE NFC ETİKETİNİ OKUTUNUZ",Toast.LENGTH_LONG).show()
        }else if(tagKullaniliyorMu==true){
            BaskaKartOkutunuz()
            musteri_bilgileri_gir.text="MÜŞTERİ BİLGİLERİNİ GİR"

        }else{
            val intent = Intent(applicationContext, musteriBilgileriGir::class.java)
            intent.putExtra("yeniMusteriEkleTag",tag)
            startActivity(intent)
            finish()
        }
    }

    fun yesilYap(){
        okutmaBekleniyor.text="OKUTMA BAŞARILI"
        okutmaBekleniyor.setTextColor(Color.GREEN)
        nfc_image.setImageResource(R.drawable.nfcyesil)
    }

    fun kirmiziYap(){
        okutmaBekleniyor.text="KULLANILAN KART"
        okutmaBekleniyor.setTextColor(Color.RED)
        nfc_image.setImageResource(R.drawable.nfckirmizi)
    }

    fun tagKayitliMi() {
            db.collection("Musteriler").addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }else{
                    if(snapshot != null){
                        if(!snapshot.isEmpty){

                            val documents = snapshot.documents

                            for(document in documents){
                                var KontrolTag = document.get("tag") as String
                                KayitliTagler.add(KontrolTag)
                            }
                        }
                    }
                }

            }
    }

    fun BaskaKartOkutunuz() {
        getEtiketData()
        okutmaBekleniyor.text="OKUTMA BEKLENİYOR"
        okutmaBekleniyor.setTextColor(getResources().getColor(R.color.sari))
        nfc_image.setImageResource(R.drawable.nfcimage)
    }



}