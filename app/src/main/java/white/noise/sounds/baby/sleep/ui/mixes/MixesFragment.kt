package white.noise.sounds.baby.sleep.ui.mixes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import white.noise.sounds.baby.sleep.databinding.FragmentMixesBinding
import white.noise.sounds.baby.sleep.ui.mixes.adapters.CategoryAdapter
import white.noise.sounds.baby.sleep.ui.mixes.adapters.MixesAdapter

class MixesFragment : Fragment() {
    private val mixesViewModel: MixesViewModel by viewModel()
    private var _binding: FragmentMixesBinding? = null
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var mixesAdapter: MixesAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMixesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpCategoryRecyclerView()
        setUpCategoryAdapter()

        setUpMixesRecyclerView()
        setUpMixesAdapter()

        setUpListeners()

        observeCategories()
        observeMixes()
        observeAdaptersEvents()
    }

    private fun observeAdaptersEvents() {
        /* mixesViewModel.currentCategory.observe(viewLifecycleOwner) {

         }
         mixesViewModel.currentMix.observe(viewLifecycleOwner) {

         }*/
        categoryAdapter.event.observe(viewLifecycleOwner) {
            mixesViewModel.handleEvent(it)
            binding.mixCategoryRv.smoothScrollToPosition(it.position)

        }
        mixesAdapter.event.observe(viewLifecycleOwner) {
            findNavController().navigate(MixesFragmentDirections.actionNavigationMixesToPlayerFragment())
            mixesViewModel.handleEvent(it)
        }

    }

    private fun setUpListeners() {
        binding.crownMixToolbarIv.setOnClickListener {
            findNavController().navigate(
                MixesFragmentDirections.actionNavigationMixesToUnlockForFreeFragment()
            )
        }
    }

    private fun observeMixes() {
        mixesViewModel.mixes.observe(viewLifecycleOwner) {
            mixesAdapter.submitList(it.toMutableList())
        }
    }

    private fun observeCategories() {
        mixesViewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.submitList(
                it.map { selectableMixCategory -> selectableMixCategory.copy() })

        }
    }

    private fun setUpCategoryRecyclerView() {
        binding.mixCategoryRv.layoutManager =
            CenterLayoutManager(context = requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }
    }

    private fun setUpCategoryAdapter() {
        categoryAdapter = CategoryAdapter()
        categoryAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.mixCategoryRv.adapter = categoryAdapter
    }

    private fun setUpMixesRecyclerView() {
        binding.mixRv.layoutManager = GridLayoutManager(context, 2)
    }

    private fun setUpMixesAdapter() {
        mixesAdapter = MixesAdapter()
        mixesAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.mixRv.adapter = mixesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}