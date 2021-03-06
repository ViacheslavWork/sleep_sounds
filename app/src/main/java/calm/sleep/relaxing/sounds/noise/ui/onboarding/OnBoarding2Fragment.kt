package calm.sleep.relaxing.sounds.noise.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.FragmentOnboarding2Binding
import calm.sleep.relaxing.sounds.noise.utils.Constants
import calm.sleep.relaxing.sounds.noise.utils.EveryDayAlarmManager
import calm.sleep.relaxing.sounds.noise.utils.MyLog.showLog
import calm.sleep.relaxing.sounds.noise.utils.ToastHelper

private const val TAG = "OnBoarding2Fragment"

class OnBoarding2Fragment : Fragment() {
    private var _binding: FragmentOnboarding2Binding? = null
    private val binding get() = _binding!!
    private val alarmManager: EveryDayAlarmManager by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
        binding.timePicker.setInitialSelectedTime("22:40 Pm")
        showLog("onViewCreated: ${binding.timePicker.getCurrentlySelectedTime()}")
    }

    private fun setUpListeners() {
        binding.nextBtn.setOnClickListener {
            val selectedTime = binding.timePicker.getCurrentlySelectedTime()
                .filter { !it.isWhitespace() }
                .split(":")
                .map { it.toInt() }
            val hour = selectedTime[0]
            val minute = selectedTime[1]
            alarmManager.scheduleCustomEveryDayAlarm(
                hour,
                minute,
                Constants.CUSTOM_ALARM_ID,
                requireContext().resources.getString(R.string.custom_alarm_notification_title)
            )

            ToastHelper.showSetSuccessfullyToast(requireActivity())

            findNavController().navigate(
                OnBoarding2FragmentDirections.actionOnBoarding2FragmentToOnBoarding3Fragment()
            )
        }
        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.skipBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding2FragmentDirections.actionOnBoarding2FragmentToOnBoarding3Fragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}