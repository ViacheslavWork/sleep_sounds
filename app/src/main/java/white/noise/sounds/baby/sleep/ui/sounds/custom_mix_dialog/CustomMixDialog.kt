package white.noise.sounds.baby.sleep.ui.sounds.custom_mix_dialog

import android.content.ComponentName
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.ui.sounds.SoundsEvent
import white.noise.sounds.baby.sleep.ui.sounds.SoundsViewModel

private const val TAG = "CustomMixDialog"
class CustomMixDialog : DialogFragment() {
    private val soundsViewModel: SoundsViewModel by sharedViewModel()
    private lateinit var adapter: CustomMixAdapter
    private var _binding: DialogCustomMixBinding? = null
    private val binding get() = _binding!!

    var playerService: PlayerService? = null

    // Boolean to check if our fragment is bound to service or not
    var isServiceBound: Boolean = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            Log.d(TAG, "ServiceConnection: connected to service.")
            // We've bound to MyService, cast the IBinder and get MyBinder instance
            val binder = iBinder as PlayerService.MyBinder
            playerService = binder.service
            isServiceBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(TAG, "ServiceConnection: disconnected from service.")
            isServiceBound = false
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogCustomMixBinding.inflate(inflater, container, false)
        return binding.root
    }
//    override fun getTheme() = R.style.RoundedCornersDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindService()
        setUpListeners()
        setUpSystemVolume()
        setUpRecyclerView()
        observeSoundEvent()
    }

    private fun observeSoundEvent() {
        adapter.event.observe(viewLifecycleOwner){
            if (it is SoundsEvent.OnSeekBarChanged) {
                Log.i(TAG, "observeSoundEvent: sound = ${it.sound}")
                playerService?.changeVolume(it.sound)
            }
        }
    }


    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        adapter = CustomMixAdapter()
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

    private fun bindService() {
        Intent(requireContext(), PlayerService::class.java).also { intent ->
            requireActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindService() {
        Intent(requireContext(), PlayerService::class.java).also {
            requireActivity().unbindService(serviceConnection)
        }
    }

    override fun onDestroy() {
        if (isServiceBound) {
            unbindService()
        }
        super.onDestroy()
    }
}