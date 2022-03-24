package relax.deep.sleep.sounds.calm.ui.dialogs

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
import androidx.navigation.fragment.findNavController
import relax.deep.sleep.sounds.calm.databinding.DialogUnlockForFreeBinding
import relax.deep.sleep.sounds.calm.model.Mix
import relax.deep.sleep.sounds.calm.model.Sound
import relax.deep.sleep.sounds.calm.ui.mixes.MixesFragment
import relax.deep.sleep.sounds.calm.ui.sounds.SoundsFragment


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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
            toDoAfterVideo()
            showToast("Open video")
        }
        binding.unlockAllSoundsBtn.setOnClickListener {
            findNavController().navigate(
                UnlockForFreeDialogDirections.actionUnlockForFreeFragmentToGoPremiumFragment()
            )
        }
    }

    /*private fun loadInterAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            resources.getString(R.string.interstitial_test_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interAd = interstitialAd
                }
            })
    }

    private fun showInterAd(function: () -> Unit) {
        if (interAd != null) {
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    function()
                    interAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    function()
                    interAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    function()
                    interAd = null
                }
            }
            interAd?.show(requireActivity())
        } else {
            function()
        }
    }*/

    private fun toDoAfterVideo() {
        requireActivity().onBackPressed()
        mix?.let {
            setFragmentResult(
                MixesFragment.playPremiumMixRequest,
                bundleOf(MixesFragment.mixIdKey to it.id)
            )
        }
        sound?.let {
            setFragmentResult(
                SoundsFragment.playPremiumSoundRequest,
                bundleOf(SoundsFragment.playAfterVideoKey to true)
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