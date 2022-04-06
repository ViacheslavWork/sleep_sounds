package relax.deep.sleep.sounds.calm.ui.onboarding.onboarding_7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.databinding.ItemOnboardingBtnBinding

class OnBoarding7Adapter(
    val event: MutableLiveData<OnBoarding7Event> = MutableLiveData(),
) : ListAdapter<OnBoarding7Item, OnBoarding7Holder>(OnBoarding7DiffCallback()) {

    lateinit var binding: ItemOnboardingBtnBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoarding7Holder {
        binding =
            ItemOnboardingBtnBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return OnBoarding7Holder(binding)
    }

    override fun onBindViewHolder(holder: OnBoarding7Holder, position: Int) {
        getItem(position).let { holder.onBind(it, event) }
    }
}

class OnBoarding7Holder(private val binding: ItemOnboardingBtnBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        item: OnBoarding7Item,
        event: MutableLiveData<OnBoarding7Event>
    ) {
        binding.imageView.setImageResource(item.image)
        binding.titleTv.text = item.title
        binding.root.setOnClickListener {
            item.apply { isChecked = !isChecked }
                .also {
                    if (it.isChecked) {
                        binding.root.animate()
                            .setDuration(100)
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                        binding.root.background = ResourcesCompat.getDrawable(
                            binding.root.context.resources,
                            R.drawable.gradient_onboarding_item_selected,
                            null
                        )
                    } else {
                        binding.root.animate()
                            .setDuration(100)
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                        binding.root.background = ResourcesCompat.getDrawable(
                            binding.root.context.resources,
                            R.drawable.gradient_onboarding_item,
                            null
                        )
                    }
                }
                .also { event.postValue(OnBoarding7Event.OnClick(it)) }
        }
    }
}

private class OnBoarding7DiffCallback : DiffUtil.ItemCallback<OnBoarding7Item>() {
    override fun areItemsTheSame(
        oldItem: OnBoarding7Item,
        newItem: OnBoarding7Item
    ): Boolean =
        (oldItem.title == newItem.title)

    override fun areContentsTheSame(
        oldItem: OnBoarding7Item,
        newItem: OnBoarding7Item
    ): Boolean =
        (oldItem == newItem)
}