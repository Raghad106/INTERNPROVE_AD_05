package com.example.qrcodescannerapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class QRCodeScanClass
    (@PrimaryKey(autoGenerate = true)
     val id:Int,
     val qrCodeAddress:String,
     var qrCodeName:String)
