package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.Yemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class YemekUiModel(
    val yemek: Yemek,
    val isFavori: Boolean
)

@HiltViewModel
class AnasayfaViewModel @Inject constructor(
    private val repository: YemeklerRepository
) : ViewModel() {

    private val _yemekler = MutableLiveData<List<YemekUiModel>>()
    val yemekler: LiveData<List<YemekUiModel>> = _yemekler

    private val _originalYemekler = MutableLiveData<List<YemekUiModel>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    init {
        yemekleriYukle()
    }

    fun yemekleriYukle() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apiYemekler = repository.tumYemekleriGetir()
                val favorilerFlow = repository.tumFavorileriGetir()
                
                favorilerFlow.collect { favoriList ->
                    val uiModelList = apiYemekler.map { yemek ->
                        YemekUiModel(
                            yemek = yemek,
                            isFavori = favoriList.any { it.yemek_id == yemek.yemek_id }
                        )
                    }
                    _originalYemekler.value = uiModelList
                    _yemekler.value = uiModelList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _toastMessage.value = "Yemekler yüklenirken hata oluştu: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun favoriDurumunuDegistir(yemek: Yemek, isSuankiFavori: Boolean) {
        viewModelScope.launch {
            try {
                if (isSuankiFavori) {
                    repository.favoridenSil(yemek.yemek_id)
                    _toastMessage.value = "${yemek.yemek_adi} favorilerden çıkarıldı."
                } else {
                    repository.favoriyeEkle(yemek)
                    _toastMessage.value = "${yemek.yemek_adi} favorilere eklendi."
                }
                 val currentList = _yemekler.value?.map { uiModel ->
                    if (uiModel.yemek.yemek_id == yemek.yemek_id) {
                        uiModel.copy(isFavori = !isSuankiFavori)
                    } else {
                        uiModel
                    }
                }
                _yemekler.value = currentList
                _originalYemekler.value = currentList 
            } catch (e: Exception) {
                 _toastMessage.value = "Favori işlemi sırasında hata: ${e.message}"
            }
        }
    }
    
    fun hizliSepeteEkle(yemek: Yemek) {
        viewModelScope.launch {
            val success = repository.sepeteYemekEkle(yemek, 1) 
            if (success) {
                _toastMessage.value = "${yemek.yemek_adi} sepete eklendi!"
            } else {
                _toastMessage.value = "${yemek.yemek_adi} sepete eklenemedi."
            }
        }
    }

    fun ara(query: String?) {
        if (query.isNullOrBlank()) {
            _yemekler.value = _originalYemekler.value
        } else {
            _yemekler.value = _originalYemekler.value?.filter {
                it.yemek.yemek_adi.contains(query, ignoreCase = true)
            }
        }
    }
    
    fun onToastMessageShown() {
        _toastMessage.value = null
    }
}
