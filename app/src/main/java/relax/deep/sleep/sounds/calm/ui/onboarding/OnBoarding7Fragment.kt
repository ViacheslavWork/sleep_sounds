package relax.deep.sleep.sounds.calm.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.databinding.FragmentOnboarding7Binding

class OnBoarding7Fragment : Fragment() {
    private var _binding: FragmentOnboarding7Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding7Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpBtnListeners()
        setUpItemsListeners()
    }

    private fun setUpItemsListeners() {
        binding.natureSoundsBtn.root.setOnClickListener {
            it.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.gradient_onboarding_item_selected,
                null
            )
            it.animate()
                .setDuration(100)
                .scaleX(0.95f)
                .scaleY(0.95f)
        }
    }

    private fun setUpBtnListeners() {
        binding.nextBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding7FragmentDirections.actionOnBoarding7FragmentToGoPremiumFragment()
            )
        }
        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.skipBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding7FragmentDirections.actionOnBoarding7FragmentToGoPremiumFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}