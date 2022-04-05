package calm.sleep.relaxing.sounds.noise.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import calm.sleep.relaxing.sounds.noise.MainActivity
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.FragmentSleepTimerBinding


class SleepTimerFragment : Fragment() {
    private var _binding: FragmentSleepTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        _binding = FragmentSleepTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.setOnClickListener { requireActivity().onBackPressed() }
        (requireActivity() as MainActivity).timeLD.observe(viewLifecycleOwner){
            binding.timerTv.text = String.format(getString(R.string.time_format),it.hour, it.minute, it.second)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        _binding = null
    }
}