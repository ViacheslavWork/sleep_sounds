package relax.deep.sleep.sounds.calm.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import relax.deep.sleep.sounds.calm.databinding.FragmentOnboarding2Binding

class OnBoarding2Fragment : Fragment() {
    private var _binding: FragmentOnboarding2Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.nextBtn.setOnClickListener {
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