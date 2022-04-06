package relax.deep.sleep.OnBoardingNeedsItems.calm.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.ItemOnboardingNeedsBtnBinding
import calm.sleep.relaxing.sounds.noise.ui.onboarding.onboarding_5.OnBoardingNeedsItem
import calm.sleep.relaxing.sounds.noise.ui.onboarding.onboarding_5.OnBoardingNeedsItemsEvent


class OnBoardingNeedsAdapter(
    val event: MutableLiveData<OnBoardingNeedsItemsEvent> = MutableLiveData(),
) : ListAdapter<OnBoardingNeedsItem, OnBoardingNeedsViewHolder>(OnBoardingNeedsItemDiffCallback()) {

    lateinit var binding: ItemOnboardingNeedsBtnBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingNeedsViewHolder {
        binding =
            ItemOnboardingNeedsBtnBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return OnBoardingNeedsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingNeedsViewHolder, position: Int) {
        getItem(position).let { OnBoardingNeedsItem -> holder.onBind(OnBoardingNeedsItem, event) }
    }
}

class OnBoardingNeedsViewHolder(private val binding: ItemOnboardingNeedsBtnBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        item: OnBoardingNeedsItem,
        event: MutableLiveData<OnBoardingNeedsItemsEvent>
    ) {
        binding.pictureIv.setImageResource(item.image)
        binding.titleTv.text = item.title
        binding.root.setOnClickListener {
            item.apply { isChecked = !isChecked }
                .also {
                    if (it.isChecked) {
                        binding.checkIv.visibility = View.VISIBLE
                        binding.root.animate()
                            .setDuration(100)
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                        binding.background.background = ResourcesCompat.getDrawable(
                            binding.root.context.resources,
                            R.drawable.gradient_onboarding_item_selected,
                            null
                        )
                    } else {
                        binding.checkIv.visibility = View.INVISIBLE
                        binding.root.animate()
                            .setDuration(100)
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                        binding.background.background = ResourcesCompat.getDrawable(
                            binding.root.context.resources,
                            R.drawable.gradient_onboarding_item,
                            null
                        )
                    }
                }
                .also { event.postValue(OnBoardingNeedsItemsEvent.OnClick(it)) }
        }
    }
}

private class OnBoardingNeedsItemDiffCallback : DiffUtil.ItemCallback<OnBoardingNeedsItem>() {
    override fun areItemsTheSame(
        oldItem: OnBoardingNeedsItem,
        newItem: OnBoardingNeedsItem
    ): Boolean =
        (oldItem.title == newItem.title)

    override fun areContentsTheSame(
        oldItem: OnBoardingNeedsItem,
        newItem: OnBoardingNeedsItem
    ): Boolean =
        (oldItem == newItem)
}
