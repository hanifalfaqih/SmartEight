package sch.id.snapan.smarteight.ui.attendance.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.R
import sch.id.snapan.smarteight.databinding.FragmentAttendanceBinding
import sch.id.snapan.smarteight.other.EventObserver
import sch.id.snapan.smarteight.ui.attendance.AttendanceViewModel
import sch.id.snapan.smarteight.ui.snackbar
import java.io.File

@AndroidEntryPoint
class AttendanceFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 1
    }

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AttendanceViewModel by viewModels()
    private var currentImageUri: Uri? = null
    private val imageUserAttendance: ImageView by lazy {
        binding.ivUserAttendance
    }
    private var stringActivity: String?= null

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUserAttendance.setImageURI(currentImageUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()
        subscribeToGetUserUidObserver()
        subscribeToSendAttendanceObserver()

        binding.btnAddImage.setOnClickListener {
            if (hasCameraAndStoragePermissions()) {
                requestTakeImage()
            } else {
                requestCameraAndStoragePermissions()
            }
        }

        binding.rgActivity.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.rb_wfh -> { stringActivity = "WFH" }
                R.id.rb_wfo -> { stringActivity = "WFO" }
            }
        }

        binding.btnSendAttendance.setOnClickListener {
            stringActivity?.let { string -> handleSendAttendance(string) }
        }

    }

    private fun handleSendAttendance(activity: String) {
        if (currentImageUri == null) {
            snackbar(getString(R.string.message_attendance_image))
        } else {
            viewModel.attendanceUser(
                currentImageUri!!,
                binding.etName.text.toString(),
                binding.etStatus.text.toString(),
                activity,
                binding.spinnerTime.selectedItem.toString()
            )
        }
    }

    private fun hasCameraAndStoragePermissions() =
        EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

    private fun requestCameraAndStoragePermissions() {
        val request = PermissionRequest.Builder(requireContext())
            .code(PERMISSIONS_REQUEST_CODE)
            .perms(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
            .rationale(getString(R.string.msg_request_permission))
            .positiveButtonText(getString(R.string.btn_positive))
            .negativeButtonText(getString(R.string.btn_negative))
            .build()
        EasyPermissions.requestPermissions(this, request)
    }

    private fun requestTakeImage() {
        currentImageUri = FileProvider.getUriForFile(
            requireContext(),
            "sch.id.snapan.smarteight.provider",
            createImageFile()
        )
        cameraLauncher.launch(currentImageUri)
    }

    private fun createImageFile(): File {
        val storageDirectory = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("temp_image", "jpg", storageDirectory)
    }

    private fun subscribeToSendAttendanceObserver() {
        viewModel.attendanceStatusUser.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.progressBarAttendance.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = { binding.progressBarAttendance.visibility = View.VISIBLE }
        ) {
            binding.progressBarAttendance.visibility = View.INVISIBLE
            snackbar(getString(R.string.attendance_user_success))
        })
    }

    private fun subscribeToGetUserUidObserver() {
        viewModel.getUserStatus.observe(viewLifecycleOwner, EventObserver(
            onError = { snackbar(it) },
            onLoading = { }
        ) {user ->
            binding.etName.setText(user?.name)
            binding.etStatus.setText(user?.status)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity())
                .title(getString(R.string.title_dialog_permission))
                .rationale(getString(R.string.msg_dialog_permission))
                .positiveButtonText(getString(R.string.btn_positive))
                .negativeButtonText(getString(R.string.btn_negative))
                .build().show()
        } else {
            requestCameraAndStoragePermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}