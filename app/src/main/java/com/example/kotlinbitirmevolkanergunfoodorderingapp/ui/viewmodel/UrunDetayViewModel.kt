package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.entity.Yemek
import com.example.kotlinbitirmevolkanergunfoodorderingapp.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UrunDetayViewModel @Inject constructor(
    private val repository: YemeklerRepository
) : ViewModel() {

    private val _yemekDetay = MutableLiveData<Yemek?>()
    val yemekDetay: LiveData<Yemek?> = _yemekDetay

    private val _adet = MutableLiveData(1)
    val adet: LiveData<Int> = _adet

    private val _toplamFiyat = MutableLiveData<Double>()
    val toplamFiyat: LiveData<Double> = _toplamFiyat

    private val _isFavori = MutableLiveData<Boolean>()
    val isFavori: LiveData<Boolean> = _isFavori

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun urunDetayiniYukle(yemek: Yemek) {
        _yemekDetay.value = yemek
        _adet.value = 1 
        hesaplaToplamFiyat(yemek.yemek_fiyat.toDoubleOrNull() ?: 0.0, 1)
        favoriDurumunuKontrolEt(yemek.yemek_id)
    }
    
    private fun favoriDurumunuKontrolEt(yemekId: String) {
        viewModelScope.launch {
            repository.isFavori(yemekId).collect { favoriMi ->
                _isFavori.value = favoriMi
            }
        }
    }

    fun adetArtir() {
        val mevcutAdet = _adet.value ?: 1
        _adet.value = mevcutAdet + 1
        _yemekDetay.value?.let {
            hesaplaToplamFiyat(it.yemek_fiyat.toDoubleOrNull() ?: 0.0, mevcutAdet + 1)
        }
    }

    fun adetAzalt() {
        val mevcutAdet = _adet.value ?: 1
        if (mevcutAdet > 1) {
            _adet.value = mevcutAdet - 1
            _yemekDetay.value?.let {
                hesaplaToplamFiyat(it.yemek_fiyat.toDoubleOrNull() ?: 0.0, mevcutAdet - 1)
            }
        }
    }

    private fun hesaplaToplamFiyat(birimFiyat: Double, adet: Int) {
        _toplamFiyat.value = birimFiyat * adet
    }

    fun sepeteEkle() {
        viewModelScope.launch {
            val yemek = _yemekDetay.value
            val siparisAdet = _adet.value
            if (yemek != null && siparisAdet != null && siparisAdet > 0) {
                val success = repository.sepeteYemekEkle(yemek, siparisAdet)
                if (success) {
                    _toastMessage.value = "${yemek.yemek_adi} (${siparisAdet} adet) sepete eklendi!"
                } else {
                    _toastMessage.value = "${yemek.yemek_adi} sepete eklenemedi."
                }
            } else {
                 _toastMessage.value = "Lütfen bir ürün seçin ve adet belirtin."
            }
        }
    }

    fun favoriDurumunuDegistir() {
        _yemekDetay.value?.let { yemek ->
            val suankiFavoriDurumu = _isFavori.value ?: false
            viewModelScope.launch {
                try {
                    if (suankiFavoriDurumu) {
                        repository.favoridenSil(yemek.yemek_id)
                        _isFavori.value = false
                        _toastMessage.value = "${yemek.yemek_adi} favorilerden çıkarıldı."
                    } else {
                        repository.favoriyeEkle(yemek)
                        _isFavori.value = true
                        _toastMessage.value = "${yemek.yemek_adi} favorilere eklendi."
                    }
                } catch (e: Exception) {
                     _toastMessage.value = "Favori işlemi sırasında hata: ${e.message}"
                }
            }
        }
    }
    
    fun onToastMessageShown() {
        _toastMessage.value = null
    }
}
