package com.example.diateamproject.fragment

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.databinding.FragmentUpdatecvDialogBinding
import com.example.diateamproject.util.*
import com.example.diateamproject.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class UpdateCVFragment : DialogFragment() {
    private var _binding: FragmentUpdatecvDialogBinding? = null
    private val binding get() = _binding!!
    private val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    private val REQUEST_FILE = 2
    private var selectedPdfUri: Uri? = null
    var onUpdate: (() -> Unit)? = null
    lateinit var pb: ProgressButtonUpdatecv
    private val viewModelProfile: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdatecvDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pb = ProgressButtonUpdatecv(requireContext(), view)
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnChooseCv.setOnClickListener {
            openDirectory()
        }

        binding.btnUpdate.cvUpdate.isEnabled = false.apply {
            binding.btnUpdate.cvUpdate.setCardBackgroundColor(Color.GRAY)
        }

        binding.btnUpdate.cvUpdate.setOnClickListener {
            pb.ActiveButton()
            updateCV()
            onUpdate?.let {
                it()
            }
        }
        setObserver()
    }

    private fun setObserver() {
        viewModelProfile.listResponseFile().observe(this, Observer {
            var errorCode = it.errorCode
            Log.d("error02", "code : $errorCode")
            if (errorCode == "02") {
                Toast.makeText(activity, "Invalid file format", Toast.LENGTH_LONG).show()
            } else {
                pb.FinishButton()
            }
//            dismiss()
        })
        viewModelProfile.getIsErrorFile().observe(this, Observer {
            Log.d("error01", "code : $it")
//            when(it)
        })
    }

    private fun updateCV() {
        val id = userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        //get file name using getFileName function
        val fileName = getFileName(selectedPdfUri!!)
        //put fileName to profile fragment
        var length = fileName?.length?.toInt()
        if (fileName?.substring(fileName?.length?.toInt()-4, length!!.toInt())!!.equals(".png") || fileName?.substring(fileName?.length?.toInt()-4, length!!.toInt())!!.equals(".pdf")) {
            val i = Bundle()
            val frag = UpdateCVFragment()
            val fragmentManager: FragmentManager? = fragmentManager
            i.putString("fileName", fileName)
            frag.arguments = i
            fragmentManager!!.beginTransaction()
                .replace(
                    R.id.content, ProfileFragment()
                )
                .commit()

            val fileHandler = FileHandler()
            val files = File(fileHandler.handleUri(requireContext(), selectedPdfUri!!)!!)
            var fl = files.length()/1024
            if (fl > 5000) {
                Toast.makeText(requireContext(), "max file 5 mb", Toast.LENGTH_SHORT).show()
                pb.FinishButton()
            }
            else {
            val requestFile: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), files)
            val bodyFile: MultipartBody.Part = MultipartBody.Part.createFormData(
                "jobseekerResume", files.name.trim(), requestFile
            )
            viewModelProfile.updateFileProfile(id, bodyFile)
        } }
        else {
            Toast.makeText(requireContext(), "formatnotvalid", Toast.LENGTH_SHORT).show()
            pb.FinishButton()
        }

    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            // arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor: Cursor? =
                requireActivity().contentResolver.query(uri, null, null, null, null)
            try {
                var ea = requireActivity().contentResolver.openInputStream(uri)
                Log.d("pdfuri", "$ea ====ea")
                if (cursor != null && cursor.moveToFirst()) {
                    Log.d("pdfuri", "$result ====content")
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            Log.d("pdfuri", "$result ====path")
        }
        return result
    }

    private fun openDirectory() {
        val intent = Intent(Intent(Intent.ACTION_GET_CONTENT))
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == this.REQUEST_FILE) {
            selectedPdfUri = data?.data
            Log.i("xxfile", "$selectedPdfUri ==try")
            val fileName = getFileName(selectedPdfUri!!)

            binding.btnChooseCv.text = fileName
            binding.btnUpdate.cvUpdate.isEnabled = true.apply {
                binding.btnUpdate.cvUpdate.setCardBackgroundColor(resources.getColor(R.color.holo_blue_dark))
            }
        }
    }
}
