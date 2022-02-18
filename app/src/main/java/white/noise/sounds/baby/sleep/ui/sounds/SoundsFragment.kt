package white.noise.sounds.baby.sleep.ui.sounds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.databinding.FragmentSoundsBinding

class SoundsFragment : Fragment() {
    private val TAG = "SoundsFragment"
    private val soundsViewModel: SoundsViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SectionAdapter

    private var _binding: FragmentSoundsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoundsBinding.inflate(inflater, container, false)
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
            it.forEach { sound -> showLog(sound.toString()) }
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

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}