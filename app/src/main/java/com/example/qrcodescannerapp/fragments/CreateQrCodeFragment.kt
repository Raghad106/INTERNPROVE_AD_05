package com.example.qrcodescannerapp.fragments

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.qrcodescannerapp.databinding.FragmentCreateQrCodeBinding
import com.example.qrcodescannerapp.listeners.OnItemClickListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateQrCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateQrCodeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCreateQrCodeBinding?=null
    private val binding get() = requireNotNull(_binding)
    private var qrCodeBitmap: Bitmap? = null
    private lateinit var listener: OnItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (context is OnItemClickListener)
        {
            listener = context
        }
        else
        {
            throw RuntimeException("$context must implement OnOperationSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCreateQrCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.BTGenerateQRCode.setOnClickListener {
            val link = binding.ETEnterYourLink.text.toString()
            if (link.isNotEmpty())
            {
                generateQRCode(link)
            }
        }

        binding.BTSaveToMyPhone.setOnClickListener {
            if (qrCodeBitmap != null)
            {
                saveImageToGallery(qrCodeBitmap!!)
            }
            else
            {
                Toast.makeText(requireContext(), "No QR code to save", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateQrCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun generateQRCode(link: String): Bitmap?
    {
        val multiFormatWriter = MultiFormatWriter()
        try
        {
            val bitMatrix: BitMatrix = multiFormatWriter.encode(link, BarcodeFormat.QR_CODE, 300, 300)
            val barcodeEncoder = BarcodeEncoder()
            qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix)
            binding.IVQRGenerated.setImageBitmap(qrCodeBitmap)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return qrCodeBitmap
    }

    private fun saveImageToGallery(bitmap: Bitmap)
    {
        val fileName = "QRCode_${System.currentTimeMillis()}.png"
        var fos: OutputStream? = null
        var imageUri: Uri? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        }
        else
        {
            // Create a directory for the QR code images
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
            val image = File(imagesDir, fileName)
            imageUri = Uri.fromFile(image)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Toast.makeText(requireContext(), "QR Code saved to gallery", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}