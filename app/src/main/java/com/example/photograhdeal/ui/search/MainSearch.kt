package com.example.photograhdeal.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.photograhdeal.R

class MainSearch : Fragment() {

    companion object {
        fun newInstance() = MainSearch()
    }

    private lateinit var viewModel: MainSearchViewModel
    lateinit var pBtn : Button
    lateinit var mBtn : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.main_search_fragment, container, false)
        pBtn = root.findViewById<Button>(R.id.p_search)
        mBtn = root.findViewById<Button>(R.id.m_search)
        pBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, Photographer())
            transaction.commit()
        }
        mBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, Model())
            transaction.commit()
        }




        return root
    }



}