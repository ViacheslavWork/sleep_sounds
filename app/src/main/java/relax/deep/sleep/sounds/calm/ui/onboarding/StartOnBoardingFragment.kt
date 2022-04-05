package relax.deep.sleep.sounds.calm.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import relax.deep.sleep.sounds.calm.databinding.FragmentOnboarding0StartBinding

class StartOnBoardingFragment : Fragment() {
    private var _binding: FragmentOnboarding0StartBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding0StartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.getStartedBtn.setOnClickListener {
            findNavController().navigate(
                StartOnBoardingFragmentDirections.actionStartOnBoardingFragmentToOnBoarding1Fragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}