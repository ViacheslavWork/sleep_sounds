package relax.deep.sleep.sounds.calm.ui.timer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import relax.deep.sleep.sounds.calm.databinding.FragmentSetTimerBinding
import relax.deep.sleep.sounds.calm.service.TimerService
import relax.deep.sleep.sounds.calm.utils.Constants


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
        timerViewModel.selectedTime.observe(viewLifecycleOwner) {
            if (it !is Times.Custom) {
                binding.timerBtn.text = it.title
            } else {
                binding.timerBtn.text =
                    String.format("%d hours %d minutes", it.time?.hour, it.time?.minute)
            }
        }
    }
    private fun setUpListeners() {
        binding.applyBtn.setOnClickListener {
            sendCommandToService(Constants.ACTION_START_TIMER)
            requireActivity().onBackPressed()
        }
        binding.closeBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.timerBtn.setOnClickListener {
            findNavController().navigate(
                SetTimerFragmentDirections.actionSetTimerFragmentToTimerDialog()
            )
        }
    }

    private fun sendCommandToService(action: String) {
        Intent(requireContext(), TimerService::class.java).also {
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