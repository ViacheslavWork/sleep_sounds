package white.noise.sounds.baby.sleep.ui.mixes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentMixesBinding
import white.noise.sounds.baby.sleep.databinding.ItemMixCategoryBinding
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.ui.mixes.adapters.ViewPagerAdapter
import white.noise.sounds.baby.sleep.ui.player.PlayerFragment
import white.noise.sounds.baby.sleep.utils.Constants

private const val TAG = "MixesFragment"

class MixesFragment : Fragment() {
    private val mixesViewModel: MixesViewModel by sharedViewModel()
    private var _binding: FragmentMixesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(resources, R.drawable.background, null)
        _binding = FragmentMixesBinding.inflate(inflater, container, false)
        setUpViewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
        observeTimer()
        setUpPlayerView()
    }

    override fun onResume() {
        startAdAnimation()
        super.onResume()
    }

    override fun onPause() {
        binding.crownMixToolbarIv.clearAnimation()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observePlayerService() {
        PlayerService.isPause.observe(viewLifecycleOwner) {
            if (it) {
                binding.playerPlayPauseBtn.btn.setImageResource(R.drawable.ic_icn_play)
            } else {
                binding.playerPlayPauseBtn.btn.setImageResource(R.drawable.icn_pause)
            }
        }
        PlayerService.isPlayable.observe(viewLifecycleOwner) {
            if (it && PlayerService.launcher == Constants.MIX_LAUNCHER) {
                binding.playerGroup.visibility = View.VISIBLE
            } else {
                binding.playerGroup.visibility = View.GONE
            }
        }
    }

    private fun observeTimer() {
        PlayerService.isTimerRunning.observe(viewLifecycleOwner) {
            if (it) {
                PlayerService.timerTime.observe(viewLifecycleOwner) { timerTime ->
                    binding.playerTimeTv.text = timerTime.toString()
                }
            } else {
                binding.playerTimeTv.text = getString(R.string.timer)
            }
        }
    }

    private fun setUpPlayerView() {
        observePlayerService()
        binding.playerView.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong(PlayerFragment.mixIdKey, PlayerService.currentMixId)
            bundle.putBoolean(PlayerFragment.isStartPlayingArg, false)
            findNavController().navigate(R.id.action_navigation_mixes_to_playerFragment, bundle)
        }
        binding.playerPlayPauseBtn.root.setOnClickListener {
            sendCommandToPlayerService(Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS, null)
        }
        binding.playerPlayPauseBtn.btn.setOnClickListener {
            sendCommandToPlayerService(Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS, null)
        }
        binding.playerCrossIb.setOnClickListener {
            sendCommandToPlayerService(Constants.ACTION_STOP_SERVICE, null)
        }
        if (PlayerService.currentMixId >= 0) {
            mixesViewModel.getMixLD(PlayerService.currentMixId).observe(viewLifecycleOwner) {
                binding.playerMixNameTv.text = it.title
            }
        }
    }

    private fun setUpViewPager() {
        mixesViewModel.categories.observe(viewLifecycleOwner) {
            val viewPager: ViewPager2 = binding.mixVp
            viewPager.adapter = ViewPagerAdapter(this, categories = it)

            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.view?.findViewById<Button>(R.id.category_btn)
                        ?.setBackgroundColor(resources.getColor(R.color.blue40))
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.view?.findViewById<Button>(R.id.category_btn)
                        ?.setBackgroundColor(Color.TRANSPARENT)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
                tab.text = it[position].toString()
                val itemMixCategoryBinding =
                    ItemMixCategoryBinding.inflate(LayoutInflater.from(context))
                itemMixCategoryBinding.categoryBtn.text = it[position].title
                itemMixCategoryBinding.categoryBtn.setOnClickListener { tab.select() }
                tab.customView = itemMixCategoryBinding.root
            }.attach()
        }
    }

    private fun setUpListeners() {
        binding.crownMixToolbarIv.setOnClickListener {
            findNavController().navigate(
                MixesFragmentDirections.actionNavigationMixesToUnlockForFreeFragment()
            )
        }
    }

    private fun sendCommandToPlayerService(action: String, mix: Mix?) {
        Intent(requireContext(), PlayerService::class.java).also {
            it.putExtra(Constants.LAUNCHER, Constants.MIX_LAUNCHER)
            it.putExtra(Constants.EXTRA_MIX, mix)
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun startAdAnimation() {
        binding.crownMixToolbarIv.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.ad_animation
            )
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}