package white.noise.sounds.baby.sleep.ui.timer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.databinding.FragmentSetTimerBinding
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.utils.Constants


class SetTimerFragment : Fragment() {
    private val timerViewModel: TimerViewModel by sharedViewModel()

    private var _binding: FragmentSetTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSetTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
        observeTime()
    }

    private fun observeTime() {
        timerViewModel.selectedTime.observe(viewLifecycleOwner){
            if (it !is Times.Custom) {
                binding.timerBtn.text = it.title
            } else {
                binding.timerBtn.text = String.format("%d hours %d minutes",it.time?.hour, it.time?.minute)
            }
        }
    }



    private fun setUpListeners() {
        binding.applyBtn.setOnClickListener {
//            ToastHelper.showToast(requireContext(), "Apply clicked")
            sendCommandToService(Constants.ACTION_START_TIMER)
            requireActivity().onBackPressed()
        }
        binding.closeBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.timerBtn.setOnClickListener { findNavController().navigate(SetTimerFragmentDirections.actionSetTimerFragmentToTimerDialog()) }
    }

    private fun sendCommandToService(action: String) {
        Intent(requireContext(), PlayerService::class.java).also {
            it.putExtra(Constants.EXTRA_TIME, timerViewModel.selectedTime.value)
            it.action = action
            requireContext().startService(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}