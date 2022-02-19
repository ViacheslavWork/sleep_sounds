package white.noise.sounds.baby.sleep.ui.mix_sounds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentAdditionalSoundsBinding
import white.noise.sounds.baby.sleep.ui.sounds.SectionAdapter
import white.noise.sounds.baby.sleep.ui.sounds.SoundAdapter


private const val TAG = "AdditionalSoundsFragment"

class AdditionalSoundsFragment : Fragment() {
    private val soundsViewModel: AdditionalSoundsViewModel by viewModel()

    private lateinit var sectionRecyclerView: RecyclerView
    private lateinit var sectionAdapter: SectionAdapter

    private lateinit var selectedSoundsRecyclerView: RecyclerView
    private lateinit var selectedSoundsAdapter: SoundAdapter

    private var _binding: FragmentAdditionalSoundsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAdditionalSoundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpSectionRecyclerView()
        setUpSectionAdapter()

        setUpSelectedSoundsRecyclerView()
        setUpSelectedSoundsAdapter()

        setUpListeners()

        observeSounds()
        observeSelection()
    }

    private fun setUpListeners() {
        binding.crownToolbarIv.setOnClickListener {
            findNavController().navigate(
                AdditionalSoundsFragmentDirections
                    .actionAdditionalSoundsFragmentToGoPremiumFragment()
            )
        }
    }

    private fun observeSelection() {
        soundsViewModel.selectedSounds.observe(viewLifecycleOwner) {
            selectedSoundsAdapter.submitList(it.toMutableList())
        }
    }

    private fun observeSounds() {
        soundsViewModel.sounds.observe(viewLifecycleOwner) {
            sectionAdapter.submitList(it.toMutableList())
        }
    }

    private fun setUpSelectedSoundsRecyclerView() {
        selectedSoundsRecyclerView = binding.selectedSoundsRv
        selectedSoundsRecyclerView.layoutManager =
            LinearLayoutManager(context).apply { orientation = RecyclerView.HORIZONTAL }
    }

    private fun setUpSelectedSoundsAdapter() {
        selectedSoundsAdapter = SoundAdapter(
            MutableLiveData(),
            isClosable = true,
            isSoundChangeable = true,
            background = ResourcesCompat.getDrawable(resources, R.drawable.gradient_liner_bg5, null)
        )
        selectedSoundsAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        selectedSoundsAdapter.event.observe(viewLifecycleOwner) {
            soundsViewModel.handleEvent(it)
        }
        selectedSoundsRecyclerView.adapter = selectedSoundsAdapter
    }

    private fun setUpSectionRecyclerView() {
        sectionRecyclerView = binding.soundsRv
        sectionRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpSectionAdapter() {
        sectionAdapter = SectionAdapter()
        sectionAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        sectionAdapter.event.observe(viewLifecycleOwner) {
            soundsViewModel.handleEvent(it)
        }
        sectionRecyclerView.adapter = sectionAdapter
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