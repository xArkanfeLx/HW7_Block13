package com.example.sqlitepractice1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter(context: Context, personList: MutableList<Product>) :
    ArrayAdapter<Product>(context, R.layout.product_list_item, personList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val product = getItem(position)
        if (view == null) view =
            LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false)

        val nameTV = view?.findViewById<TextView>(R.id.nameTV)
        val weightTV = view?.findViewById<TextView>(R.id.weightTV)
        val priceTV = view?.findViewById<TextView>(R.id.priceTV)

        nameTV?.text = product?.name
        weightTV?.text = product?.weight
        priceTV?.text = product?.price

        return view!!
    }
}