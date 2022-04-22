package com.example.testfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.testfirebase.R
import com.google.firebase.database.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonSave: Button
    private lateinit var listViewData: ListView
    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        initializeDatabase()
        displayResult()
        getSpecificData()
        updateSpecificValue()

        //executed when save is clicked

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toInt()
            val user = User(name = name,age = age)
            databaseRef.child(user.id).setValue(user)
        }
    }

    private fun getSpecificData() {
        //here 3 is the key of the value i want to get
        databaseRef.child("3").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.getValue(User::class.java)
                Log.d("Main: Username",user?.name.toString())
                Log.d("Main: age",user?.age.toString())
            }
        }
    }

    private fun displayResult() {

        //this will display results in the listview; called for the first time and for every update in the database

        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<String>()
                for (dataSnapShot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)
                    dataList.add("${user?.name} ${user?.age}")
                }
                adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,dataList)
                listViewData.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun updateSpecificValue() {
        //if we want to update username of key 4

        databaseRef.child("4").child("name").setValue("new username")
    }

    private fun initializeViews() {
        editTextName = findViewById(R.id.et_name)
        editTextAge = findViewById(R.id.et_age)
        buttonSave = findViewById(R.id.btn_save)
        listViewData = findViewById(R.id.lv_data)
    }

    private fun initializeDatabase() {
        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("users") //users is the database name

    }
}