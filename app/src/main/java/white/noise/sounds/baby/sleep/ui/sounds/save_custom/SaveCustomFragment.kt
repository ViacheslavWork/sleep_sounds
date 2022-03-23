package white.noise.sounds.baby.sleep.ui.sounds.save_custom

import android.app.Activity.RESULT_OK
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentSaveCustomBinding
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory
import white.noise.sounds.baby.sleep.ui.mixes.MixesEvent
import white.noise.sounds.baby.sleep.ui.mixes.MixesViewModel
import white.noise.sounds.baby.sleep.ui.sounds.SoundsViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

private const val TAG = "SaveCustomFragment"

class SaveCustomFragment : Fragment() {
    companion object {
        const val UPLOAD_REQUEST = 1
    }

    private val soundsViewModel: SoundsViewModel by sharedViewModel()
    private val mixesViewModel: MixesViewModel by sharedViewModel()
    private var _binding: FragmentSaveCustomBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null
    private val _selectedImageUriLD = MutableLiveData<Uri?>()
    private val selectedImageUriLD: LiveData<Uri?> = _selectedImageUriLD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(resources, R.drawable.background, null)
        _binding = FragmentSaveCustomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        setUpImagePlaceholder()
        setUpListeners()
        observeSelectedImageUri()
    }

    private fun observeSelectedImageUri() {
        selectedImageUriLD.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.applyBtn.isEnabled = false
                binding.uploadImageBtn.isEnabled = true
                binding.imageLayout.root.visibility = View.GONE
            } else {
                binding.uploadImageBtn.isEnabled = false
                binding.applyBtn.isEnabled = binding.customNameEt.text.isNotEmpty()
                setImage(it)
            }
        }
    }

    /*private fun setUpImagePlaceholder() {
        selectedImageUri =
                Uri.parse("android.resource://white.noise.sounds.baby.sleep/drawable/mix_placeholder")
        setImage(selectedImageUri)
    }*/

    private fun setUpListeners() {
        binding.customNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                binding.applyBtn.isEnabled =
                    binding.customNameEt.text.isNotEmpty() && selectedImageUri != null
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.imageLayout.photoRemoveIv.setOnClickListener {
            selectedImageUri = null
            _selectedImageUriLD.postValue(null)
        }

        binding.imageLayout.photoEditIv.setOnClickListener {
            openGallery(UPLOAD_REQUEST)
        }

        binding.uploadImageBtn.setOnClickListener {
            openGallery(UPLOAD_REQUEST)
        }

        binding.closeBtn.setOnClickListener { requireActivity().onBackPressed() }

        binding.applyBtn.setOnClickListener {
            selectedImageUri?.let {
                val imageInExternalStorageUri = saveImageToExternalStorage(imageUri = it)
                val mix = Mix(
                    id = 0,
                    title = binding.customNameEt.text.toString(),
                    sounds = soundsViewModel.selectedSounds.value?.toMutableList()!!,
                    picturePath = imageInExternalStorageUri,
                    category = MixCategory.Others,
                    isPremium = false,
                    isCustom = true
                )
                mixesViewModel.handleEvent(MixesEvent.OnMixSave(mix))
                requireActivity().onBackPressed()
            }
        }
    }

    private fun openGallery(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    private fun setImage(imageUri: Uri) {
        setPhotoVisibility(isVisible = true)
        binding.imageLayout.photoIv.setImageURI(imageUri)
    }

    private fun setPhotoVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.imageLayout.root.visibility = visibility
    }

    private fun saveImageToExternalStorage(imageUri: Uri): Uri {
        val fileName = imageUri.toString().substringAfterLast("/")

        // Get the bitmap
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                imageUri
            )
            else -> {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            }
        }

        // Get the context wrapper instance
        val wrapper = ContextWrapper(requireContext())

        // Initializing a new file
        // The bellow line return a directory in external storage
        var file = File(wrapper.getExternalFilesDir(null), "custom_mix_images")
        file.mkdirs()
        // Create a file to save the image
        file = File(file, "${fileName}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val imageUri = data.data!!
            selectedImageUri = imageUri
            _selectedImageUriLD.value = imageUri
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}