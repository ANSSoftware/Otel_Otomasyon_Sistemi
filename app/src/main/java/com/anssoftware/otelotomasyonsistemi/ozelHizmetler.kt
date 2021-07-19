package com.anssoftware.otelotomasyonsistemi

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_ozel_hizmetler.*

class ozelHizmetler : AppCompatActivity() {

    lateinit var odaServisi_satin_al_sp: Spinner
    var hizmetler = arrayListOf("LÜTFEN BİR ÖZEL HİZMET SEÇİNİZ")
    var secilenHizmet = ""
    private lateinit var db : FirebaseFirestore
    var tag=""
    var tagKullaniliyorMu=false
    var KayitliTagler = ArrayList<String>()
    var KayitliBakiyeler = ArrayList<Number>()
    var TutulanBakiye : Number = 0
    var uruntekliTutar=0
    var servisTutari =0
    var bakiyeSondurum=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ozel_hizmetler)
        listeOlustur()
        urun_satin_al_spfun()
        db = FirebaseFirestore.getInstance()
        tagKayitliMi()
        getEtiketData()

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
        hizmetler.add("Aile Havuzu")
        hizmetler.add("Odada Maç Keyfi")
    }

    fun onClickOdaServisiSatisiTamamla(view: View) {
        if(tag==""){
            Toast.makeText(this,"LÜTFEN ÖNCE NFC ETİKETİNİ OKUTUNUZ", Toast.LENGTH_LONG).show()
        }else if(tagKullaniliyorMu==false){
            BaskaKartOkutunuz()
            oda_servisi_satisi_tamamla.textSize= 30F
            oda_servisi_satisi_tamamla.text="SATIŞI TAMAMLA"

        }else if(secilenHizmet=="LÜTFEN BİR ÖZEL HİZMET SEÇİNİZ"){
            Toast.makeText(this,"LÜTFEN ÖNCE BİR ÖZEL HİZMET SEÇİNİZ", Toast.LENGTH_LONG).show()
        }else if(oda_servisi_adedi.text.toString()==""){
            Toast.makeText(this,"LÜTFEN ÖNCE ÖZEL HİZMET SAATİNİ GİRİNİZ", Toast.LENGTH_LONG).show()
        }else if(servisTutari>TutulanBakiye.toInt()){
            Toast.makeText(this,"BAKİYE YETERSİZ", Toast.LENGTH_LONG).show()
        }else{
            bakiyeSondurum=TutulanBakiye.toInt()-servisTutari
            bakiyeGuncelle()
            Toast.makeText(this, "SATIŞ TAMAMLANDI", Toast.LENGTH_LONG).show()

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

                                for(tagSayac in 1.. KayitliTagler.size ){
                                    if(tag==KayitliTagler[tagSayac-1]){
                                        EsitlikVarMi=true
                                        TutulanBakiye=KayitliBakiyeler[tagSayac-1]
                                    }
                                }
                                if(!EsitlikVarMi){

                                    Toast.makeText(this, "BU NFC KARTI SİSTEME KAYITLI DEĞİLDİR LÜTFEN BAŞKA KART OKUTUNUZ", Toast.LENGTH_LONG).show()
                                    db.collection("Etiketler").document(document.id)
                                        .update("IslemTamamlandi", true)
                                    tagKullaniliyorMu=false
                                    kirmiziYap()
                                    oda_servisi_satisi_tamamla.textSize= 15F
                                    oda_servisi_satisi_tamamla.text="BAŞKA KART OKUTMAK İÇİN LÜTFEN BASINIZ"
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
        okutmaBekleniyor.text="KAYITSIZ KART"
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
                            var bakiye = document.get("bakiye") as Number
                            KayitliTagler.add(KontrolTag)
                            KayitliBakiyeler.add(bakiye)
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

    fun tutarHesapla(){

        if(secilenHizmet=="Aile Havuzu"){
            uruntekliTutar=300
        }else if(secilenHizmet=="Odada Maç Keyfi"){
            uruntekliTutar=200
        }
        if(oda_servisi_adedi.text.toString()!=""){
            servisTutari=uruntekliTutar*oda_servisi_adedi.text.toString().toInt()
        }

    }

    fun bakiyeGuncelle(){
        var d=0
        db.collection("Musteriler").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                if(snapshot != null){
                    if(!snapshot.isEmpty){

                        val documents = snapshot.documents

                        for(document in documents){
                            var KontrolTag = document.get("tag") as String
                            if(KontrolTag==tag && d==0){
                                d++
                                db.collection("Musteriler").document(document.id)
                                    .update("bakiye", bakiyeSondurum)
                            }
                        }
                    }
                }
            }

        }
    }

    fun onClickTutariOnayla(view: View) {
        tutarHesapla()
        fiyatText.text="Tutar: "+servisTutari+"TL"
    }


}