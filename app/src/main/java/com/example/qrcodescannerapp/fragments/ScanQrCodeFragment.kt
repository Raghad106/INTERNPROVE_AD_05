package com.example.qrcodescannerapp.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrcodescannerapp.QRCodeApplication
import com.example.qrcodescannerapp.R
import com.example.qrcodescannerapp.adapters.ScanAdapter
import com.example.qrcodescannerapp.data.QRCodeScanClass
import com.example.qrcodescannerapp.databinding.FragmentScanQrCodeBinding
import com.example.qrcodescannerapp.listeners.OnItemClickListener
import com.example.qrcodescannerapp.myDatabase.QRCodeViewModel
import com.example.qrcodescannerapp.myDatabase.QRCodeViewModelFactory
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanQrCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanQrCodeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var listener:OnItemClickListener
    private val viewModel: QRCodeViewModel by viewModels {
        QRCodeViewModelFactory((requireActivity().application as QRCodeApplication).repository)
    }

    private val CAMERA_REQUEST_CODE = 100

    private var _binding:FragmentScanQrCodeBinding?=null
    private val binding get() = requireNotNull(_binding)
    private lateinit var barcodeScannerView: DecoratedBarcodeView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentScanQrCodeBinding.inflate(inflater,container,false)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
        viewModel.getAllScanCodes().observe(viewLifecycleOwner){codes->
            val adapter=ScanAdapter(codes,listener)
            binding.RVHistoryItem.layoutManager=LinearLayoutManager(requireContext())
            binding.RVHistoryItem.adapter=adapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        barcodeScannerView = binding.barcodeScanner

        // Set up barcode scanner
        binding.BTScanQRCode.setOnClickListener {
            barcodeScannerView.decodeContinuous(object : BarcodeCallback
            {
                override fun barcodeResult(result: BarcodeResult?)
                {
                    result?.let {
                        lifecycleScope.launch {
                            val processedResult = processScannedResult(it.text)
                            listener.onWebsiteOpen(processedResult)
                            binding.TVQRCodeAddress.setText(processedResult)
                            barcodeScannerView.pause()
                            listener.onWebsiteOpen(processedResult)
                            barcodeScannerView.resume()
                            binding.ETNameQRCode.visibility=View.VISIBLE
                        }
                    }
                }
            })
        }

        binding.BTSaveQRCode.setOnClickListener {
            viewModel.insertScanCode(QRCodeScanClass(0,binding.TVQRCodeAddress.text.toString(),binding.ETNameQRCode.text.toString()))
            binding.ETNameQRCode.visibility=View.GONE
        }


    }

    companion object
    {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanQrCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun startQRScanner() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan a QR code")
            setCameraId(0)  // Use a specific camera of the device
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
        }
    }

    private suspend fun processScannedResult(result: String): String
    {
        return withContext(Dispatchers.IO) {
            result.uppercase()
        }
    }

    override fun onResume()
    {
        super.onResume()
        barcodeScannerView.resume()
    }

    override fun onPause()
    {
        super.onPause()
        barcodeScannerView.pause()
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }


}