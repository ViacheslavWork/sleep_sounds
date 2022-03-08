package white.noise.sounds.baby.sleep.ui.sounds.custom_mix_dialog

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.DialogCustomMixBinding
import white.noise.sounds.baby.sleep.ui.sounds.SoundsViewModel


class CustomMixDialog : DialogFragment() {
    private val soundsViewModel: SoundsViewModel by sharedViewModel()
    private var _binding: DialogCustomMixBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DialogCustomMixBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
        setUpSystemVolume()
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        val adapter = CustomMixAdapter()
        recyclerView.layoutManager =
            LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
        recyclerView.adapter = adapter
        soundsViewModel.selectedSounds.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setUpListeners() {
        binding.closeTv.setOnClickListener { requireActivity().onBackPressed() }
        binding.saveCustomBtn.setOnClickListener {
            findNavController().navigate(
                CustomMixDialogDirections.actionCustomMixDialogToSaveCustomFragment()
            )
        }
    }

    private fun setUpSystemVolume() {
        val audioManager = requireContext().getSystemService(AUDIO_SERVICE) as AudioManager
        binding.systemVolumeInclude.volumeSeekBar.max =
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        binding.systemVolumeInclude.volumeSeekBar.progress =
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        binding.systemVolumeInclude.volumeSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0);
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        binding.systemVolumeInclude.cross.visibility = View.INVISIBLE
        binding.systemVolumeInclude.icon.setImageResource(R.drawable.ic_dynamic)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}