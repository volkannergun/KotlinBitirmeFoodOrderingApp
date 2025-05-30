package com.example.kotlinbitirmevolkanergunfoodorderingapp.ui.viewmodel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HesabimViewModel @Inject constructor() : ViewModel() {
    val kullaniciAdi = "Volkan Ergün" 
}
