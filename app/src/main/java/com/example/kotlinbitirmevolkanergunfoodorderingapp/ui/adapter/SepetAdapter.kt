package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinbitirmevolkanergunfoodorderingapp.R
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.SepetYemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.databinding.ItemSepetYemekCardBinding

class SepetAdapter(
    private val onSilClicked: (String) -> Unit 
) : ListAdapter<SepetYemek, SepetAdapter.SepetViewHolder>(SepetYemekDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SepetViewHolder {
        val binding = ItemSepetYemekCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SepetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SepetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SepetViewHolder(private val binding: ItemSepetYemekCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.buttonSepettenSil.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onSilClicked(getItem(adapterPosition).sepet_yemek_id)
                }
            }
        }

        fun bind(sepetYemek: SepetYemek) {
            binding.textViewSepetYemekAdi.text = sepetYemek.yemek_adi
            binding.textViewSepetYemekFiyatBirim.text = "Birim Fiyat: ₺${sepetYemek.yemek_fiyat}"
            binding.textViewSepetYemekAdet.text = "Adet: ${sepetYemek.yemek_siparis_adet}"

            val birimFiyat = sepetYemek.yemek_fiyat.toDoubleOrNull() ?: 0.0
            val adet = sepetYemek.yemek_siparis_adet.toIntOrNull() ?: 0
            val toplamUrunFiyati = birimFiyat * adet
            binding.textViewSepetUrunToplamFiyat.text = "₺${String.format("%.2f", toplamUrunFiyati)}"


            Glide.with(binding.imageViewSepetYemek.context)
                .load(sepetYemek.getFullImageUrl())
                .placeholder(R.drawable.ic_placeholder_food)
                .error(R.drawable.ic_placeholder_food)
                .into(binding.imageViewSepetYemek)
        }
    }
}

class SepetYemekDiffCallback : DiffUtil.ItemCallback<SepetYemek>() {
    override fun areItemsTheSame(oldItem: SepetYemek, newItem: SepetYemek): Boolean {
        return oldItem.sepet_yemek_id == newItem.sepet_yemek_id
    }

    override fun areContentsTheSame(oldItem: SepetYemek, newItem: SepetYemek): Boolean {
        return oldItem == newItem
    }
}
