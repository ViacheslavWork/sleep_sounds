package white.noise.sounds.baby.sleep.ui.go_premium

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SimpleOnItemTouchListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentGoPremiumBinding


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
        // Inflate the layout for this fragment
        _binding = FragmentGoPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        recyclerView = binding.goPremiumRv
        recyclerView.layoutManager =
            LinearLayoutManager(context).apply { orientation = RecyclerView.HORIZONTAL }
        adapter = TickerAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(
            listOf(
                Picture(R.drawable.icon_wolf,"wolf"),
                Picture(R.drawable.icon_car,"car"),
                Picture(R.drawable.icon_concentration,"concentration"),
                Picture(R.drawable.icon_zen,"zen"),
                Picture(R.drawable.icon_heavy_rain,"heavy-rain"),
                Picture(R.drawable.icon_underwater,"icon-underwater"),

                Picture(R.drawable.icon_wolf,"wolf"),
                Picture(R.drawable.icon_car,"car"),
                Picture(R.drawable.icon_concentration,"concentration"),
                Picture(R.drawable.icon_zen,"zen"),
                Picture(R.drawable.icon_heavy_rain,"heavy-rain"),
                Picture(R.drawable.icon_underwater,"icon-underwater"),

                Picture(R.drawable.icon_wolf,"wolf"),
                Picture(R.drawable.icon_car,"car"),
                Picture(R.drawable.icon_concentration,"concentration"),
                Picture(R.drawable.icon_zen,"zen"),
                Picture(R.drawable.icon_heavy_rain,"heavy-rain"),
                Picture(R.drawable.icon_underwater,"icon-underwater"),
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

    private class DisableTouchLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {
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
        binding.yearBtn.setOnClickListener { Toast.makeText(context,"Year subscription",Toast.LENGTH_SHORT).show() }
        binding.monthBtn.setOnClickListener { Toast.makeText(context,"Month subscription",Toast.LENGTH_SHORT).show() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}