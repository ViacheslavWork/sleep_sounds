package calm.sleep.relaxing.sounds.noise.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.advertising.MyInterstitialAd
import calm.sleep.relaxing.sounds.noise.data.Repository
import calm.sleep.relaxing.sounds.noise.databinding.DialogUnlockForFreeBinding
import calm.sleep.relaxing.sounds.noise.model.Mix
import calm.sleep.relaxing.sounds.noise.model.Sound
import calm.sleep.relaxing.sounds.noise.ui.mixes.MixesFragment
import calm.sleep.relaxing.sounds.noise.ui.sounds.SoundsFragment


class UnlockForFreeDialog : DialogFragment() {
    companion object {
        const val mixKey = "MIX_KEY"
        const val soundKey = "SOUND_KEY"
    }

    private var _binding: DialogUnlockForFreeBinding? = null
    private val binding get() = _binding!!
    private var mix: Mix? = null
    private var sound: Sound? = null
    private val interstitialAd: MyInterstitialAd by inject()
    private val repository: Repository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mix = arguments?.getParcelable(mixKey)
        sound = arguments?.getParcelable(soundKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        interstitialAd.loadInterAd()
        _binding = DialogUnlockForFreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setImage()
        setUpListeners()
    }

    private fun setImage() {
        mix?.let { binding.unlockFreeIv.setImageURI(it.picturePath) }
        sound?.let {
            binding.unlockFreeIv.setImageResource(it.icon)
            binding.unlockFreeIv.setColorFilter(Color.argb(255, 255, 255, 255))
        }
    }

    private fun setUpListeners() {
        binding.closeUnlockFreeBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.watchVideoBtn.setOnClickListener {
            val isShown = interstitialAd.showInterAd(requireActivity()) { toDoAfterVideo() }
            if (!isShown) findNavController().navigate(R.id.action_global_to_luckyDialog)
        }
        binding.unlockAllSoundsBtn.setOnClickListener {
            findNavController().navigate(
                UnlockForFreeDialogDirections.actionUnlockForFreeFragmentToGoPremiumFragment()
            )
        }
    }

    private fun toDoAfterVideo() {
        requireActivity().onBackPressed()
        mix?.let {
            setFragmentResult(
                MixesFragment.playPremiumMixRequest,
                bundleOf(MixesFragment.mixIdKey to it.id)
            )
            lifecycleScope.launch {
                repository.saveMix(it.apply { isPremium = false })
            }
        }
        sound?.let {
            setFragmentResult(
                SoundsFragment.playPremiumSoundRequest,
                bundleOf(SoundsFragment.playAfterVideoKey to true)
            )
            lifecycleScope.launch {
                repository.saveSound(it.apply { isPremium = false })
            }
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