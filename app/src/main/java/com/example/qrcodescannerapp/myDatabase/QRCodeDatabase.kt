package com.example.qrcodescannerapp.myDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.qrcodescannerapp.data.QRCodeScanClass
import com.example.qrcodescannerapp.myDatabase.daos.QRCodeScanDao
import kotlinx.coroutines.CoroutineScope

@Database(entities = [QRCodeScanClass::class],version = 1, exportSchema = false)
abstract class QRCodeDatabase:RoomDatabase()
{
    abstract fun scanQRCodeDAO():QRCodeScanDao

    companion object
    {
        @Volatile
        private var instant:QRCodeDatabase?=null

        fun getInstance(context: Context, scope: CoroutineScope):QRCodeDatabase
        {
            return instant ?: synchronized(Any())
            {
                instant?:buildDatabase(context)
                    .also{ instant=it}
            }

        }

        private fun buildDatabase(context: Context):QRCodeDatabase
        {
            return databaseBuilder(context,QRCodeDatabase::class.java,"code_database").build()
        }


    }
}