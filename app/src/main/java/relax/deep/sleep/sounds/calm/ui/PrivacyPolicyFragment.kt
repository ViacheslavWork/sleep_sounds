package relax.deep.sleep.sounds.calm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import relax.deep.sleep.sounds.calm.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicyFragment : Fragment() {


    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backPrivacyPolicyToolbarIv.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}