package com.example.qrcodescannerapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcodescannerapp.databinding.ActivityMainBinding
import com.example.qrcodescannerapp.fragments.ScanQrCodeFragment

import androidx.fragment.app.Fragment
import com.example.qrcodescannerapp.data.QRCodeScanClass
import com.example.qrcodescannerapp.fragments.CreateQrCodeFragment
import com.example.qrcodescannerapp.listeners.OnItemClickListener
import com.example.qrcodescannerapp.myDatabase.QRCodeViewModel
import com.example.qrcodescannerapp.myDatabase.QRCodeViewModelFactory
import java.util.Locale

class MainActivity : AppCompatActivity() ,OnItemClickListener
{
    private lateinit var  binding:ActivityMainBinding
    private val viewModel: QRCodeViewModel by viewModels {
        QRCodeViewModelFactory((this.application as QRCodeApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.FLFragmentContainer, ScanQrCodeFragment()).commit()

        binding.BNBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.scan -> replaceFragment(ScanQrCodeFragment())
                R.id.create -> replaceFragment(CreateQrCodeFragment())

                else -> {
                    replaceFragment(ScanQrCodeFragment())
                }
            }
            true
        }
    }
    private fun replaceFragment (fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().
        replace(R.id .FLFragmentContainer,fragment).commit()
    }

    override fun onSaveIconClickListener(link:String)
    {
        val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Link", link)
        clipboardManager.setPrimaryClip(clip)
    }

    override fun onShareLinkClickListener(link: String)
    {
        val shareLink=reformatLink(link)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareLink)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share link via"))
    }

    override fun <T> onDeleteIconClickListener(data: T)
    {
        if (data is QRCodeScanClass)
            viewModel.deleteScanCode(data)
    }


    override fun onWebsiteOpen(link:String)
    {
        val formattedLink = reformatLink(link)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedLink))
        startActivity(browserIntent)
    }

    private fun reformatLink(url: String): String {
        val lowerCaseUrl = url.lowercase(Locale.ROOT)
        return if (!lowerCaseUrl.startsWith("http://") && !lowerCaseUrl.startsWith("https://")) {
            "http://$lowerCaseUrl"
        } else {
            lowerCaseUrl
        }
    }
}

