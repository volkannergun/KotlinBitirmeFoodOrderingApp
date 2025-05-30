package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlinbitirmevolkanergunfoodorderingapp.databinding.FragmentAnasayfaBinding
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.adapter.YemeklerAdapter
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel.AnasayfaViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnasayfaFragment : BaseFragment() {

    private var _binding: FragmentAnasayfaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnasayfaViewModel by viewModels()
    private lateinit var yemeklerAdapter: YemeklerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnasayfaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) 

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        yemeklerAdapter = YemeklerAdapter(
            onItemClicked = { yemek ->
                val action = AnasayfaFragmentDirections.actionAnasayfaFragmentToUrunDetayFragment(yemek)
                findNavController().navigate(action)
            },
            onFavoriClicked = { yemek, isSuankiFavori ->
                viewModel.favoriDurumunuDegistir(yemek, isSuankiFavori)
            },
            onHizliEkleClicked = { yemek ->
                viewModel.hizliSepeteEkle(yemek)
            }
        )
        binding.recyclerViewYemekler.apply {
            layoutManager = GridLayoutManager(context, 3) 
            adapter = yemeklerAdapter
        }
    }
    
    private fun setupSearchView() {
        binding.searchViewYemek.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.ara(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.ara(newText)
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewModel.yemekler.observe(viewLifecycleOwner) { yemekList ->
            yemeklerAdapter.submitList(yemekList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarAnasayfa.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                viewModel.onToastMessageShown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
