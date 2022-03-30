package com.example.photograhdeal.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photograhdeal.Communicator
import com.example.photograhdeal.R
import com.example.photograhdeal.ui.profile.ProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Search : Fragment() {

    companion object {
        fun newInstance() = Search()
    }
    private lateinit var communicator: Communicator
    private lateinit var viewModel: SearchViewModel
    lateinit var mAuth: FirebaseAuth
    private lateinit var dataRef: DatabaseReference
    lateinit var listSearch: ArrayList<ProfileData>
    lateinit var searchRecyclerView: RecyclerView
    lateinit var searchBar: SearchView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_fragment, container, false)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        searchBar = root.findViewById<SearchView>(R.id.search_text)
        searchRecyclerView = root.findViewById<RecyclerView>(R.id.searchRecycler)
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)

        searchRecyclerView.setHasFixedSize(true)
        listSearch = arrayListOf<ProfileData>()
        communicator = activity as Communicator

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(searchtxt: String?): Boolean {
                if (searchtxt != null) {
                    val input = searchtxt.lowercase()

                    dataRef = FirebaseDatabase.getInstance().getReference("User").child("Auth")

                    dataRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                listSearch.clear()
                                for (userSnapshot in snapshot.children) {
                                    val search = userSnapshot.getValue(ProfileData::class.java)
                                    if (search != null) {
                                        if (search.name!!.lowercase().contains(input)) {
                                                    listSearch.add(search)
                                        }
                                        var adapter = SAdapter(listSearch)
                                        searchRecyclerView.adapter = adapter

                                        adapter.setOnSearchClickListener(object : SAdapter.onClickListener{
                                            override fun onItemClick(position: Int) {
                                                communicator.passDataCom(adapter.getArraylist()[position].name)

                                            }

                                        })
                                    }
                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                return true
            }
        })

        return root
    }


}