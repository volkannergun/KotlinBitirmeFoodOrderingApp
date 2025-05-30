package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinbitirmevolkanergunfoodorderingapp.R
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.Yemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.databinding.ItemYemekCardBinding
import com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel.YemekUiModel

class YemeklerAdapter(
    private val onItemClicked: (Yemek) -> Unit,
    private val onFavoriClicked: (Yemek, Boolean) -> Unit,
    private val onHizliEkleClicked: (Yemek) -> Unit
) : ListAdapter<YemekUiModel, YemeklerAdapter.YemekViewHolder>(YemekDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekViewHolder {
        val binding = ItemYemekCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YemekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YemekViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class YemekViewHolder(private val binding: ItemYemekCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked(getItem(adapterPosition).yemek)
                }
            }
            binding.buttonFavori.setOnClickListener {
                 if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(adapterPosition)
                    onFavoriClicked(item.yemek, item.isFavori)
                }
            }
            binding.buttonHizliEkle.setOnClickListener {
                 if (adapterPosition != RecyclerView.NO_POSITION) {
                    onHizliEkleClicked(getItem(adapterPosition).yemek)
                    binding.buttonHizliEkle.playAnimation()
                }
            }
        }

        fun bind(yemekUiModel: YemekUiModel) {
            val yemek = yemekUiModel.yemek
            binding.textViewYemekAdi.text = yemek.yemek_adi
            binding.textViewYemekFiyat.text = "₺${yemek.yemek_fiyat}"

            Glide.with(binding.imageViewYemek.context)
                .load(yemek.getFullImageUrl())
                .placeholder(R.drawable.ic_placeholder_food) 
                .error(R.drawable.ic_placeholder_food) 
                .into(binding.imageViewYemek)

            val favoriIconRes = if (yemekUiModel.isFavori) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline2
            binding.buttonFavori.setImageResource(favoriIconRes)
        }
    }
}

class YemekDiffCallback : DiffUtil.ItemCallback<YemekUiModel>() {
    override fun areItemsTheSame(oldItem: YemekUiModel, newItem: YemekUiModel): Boolean {
        return oldItem.yemek.yemek_id == newItem.yemek.yemek_id
    }

    override fun areContentsTheSame(oldItem: YemekUiModel, newItem: YemekUiModel): Boolean {
        return oldItem == newItem
    }
}
