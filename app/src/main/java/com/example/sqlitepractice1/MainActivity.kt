package com.example.sqlitepractice1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlite.DBHelper

class MainActivity : AppCompatActivity() {

    private val db = DBHelper(this, null)

    var products: MutableList<Product> = mutableListOf()

    private lateinit var toolbarTB: Toolbar
    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var priceET: EditText
    private lateinit var saveBTN: Button
    private lateinit var updateBTN: Button
    private lateinit var removeBTN: Button
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

        saveBTN.setOnClickListener {
            saveProductInDB()
        }

        updateBTN.setOnClickListener {
            updateRecord()
        }
        removeBTN.setOnClickListener {
            //deleteAllDB()
            deleteRecord()
        }
    }

    override fun onResume() {
        super.onResume()
        getAllProducts()
    }

    private fun saveProductInDB() {
        val name = nameET.text.toString()
        val weight = weightET.text.toString()
        val price = priceET.text.toString()
        val product = Product(name, weight, price,db.getLastKey())
        products.add(product)
        db.addProduct(product)
        reloadProductLV()
        clearFields()
    }

    @SuppressLint("MissingInflatedId")
    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val editId = dialogView.findViewById<EditText>(R.id.idET)
        val editName = dialogView.findViewById<EditText>(R.id.nameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.weightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.priceET)

        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("введите данные ниже:")
        dialogBuilder.setPositiveButton("Обновить") { _, _ ->
            val id = editId.text.toString()
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updatePrice = editPrice.text.toString()
            if (id.trim() != "" && updateName.trim() != "" && updateWeight.trim() != "" && updatePrice.trim() != "") {
                val product = Product(updateName, updateWeight, updatePrice,Integer.parseInt(id))
                db.updateProduct(product)
                getAllProducts()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { dialog, whitch -> }
        dialogBuilder.create().show()
    }

    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)
        val editId = dialogView.findViewById<EditText>(R.id.idET)

        dialogBuilder.setTitle("Удалить запись")
        dialogBuilder.setMessage("введите данные ниже:")
        dialogBuilder.setPositiveButton("Удалить") { _, _ ->
            val id = editId.text.toString()
            if(id.trim() != ""){
                db.deleteProduct(Integer.parseInt(id))
                getAllProducts()
            }


        }
        dialogBuilder.setNegativeButton("Отмена") { dialog, whitch -> }
        dialogBuilder.create().show()
    }

    private fun deleteAllDB(){
        db.deleteAll()
        reloadProductLV()
    }


    private fun getAllProducts() {
        products = db.getInfo()
        reloadProductLV()
    }

    private fun reloadProductLV() {
        val listAdapter = ListAdapter(this@MainActivity, products)
        productsLV.adapter = listAdapter
        listAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        nameET.text.clear()
        weightET.text.clear()
        priceET.text.clear()
    }

    private fun init() {
        toolbarTB = findViewById(R.id.toolbarTB)
        setSupportActionBar(toolbarTB)

        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        priceET = findViewById(R.id.priceET)
        saveBTN = findViewById(R.id.saveBTN)
        updateBTN = findViewById(R.id.updateBTN)
        removeBTN = findViewById(R.id.removeBTN)
        productsLV = findViewById(R.id.productsLV)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}