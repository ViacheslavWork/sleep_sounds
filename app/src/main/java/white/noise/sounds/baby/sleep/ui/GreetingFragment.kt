package white.noise.sounds.baby.sleep.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import white.noise.sounds.baby.sleep.databinding.FragmentGreetingBinding


class GreetingFragment : Fragment() {

    private var _binding: FragmentGreetingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGreetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.animate()
            .setDuration(3000)
            .scaleX(1.3f)
            .scaleY(1.3f)
        lifecycleScope.launch(Dispatchers.Default) {
//            delay(3000)
            withContext(Dispatchers.Main){
                findNavController().navigate(GreetingFragmentDirections.actionGreetingFragmentToNavigationMixes())
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}