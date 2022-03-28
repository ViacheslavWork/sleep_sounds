package relax.deep.sleep.sounds.calm.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.databinding.DialogRatingBinding
import relax.deep.sleep.sounds.calm.utils.ToastHelper

class RatingDialog : DialogFragment() {
    private var _binding: DialogRatingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.estimateBtn.setOnClickListener { ToastHelper.showToast(context,"Estimate") }

        binding.star1Iv.setOnClickListener {
            binding.smileIv.setImageDrawable(ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_smile_1,
                null
            ))
            setStarsColors(0)
            setNotGoodRating()
        }
        binding.star2Iv.setOnClickListener {
            binding.smileIv.setImageDrawable(ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_smile_2,
                null
            ))
            setNotGoodRating()
            setStarsColors(1)
        }
        binding.star3Iv.setOnClickListener {
            binding.smileIv.setImageDrawable(ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_smile_3,
                null
            ))
            setNotGoodRating()
            setStarsColors(2)
        }
        binding.star4Iv.setOnClickListener {
            binding.smileIv.setImageDrawable(ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_smile_4,
                null
            ))
            setNotGoodRating()
            setStarsColors(3)
        }
        binding.star5Iv.setOnClickListener {
            binding.smileIv.setImageDrawable(ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_smile_5,
                null
            ))
            setGoodRating()
            setStarsColors(4)
        }

    }

    private fun setGoodRating() {
        binding.estimateBtn.isEnabled = true
        binding.ohNoTv.visibility = View.VISIBLE
        binding.ohNoTv.text = getString(R.string.we_like_you)
        binding.ratingContentTv.text = getString(R.string.thanks_for_feedback)
        binding.estimateBtn.text = getString(R.string.estimate)
    }

    private fun setNotGoodRating() {
        binding.estimateBtn.isEnabled = true
        binding.ohNoTv.visibility = View.VISIBLE
        binding.ratingContentTv.text = getString(R.string.tell_us_what_we_can_improve)
        binding.estimateBtn.text = getString(R.string.estimate)
    }

    private fun setStarsColors(index: Int) {
        for (i in 0..index) {
            getStars()[i].setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_star_yellow,
                    null
                )
            )
        }
        for (i in index + 1 until getStars().size) {
            getStars()[i].setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_star,
                    null
                )
            )
        }
    }

    private fun getStars(): List<ImageView> {
        return mutableListOf<ImageView>()
            .apply {
                add(binding.star1Iv)
                add(binding.star2Iv)
                add(binding.star3Iv)
                add(binding.star4Iv)
                add(binding.star5Iv)
            }.toList()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}