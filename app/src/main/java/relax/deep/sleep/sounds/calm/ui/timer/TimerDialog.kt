package relax.deep.sleep.sounds.calm.ui.timer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.threeten.bp.LocalTime
import relax.deep.sleep.sounds.calm.databinding.DialogTimerBinding

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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpListeners() {
        binding.offBtn.setOnClickListener(this)
        binding.customBtn.setOnClickListener(this)
        binding.minutes10Btn.setOnClickListener(this)
        binding.minutes15Btn.setOnClickListener(this)
        binding.minutes20Btn.setOnClickListener(this)
        binding.minutes30Btn.setOnClickListener(this)
        binding.minutes40Btn.setOnClickListener(this)
        binding.minutes50Btn.setOnClickListener(this)
        binding.hour1Btn.setOnClickListener(this)
        binding.hour2Btn.setOnClickListener(this)
        binding.hour3Btn.setOnClickListener(this)
        binding.hour4Btn.setOnClickListener(this)
        binding.hour8Btn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        var isDismiss = true
        when (view) {
            binding.offBtn -> timerViewModel.setTime(Times.off)
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
            binding.customBtn -> {
                isDismiss = false
                val picker = showTimePicker()
                picker.addOnPositiveButtonClickListener {
                    timerViewModel.setTime(Times.Custom(LocalTime.of(picker.hour, picker.minute)))
                    isDismiss = true
                    this.dismiss()
                }
            }
        }
        if (isDismiss) this.dismiss()
    }

    private fun showTimePicker(): MaterialTimePicker {
        val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(0)
                .setMinute(0)
                .setTitleText("Select Appointment time")
                .build()
        picker.show(childFragmentManager, "TimePicker")
        return picker
    }

}