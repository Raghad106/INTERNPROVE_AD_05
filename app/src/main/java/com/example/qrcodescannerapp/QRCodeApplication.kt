package com.example.qrcodescannerapp

import android.app.Application
import com.example.qrcodescannerapp.myDatabase.QRCodeDatabase
import com.example.qrcodescannerapp.myDatabase.ScanQRCodeRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class QRCodeApplication : Application()
{
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy {QRCodeDatabase.getInstance(this ,applicationScope) }
    val repository by lazy {ScanQRCodeRepo(database.scanQRCodeDAO()) }
}