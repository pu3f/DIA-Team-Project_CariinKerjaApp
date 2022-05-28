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
import com.example.diateamproject.R
import com.example.diateamproject.databinding.FragmentApplyDialogBinding
import com.example.diateamproject.util.*
import com.example.diateamproject.viewmodel.ApplicationStatusViewModel
import com.example.diateamproject.viewmodel.ApplyViewModel
import com.example.diateamproject.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ApplyDialogFragment : DialogFragment() {
    private var _binding: FragmentApplyDialogBinding? = null
    private val binding get() = _binding!!
    private val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    var onApplied: (() -> Unit)? = null
    private val REQUEST_FILE = 2
    private var selectedPdfUri: Uri? = null
    var page = 0
    var size = 0
    var tempCv = ""
    lateinit var pb: ProgressButtonUploadNewCV
    lateinit var pbJustApply: ProgressButtonJustApply
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
        pb = ProgressButtonUploadNewCV(requireContext(), view)
        pbJustApply = ProgressButtonJustApply(requireContext(), view)
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        viewModelProfile.getProfile(userId)
        binding.btnJustApply.cvJustApply.setOnClickListener {
            pbJustApply.ActiveButton()
            //resume null condition
            if (tempCv.isEmpty()) {
                Log.d("resume test", "is empty $tempCv")
                Toast.makeText(activity, "Upload your newest CV first", Toast.LENGTH_SHORT).show()
                pbJustApply.FinishButton()

            } else {
                applyJob()
                onApplied?.let {
                    it()
                }
            }
        }

        binding.tvUpdateCv.setOnClickListener {
            openDirectory()
        }

        binding.btnUpdateCv.cvUpload.isEnabled = false.apply {
            binding.btnUpdateCv.cvUpload.setCardBackgroundColor(Color.GRAY)
        }
        binding.btnUpdateCv.cvUpload.setOnClickListener {
            pb.ActiveButton()
            updateCV()
        }
        setObserver()
    }

    private fun setObserver() {
        viewModelApply.responseApply().observe(this, Observer {
            pbJustApply.FinishButton()
            getApplicationStatus()
            dismiss()
            onApplied?.let {
                it()
            }
        })

        viewModelProfile.responseProfile().observe(this, Observer {
            tempCv = it.data.jobseekerResume
        })

        viewModelProfile.listResponseFile().observe(this, Observer {
            pb.FinishButton()
            Toast.makeText(activity, "CV Updated", Toast.LENGTH_SHORT).show()
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

    private fun getApplicationStatus() {
        Log.d("testStatus", "=====status")
        viewModelApplication.getApplyJobStatus(userId, page, size)
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
            binding.tvUpdateCv.text = fileName
            binding.tvUpdateCv.setCompoundDrawables(null, null, null, null)
            binding.btnUpdateCv.cvUpload.isEnabled = true.apply {
                binding.btnUpdateCv.cvUpload.setBackgroundResource(R.drawable.bg_border_mediumblue)
                binding.btnUpdateCv.tvUpload.setTextColor(resources.getColor(R.color.medium_blue))
            }
        }
    }

    private fun updateCV() {
        //get file name using getFileName function
        val fileName = getFileName(selectedPdfUri!!)
        Log.d("pdfuri", "$fileName")
        //handle file format
        val length = fileName?.length
        if (fileName?.substring(fileName.length - 4, length!!.toInt()).equals(".png") ||
            fileName?.substring(fileName.length - 4, length!!.toInt()).equals(".pdf")
        ) {
            val fileHandler = FileHandler()
            val files = File(fileHandler.handleUri(requireContext(), selectedPdfUri!!)!!)
            //handle file size
            val fl = files.length() / 1024
            if (fl > 5000) {
                Toast.makeText(requireContext(), "max file 5 mb", Toast.LENGTH_SHORT).show()
                pb.FinishButton()
            } else {
                val id = userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val requestFile: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), files)
                val bodyFile: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "jobseekerResume", files.name.trim(), requestFile
                )
                viewModelProfile.updateFileProfile(id, bodyFile)
            }
        } else {
            Toast.makeText(requireContext(), "Invalid file format", Toast.LENGTH_SHORT).show()
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
}
