package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.fragment
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.kotlinbitirmevolkanergunfoodorderingapp.R

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToHome()
            }
        })
    }

    protected fun navigateToHome() {
        findNavController().navigate(
            R.id.anasayfaFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.main_nav_graph, true) 
                .build()
        )
    }
    
    protected fun setupTopLeftBackToHomeIcon(backButton: View) {
        backButton.setOnClickListener {
            navigateToHome()
        }
    }
}
