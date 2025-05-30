package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinbitirmevolkanergunfoodorderingapp.databinding.FragmentSepetBinding
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.adapter.SepetAdapter
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel.SepetViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SepetFragment : BaseFragment() {

    private var _binding: FragmentSepetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SepetViewModel by viewModels()
    private lateinit var sepetAdapter: SepetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSepetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.sepetiYukle()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopLeftBackToHomeIcon(binding.imageViewBackToHomeSepet)

        setupRecyclerView()
        observeViewModel()

        binding.buttonSepetiOnayla.setOnClickListener {
            viewModel.sepetiOnayla()
        }
    }

    private fun setupRecyclerView() {
        sepetAdapter = SepetAdapter { sepetYemekId ->
            viewModel.sepettenUrunSil(sepetYemekId)
        }
        binding.recyclerViewSepet.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sepetAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.sepetYemekler.observe(viewLifecycleOwner) { sepetList ->
             binding.textViewSepetBos.visibility = if (sepetList.isNullOrEmpty()) View.VISIBLE else View.GONE
            sepetAdapter.submitList(sepetList)
        }

        viewModel.toplamSepetTutari.observe(viewLifecycleOwner) { toplamTutar ->
            binding.textViewSepetToplamTutar.text = "₺${String.format("%.2f", toplamTutar)}"
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarSepet.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.onToastMessageShown()
            }
        }
        
        viewModel.siparisOnaylandiEvent.observe(viewLifecycleOwner) { onaylandi ->
            if(onaylandi) {
                viewModel.onSiparisOnaylandiEventConsumed() 
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
