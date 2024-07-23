package com.example.qrcodescannerapp.myDatabase.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.qrcodescannerapp.data.QRCodeScanClass
import kotlinx.coroutines.flow.Flow

@Dao
interface QRCodeScanDao
{
    @Insert
    suspend fun insertQRCode(code:QRCodeScanClass)

    @Update
    suspend fun updateQRCode(code:QRCodeScanClass)

    @Delete
    suspend fun deleteQRCode(code:QRCodeScanClass)

    @Query("SELECT * FROM QRCodeScanClass ORDER BY id DESC")
    fun getAllQRCodes():Flow<List<QRCodeScanClass>>
}