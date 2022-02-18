package white.noise.sounds.baby.sleep.ui.mixes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.databinding.FragmentPagerBinding
import white.noise.sounds.baby.sleep.model.MixCategory
import white.noise.sounds.baby.sleep.ui.mixes.adapters.MixesAdapter

const val ARG_CATEGORY = "category"
private const val TAG = "PagerFragment"

class PagerFragment : Fragment() {
    private val mixesViewModel: MixesViewModel by sharedViewModel()
    private var _binding: FragmentPagerBinding? = null
    private lateinit var mixesAdapter: MixesAdapter
    private var category: MixCategory? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { bundle -> bundle.containsKey(ARG_CATEGORY) }
            .apply { category = arguments?.getSerializable(ARG_CATEGORY) as MixCategory? }

        setUpMixesRecyclerView()
        setUpMixesAdapter()

        observeMixes()
        observeAdaptersEvents()
    }

    private fun setUpMixesAdapter() {
        mixesAdapter = MixesAdapter()
        mixesAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.mixRv.adapter = mixesAdapter
    }

    private fun observeAdaptersEvents() {
        mixesAdapter.event.observe(viewLifecycleOwner) {
            findNavController().navigate(MixesFragmentDirections.actionNavigationMixesToPlayerFragment())
            mixesViewModel.handleEvent(it)
        }
    }

    private fun setUpMixesRecyclerView() {
        binding.mixRv.layoutManager = GridLayoutManager(context, 2)
    }

    private fun observeMixes() {
        mixesViewModel.mixes.observe(viewLifecycleOwner) { mixes ->
            if (category != MixCategory.AllSounds) {
                mixesAdapter.submitList(mixes.filter { mix -> mix.category == category })
            } else mixesAdapter.submitList(mixes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }


}