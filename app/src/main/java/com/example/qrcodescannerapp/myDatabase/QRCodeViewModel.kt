package com.example.qrcodescannerapp.myDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.qrcodescannerapp.data.QRCodeScanClass
import kotlinx.coroutines.launch

class QRCodeViewModel(var repo:ScanQRCodeRepo):ViewModel()
{
    fun insertScanCode(sc:QRCodeScanClass)= viewModelScope.launch {
        repo.insertScanCode(sc)
    }

    fun updateScanCode(sc:QRCodeScanClass)= viewModelScope.launch {
        repo.updateScanCode(sc)
    }

    fun deleteScanCode(sc:QRCodeScanClass)= viewModelScope.launch {
        repo.deleteScanCode(sc)
    }

    fun getAllScanCodes():LiveData<List<QRCodeScanClass>>
    {
        return repo.getAllScanCodes().asLiveData()
    }

}
class QRCodeViewModelFactory(private val repository: ScanQRCodeRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QRCodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QRCodeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}