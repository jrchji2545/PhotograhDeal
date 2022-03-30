package com.example.photograhdeal.ui.profile

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photograhdeal.LoginActivity
import com.example.photograhdeal.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class Profile : Fragment() {

    companion object {
        fun newInstance() = Profile()
    }

    private lateinit var viewModel: ProfileViewModel
    lateinit var profileImg: ImageView
    lateinit var dbRef: DatabaseReference
    lateinit var profileName: TextView
    lateinit var bioText: TextView
    lateinit var edit_btn: ImageView
    lateinit var upload_btn: ImageView
    lateinit var test : ImageView
    lateinit var mAuth: FirebaseAuth
    lateinit var galleryRec : RecyclerView
    lateinit var listProfile: ArrayList<ProfileData>
    lateinit var listGallery: ArrayList<Gallery>
    lateinit var urlImage : Uri
    lateinit var logout : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.profile_fragment, container, false)
        profileImg = root.findViewById<ImageView>(R.id.profile_image)
        profileName = root.findViewById<TextView>(R.id.profile_name)
        edit_btn = root.findViewById<ImageView>(R.id.edit_btn)
        bioText = root.findViewById<TextView>(R.id.bio_frame)
        upload_btn = root.findViewById<ImageView>(R.id.upload_btn)
        test = root.findViewById<ImageView>(R.id.test)
        galleryRec = root.findViewById<RecyclerView>(R.id.gallery)
        logout = root.findViewById<Button>(R.id.logout)
        galleryRec.layoutManager = LinearLayoutManager(this.context,RecyclerView.HORIZONTAL,false)
        galleryRec.setHasFixedSize(true)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        listGallery = arrayListOf<Gallery>()
        listProfile = arrayListOf<ProfileData>()


        upload_btn.setOnClickListener {
            gallerySelect()
        }
        test.setOnClickListener {
            galleryUpload()
        }
        logout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        getProfile()
        getGallery()

        return root
    }

    private fun showEditTextDialog() {
        edit_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_bio, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et_editText)

            with(builder) {
                setTitle("Enter your bio")
                setPositiveButton("OK") { dialog, which ->
                    dbRef = FirebaseDatabase.getInstance().getReference("User").child("Auth").child(mAuth.currentUser!!.uid).child("bio")
                    dbRef.setValue(editText.text.toString())

                }
                setNegativeButton("Cancel") { dialog, which ->
                    Log.d("Main", "Click End")

                }
                setView(dialogLayout)
                show()
            }
        }
    }
    private fun getGallery(){
        dbRef = FirebaseDatabase.getInstance().getReference("User").child("Album")

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var user = mAuth.currentUser
                    for (userSnapshot in snapshot.children) {
                        val gallery = userSnapshot.getValue(Gallery::class.java)
                        if (user!!.displayName == gallery!!.name) {
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
    }

    private fun getProfile(){
        dbRef = FirebaseDatabase.getInstance().getReference("User").child("Auth")

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var user = mAuth.currentUser
                    for (userSnapshot in snapshot.children) {
                        val profile = userSnapshot.getValue(ProfileData::class.java)
                        if (user!!.uid == profile!!.uid) {
                            listProfile.add(profile)
                        }
                        //Toast.makeText(activity, profile.uid, Toast.LENGTH_SHORT).show()
                    }
                    val adapter = PAdapter(listProfile)
                    Glide.with(requireActivity()).load(user?.photoUrl).into(profileImg)
                    profileName.text = adapter.getProfileAdapter()[0].name.toString()
                    if(adapter.getProfileAdapter()[0].bio.toString() == "null"){
                        bioText.setText("Put your bio here....")
                    }else{
                        bioText.text = adapter.getProfileAdapter()[0].bio.toString()
                    }


                    if(user!!.displayName != profileName.text){
                        edit_btn.visibility = View.INVISIBLE
                    }else{
                        showEditTextDialog()
                    }
//                    val adapter = MyAdapter(musicNewArrayList)
//                    musicNewRecyclerView.adapter = adapter


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun galleryUpload() {
        val user = mAuth.currentUser
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("กำลังอัพโหลด")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val storageReference =
            FirebaseStorage.getInstance().getReference("${user!!.displayName}/${"album"}")
        storageReference.putFile(urlImage).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener(OnSuccessListener { uri ->
                val userRef: DatabaseReference = FirebaseDatabase.getInstance()
                    .getReference("User").child("Album")
                    .child(user!!.uid).child("img")
                val nameRef: DatabaseReference = FirebaseDatabase.getInstance()
                    .getReference("User").child("Album")
                    .child(user!!.uid).child("name")

                userRef.setValue(uri.toString())
                nameRef.setValue(user.displayName.toString())

            })
        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(activity, "อัพโหลดไม่เสร็จสิ้น", Toast.LENGTH_SHORT).show()
        }
        if (progressDialog.isShowing) progressDialog.dismiss()
        Toast.makeText(activity, "อัพโหลดเสร็จสิ้น", Toast.LENGTH_SHORT).show()
    }
    private fun gallerySelect() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 10)


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && resultCode == AppCompatActivity.RESULT_OK) {
            urlImage = data?.data!!
//            img!!.setImageURI(urlImage)


        }

    }


}