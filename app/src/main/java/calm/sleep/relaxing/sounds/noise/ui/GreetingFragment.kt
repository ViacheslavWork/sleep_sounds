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
import calm.sleep.relaxing.sounds.noise.utils.FirstRunPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GreetingFragment : Fragment() {
    private var _binding: FragmentGreetingBinding? = null
    private val binding get() = _binding!!

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
            if (FirstRunPreferences.isFirstRun(requireContext())) {
                FirstRunPreferences.setIsNotFirstRun(requireContext())
                findNavController().navigate(GreetingFragmentDirections.actionGreetingFragmentToStartOnBoardingFragment())
            } else {
                binding.root.animate()
                    .setDuration(3000)
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                lifecycleScope.launch(Dispatchers.Default) {
                    delay(3500)
                    withContext(Dispatchers.Main) {
                        findNavController().navigate(R.id.action_global_to_mixFragment)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        super.onDestroyView()
        _binding = null
    }
}