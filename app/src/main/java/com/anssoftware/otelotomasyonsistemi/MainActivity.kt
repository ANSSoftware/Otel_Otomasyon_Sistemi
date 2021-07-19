package com.anssoftware.otelotomasyonsistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    var x=0
    var Sicaklik: Number= 0
    var SicaklikString =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()
        sicaklikGetir()
    }

    fun onClickMusteriIslemleri(view: View) {
        val intent = Intent(applicationContext, musteriIslemleri::class.java)
        startActivity(intent)
    }

    fun onClickBakiyeYukle(view: View) {
        val intent = Intent(applicationContext, bakiyeYukle::class.java)
        startActivity(intent)
    }

    fun onClickSatisIslemleri(view: View) {
        val intent = Intent(applicationContext, satisIslemleri::class.java)
        startActivity(intent)
    }


    fun sicaklikGetir() {

        db.collection("Sicaklik").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                if(snapshot != null){
                    if(!snapshot.isEmpty){

                        val documents = snapshot.documents

                        for(document in documents){
                            if(x==0){
                                x++
                                Sicaklik = document.get("temp") as Number
                                var SicaklikD = Sicaklik.toDouble()
                                SicaklikString=(Math.floor(SicaklikD*100)/100).toString()
                                sicaklik.text="SICAKLIK "+SicaklikString+"°C"
                            }

                        }
                    }
                }
            }
        }
    }


}