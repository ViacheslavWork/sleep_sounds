package white.noise.sounds.baby.sleep.ui.dialogs

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.databinding.DialogUnlockForFreeBinding
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.ui.player.PlayerFragment


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
        sound?.let {binding.unlockFreeIv.setImageResource(it.icon) }
    }

    private fun setUpListeners() {
        binding.closeUnlockFreeBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.watchVideoBtn.setOnClickListener { showToast("Open video") }
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