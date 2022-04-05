package calm.sleep.relaxing.sounds.noise.ui.mixes.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import calm.sleep.relaxing.sounds.noise.model.MixCategory
import calm.sleep.relaxing.sounds.noise.ui.mixes.PagerFragment
import calm.sleep.relaxing.sounds.noise.ui.mixes.PagerFragment.Companion.ARG_CATEGORY

class ViewPagerAdapter(fragment: Fragment, private val categories: List<MixCategory>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        val pagerFragment = PagerFragment()
        pagerFragment.arguments = Bundle().apply {
            putSerializable(ARG_CATEGORY, categories[position])
        }
        return pagerFragment
    }
}