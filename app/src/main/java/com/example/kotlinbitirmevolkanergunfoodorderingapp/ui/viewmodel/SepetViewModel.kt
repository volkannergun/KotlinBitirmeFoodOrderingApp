package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.SepetYemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SepetViewModel @Inject constructor(
    private val repository: YemeklerRepository
) : ViewModel() {

    private val _sepetYemekler = MutableLiveData<List<SepetYemek>>()
    val sepetYemekler: LiveData<List<SepetYemek>> = _sepetYemekler

    private val _toplamSepetTutari = MutableLiveData(0.0)
    val toplamSepetTutari: LiveData<Double> = _toplamSepetTutari

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage
    
    private val _siparisOnaylandiEvent = MutableLiveData<Boolean>()
    val siparisOnaylandiEvent: LiveData<Boolean> = _siparisOnaylandiEvent


    init {
        sepetiYukle()
    }

    fun sepetiYukle() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val yemekler = repository.sepettekiYemekleriGetir()
                _sepetYemekler.value = yemekler
                hesaplaToplamTutar(yemekler)
            } catch (e: Exception) {
                _sepetYemekler.value = emptyList()
                hesaplaToplamTutar(emptyList())
                 _toastMessage.value = "Sepet yüklenirken hata: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun hesaplaToplamTutar(yemekler: List<SepetYemek>) {
        var toplam = 0.0
        yemekler.forEach { sepetYemek ->
            val fiyat = sepetYemek.yemek_fiyat.toDoubleOrNull() ?: 0.0
            val adet = sepetYemek.yemek_siparis_adet.toIntOrNull() ?: 0
            toplam += fiyat * adet
        }
        _toplamSepetTutari.value = toplam
    }

    fun sepettenUrunSil(sepetYemekId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.sepettenYemekSil(sepetYemekId)
                if (success) {
                    _toastMessage.value = "Ürün sepetten silindi."
                    sepetiYukle() 
                } else {
                    _toastMessage.value = "Ürün sepetten silinemedi."
                }
            } catch (e: Exception) {
                 _toastMessage.value = "Silme işlemi sırasında hata: ${e.message}"
            } finally {
                 _isLoading.value = false
            }
        }
    }

    fun sepetiOnayla() {
        viewModelScope.launch {
            val mevcutSepet = _sepetYemekler.value
            if (mevcutSepet.isNullOrEmpty()) {
                _toastMessage.value = "Sepetiniz boş."
                return@launch
            }

            _isLoading.value = true
            var hepsiSilindi = true
            try {
                for (item in mevcutSepet) {
                    val success = repository.sepettenYemekSil(item.sepet_yemek_id)
                    if (!success) {
                        hepsiSilindi = false
                    }
                }
                if (hepsiSilindi) {
                    _toastMessage.value = "Siparişiniz onaylandı! Sepetiniz temizlendi."
                    _siparisOnaylandiEvent.value = true
                    sepetiYukle() 
                } else {
                    _toastMessage.value = "Bazı ürünler sepetten silinemedi. Lütfen tekrar deneyin."
                     sepetiYukle() 
                }
            } catch (e: Exception) {
                _toastMessage.value = "Sipariş onayı sırasında hata: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun onToastMessageShown() {
        _toastMessage.value = null
    }

    fun onSiparisOnaylandiEventConsumed() {
        _siparisOnaylandiEvent.value = false 
    }
}
