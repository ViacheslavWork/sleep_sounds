package relax.deep.sleep.sounds.calm.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import relax.deep.sleep.sounds.calm.databinding.FragmentOnboarding1Binding

class OnBoarding1Fragment : Fragment() {
    private var _binding: FragmentOnboarding1Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.nextBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding1FragmentDirections.actionOnBoarding1FragmentToOnBoarding2Fragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}