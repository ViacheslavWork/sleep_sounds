package white.noise.sounds.baby.sleep.ui.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.databinding.FragmentSetTimerBinding
import white.noise.sounds.baby.sleep.ui.sounds.SoundsViewModel
import white.noise.sounds.baby.sleep.utils.ToastHelper


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

    }

    private fun setUpListeners() {
        binding.applyBtn.setOnClickListener { ToastHelper.showToast(requireContext(),"Apply clicked") }
        binding.closeBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.timerBtn.setOnClickListener { findNavController().navigate(SetTimerFragmentDirections.actionSetTimerFragmentToTimerDialog()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}