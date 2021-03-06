package com.example.diateamproject.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
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
import androidx.core.widget.addTextChangedListener
import com.example.diateamproject.model.allskills.Data
import com.example.diateamproject.model.updateprofile.SkillData
import com.example.diateamproject.viewmodel.SkillViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable


class ProfileFragment : Fragment(), Skill {
    //view binding fragment declaration
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_IMAGE = 1
    val userId = PrefsLogin.loadInt(PrefsLoginConstant.USERID, 0)
    private var selectedImageUri: Uri? = null
    lateinit var pb: ProgressButtonSaveProfile
    val dialog = UpdateCVFragment()
    var tempPhone = ""
    var arraySkill: ArrayList<Data> = ArrayList<Data>()
    var temp: ArrayList<String> = ArrayList<String>()
    var tempSkill: ArrayList<String> = ArrayList<String>()
    var tempSkillApi: String = ""
    var texttemp = ""
    var getSkillList: ArrayList<SkillData> = ArrayList<SkillData>()
    private val viewModelProfile: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }
    private val viewModelSkill: SkillViewModel by lazy {
        ViewModelProviders.of(this).get(SkillViewModel::class.java)
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

        pb = ProgressButtonSaveProfile(requireContext(), view)

        // call function requestPermission from menu activity
        (activity as MenuActivity).requestPermission()
        viewModelProfile.getProfile(userId)
        Log.d("profileId", "$userId======getProfile")

        binding.btnUpdateImg.setOnClickListener {
            openGallery()
        }
        //show bottom sheet skill
        binding.tfSkill.setOnClickListener {
            val skillFragment = BottomSheetSkillFragment(this)
            val bundle = Bundle()
            bundle.putSerializable("tes", getSkillList)
            Log.d("putTes1", "tes = $getSkillList")
            skillFragment.arguments = bundle
            skillFragment.show(requireFragmentManager(), "bottomSheetSkill")
        }
        //show update cv dialog
        binding.tfCV.setOnClickListener {
            val supportFragmentManager = requireActivity().supportFragmentManager
            dialog.onUpdate = {
                //get the paramater from updateCV fragment
                val intent = requireActivity().intent
                if (intent.extras != null) {
                    val name = intent.getStringExtra("fileName")
                    Log.i("showName", "file name = $name")
                    binding.tfCV.setText(name)
                }
                Snackbar.make(view, "CV updated", Snackbar.LENGTH_SHORT).show()
            }
            dialog.show(supportFragmentManager, "updateCVDialog")
        }
        binding.tfCV.setHint("Upload here")

        binding.btnSaveProfile.cvSaveProfile.setOnClickListener {
            pb.ActiveButton()
            saveProfile()
        }
        setObserver()

        binding.tfPhone.addTextChangedListener {
            if (binding.tfPhone.text.toString().startsWith("0") || binding.tfPhone.text.toString()
                    .startsWith("+") || binding.tfPhone.text.toString().startsWith("6")
            ) {
                binding.tfPhone.text?.clear()
            }
        }

        binding.tfBirth.setOnClickListener {
            datePicker()
        }

        binding.ivBack.setOnClickListener {
            val intentBack = Intent(activity, MenuActivity::class.java)
            startActivity(intentBack)
        }
        actionLogout()
    }

    private fun setObserver() {
        viewModelSkill.responseSkill().observe(viewLifecycleOwner, Observer {
            arraySkill = it.data as ArrayList<Data>
            Log.d("txterei", arraySkill.toString())
            Log.d("txterei", tempSkill.toString())
            for (i in tempSkill.indices) {
                for (a in arraySkill.indices) {
                    if (tempSkill[i].toString() == arraySkill[a].skillId.toString()) {
                        if (texttemp == "") {
                            texttemp += arraySkill[a].skillName
                        } else {
                            texttemp += "," + arraySkill[a].skillName
                        }
                        binding.tfSkill.setText(texttemp)
                        Log.d("texttemptoo", "$texttemp")
                    }

                }
            }
        })
        viewModelProfile.responseProfile().observe(viewLifecycleOwner, Observer {
            tempPhone = it.data.jobseekerPhone
            binding.tfBio.setText(it.data.jobseekerAbout)
            binding.tfName.setText(it.data.jobseekerName)
            binding.tfEmail.setText(it.data.jobseekerEmail)
            if (!tempPhone.isNullOrEmpty()) {
                binding.tfPhone.setText(it.data.jobseekerPhone.substring(2))
            }
            binding.tfAddress.setText(it.data.jobseekerAddress)
            binding.actvDegree.setText(it.data.jobseekerEducation)

            //jobseekerSkill isNotEmpty condition
            if (!it.data.skills.isNullOrEmpty()) {
                getSkillList = it.data.skills as ArrayList<SkillData>
                Log.d("LookSkillSize", "sizexx = ${getSkillList.size}")
                for (i in getSkillList.indices) {
                    if (texttemp.equals("")) {
                        texttemp += getSkillList[i].skillName
                    } else {
                        texttemp += "," + getSkillList[i].skillName
                    }
                }
                binding.tfSkill.setText(texttemp)
                Log.d("texttemptest", texttemp)

                for (i in temp.indices) {
                    if (temp[i] == getSkillList[id].skillName) {
                        temp.addAll(getSkillList as ArrayList<String>)
                    }
                }
            }
            binding.tfProfession.setText(it.data.jobseekerProfession)
            binding.tfSosmed.setText(it.data.jobseekerMedsos)
            binding.tfPorto.setText(it.data.jobseekerPortfolio)
            binding.tfBirth.setText(it.data.jobseekerDateOfBirth)
            if (!it.data.jobseekerResume.isNullOrEmpty()) {
                binding.tfCV.setText(it.data.jobseekerResume)
            }
            binding.tfCompanyName.setText(it.data.jobsekerCompany)
            if (it.data.workStartYear != 0 && it.data.workEndYear != 0) {
                binding.tfDateStart.setText(it.data.workStartYear.toString())
                binding.tfDateEnd.setText(it.data.workEndYear.toString())
            }

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
        })

        viewModelProfile.listResponseProfile().observe(viewLifecycleOwner, Observer {
            viewModelProfile.getProfile(userId)
            UpdateProfileSuccess()
            pb.FinishButton()
        })

        viewModelProfile.getIsErrorUpdate().observe(viewLifecycleOwner, Observer {
            val snackbar = Snackbar.make(requireView(), "Something Wrong", Snackbar.LENGTH_SHORT)
            snackbar.setAction("Try again") {
                it.setOnClickListener {
                    snackbar.dismiss()
                }
            }
            snackbar.show()
            pb.FinishButton()
        })

        viewModelProfile.listResponseImage().observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "Image Updated", Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateProfile() {
        val phoneccp = binding.ccp.selectedCountryCode.toString() + binding.tfPhone.text.toString()
        val id = userId
        val updateBio = binding.tfBio.text.toString()
        val updateName = binding.tfName.text.toString()
        val updateEmail = binding.tfEmail.text.toString()
        val updateAddress = binding.tfAddress.text.toString()
        val updateDateOfBirth = binding.tfBirth.text.toString()
        val updateEducation = binding.actvDegree.text.toString()
        val updateProfession = binding.tfProfession.text.toString()
        val updatePortfolio = binding.tfPorto.text.toString()
        val updateSkill = tempSkillApi
        val updateMedsos = binding.tfSosmed.text.toString()
        val updateCompanyName = binding.tfCompanyName.text.toString().capitalize()
        val updateYearStart = binding.tfDateStart.text.toString().toIntOrNull()
        val updateYearEnd = binding.tfDateEnd.text.toString().toIntOrNull()

        viewModelProfile.updateProfile(
            id,
            updateBio,
            updateName,
            updateEmail,
            phoneccp,
            updateDateOfBirth,
            updateAddress,
            updateEducation,
            updateProfession,
            updatePortfolio,
            updateSkill,
            updateMedsos,
            updateCompanyName,
            updateYearStart,
            updateYearEnd
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
        selectedImageUri?.let {
            pathImage =
                uriPathHelper.getPath(requireContext(), it).toString()
        }

        val file: File = File(pathImage)
        val requestImage: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val bodyImage: MultipartBody.Part = MultipartBody.Part.createFormData(
            "jobseekerImage", file.name.trim(), requestImage
        )

        viewModelProfile.updateImageProfile(id, bodyImage)
    }

    //clean resource to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun datePicker() {
        val datePickerFragment = DatePickerFragment()
        val supportFragmentManager = requireActivity().supportFragmentManager
        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            viewLifecycleOwner
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("SELECTED_DATE")
                Log.d("testDate", "$date ====new date")
                binding.tfBirth.setText(date)
            }
        }
        datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
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

        if (validName && validAddress && validProfession && validSkill && validResume) {
            pb.ActiveButton()
            updateProfile()
        } else {
            pb.ActiveButton()
            invalidForm()
            pb.FinishButton()
        }
    }

    //function open gallery
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, REQUEST_IMAGE)
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
        }
    }

    private fun invalidForm() {
        AlertDialog.Builder(activity)
            .setTitle("Invalid Form")
            .setMessage("Complete your profile form!")
            .setPositiveButton("OK") { _, _ ->
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
        val userSkill = PrefsLogin.loadString(PrefsLoginConstant.SKILL, "")
        val skillText = userSkill
        if (skillText.isNullOrEmpty()) {
            return "Choose your skills"
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

    @Suppress("MoveLambdaOutsideParentheses")
    fun actionLogout() {
        binding.ivLogout.setOnClickListener {
            PrefsLogin.clear()
            val alertLogout = AlertDialog.Builder(activity)
            alertLogout.setTitle("Confirm Logout")
            alertLogout.setMessage("Are you sure want to logout?")
            alertLogout.setPositiveButton("Sure", { dialog: DialogInterface?, which: Int ->
                val intentLogin = Intent(activity, LoginActivity::class.java)
                startActivity(intentLogin)
                activity?.finish()
            })
            alertLogout.setNegativeButton("Cancel", { dialog: DialogInterface?, which: Int -> })
            alertLogout.show()
        }
    }

    private fun UpdateProfileSuccess() {
        AlertDialog.Builder(requireContext())
            .setTitle("Successfull")
            .setMessage("Your profile updated")
            .setPositiveButton("OK") { _, _ ->
                //do nothing
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        val degrees = resources.getStringArray(R.array.degrees)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.degree_item, degrees)
        binding.actvDegree.setAdapter(arrayAdapter)
    }

    override fun setData(arrayList: Serializable) {
        tempSkillApi = ""
        texttemp = ""
        tempSkill = arrayList as ArrayList<String>
        for (i in tempSkill.indices) {
            if (tempSkillApi.equals("")) {
                tempSkillApi += tempSkill[i].toString()
            } else {
                tempSkillApi += "," + tempSkill[i].toString()
            }
        }
        viewModelSkill.getSkill()
        Log.d("lastval", tempSkillApi)
    }
}