package white.noise.sounds.baby.sleep.ui.mixes.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import white.noise.sounds.baby.sleep.model.MixCategory
import white.noise.sounds.baby.sleep.ui.mixes.ARG_CATEGORY
import white.noise.sounds.baby.sleep.ui.mixes.PagerFragment

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