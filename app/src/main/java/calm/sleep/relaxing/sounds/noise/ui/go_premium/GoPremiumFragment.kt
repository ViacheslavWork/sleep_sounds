package calm.sleep.relaxing.sounds.noise.ui.go_premium

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SimpleOnItemTouchListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.FragmentGoPremiumBinding
import calm.sleep.relaxing.sounds.noise.subscription.Subscribable
import calm.sleep.relaxing.sounds.noise.subscription.Subscription
import calm.sleep.relaxing.sounds.noise.utils.PremiumPreferences


private const val DELAY_BETWEEN_SCROLL_MS = 25L
private const val SCROLL_DX = 5
private const val DIRECTION_RIGHT = 1

class GoPremiumFragment : Fragment() {
    private var _binding: FragmentGoPremiumBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(resources, R.drawable.gradient_liner_bg, null)
        // Inflate the layout for this fragment

        //TODO only for debugging
//        PremiumPreferences.setStoredPremiumStatus(requireContext(), true)


        _binding = FragmentGoPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setEnablingToSubscriptionButtons()
        setUpListeners()
        setUpRecyclerView()
        observeSubscriptionsPrices()
    }

    private fun setEnablingToSubscriptionButtons() {
        if (PremiumPreferences.userHasPremiumStatus(requireContext())) {
            binding.yearBtn.isEnabled = false
            binding.monthBtn.isEnabled = false
        } else {
            binding.yearBtn.isEnabled = true
            binding.monthBtn.isEnabled = true
        }
    }

    private fun observeSubscriptionsPrices() {
        (requireActivity() as Subscribable).getSubscriptionsToPricesLD()
            .observe(viewLifecycleOwner) {
                val yearPrice = it[Subscription.YEAR]
                val monthPrice = it[Subscription.MONTH]
                binding.infoTv.text = String.format(
                    resources.getString(R.string.go_premium_info),
                    yearPrice?.currency,
                    yearPrice?.price,
                    monthPrice?.currency,
                    monthPrice?.price
                )
                binding.monthBtn.text = String.format(
                    resources.getString(R.string.per_month), monthPrice?.currency, monthPrice?.price
                )
                val yearBtnText = String.format(
                    resources.getString(R.string.try_for_free_7_days),
                    yearPrice?.currency,
                    yearPrice?.price,
                )
                val cs = SpannableStringBuilder(yearBtnText)
                cs.setSpan(
                    RelativeSizeSpan(0.625f),
                    20,
                    cs.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.yearBtn.text = cs
            }
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.goPremiumRv
        recyclerView.layoutManager =
            LinearLayoutManager(context).apply { orientation = RecyclerView.HORIZONTAL }
        adapter = TickerAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(
            listOf(
                Picture(R.drawable.icon_wolf, "wolf"),
                Picture(R.drawable.icon_car, "car"),
                Picture(R.drawable.icon_concentration, "concentration"),
                Picture(R.drawable.icon_zen, "zen"),
                Picture(R.drawable.icon_heavy_rain, "heavy-rain"),
                Picture(R.drawable.icon_underwater, "icon-underwater"),

                Picture(R.drawable.icon_wolf, "wolf"),
                Picture(R.drawable.icon_car, "car"),
                Picture(R.drawable.icon_concentration, "concentration"),
                Picture(R.drawable.icon_zen, "zen"),
                Picture(R.drawable.icon_heavy_rain, "heavy-rain"),
                Picture(R.drawable.icon_underwater, "icon-underwater"),

                Picture(R.drawable.icon_wolf, "wolf"),
                Picture(R.drawable.icon_car, "car"),
                Picture(R.drawable.icon_concentration, "concentration"),
                Picture(R.drawable.icon_zen, "zen"),
                Picture(R.drawable.icon_heavy_rain, "heavy-rain"),
                Picture(R.drawable.icon_underwater, "icon-underwater"),
            )
        )

        recyclerView.addOnItemTouchListener(object : SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                // true: consume touch event
                // false: dispatch touch event
                return true
            }
        })
        lifecycleScope.launch { autoScrollFeaturesList() }
    }

    private tailrec suspend fun autoScrollFeaturesList() {
        if (recyclerView.canScrollHorizontally(DIRECTION_RIGHT)) {
            recyclerView.smoothScrollBy(SCROLL_DX, 0)
        } else {
            val firstPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (firstPosition != RecyclerView.NO_POSITION) {
                val currentList = adapter.currentList
                val secondPart = currentList.subList(0, firstPosition)
                val firstPart = currentList.subList(firstPosition, currentList.size)
                adapter.submitList(firstPart + secondPart)
            }
        }
        delay(DELAY_BETWEEN_SCROLL_MS)
        autoScrollFeaturesList()
    }

    private class DisableTouchLinearLayoutManager(context: Context?) :
        LinearLayoutManager(context) {
        private var isScrollEnabled = false
        fun setScrollEnabled(flag: Boolean) {
            isScrollEnabled = flag
        }

        override fun canScrollVertically(): Boolean {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically()
        }

        override fun canScrollHorizontally(): Boolean {
            return isScrollEnabled && super.canScrollHorizontally()
        }
    }


    private fun setUpListeners() {
        binding.crossGoPremiumToolbarIv.setOnClickListener { requireActivity().onBackPressed() }
        binding.detailsTv.setOnClickListener {
            findNavController().navigate(
                GoPremiumFragmentDirections.actionGoPremiumFragmentToDetailsFragment()
            )
        }
        binding.yearBtn.setOnClickListener {
            (requireActivity() as Subscribable).subscribe(Subscription.YEAR)
        }
        binding.monthBtn.setOnClickListener {
            (requireActivity() as Subscribable).subscribe(Subscription.MONTH)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}