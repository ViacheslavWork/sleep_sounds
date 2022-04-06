package calm.sleep.relaxing.sounds.noise.ui.onboarding.onboarding_5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import relax.deep.sleep.OnBoardingNeedsItems.calm.ui.onboarding.OnBoardingNeedsAdapter
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.FragmentOnboarding5Binding

class OnBoarding5Fragment : Fragment() {
    private var _binding: FragmentOnboarding5Binding? = null
    private val binding get() = _binding!!
    private val mapTitleToCheckedItem = mutableMapOf<String, OnBoardingNeedsItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OnBoardingNeedsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
        setUpAdapter()
        setUpRecyclerView()
        updateItems()
        listenRecyclerEvent()
    }

    override fun onStop() {
        super.onStop()
        mapTitleToCheckedItem.clear()
    }

    private fun listenRecyclerEvent() {
        adapter.event.observe(viewLifecycleOwner){
            if (it is OnBoardingNeedsItemsEvent.OnClick) {
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
        adapter.submitList(getItems().toMutableList())
    }

    private fun getItems(): List<OnBoardingNeedsItem> {
        return listOf(
            OnBoardingNeedsItem(
                requireContext().resources.getString(R.string.fall_asleep_faster),
                R.drawable.onboarding_needs_picture_fall_asleep_faster
            ),
            OnBoardingNeedsItem(
                requireContext().resources.getString(R.string.sleep_all_night),
                R.drawable.onboarding_needs_picture_sleep_all_night
            ),
            OnBoardingNeedsItem(
                requireContext().resources.getString(R.string.relax_unwind),
                R.drawable.onboarding_needs_picture_relax_unwind
            ),
            OnBoardingNeedsItem(
                requireContext().resources.getString(R.string.manage_tinnitus),
                R.drawable.onboarding_needs_picture_manage_t
            ),
            OnBoardingNeedsItem(
                requireContext().resources.getString(R.string.to_help_children_sleep),
                R.drawable.onboarding_needs_picture_help_children
            ),
            OnBoardingNeedsItem(
                requireContext().resources.getString(R.string.coping_with_stress),
                R.drawable.onboarding_needs_picture_stress
            ),
        )
    }

    private fun setUpAdapter() {
        adapter = OnBoardingNeedsAdapter()
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.rv
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
    }

    private fun setUpListeners() {
        binding.nextBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding5FragmentDirections.actionOnBoarding5FragmentToOnBoarding7Fragment()
            )
        }
        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
        binding.skipBtn.setOnClickListener {
            findNavController().navigate(
                OnBoarding5FragmentDirections.actionOnBoarding5FragmentToOnBoarding7Fragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}