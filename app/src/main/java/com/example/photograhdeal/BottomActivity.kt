package com.example.photograhdeal

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.photograhdeal.databinding.ActivityBottom2Binding
import com.example.photograhdeal.ui.dashboard.DashboardFragment
import com.example.photograhdeal.ui.home.HomeFragment
import com.example.photograhdeal.ui.profile.Profile
import com.example.photograhdeal.ui.profile.ProfileClick
import com.example.photograhdeal.ui.search.Search

class BottomActivity : AppCompatActivity(),Communicator {

    private lateinit var binding: ActivityBottom2Binding
    private val homeFragment = HomeFragment()
    private val searchFragment = Search()
    private val profileFragment = Profile()
    private val dashboardFragment = DashboardFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottom2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> replace(homeFragment)
                R.id.navigation_dashboard -> replace(dashboardFragment)
                R.id.navigation_search -> replace(searchFragment)
                R.id.navigation_profile -> replace(profileFragment)
            }
            true
        }
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard,R.id.navigation_search , R.id.navigation_profile
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }
    override fun passDataCom(position: String?) {
        val bundle = Bundle()
        bundle.putString("name",position)

        val transaction = this.supportFragmentManager.beginTransaction()
        val profileClick = ProfileClick()
        profileClick.arguments = bundle

        transaction.replace(R.id.container,profileClick).addToBackStack(null).commit()
    }

    fun replace(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
    }

}