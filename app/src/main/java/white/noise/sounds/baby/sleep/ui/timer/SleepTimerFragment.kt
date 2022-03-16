package white.noise.sounds.baby.sleep.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import white.noise.sounds.baby.sleep.MainActivity
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentSleepTimerBinding


class SleepTimerFragment : Fragment() {
    private var _binding: FragmentSleepTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSleepTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.setOnClickListener { requireActivity().onBackPressed() }
        (requireActivity() as MainActivity).timeLD.observe(viewLifecycleOwner){
            binding.timerTv.text = String.format(getString(R.string.time_format), it.minute, it.second)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}