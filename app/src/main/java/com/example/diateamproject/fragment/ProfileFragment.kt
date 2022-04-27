package com.example.diateamproject.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

class ProfileFragment : Fragment() {
    //view binding fragment declaration
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_IMAGE = 1
    private val REQUEST_FILE = 2
    val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    private var selectedImageUri: Uri? = null
    private var selectedPdfUri: Uri? = null
    private val viewModelProfile: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // call function requestPermission from menu activity
        (activity as MenuActivity).requestPermission()

        binding.ivProfile.setOnClickListener {
            openGallery()
        }

        binding.tfCV.setOnClickListener {
            updateResume()
        }

        binding.tfCV.setHint("Upload here")
        viewModelProfile.getProfile(userId)
        Log.d("profileId", "$userId======getProfile")
        setObserver()

        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
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
            binding.tfBio.setText(it.data.jobseekerAbout)
            binding.tfName.setText(it.data.jobseekerName)
            binding.tfEmail.setText(it.data.jobseekerEmail)
            binding.tfPhone.setText(it.data.jobseekerPhone)
            binding.tfAddress.setText(it.data.jobseekerAddress)
            binding.tfDegree.setText(it.data.jobseekerEducation)
            binding.tfSkill.setText(it.data.jobseekerSkill)
            binding.tfProfession.setText(it.data.jobseekerProfession)
            binding.tfSosmed.setText(it.data.jobseekerMedsos)
            binding.tfPorto.setText(it.data.jobseekerPortfolio)
            val dateProfile = it.data.jobseekerDateOfBirth
            binding.tfBirth.setText(dateProfile)

            fullnameFocusListener()
            addressFocusListener()
            professionFocusListener()
            skillFocusListener()
            resumeFocusListener()

            //get image from profile response
            if (it.data.jobseekerImage != null) {
                Log.i("xxload", "xxload ${it.data.jobseekerImage}")
                Glide.with(requireContext())
                    .load(Path.IMAGE_URL + it.data.jobseekerImage)
                    .centerCrop()
                    .into(binding.ivProfile)
                binding.tvPickImage.isGone = true
            } else {
                Log.i("xxpick", "xxpick")
                binding.tvPickImage.isGone = false
            }
            val resumeFile = it.data.jobseekerResume
            binding.tfCV.setText(resumeFile)
        })

        viewModelProfile.listResponseProfile().observe(viewLifecycleOwner, Observer {
            viewModelProfile.getProfile(userId)
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
        val id = userId
        val updateBio = binding.tfBio.text.toString()
        val updateName = binding.tfName.text.toString()
        val updateEmail = binding.tfEmail.text.toString()
        val updatePhone = binding.tfPhone.text.toString()
        val updateAddress = binding.tfAddress.text.toString()
        val updateDateOfBirth = binding.tfBirth.text.toString()
        val updateEducation = binding.tfDegree.text.toString()
        val updateProfession = binding.tfProfession.text.toString()
        val updatePortfolio = binding.tfPorto.text.toString()
        val updateSkill = binding.tfSkill.text.toString()
        val updateMedsos = binding.tfSosmed.text.toString()

        viewModelProfile.updateProfile(
            id,
            updateBio,
            updateName,
            updateEmail,
            updatePhone,
            updateDateOfBirth,
            updateAddress,
            updateEducation,
            updateProfession,
            updatePortfolio,
            updateSkill,
            updateMedsos
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
        val id = userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val tes = getFileName(selectedPdfUri!!)
        binding.tfCV.setText(tes)

        val fileHandler = FileHandler()
        val files = File(fileHandler.handleUri(requireContext(), selectedPdfUri!!)!!)
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
        resumeAlertDialog.setCancelable(false)
        resumeAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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

    private fun saveProfile() {
        binding.tilName.helperText = validName()
        binding.tflAddress.helperText = validAddress()
        binding.tflProfession.helperText = validProfession()
        binding.tflSkill.helperText = validSkill()
        binding.tflResume.helperText = validCv()

        val validName = binding.tilName.helperText == null
        val validAddress = binding.tflAddress.helperText == null
        val validProfession = binding.tflProfession.helperText == null
        val validSkill = binding.tflSkill.helperText == null
        val validResume = binding.tflResume.helperText == null

        if (validName && validAddress && validProfession && validSkill && validResume)
            updateProfile()
        else
            invalidForm()
    }

    //function open gallery
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun openDirectory() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
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
                Log.i("xxfile", "${selectedPdfUri?.path} ==try")
                binding.tfCV.setText(selectedPdfUri?.path?.substring(selectedPdfUri!!.path!!.lastIndexOf('/')+1))
            }
    }

    private fun invalidForm() {
        AlertDialog.Builder(activity)
            .setTitle("Invalid Form")
            .setMessage("Complete your profile form!")
            .setPositiveButton("OK"){ _,_ ->
                // do nothing
            }
            .show()
    }

    private fun fullnameFocusListener() {
        binding.tfName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilName.helperText = validName()
            } else {
                binding.tilName.isHelperTextEnabled = false
            }
        }
    }

    private fun validName(): String? {
        val nameText = binding.tfName.text.toString()
        if (nameText.isEmpty()) {
            return "Enter your full name"
        }
        return null
    }

    private fun addressFocusListener() {
        binding.tfAddress.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tflAddress.helperText = validAddress()
            }
        }
    }

    private fun validAddress(): String? {
        val addressText = binding.tfAddress.text.toString()
        if (addressText.isEmpty()) {
            return "Enter your Current address"
        }
        return null
    }

    private fun professionFocusListener() {
        binding.tfProfession.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tflProfession.helperText = validProfession()
            }
        }
    }

    private fun validProfession(): String? {
        val professionText = binding.tfProfession.text.toString()
        if (professionText.isEmpty()) {
            return "Enter your Current profession"
        }
        return null
    }

    private fun skillFocusListener() {
        binding.tfSkill.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tflSkill.helperText = validSkill()
            }
        }
    }

    private fun validSkill(): String? {
        val skillText = binding.tfSkill.text.toString()
        if (skillText.isEmpty()) {
            return "Enter your Main skill"
        }
        return null
    }

    private fun resumeFocusListener() {
        binding.tfCV.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tflResume.helperText = validCv()
            }
        }
    }

    private fun validCv(): String? {
        val cvText = binding.tfCV.text.toString()
        if (cvText.isNullOrEmpty()) {
            return "Upload your newest Resume"
        }
        return null
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
            Log.d("pdfuri", "$result ====path")
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
            alertLogout.setPositiveButton("Sure", { dialog: DialogInterface?, which: Int ->
                var intentLogin = Intent(activity, LoginActivity::class.java)
                startActivity(intentLogin)
                activity?.finish()
            })
            alertLogout.setNegativeButton("Cancel", { dialog: DialogInterface?, which: Int -> })
            alertLogout.show()
        }
    }
}

