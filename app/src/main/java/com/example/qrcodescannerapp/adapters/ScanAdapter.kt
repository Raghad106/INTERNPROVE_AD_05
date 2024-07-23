package com.example.qrcodescannerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.qrcodescannerapp.data.QRCodeScanClass
import com.example.qrcodescannerapp.databinding.ItemScanQrCodeSavedBinding
import com.example.qrcodescannerapp.listeners.OnItemClickListener

class ScanAdapter(private var list:List<QRCodeScanClass>,private  val listener:OnItemClickListener?):RecyclerView.Adapter<ScanAdapter.ScanViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder
    {
         return ScanViewHolder(ItemScanQrCodeSavedBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int
    {
        return list.size
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int)
    {
        val pos=position
        val scanItem:QRCodeScanClass=list[pos]
        holder.nameQRCode.setText(scanItem.qrCodeName)
        holder.linkAddressQRCode.setText(scanItem.qrCodeAddress)
        holder.copyQRCodeIcon.setOnClickListener {
            listener?.onSaveIconClickListener(scanItem.qrCodeAddress)
        }
        holder.layout.setOnClickListener {
            listener?.onWebsiteOpen(scanItem.qrCodeAddress)
        }
        holder.cancelQRCodeIcon.setOnClickListener {
            listener?.onDeleteIconClickListener(scanItem)
        }
        holder.shareQRCodeIcon.setOnClickListener {
            listener?.onShareLinkClickListener(scanItem.qrCodeAddress)
        }
    }

    class ScanViewHolder(private val binding:ItemScanQrCodeSavedBinding)
        :ViewHolder(binding.root)
    {
        var nameQRCode:TextView=binding.TVCodeName
        var linkAddressQRCode:TextView=binding.TVCodeAddressLink
        var copyQRCodeIcon:ImageView=binding.IVCopyIcon
        var cancelQRCodeIcon:ImageView=binding.IVCancelIcon
        var shareQRCodeIcon:ImageView=binding.IVShareIcon
        var layout:ConstraintLayout=binding.CLItem
    }
}