package com.example.sqlitepractice1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlite.DBHelper

class MainActivity : AppCompatActivity() {

    private val db = DBHelper(this,null)

    val products: MutableList<Product> = mutableListOf()

    private lateinit var toolbarTB: Toolbar
    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var priceET: EditText
    private lateinit var saveBTN: Button
    private lateinit var productsLV: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()

        saveBTN.setOnClickListener{
            val name = nameET.text.toString()
            val weight = weightET.text.toString()
            val price = priceET.text.toString()
            db.addProduct(name,weight,price)
            products.add(Product(name,weight,price))
            reloadProductLV()
            clearFields()
        }
    }

    override fun onResume() {
        super.onResume()
        getAllProducts()
    }

    private fun getAllProducts(){
        val cursor = db.getInfo()
        if(cursor!=null && cursor.moveToFirst()) {
            cursor.moveToFirst()
            do {
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                val weight = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WEIGHT))
                val price = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PRICE))
                products.add(Product(name,weight,price))
            } while (cursor.moveToNext())
            reloadProductLV()
        }
    }

    private fun reloadProductLV(){
        val listAdapter = ListAdapter(this@MainActivity, products)
        productsLV.adapter = listAdapter
        listAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        nameET.text.clear()
        weightET.text.clear()
        priceET.text.clear()
    }

    private fun init(){
        toolbarTB = findViewById(R.id.toolbarTB)
        setSupportActionBar(toolbarTB)

        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        priceET = findViewById(R.id.priceET)
        saveBTN = findViewById(R.id.saveBTN)
        productsLV = findViewById(R.id.productsLV)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}