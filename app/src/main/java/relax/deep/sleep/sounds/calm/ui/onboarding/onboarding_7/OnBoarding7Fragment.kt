package relax.deep.sleep.sounds.calm.ui.onboarding.onboarding_7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.databinding.FragmentOnboarding7Binding
import relax.deep.sleep.sounds.calm.ui.onboarding.onboarding_5.OnBoardingNeedsItemsEvent

class OnBoarding7Fragment : Fragment() {
    private var _binding: FragmentOnboarding7Binding? = null
    private val binding get() = _binding!!
    private val mapTitleToCheckedItem = mutableMapOf<String, OnBoarding7Item>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OnBoarding7Adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding7Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpBtnListeners()
        setUpAdapter()
        setUpRecyclerView()
        updateItems()
        listenRecyclerEvent()
    }

    private fun listenRecyclerEvent() {
        adapter.event.observe(viewLifecycleOwner) {
            if (it is OnBoarding7Event.OnClick) {
                if (it.item.isChecked) {
                    binding.nextBtnGroup.visibility = View.VISIBLE
                    mapTitleToCheckedItem[it.item.title] = it.item
                } else {
                    mapTitleToCheckedItem.remove(it.item.title)
                    if (mapTitleToCheckedItem.isEmpty()) {
                        binding.nextBtnGroup.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun updateItems() {
        adapter.submitList(getItems())
    }

    private fun getItems(): List<OnBoarding7Item> {
        return listOf<OnBoarding7Item>(
            OnBoarding7Item(
                requireContext().resources.getString(R.string.nature_sounds),
                R.drawable.icon_forest
            ),

            OnBoarding7Item(
                requireContext().resources.getString(R.string.animal_sounds),
                R.drawable.icon_whale
            ),

            OnBoarding7Item(
                requireContext().resources.getString(R.string.rain_sounds),
                R.drawable.icon_heavy_rain
            ),

            OnBoarding7Item(
                requireContext().resources.getString(R.string.relax_sounds),
                R.drawable.ic_flower
            ),

            OnBoarding7Item(
                requireContext().resources.getString(R.string.all_sounds),
                R.drawable.ic_dynamic
            ),
        )
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setUpAdapter() {
        adapter = OnBoarding7Adapter()
    }


    private fun setUpBtnListeners() {
        binding.nextBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding7FragmentDirections.actionOnBoarding7FragmentToGoPremiumFragment()
            )
        }
        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.skipBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding7FragmentDirections.actionOnBoarding7FragmentToGoPremiumFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}