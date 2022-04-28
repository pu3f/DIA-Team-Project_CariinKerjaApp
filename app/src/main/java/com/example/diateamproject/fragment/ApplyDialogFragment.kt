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
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diateamproject.databinding.FragmentApplyDialogBinding
import com.example.diateamproject.util.FileHandler
import com.example.diateamproject.util.PrefsLogin
import com.example.diateamproject.util.PrefsLoginConstant
import com.example.diateamproject.util.URIPathHelper
import com.example.diateamproject.viewmodel.ApplicationStatusViewModel
import com.example.diateamproject.viewmodel.ApplyViewModel
import com.example.diateamproject.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ApplyDialogFragment: DialogFragment() {
    private var _binding: FragmentApplyDialogBinding? = null
    private val binding get() = _binding!!
    private val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    var onApplied: (() -> Unit)? = null
    private val REQUEST_FILE = 2
    private var selectedPdfUri: Uri? = null
    var page = 0
    var size = 0
    private val viewModelApply: ApplyViewModel by lazy {
        ViewModelProviders.of(this).get(ApplyViewModel::class.java)
    }
    private val viewModelApplication: ApplicationStatusViewModel by lazy {
        ViewModelProviders.of(this).get(ApplicationStatusViewModel::class.java)
    }

    private val viewModelProfile: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplyDialogBinding.inflate(inflater, container, false)
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

        binding.btnJustApply.setOnClickListener {
            applyJob()
        }

        binding.tvUpdateCv.setOnClickListener {
            openDirectory()
        }

        binding.btnUpdateCv.setOnClickListener {
            updateResume()
        }
        setObserver()
    }

    private fun setObserver() {
        viewModelApply.responseApply().observe(this, Observer {
            viewModelProfile.responseProfile().observe(this, Observer {
                //resume null condition
                if (it.data.jobseekerResume.isNullOrEmpty()) {
                    Toast.makeText(activity, "Upload CV first", Toast.LENGTH_LONG).show()
                } else {
                    test()
                }
            })
            onApplied?.let {
                it()
            }
            dismiss()
        })

        viewModelProfile.listResponseFile().observe(this, Observer {
            Toast.makeText(activity, "CV Updated", Toast.LENGTH_LONG).show()
        })
    }

    private fun applyJob() {
        val idJob = activity?.intent?.getIntExtra("jobId", 0)
        val jobId = idJob.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val jobseekerId =
            userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        Log.d("testApply", "=====$idJob")
        viewModelApply.postApply(jobId, jobseekerId)
        Log.d("testStatus", "=====apply")
    }

    private fun test() {
        Log.d("testStatus", "=====status")
        viewModelApplication.getApplyJobStatus(userId, page, size)
        context?.let { Toast.makeText(it, "Success Applied", Toast.LENGTH_LONG).show() }
        Log.d("testToast", "=====toastApply")
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
            binding.tvUpdateCv.text = fileName
            binding.tvUpdateCv.setCompoundDrawables(null,null,null,null)
            binding.btnUpdateCv.isEnabled = true
        }
    }

    private fun updateResume() {
        val id = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
            .toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        //get file name using getFileName function
        val fileName = getFileName(selectedPdfUri!!)
        Log.d("pdfuri","$fileName")

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
}
