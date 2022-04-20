package calm.sleep.relaxing.sounds.noise.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.FragmentGreetingBinding
import calm.sleep.relaxing.sounds.noise.utils.Constants
import calm.sleep.relaxing.sounds.noise.utils.FirstRunPreferences
import calm.sleep.relaxing.sounds.noise.utils.MyLog.showLog
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "GreetingFragment"

class GreetingFragment : Fragment() {
    private var _binding: FragmentGreetingBinding? = null
    private val binding get() = _binding!!
    private var isOnBoarding = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        _binding = FragmentGreetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fetchIsOnBoardingFromFirebaseRemoteConfig()
            startSplashAnimation()
            lifecycleScope.launch(Dispatchers.Default) {
                delay(3500)
                withContext(Dispatchers.Main) {
                    if (
                        FirstRunPreferences.isFirstRun(requireContext()) &&
                        isOnBoarding
                    ) {
                        FirstRunPreferences.setIsNotFirstRun(requireContext())
                        findNavController().navigate(GreetingFragmentDirections.actionGreetingFragmentToStartOnBoardingFragment())
                    } else {
                        findNavController().navigate(R.id.action_global_to_mixFragment)
                    }
                }
            }
        }
    }

    private fun startSplashAnimation() {
        binding.root.animate()
            .setDuration(3000)
            .scaleX(1.3f)
            .scaleY(1.3f)
    }

    private fun fetchIsOnBoardingFromFirebaseRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(Constants.IS_ON_BOARDING_KEY to true))
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    isOnBoarding = remoteConfig.getBoolean(Constants.IS_ON_BOARDING_KEY)
                    showLog(
                        "fetchIsOnBoardingFromFirebaseRemoteConfig: ${remoteConfig.getBoolean("is_onboarding")}",
                        TAG
                    )
                }
            }
    }

    override fun onDestroyView() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        super.onDestroyView()
        _binding = null
    }
}