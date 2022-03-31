package relax.deep.sleep.sounds.calm.ui.mixes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import relax.deep.sleep.sounds.calm.BuildConfig
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.databinding.FragmentPagerBinding
import relax.deep.sleep.sounds.calm.model.Mix
import relax.deep.sleep.sounds.calm.model.MixCategory
import relax.deep.sleep.sounds.calm.service.PlayerService
import relax.deep.sleep.sounds.calm.ui.dialogs.UnlockForFreeDialog
import relax.deep.sleep.sounds.calm.ui.mixes.adapters.MixesAdapter
import relax.deep.sleep.sounds.calm.ui.player.PlayerFragment
import relax.deep.sleep.sounds.calm.utils.Constants
import relax.deep.sleep.sounds.calm.utils.PremiumPreferences


class PagerFragment : Fragment() {
    companion object {
        const val ARG_CATEGORY = "category"
        private const val TAG = "PagerFragment"
    }

    private val mixesViewModel: MixesViewModel by sharedViewModel()
    private var _binding: FragmentPagerBinding? = null
    private lateinit var mixesAdapter: MixesAdapter
    private var category: MixCategory? = null
    private lateinit var lastClickedMix: Mix

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
            mixesViewModel.handleEvent(it)
            when (it) {
                is MixesEvent.OnMixClick -> {
                    lastClickedMix = it.mix
                    if (!it.mix.isPremium
                        || PremiumPreferences.userHasPremiumStatus(requireContext())
                    ) {
                        findNavController().navigate(
                            R.id.action_navigation_mixes_to_playerFragment,
                            bundleOf(
                                PlayerFragment.mixIdKey to it.mix.id,
                                PlayerFragment.isStartPlayingArg to true
                            )
                        )
                    } else {
                        findNavController().navigate(
                            R.id.action_navigation_mixes_to_unlockForFreeFragment,
                            bundleOf(UnlockForFreeDialog.mixKey to it.mix)
                        )
                    }
                }
                is MixesEvent.OnDeleteMixClick -> {
                    if (PlayerService.launcher == Constants.MIX_LAUNCHER
                        && PlayerService.currentMixId == it.mix.id
                    ) {
                        sendCommandToPlayerService(Constants.ACTION_STOP_SERVICE)
                    }
                }

                else -> {}
            }

        }
    }

    private fun sendCommandToPlayerService(action: String) {
        Intent(requireContext(), PlayerService::class.java).also {
            it.putExtra(Constants.LAUNCHER, Constants.MIX_LAUNCHER)
            it.action = action
            requireContext().startService(it)
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