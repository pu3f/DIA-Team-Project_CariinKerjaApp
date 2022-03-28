package com.example.diateamproject.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.diateamproject.R
import com.example.diateamproject.activity.LoginActivity
import com.example.diateamproject.activity.MenuActivity
import com.example.diateamproject.databinding.FragmentProfileBinding
import com.example.diateamproject.util.*
import com.example.diateamproject.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class ProfileFragment : Fragment() {

    //view binding fragment declaration
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_IMAGE = 1
    private val REQUEST_FILE = 2
    val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private var selectedImageUri: Uri? = null
    private var selectedPdfUri: Uri? = null
    private val viewModelProfile: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        // call function requestPermission from menu activity
        (activity as MenuActivity).requestPermission()

        binding.ivProfile.setOnClickListener {
            openGallery()
        }

        binding.tfCV.setOnClickListener {
            updateResume()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
        viewModelProfile.getProfile(userId)

        setObserver()

        binding.btnSaveProfile.setOnClickListener {
            updateProfile()

//            viewModelProfile.getProfile(userId)

        }

        binding.apply {
            binding.tfBirth.setOnClickListener {
                datePicker()
            }
        }

        binding.ivBack.setOnClickListener {
            val intentBack = Intent(activity, MenuActivity::class.java)
            startActivity(intentBack)
        }
        actionLogout()

    }

    private fun setObserver() {
        viewModelProfile.responseProfile().observe(viewLifecycleOwner, Observer {
            binding.tfBio.setText(it.jobseekerAbout)
            binding.tfFullName.setText(it.jobseekerName)
            binding.tfEmail.setText(it.jobseekerEmail)
            binding.tfPhone.setText(it.jobseekerPhone)
            binding.tfAddress.setText(it.jobseekerAddress)
            binding.tfDegree.setText(it.jobseekerEducation)

            val dateProfile = it.jobseekerDateOfBirth
            if(dateProfile > 0) {
                val dateString = formatDate.format(dateProfile)
                binding.tfBirth.setText(String.format("%s", dateString))
            }

            //get image from profile response
            if (it.jobseekerImage != null) {
                Log.i("xxload", "xxload ${it.jobseekerImage}")
                Glide.with(requireContext())
                    .load(Path.IMAGE_URL + it.jobseekerImage)
                    .centerCrop()
                    .into(binding.ivProfile)
                binding.tvPickImage.isGone = true
            } else {
                Log.i("xxpick", "xxpick")
                binding.tvPickImage.isGone = false
            }

            val resumeFile = it.jobseekerResume
            binding.tfCV.setText(resumeFile)
        })

        viewModelProfile.listResponseProfile().observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "Profile Updated", Toast.LENGTH_SHORT).show()
        })

        viewModelProfile.listResponseImage().observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "Image Updated", Toast.LENGTH_SHORT).show()
        })

        viewModelProfile.listResponseFile().observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "Resume Updated", Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateProfile() {
        //parse input text to request body
        val id = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
            .toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val updateBio = binding.tfBio.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val updateName = binding.tfFullName.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val updateEmail = binding.tfEmail.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val updatePhone = binding.tfPhone.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val updateAddress = binding.tfAddress.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val updateDateOfBirth = binding.tfBirth.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val updateEducation = binding.tfDegree.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModelProfile.updateProfile(
            id,
            updateBio,
            updateName,
            updateEmail,
            updatePhone,
            updateDateOfBirth,
            updateAddress,
            updateEducation,
        )
    }

    private fun updateImageProfile() {
        if (selectedImageUri == null) {
            Toast.makeText(activity, "Select an Image", Toast.LENGTH_SHORT).show()
        }
        val id = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
            .toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        //get image path using URI Path Helper function
        val uriPathHelper = URIPathHelper()
        var pathImage = ""
        selectedImageUri?.let {  pathImage =
            uriPathHelper.getPath(requireContext(), it).toString()
        }

        var file: File = File(pathImage)
        val requestImage: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val bodyImage: MultipartBody.Part = MultipartBody.Part.createFormData(
            "jobseekerImage", file.name.trim(), requestImage
        )

        viewModelProfile.updateImageProfile(id, bodyImage)

    }

    private fun updateFileProfile() {
        val id = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
            .toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        //get file path using URI Path Helper function
        Log.d("pdfuri","$selectedPdfUri")
        val uriPathHelper = URIPathHelper()
        var pathFile = ""

        //get file name using getFileName function
        var tes = getFileName(selectedPdfUri!!)
        binding.tfCV.setText(tes)

        Log.d("pdfuri","$tes")
        selectedPdfUri!!.let {  pathFile =
            uriPathHelper.getPath(requireContext(), it).toString()
        }

        var files: File = File(pathFile)
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), files)
        val bodyFile: MultipartBody.Part = MultipartBody.Part.createFormData(
            "jobseekerResume", files.name.trim(), requestFile
        )
        viewModelProfile.updateFileProfile(id, bodyFile)

    }

    //clean resource to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun datePicker() {
        val datePickerFragment = DatePickerFragment()
        val supportFragmentManager = requireActivity().supportFragmentManager

        // we have to implement setFragmentResultListener
        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            viewLifecycleOwner
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("SELECTED_DATE")
                Log.d("testDate","$date ====new date")
                binding.tfBirth.setText(date)
            }
        }

        // show
        datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
    }

    private fun updateResume() {
        val resumeDialogView = LayoutInflater.from(activity).inflate(R.layout.updatecv_dialog, null)

        val resumeBuilder = AlertDialog.Builder(activity)
            .setView(resumeDialogView)

        val btnClose = resumeDialogView.findViewById<ImageView>(R.id.ivClose)
        val btnChoose = resumeDialogView.findViewById<Button>(R.id.btnChooseCv)
        val btnUpdate = resumeDialogView.findViewById<Button>(R.id.btnUpdate)
        val resumeAlertDialog = resumeBuilder.show()

        btnClose.setOnClickListener {
            resumeAlertDialog.dismiss()
        }
        btnChoose.setOnClickListener {
            openDirectory()
        }
        btnUpdate.setOnClickListener {
            updateFileProfile()
            resumeAlertDialog.dismiss()
        }

    }

    //function open gallery
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun openDirectory() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == this.REQUEST_IMAGE) {
            selectedImageUri = data?.data
            Log.i("xximage", "xximage $selectedImageUri")

            //put selectedImage to ivProfile
            binding.ivProfile.setImageURI(selectedImageUri)
            binding.tvPickImage.isGone = true
            updateImageProfile()

        } else
            if (resultCode == Activity.RESULT_OK && requestCode == this.REQUEST_FILE) {
                selectedPdfUri = data?.data
                Log.i("xxfile", "$selectedPdfUri ==try")
                binding.tfCV.setText(selectedPdfUri?.path)

//                    selectedPdfUri?.path?.substring(selectedPdfUri!!.path!!.lastIndexOf('/')+1)
            }
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            // arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor: Cursor? = requireActivity().contentResolver.query(uri,null, null, null, null)
            try {
                var ea = requireActivity().contentResolver.openInputStream(uri)
                Log.d("pdfuri","$ea ====ea")
                if (cursor != null && cursor.moveToFirst()) {
                    Log.d("pdfuri","$result ====content")
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            Log.d("pdfuri","$result ====path")
//            val cut = result!!.lastIndexOf('/')
//            if (cut != -1) {
//                result = result.substring(cut + 1)
//            }
        }
        return result
    }

    @Suppress("MoveLambdaOutsideParentheses")
    fun actionLogout() {
        binding.ivLogout.setOnClickListener {
            PrefsLogin.clear()
            var alertLogout = AlertDialog.Builder(activity)
            alertLogout.setTitle("Confirm Logout")
            alertLogout.setMessage("Are you sure want to logout?")
            alertLogout.setPositiveButton("Logout", { dialog: DialogInterface?, which: Int ->
                var intentLogin = Intent(activity, LoginActivity::class.java)
                startActivity(intentLogin)
                Toast.makeText(activity, "Logout Success", Toast.LENGTH_SHORT).show()
            })
            alertLogout.setNegativeButton("Cancel", { dialog: DialogInterface?, which: Int -> })
            alertLogout.show()
        }
    }
}

