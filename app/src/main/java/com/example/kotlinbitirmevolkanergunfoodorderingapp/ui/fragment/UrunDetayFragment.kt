package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kotlinbitirmevolkanergunfoodorderingapp.R
import com.example.kotlinbitirmevolkanergunfoodorderingapp.databinding.FragmentUrunDetayBinding
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel.UrunDetayViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UrunDetayFragment : BaseFragment() {

    private var _binding: FragmentUrunDetayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UrunDetayViewModel by viewModels()
    private val args: UrunDetayFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUrunDetayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopLeftBackToHomeIcon(binding.imageViewBackToHome) 

        viewModel.urunDetayiniYukle(args.secilenYemek)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.buttonAdetAzalt.setOnClickListener { viewModel.adetAzalt() }
        binding.buttonAdetArtir.setOnClickListener { viewModel.adetArtir() }
        binding.buttonSepeteEkleLottie.setOnClickListener {
            viewModel.sepeteEkle()
            binding.buttonSepeteEkleLottie.playAnimation() 
        }
        binding.imageViewUrunFavori.setOnClickListener {
            viewModel.favoriDurumunuDegistir()
        }
    }

    private fun observeViewModel() {
        viewModel.yemekDetay.observe(viewLifecycleOwner) { yemek ->
            yemek?.let {
                binding.textViewUrunAdi.text = it.yemek_adi
                binding.textViewUrunFiyat.text = "₺${it.yemek_fiyat}"
                Glide.with(this)
                    .load(it.getFullImageUrl())
                    .placeholder(R.drawable.ic_placeholder_food)
                    .into(binding.imageViewUrun)
            }
        }

        viewModel.adet.observe(viewLifecycleOwner) { adet ->
            binding.textViewAdet.text = adet.toString()
        }

        viewModel.toplamFiyat.observe(viewLifecycleOwner) { toplamFiyat ->
            binding.textViewToplamUrunFiyati.text = "Toplam: ₺${String.format("%.2f", toplamFiyat)}"
        }
        
        viewModel.isFavori.observe(viewLifecycleOwner) { isFavori ->
            val favoriIconRes = if (isFavori) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline2
            binding.imageViewUrunFavori.setImageResource(favoriIconRes)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.onToastMessageShown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
