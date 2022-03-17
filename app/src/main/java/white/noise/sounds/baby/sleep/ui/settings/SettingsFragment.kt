package white.noise.sounds.baby.sleep.ui.settings

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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentSettingsBinding
import white.noise.sounds.baby.sleep.utils.ToastHelper

class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by sharedViewModel()

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
        /*requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(
                resources, R.drawable.gradient_liner_bg, null
            )*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.versionTv.text = String.format(resources.getString(R.string.version), "1.1")
        setListeners()
        setSwitchStatus()
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
                        val alarm = settingsViewModel.getAlarm(1)
                        alarm?.let {
                            withContext(Dispatchers.Main) {
                                binding.bedTimeReminderSwitch.text =
                                    String.format(
                                        getString(R.string.time_format),
                                        alarm.hour,
                                        alarm.minute
                                    )
                            }
                            if (!alarm.started) {
                                alarm.schedule(requireContext())
                                settingsViewModel.setAlarm(alarm)
                                ToastHelper.showCustomToast(requireActivity())
                            }
                        }
                    }
                } else {
                    lifecycleScope.launch {
                        val alarm = settingsViewModel.getAlarm(1)
                        alarm?.let {
                            it.cancelAlarm(requireContext())
                            settingsViewModel.setAlarm(alarm)
                        }
                    }
                    binding.bedTimeReminderSwitch.text = ""
                }
            }
    }

    private fun setSwitchStatus() {
        lifecycleScope.launch {
            val alarm = settingsViewModel.getAlarm(1)
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