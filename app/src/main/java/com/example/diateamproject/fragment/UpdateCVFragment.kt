package com.example.diateamproject.fragment

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
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnChooseCv.setOnClickListener {
            openDirectory()
        }

        binding.btnUpdate.setOnClickListener {
            updateCV()
            onUpdate?.let {
                it()
            }
        }
        setObserver()
    }

    private fun setObserver() {
        viewModelProfile.listResponseFile().observe(this, Observer {
            Toast.makeText(activity, "CV Updated", Toast.LENGTH_LONG).show()
            dismiss()
        })
    }

    private fun updateCV() {
        val id = userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        //get file name using getFileName function
        val fileName = getFileName(selectedPdfUri!!)
        PrefsFileName.saveString(PrefsFileNameConstant.FILENAME, fileName!!)

        val fileHandler = FileHandler()
        val files = File(fileHandler.handleUri(requireContext(), selectedPdfUri!!)!!)
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), files)
        val bodyFile: MultipartBody.Part = MultipartBody.Part.createFormData(
            "jobseekerResume", files.name.trim(), requestFile
        )
        viewModelProfile.updateFileProfile(id, bodyFile)
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
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == this.REQUEST_FILE) {
            selectedPdfUri = data?.data
            Log.i("xxfile", "$selectedPdfUri ==try")
            val fileName = getFileName(selectedPdfUri!!)
            binding.btnChooseCv.text =  fileName
//                selectedPdfUri?.path?.substring(selectedPdfUri!!.path!!.lastIndexOf(':')+1)
            binding.btnUpdate.isEnabled = true
        }
    }
}