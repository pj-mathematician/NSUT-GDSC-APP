package com.pj.nsutgdscapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.pj.nsutgdscapp.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // sharedElementEnterTransition = MaterialContainerTransform()
        // val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)

            //.findViewById<FloatingActionButton>(R.id.fab)
        binding.itemcancel.setOnClickListener {
            //val extras = FragmentNavigatorExtras(view to "shared_element_container")
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            fab.show()
        }
        binding.itemsave.setOnClickListener {
            //val extras = FragmentNavigatorExtras(view to "shared_element_container")


            val item = requireActivity().findViewById<TextInputEditText>(R.id.item)
            val price = requireActivity().findViewById<TextInputEditText>(R.id.price)
            if (item.text.toString().isEmpty() || price.text.toString().isEmpty()) {
                showEmptyMessage()
            } else {
                val db = DBHelper(requireActivity(), null)
                if (db.checkexists(item.text.toString())){
                    if (db.editItem(item.text.toString(),price.text.toString())==1) {
                        Toast.makeText(
                            context,
                            "${item.text.toString()} Edited Successfully",
                        Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to Edit ${item.text.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    db.addItem(item.text.toString(),price.text.toString())
                    Toast.makeText(
                        context,
                        "Added ${item.text.toString()} Successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                //val listview = requireActivity().findViewById<ListView>(R.id.itemlist)

                fab.show()

            }
        }

    }
    private fun showEmptyMessage() {
        val ad = AlertDialog.Builder(requireActivity())
        val msg = "No Empty Boxes Allowed!!"
        ad.setTitle("ALERT")
        ad.setMessage(msg)
        ad.setNeutralButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = ad.create()
        alert.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun hideKeyboard(activity: Activity) {
        try {
            val inputManager = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}