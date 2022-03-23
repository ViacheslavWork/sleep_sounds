package relax.deep.sleep.sounds.calm.ui.mixes.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import relax.deep.sleep.sounds.calm.model.MixCategory
import relax.deep.sleep.sounds.calm.ui.mixes.PagerFragment
import relax.deep.sleep.sounds.calm.ui.mixes.PagerFragment.Companion.ARG_CATEGORY

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