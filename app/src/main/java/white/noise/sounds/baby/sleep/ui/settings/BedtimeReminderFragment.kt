package white.noise.sounds.baby.sleep.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.threeten.bp.LocalTime
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.data.database.entity.AlarmEntity
import white.noise.sounds.baby.sleep.databinding.FragmentBedtimeReminderBinding
import white.noise.sounds.baby.sleep.utils.Constants

class BedtimeReminderFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by sharedViewModel()
    private var _binding: FragmentBedtimeReminderBinding? = null
    private val binding get() = _binding!!

    private var alarmTime: LocalTime = LocalTime.of(0, 0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBedtimeReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewsAvailability(binding.bedtimeReminderSwitch.isChecked)

        setUpWeekButtons()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.bedtimeReminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            setViewsAvailability(isEnabled = isChecked)
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            with(sharedPref?.edit()) {
                this?.putBoolean(Constants.PREFERENCE_ALARM_IS_SET, isChecked)
                this?.apply()
            }
            if (!isChecked) {
                lifecycleScope.launch {
                    settingsViewModel.getAlarm(1).cancelAlarm(requireContext())
                }
            }
        }
        binding.backToolbarIv.setOnClickListener { requireActivity().onBackPressed() }
        binding.timeBlock.setOnClickListener {
            val picker = showTimePicker()
            picker.addOnPositiveButtonClickListener {
                binding.hoursTv.text = String.format("%02d", picker.hour)
                binding.minutesTv.text = String.format("%02d", picker.minute)
                alarmTime = LocalTime.of(picker.hour, picker.minute)
            }
        }
        binding.okBtn.setOnClickListener {
            scheduleAlarm(alarmTime)
        }
        setUpWeekButtonsListeners()
    }

    private fun setViewsAvailability(isEnabled: Boolean) {
        var alpha = 0.5f
        if (isEnabled) alpha = 1f
        binding.selectTimeTv.alpha = alpha
        binding.repeatTv.alpha = alpha
        binding.hoursTv.alpha = alpha
        binding.minutesTv.alpha = alpha
        binding.dots.alpha = alpha
        binding.timeBlock.alpha = alpha

        getButtons().map { it.isEnabled = isEnabled }
        binding.timeBlock.isClickable = isEnabled
        binding.timeBlock.isEnabled = isEnabled
        Log.i("TAG", "setUpListeners: ${binding.saturdayBtn.btn.isEnabled}")
    }

    private fun setUpWeekButtonsListeners() {
        binding.sundayBtn.btn.setOnClickListener {
            binding.sundayBtn.btn.isSelected = !binding.sundayBtn.btn.isSelected
        }
        binding.mondayBtn.btn.setOnClickListener {
            binding.mondayBtn.btn.isSelected = !binding.mondayBtn.btn.isSelected
        }
        binding.tuesdayBtn.btn.setOnClickListener {
            binding.tuesdayBtn.btn.isSelected = !binding.tuesdayBtn.btn.isSelected
        }
        binding.wednesdayBtn.btn.setOnClickListener {
            binding.wednesdayBtn.btn.isSelected = !binding.wednesdayBtn.btn.isSelected
        }
        binding.thursdayBtn.btn.setOnClickListener {
            binding.thursdayBtn.btn.isSelected = !binding.thursdayBtn.btn.isSelected
        }
        binding.fridayBtn.btn.setOnClickListener {
            binding.fridayBtn.btn.isSelected = !binding.fridayBtn.btn.isSelected
        }
        binding.saturdayBtn.btn.setOnClickListener {
            binding.saturdayBtn.btn.isSelected = !binding.saturdayBtn.btn.isSelected
        }
    }

    private fun showTimePicker(): MaterialTimePicker {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(10)
                .setMinute(20)
                .setTitleText("Select Appointment time")
                .build()
        picker.show(childFragmentManager, "TimePicker")
        return picker
    }

    private fun setUpWeekButtons() {
        binding.sundayBtn.btn.text = resources.getString(R.string.sunday)
        binding.mondayBtn.btn.text = resources.getString(R.string.monday)
        binding.tuesdayBtn.btn.text = resources.getString(R.string.tuesday)
        binding.wednesdayBtn.btn.text = resources.getString(R.string.wednesday)
        binding.thursdayBtn.btn.text = resources.getString(R.string.thursday)
        binding.fridayBtn.btn.text = resources.getString(R.string.friday)
        binding.saturdayBtn.btn.text = resources.getString(R.string.saturday)
        getButtons().map { it.isEnabled = binding.bedtimeReminderSwitch.isChecked }
    }

    private fun scheduleAlarm(time: LocalTime) {
        val alarmId: Int = 1
        val alarm = AlarmEntity(
            alarmId = alarmId,
            hour = time.hour,
            minute = time.minute,
            title = "It is time to sleep",
            started = true,
            monday = binding.mondayBtn.btn.isSelected,
            tuesday = binding.tuesdayBtn.btn.isSelected,
            wednesday = binding.wednesdayBtn.btn.isSelected,
            thursday = binding.thursdayBtn.btn.isSelected,
            friday = binding.fridayBtn.btn.isSelected,
            saturday = binding.saturdayBtn.btn.isSelected,
            sunday = binding.sundayBtn.btn.isSelected
        )
        settingsViewModel.setAlarm(alarm)
        alarm.schedule(requireContext())
    }

    private fun getButtons(): List<Button> {
        return mutableListOf<Button>()
            .apply {
                add(binding.sundayBtn.btn)
                add(binding.mondayBtn.btn)
                add(binding.tuesdayBtn.btn)
                add(binding.wednesdayBtn.btn)
                add(binding.thursdayBtn.btn)
                add(binding.fridayBtn.btn)
                add(binding.saturdayBtn.btn)
            }.toList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}