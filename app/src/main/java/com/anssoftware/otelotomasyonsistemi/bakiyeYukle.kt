package com.anssoftware.otelotomasyonsistemi

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_bakiye_yukle.*
import kotlinx.android.synthetic.main.activity_yeni_musteri_ekle.*
import kotlinx.android.synthetic.main.activity_yeni_musteri_ekle.nfc_image
import kotlinx.android.synthetic.main.activity_yeni_musteri_ekle.okutmaBekleniyor

class bakiyeYukle : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    var tag=""
    var tagKullaniliyorMu=false
    var KayitliTagler = ArrayList<String>()
    var bakiye = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bakiye_yukle)

        db = FirebaseFirestore.getInstance()
        tagKayitliMi()
        getEtiketData()

    }

    fun onClickBakiyeEkle(view: View) {
        if(tag=="" && bakiye_ekle.text=="BAKİYE EKLE"){
            Toast.makeText(this,"LÜTFEN ÖNCE NFC ETİKETİNİ OKUTUNUZ",Toast.LENGTH_LONG).show()
        }else if(tagKullaniliyorMu==false){
            BaskaKartOkutunuz()
            bakiye_ekle.textSize=30F
            bakiye_ekle.text="BAKİYE EKLE"
        }else if(bakiyeTutari.text.toString()==""){
            Toast.makeText(this,"LÜTFEN BAKİYE BİLGİSİ GİRİNİZ",Toast.LENGTH_LONG).show()
        }else{
            bakiyeEkle()
            Toast.makeText(this,"BAKİYE EKLENDİ",Toast.LENGTH_LONG).show()
        }
    }


    fun getEtiketData(){
        var x=0
        var EsitlikVarMi=false
        db.collection("Etiketler").orderBy("tarih",
            Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
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
                                if(!EsitlikVarMi){

                                    Toast.makeText(this, "BU KART SİSTEME KAYITLI DEĞİLDİR", Toast.LENGTH_LONG).show()
                                    db.collection("Etiketler").document(document.id)
                                        .update("IslemTamamlandi", true)
                                    tagKullaniliyorMu=false
                                    kirmiziYap()
                                    bakiye_ekle.textSize= 15F
                                    bakiye_ekle.text="BAŞKA KART EKLEMEK İÇİN LÜTFEN BASINIZ"
                                    break
                                }else{
                                    db.collection("Etiketler").document(document.id)
                                        .update("IslemTamamlandi", true)
                                    tagKullaniliyorMu=true
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

    fun yesilYap(){
        okutmaBekleniyor.text="OKUTMA BAŞARILI"
        okutmaBekleniyor.setTextColor(Color.GREEN)
        nfc_image.setImageResource(R.drawable.nfcyesil)
    }

    fun kirmiziYap(){
        okutmaBekleniyor.text="KAYITLI OLMAYAN KART"
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

    fun bakiyeEkle(){
        var y=0
        db.collection("Musteriler").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                if(snapshot != null){
                    if(!snapshot.isEmpty){

                        val documents = snapshot.documents

                        for(document in documents){
                            var KontrolTag = document.get("tag") as String
                            var Bakiye = document.get("bakiye") as Number
                            var BakiyeD = Bakiye.toDouble()
                            if(tag==KontrolTag && y==0) {
                                y++
                                bakiye= (bakiyeTutari.text).toString().toDouble()
                                bakiye+=BakiyeD
                                db.collection("Musteriler").document(document.id)
                                    .update("bakiye", bakiye)
                            }
                        }
                    }
                }
            }

        }
        }
    }

