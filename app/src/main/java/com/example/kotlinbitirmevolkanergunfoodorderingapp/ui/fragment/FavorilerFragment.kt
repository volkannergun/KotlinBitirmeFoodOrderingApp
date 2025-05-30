package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.FavoriYemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.Yemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.databinding.FragmentFavorilerBinding
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.adapter.YemeklerAdapter
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel.FavorilerViewModel
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel.YemekUiModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavorilerFragment : BaseFragment() {

    private var _binding: FragmentFavorilerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavorilerViewModel by viewModels()
    private lateinit var favorilerAdapter: YemeklerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavorilerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopLeftBackToHomeIcon(binding.imageViewBackToHomeFavoriler)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        favorilerAdapter = YemeklerAdapter(
            onItemClicked = { yemek -> 
                 val action = FavorilerFragmentDirections.actionFavorilerFragmentToUrunDetayFragment(yemek)
                 findNavController().navigate(action)
            },
            onFavoriClicked = { yemek, _ -> 
                 val favoriYemek = FavoriYemek(yemek.yemek_id, yemek.yemek_adi, yemek.yemek_resim_adi, yemek.yemek_fiyat)
                 viewModel.favoridenSil(favoriYemek)
            },
            onHizliEkleClicked = { yemek ->
                Snackbar.make(binding.root, "${yemek.yemek_adi} hızlı ekle (Favoriler).", Snackbar.LENGTH_SHORT).show()
            }
        )
        binding.recyclerViewFavoriler.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = favorilerAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.favoriYemekler.observe(viewLifecycleOwner) { favoriList ->
            binding.textViewFavoriYok.visibility = if (favoriList.isNullOrEmpty()) View.VISIBLE else View.GONE
            val uiModelList = favoriList.map { favori ->
                YemekUiModel(
                    yemek = Yemek(favori.yemek_id, favori.yemek_adi, favori.yemek_resim_adi, favori.yemek_fiyat),
                    isFavori = true 
                )
            }
            favorilerAdapter.submitList(uiModelList)
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
