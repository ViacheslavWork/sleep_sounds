package calm.sleep.relaxing.sounds.noise.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import calm.sleep.relaxing.sounds.noise.databinding.FragmentOnboarding4Binding

class OnBoarding4Fragment : Fragment() {
    private var _binding: FragmentOnboarding4Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.yesBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding4FragmentDirections.actionOnBoarding4FragmentToOnBoarding5Fragment()
            )
        }
        binding.noBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding4FragmentDirections.actionOnBoarding4FragmentToOnBoarding5Fragment()
            )
        }
        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.skipBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding4FragmentDirections.actionOnBoarding4FragmentToOnBoarding5Fragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}