package relax.deep.sleep.sounds.calm.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import relax.deep.sleep.sounds.calm.databinding.FragmentOnboarding3Binding
import relax.deep.sleep.sounds.calm.databinding.FragmentOnboarding5Binding

class OnBoarding5Fragment : Fragment() {
    private var _binding: FragmentOnboarding5Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.nextBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding5FragmentDirections.actionOnBoarding5FragmentToOnBoarding7Fragment()
            )
        }
        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.skipBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding5FragmentDirections.actionOnBoarding5FragmentToOnBoarding7Fragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}