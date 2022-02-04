package com.pj.nsutgdscapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pj.nsutgdscapp.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var listview: ListView? = null
    private var fab: FloatingActionButton? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    // private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_first, container, false)
        listview = view.findViewById(R.id.itemlist)
        fab = view.findViewById(R.id.fab)
        //_binding = FragmentFirstBinding.inflate(inflater, container, false)
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab?.show()
        val db = DBHelper(requireActivity(),null)
        //sharedElementEnterTransition = MaterialContainerTransform()
        //val listview = requireActivity().layoutInflater.inflate(R.layout.fragment_first,null).findViewById<ListView>(R.id.itemlist)
        //val listview = requireActivity().findViewById<ListView>(R.id.itemlist)
        //val context = context as ListActivity
        //val listview = context.findViewById<ListView>(R.id.itemlist)
        var list = mutableListOf<Model>()
        val cur = db.getItem()
        cur!!.moveToFirst()
        var j = 0
        while(cur.moveToNext()){
            val temp = Model(cur.getString(cur.getColumnIndexOrThrow(DBHelper.ITEM_NAME_COL)),
                cur.getString(cur.getColumnIndexOrThrow(DBHelper.ITEM_TIME_COL)),
                cur.getString(cur.getColumnIndexOrThrow(DBHelper.ITEM_PRICE_COL)))
            list.add(temp)
            j += 1
        }
        Log.d("TAG",list.toString())
        listview?.adapter = MyListAdapter(requireActivity(),R.layout.mylistitem,list)
        cur.close()
        listview?.setOnItemLongClickListener { parent, view, position, id ->
            val element : Model = parent.getItemAtPosition(position) as Model
            confirmDelete(element.title,element.price)
            true
        }



    }
    private fun confirmDelete(itemname: String,itemprice: String) {
        val ad = AlertDialog.Builder(requireActivity())
        val msg = " Delete $itemname?"
        ad.setTitle("Confirm")
        ad.setMessage(msg)
        ad.setPositiveButton("YES"){dialog, id ->
            val db = DBHelper(requireActivity(),null)
            db.delItem(itemname,itemprice)
            Toast.makeText(requireContext(),"$itemname deleted successfully" ,Toast.LENGTH_SHORT).show()
            var list = mutableListOf<Model>()
            val cur = db.getItem()
            cur!!.moveToFirst()
            var j = 0
            while(cur.moveToNext()){
                val temp = Model(cur.getString(cur.getColumnIndexOrThrow(DBHelper.ITEM_NAME_COL)),
                    cur.getString(cur.getColumnIndexOrThrow(DBHelper.ITEM_TIME_COL)),
                    cur.getString(cur.getColumnIndexOrThrow(DBHelper.ITEM_PRICE_COL)))
                list.add(temp)
                j += 1
            }
            Log.d("TAG",list.toString())
            listview?.adapter = MyListAdapter(requireActivity(),R.layout.mylistitem,list)
            dialog.dismiss()
        }
        ad.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = ad.create()
        alert.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}