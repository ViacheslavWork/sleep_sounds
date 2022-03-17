package white.noise.sounds.baby.sleep.ui.dialogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.DialogUnlockForFreeBinding
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.ui.sounds.SoundsFragment


class UnlockForFreeDialog : DialogFragment() {
    companion object {
        const val mixKey = "MIX_KEY"
        const val soundKey = "SOUND_KEY"
    }

    private var _binding: DialogUnlockForFreeBinding? = null
    private val binding get() = _binding!!
    private var mix: Mix? = null
    private var sound: Sound? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mix = arguments?.getParcelable(mixKey)
        sound = arguments?.getParcelable(soundKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUnlockForFreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setImage()
        setUpListeners()
    }

    private fun setImage() {
        mix?.let { binding.unlockFreeIv.setImageURI(it.picturePath) }
        sound?.let { binding.unlockFreeIv.setImageResource(it.icon) }
    }

    private fun setUpListeners() {
        binding.closeUnlockFreeBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.watchVideoBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_unlockForFreeFragment_to_navigation_sounds,
                bundleOf(SoundsFragment.PLAY_PREMIUM_SOUND_AFTER_VIDEO to sound)
            )
            parentFragment?.onActivityResult(SoundsFragment.UNLOCK_FOR_FREE_DIALOG_REQUEST_CODE,
                Activity.RESULT_OK, Intent().apply { putExtra(SoundsFragment.soundKey, sound) }
            )
            showToast("Open video")
        }
        binding.unlockAllSoundsBtn.setOnClickListener {
            findNavController().navigate(
                UnlockForFreeDialogDirections.actionUnlockForFreeFragmentToGoPremiumFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}