package white.noise.sounds.baby.sleep.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentAdditionalSoundsBinding
import white.noise.sounds.baby.sleep.databinding.FragmentSoundsBinding
import white.noise.sounds.baby.sleep.ui.sounds.SectionAdapter
import white.noise.sounds.baby.sleep.ui.sounds.SoundsViewModel


class AdditionalSoundsFragment : Fragment() {
    private val soundsViewModel: SoundsViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SectionAdapter
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
        setUpRecyclerView()
        setUpAdapter()

        observeSounds()

        soundsViewModel.loadAllSounds()
    }

    private fun observeSounds() {
        soundsViewModel.sounds.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.soundsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpAdapter() {
        adapter = SectionAdapter()
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        adapter.event.observe(viewLifecycleOwner) {
            TODO()
        }
        recyclerView.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}