package com.pj.nsutgdscapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class MyListAdapter (var mCtx:Context, var resource:Int, var items:List<Model>)
    : ArrayAdapter<Model>(mCtx, resource, items ) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutInflater :LayoutInflater = LayoutInflater.from(mCtx)
            val view : View = layoutInflater.inflate(resource,null)
            val titleview : TextView = view.findViewById(R.id.titletext)
            val timeview : TextView = view.findViewById(R.id.timetext)
            val priceview : TextView = view.findViewById(R.id.pricetext)
            val row : Model = items[position]
            titleview.text = row.title
            priceview.text = row.price
            timeview.text = row.time
            return view
        }
}