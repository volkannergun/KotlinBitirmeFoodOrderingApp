package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.kotlinbitirmevolkanergunfoodorderingapp.databinding.FragmentHesabimBinding
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel.HesabimViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HesabimFragment : BaseFragment() {

    private var _binding: FragmentHesabimBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HesabimViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHesabimBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopLeftBackToHomeIcon(binding.imageViewBackToHomeHesabim)
        
        binding.textViewKullaniciAdi.text = viewModel.kullaniciAdi
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
