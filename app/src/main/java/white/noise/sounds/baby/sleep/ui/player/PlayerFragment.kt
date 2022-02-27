package white.noise.sounds.baby.sleep.ui.player

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ButtonPlayerBinding
import white.noise.sounds.baby.sleep.databinding.FragmentPlayerBinding
import white.noise.sounds.baby.sleep.service.TimerService
import white.noise.sounds.baby.sleep.ui.mix_sounds.AdditionalSoundsViewModel

private const val TAG = "PlayerFragment"

class PlayerFragment : Fragment() {
    private val soundsViewModel: AdditionalSoundsViewModel by sharedViewModel()
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    //    override fun getTheme() = R.style.FullScreenDialogTheme
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpEditBtn()

        setUpListeners()

        observeSounds()
        observeTimer()
    }

    private fun observeTimer() {
        TimerService.isTimerRunning.observe(viewLifecycleOwner) {
            if (it) {
                binding.timeTv.visibility = View.VISIBLE
                TimerService.timerTime.observe(viewLifecycleOwner) { timerTime ->
                    binding.timeTv.text = timerTime.toString()
                }
            } else {
                binding.timeTv.visibility = View.GONE
            }
        }
    }


    private fun setUpListeners() {
        binding.downArrowToolbarIv.setOnClickListener { requireActivity().onBackPressed() }
        binding.playerPlayPauseBtn.setOnClickListener { showToast("play button clicked`") }
        binding.timerTv.setOnClickListener { findNavController().navigate(PlayerFragmentDirections.actionPlayerFragmentToSetTimerFragment()) }
    }

    private fun observeSounds() {
        soundsViewModel.playerSounds.observe(viewLifecycleOwner) {
            binding.editBtn.numSoundsTv.text = it.size.toString()
            it.forEachIndexed { index, sound ->
                getButtons()[index].apply {
                    playerBtn.setOnClickListener {
                        findNavController().navigate(
                            PlayerFragmentDirections.actionPlayerFragmentToAdditionalSoundsFragment()
                        )
                    }
                    root.visibility = View.VISIBLE
                    btnTv.text =
                        String.format(resources.getString(R.string.volume_percentage), sound.volume)
                    val imageLoader = ImageLoader.Builder(binding.root.context)
                        .componentRegistry { add(SvgDecoder(binding.root.context)) }
                        .build()
                    val request = ImageRequest.Builder(binding.root.context)
                        .placeholder(R.drawable.ic_sound_placeholder)
                        .error(R.drawable.ic_sound_placeholder)
                        .data(Uri.parse("file:///android_asset/icons/${sound.icon}"))
                        .target(playerBtn)
                        .build()
                    imageLoader.enqueue(request)
                }
            }
            showLog(it.toString())
        }
    }

    private fun getButtons(): List<ButtonPlayerBinding> {
        return mutableListOf<ButtonPlayerBinding>()
            .apply {
                add(binding.btn1)
                add(binding.btn2)
                add(binding.btn3)
                add(binding.btn4)
                add(binding.btn5)
            }.toList()
    }


    private fun setUpEditBtn() {
        binding.editBtn.root.visibility = View.VISIBLE
        binding.editBtn.smallRoundIv.visibility = View.VISIBLE
        binding.editBtn.numSoundsTv.visibility = View.VISIBLE
        binding.editBtn.playerBtn.setOnClickListener {
            findNavController().navigate(PlayerFragmentDirections.actionPlayerFragmentToAdditionalSoundsFragment())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}