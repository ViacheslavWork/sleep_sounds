package calm.sleep.relaxing.sounds.noise.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.FragmentSettingsBinding
import calm.sleep.relaxing.sounds.noise.utils.Constants
import calm.sleep.relaxing.sounds.noise.utils.Constants.CUSTOM_ALARM_ID
import calm.sleep.relaxing.sounds.noise.utils.EveryDayAlarmManager
import calm.sleep.relaxing.sounds.noise.utils.ToastHelper

class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by sharedViewModel()
    private val everyDayAlarmManager: EveryDayAlarmManager by inject()
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(resources, R.drawable.gradient_liner_bg, null)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.versionTv.text =
            String.format(resources.getString(R.string.version), Constants.CURRENT_VERSION)
        setSwitchAccessibility()
        setListeners()
        setSwitchStatus()
    }

    private fun setSwitchAccessibility() {
        lifecycleScope.launch {
            val alarm = async { settingsViewModel.getAlarm(CUSTOM_ALARM_ID) }
            alarm.await()?.let { binding.bedTimeReminderSwitch.isEnabled = true }
        }
    }

    private fun setListeners() {
        binding.startTrialBtn.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionNavigationSettingsToGoPremiumFragment()
            )
        }
        binding.bedTimeReminderTv.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionNavigationSettingsToBedtimeReminderFragment()
            )
        }
        binding.feedbackTv.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionNavigationSettingsToRatingDialog()
            )
        }
        binding.privacyPolicyTv.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionNavigationSettingsToPrivacyPolicyFragment()
            )
        }
        binding.bedTimeReminderSwitch
            .setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    lifecycleScope.launch {
                        val alarm = settingsViewModel.getAlarm(CUSTOM_ALARM_ID)
                        alarm?.let {
                            withContext(Dispatchers.Main) {
                                binding.bedTimeReminderSwitch.text =
                                    String.format(
                                        getString(R.string.time_format_short),
                                        alarm.hour,
                                        alarm.minute
                                    )
                            }
                            if (!alarm.started) {
                                alarm.schedule(requireContext())
                                everyDayAlarmManager.cancelEveryDayAlarm()
                                settingsViewModel.setAlarm(alarm)
                                ToastHelper.showCustomToast(requireActivity())
                            }
                        }
                    }
                } else {
                    lifecycleScope.launch {
                        val alarm = settingsViewModel.getAlarm(CUSTOM_ALARM_ID)
                        alarm?.let {
                            it.cancelAlarm(requireContext())
                            settingsViewModel.setAlarm(alarm)
                            everyDayAlarmManager.scheduleEveryDayAlarm()
                        }
                    }
                    binding.bedTimeReminderSwitch.text = ""
                }
            }
    }

    private fun setSwitchStatus() {
        lifecycleScope.launch {
            val alarm = settingsViewModel.getAlarm(CUSTOM_ALARM_ID)
            alarm?.let {
                withContext(Dispatchers.Main) {
                    binding.bedTimeReminderSwitch.isChecked = alarm.started
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}