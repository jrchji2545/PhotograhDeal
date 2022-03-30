package com.example.photograhdeal.ui.profile

import android.content.ComponentName
import android.content.ServiceConnection
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photograhdeal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileClick : Fragment(), ServiceConnection {

    lateinit var profileImg: ImageView
    lateinit var dbRef: DatabaseReference
    lateinit var abRef: DatabaseReference
    lateinit var profileName: TextView
    lateinit var bioText: TextView
    lateinit var mAuth: FirebaseAuth
    lateinit var galleryRec : RecyclerView
    lateinit var listProfile: ArrayList<ProfileData>
    lateinit var listGallery: ArrayList<Gallery>
    lateinit var getname:String
    var position:Int = 0
    var pos:Int = 0

    private lateinit var viewModel: ProfileClickViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.profile_click_fragment, container, false)
        profileImg = root.findViewById<ImageView>(R.id.profile_image_click)
        profileName = root.findViewById<TextView>(R.id.profile_name_click)
        bioText = root.findViewById<TextView>(R.id.bio_click)
        galleryRec = root.findViewById<RecyclerView>(R.id.gallery_click)
        mAuth = FirebaseAuth.getInstance()
        galleryRec.layoutManager = LinearLayoutManager(this.context,RecyclerView.HORIZONTAL,false)
        galleryRec.setHasFixedSize(true)

        listGallery = arrayListOf<Gallery>()
        listProfile = arrayListOf<ProfileData>()
        dbRef = FirebaseDatabase.getInstance().getReference("User").child("Auth")

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {
                        val profile = userSnapshot.getValue(ProfileData::class.java)

                            listProfile.add(profile!!)

                        //Toast.makeText(activity, profile.uid, Toast.LENGTH_SHORT).show()
                    }
                    val adapter = PAdapter(listProfile)
                    getname = arguments?.getString("name").toString()

                    var size = adapter.getArraylist().size-1
                    for(i in 0..size){
                        if(getname.equals(adapter.getArraylist()[i].name)){
                            position = i
                        }
                    }
                    pos+=position

                    Glide.with(requireActivity()).load(adapter.getArraylist()[pos].profile).into(profileImg)
                    profileName.text = adapter.getProfileAdapter()[pos].name.toString()
                    bioText.text = adapter.getProfileAdapter()[pos].bio.toString()

//                    val adapter = PAdapter(musicNewArrayList)
//                    musicNewRecyclerView.adapter = adapter


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        abRef = FirebaseDatabase.getInstance().getReference("User").child("Album")

        abRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                   // var user = mAuth.currentUser
                    for (userSnapshot in snapshot.children) {
                        val gallery = userSnapshot.getValue(Gallery::class.java)
                        if (getname == gallery!!.name) {
                            listGallery.add(gallery!!)
                        }
                        //Toast.makeText(activity, profile.uid, Toast.LENGTH_SHORT).show()
                    }
                    val adapter = GAdapter(listGallery)
                    galleryRec.adapter = adapter


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



        return root
    }


    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

    }

    override fun onServiceDisconnected(p0: ComponentName?) {

    }


}