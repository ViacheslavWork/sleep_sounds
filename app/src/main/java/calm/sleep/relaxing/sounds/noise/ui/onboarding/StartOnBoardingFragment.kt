package calm.sleep.relaxing.sounds.noise.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.FragmentOnboarding0StartBinding

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
        val imageView = binding.rippleIv.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.btn_animation
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}