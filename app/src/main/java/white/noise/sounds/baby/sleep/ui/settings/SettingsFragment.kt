package white.noise.sounds.baby.sleep.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import white.noise.sounds.baby.sleep.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        /*requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(
                resources, R.drawable.gradient_liner_bg, null
            )*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
                    binding.bedTimeReminderSwitch.text = "00:00"
                } else {
                    binding.bedTimeReminderSwitch.text = ""
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}