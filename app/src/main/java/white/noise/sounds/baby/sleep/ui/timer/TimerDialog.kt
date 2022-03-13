package white.noise.sounds.baby.sleep.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.threeten.bp.LocalTime
import white.noise.sounds.baby.sleep.databinding.DialogTimerBinding

class TimerDialog : DialogFragment(), View.OnClickListener {
    private var _binding: DialogTimerBinding? = null
    private val timerViewModel: TimerViewModel by sharedViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DialogTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.offBtn.setOnClickListener (this)
        binding.customBtn.setOnClickListener (this)
        binding.minutes10Btn.setOnClickListener (this)
        binding.minutes15Btn.setOnClickListener (this)
        binding.minutes20Btn.setOnClickListener (this)
        binding.minutes30Btn.setOnClickListener (this)
        binding.minutes40Btn.setOnClickListener (this)
        binding.minutes50Btn.setOnClickListener (this)
        binding.hour1Btn.setOnClickListener(this)
        binding.hour2Btn.setOnClickListener (this)
        binding.hour3Btn.setOnClickListener (this)
        binding.hour4Btn.setOnClickListener (this)
        binding.hour8Btn.setOnClickListener (this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.offBtn -> timerViewModel.setTime(Times.off)
            binding.customBtn -> timerViewModel.setTime(Times.Custom(LocalTime.of(0,0,5)))
            binding.minutes10Btn -> timerViewModel.setTime(Times._10minutes)
            binding.minutes15Btn -> timerViewModel.setTime(Times._15minutes)
            binding.minutes20Btn -> timerViewModel.setTime(Times._20minutes)
            binding.minutes30Btn -> timerViewModel.setTime(Times._30minutes)
            binding.minutes40Btn -> timerViewModel.setTime(Times._40minutes)
            binding.minutes50Btn -> timerViewModel.setTime(Times._50minutes)
            binding.hour1Btn -> timerViewModel.setTime(Times._1hour)
            binding.hour2Btn -> timerViewModel.setTime(Times._2hours)
            binding.hour3Btn -> timerViewModel.setTime(Times._3hours)
            binding.hour4Btn -> timerViewModel.setTime(Times._4hours)
            binding.hour8Btn -> timerViewModel.setTime(Times._8hours)
        }
        this.dismiss()
    }

}