package calm.sleep.relaxing.sounds.noise.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import calm.sleep.relaxing.sounds.noise.databinding.FragmentOnboarding3Binding

class OnBoarding3Fragment : Fragment() {
    private var _binding: FragmentOnboarding3Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
        setUpInitialTime()
    }

    private fun setUpInitialTime() {
        binding.timePicker.setInitialSelectedTime("08:25 Pm")
    }

    private fun setUpListeners() {
        binding.nextBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding3FragmentDirections.actionOnBoarding3FragmentToOnBoarding4Fragment()
            )
        }
        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.skipBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding3FragmentDirections.actionOnBoarding3FragmentToOnBoarding4Fragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}