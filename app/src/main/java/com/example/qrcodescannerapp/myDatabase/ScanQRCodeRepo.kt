package com.example.qrcodescannerapp.myDatabase

import com.example.qrcodescannerapp.data.QRCodeScanClass
import com.example.qrcodescannerapp.myDatabase.daos.QRCodeScanDao
import kotlinx.coroutines.flow.Flow

class ScanQRCodeRepo(private val scanCodeDao:QRCodeScanDao)
{
    suspend fun insertScanCode(sc:QRCodeScanClass)
    {
        scanCodeDao.insertQRCode(sc)
    }

    suspend fun updateScanCode(sc:QRCodeScanClass)
    {
        scanCodeDao.updateQRCode(sc)
    }

    suspend fun deleteScanCode(sc:QRCodeScanClass)
    {
        scanCodeDao.deleteQRCode(sc)
    }

    fun getAllScanCodes(): Flow<List<QRCodeScanClass>>
    {
        return scanCodeDao.getAllQRCodes()
    }

}