package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.kotlinbitirmevolkanergunfoodorderingapp.R
import com.example.kotlinbitirmevolkanergunfoodorderingapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.anasayfaFragment,
                R.id.favorilerFragment,
                R.id.sepetFragment,
                R.id.hesabimFragment -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                }
                else -> { 
                    binding.bottomNavView.visibility = View.GONE
                }
            }
        }
    }
}
