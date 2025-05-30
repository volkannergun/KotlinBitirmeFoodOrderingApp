package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.FavoriYemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavorilerViewModel @Inject constructor(
    private val repository: YemeklerRepository
) : ViewModel() {

    val favoriYemekler: LiveData<List<FavoriYemek>> = repository.tumFavorileriGetir().asLiveData()
    
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun favoridenSil(favoriYemek: FavoriYemek) {
        viewModelScope.launch {
            try {
                repository.favoridenSil(favoriYemek)
                 _toastMessage.value = "${favoriYemek.yemek_adi} favorilerden çıkarıldı."
            } catch (e: Exception) {
                 _toastMessage.value = "Favoriden silme işlemi sırasında hata: ${e.message}"
            }
        }
    }
    
    fun onToastMessageShown() {
        _toastMessage.value = null
    }
}
