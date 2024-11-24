package com.example.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlitepractice1.Product

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "PRODUCT_DATABASE_1"
        private val DATABASE_VERSION = 1
        var lastKey:Int = 1
        val TABLE_NAME = "product_table"
        val KEY_ID = "id"
        val KEY_NAME = "name"
        val KEY_WEIGHT = "weight"
        val KEY_PRICE = "price"
    }

    fun getLastKey():Int{
        return lastKey++
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE $TABLE_NAME ($KEY_ID INTEGET PRIMARY KEY, $KEY_NAME TEXT, $KEY_WEIGHT TEXT,$KEY_PRICE TEXT)")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun addProduct(product: Product) {
        val values = ContentValues()
        values.put(KEY_ID,product.id)
        values.put(KEY_NAME,product.name)
        values.put(KEY_WEIGHT,product.weight)
        values.put(KEY_PRICE,product.price)
        val db = this.writableDatabase
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    @SuppressLint("Range")
    fun getInfo():MutableList<Product>{
        val products: MutableList<Product> = mutableListOf()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME",null)
        if(cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val weight = cursor.getString(cursor.getColumnIndex(KEY_WEIGHT))
                val price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                products.add(Product(name, weight, price,id))
            } while (cursor.moveToNext())
        }
        return products
    }

    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_ID,product.id)
        values.put(KEY_NAME,product.name)
        values.put(KEY_WEIGHT,product.weight)
        values.put(KEY_PRICE,product.price)
        db.update(TABLE_NAME,values,"id=${product.id}",null)
        db.close()
    }

    fun deleteProduct(id:Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_ID,id)
        db.delete(TABLE_NAME,"id=$id",null)
        db.close()
    }
    fun deleteAll(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,null,null)
        lastKey=1
    }
}