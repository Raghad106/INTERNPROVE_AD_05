package com.example.qrcodescannerapp.listeners

import android.net.Uri
import java.util.Objects

interface OnItemClickListener
{
    fun onSaveIconClickListener(link:String)
    fun onShareLinkClickListener(link:String)
    fun<T> onDeleteIconClickListener(data:T)
    fun onWebsiteOpen(link:String)
}