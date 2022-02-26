package white.noise.sounds.baby.sleep.ui.sounds.save_custom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.databinding.FragmentSaveCustomBinding
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.MixCategory
import white.noise.sounds.baby.sleep.ui.mixes.MixesEvent
import white.noise.sounds.baby.sleep.ui.mixes.MixesViewModel
import white.noise.sounds.baby.sleep.ui.sounds.SoundsViewModel


class SaveCustomFragment : Fragment() {
    private val soundsViewModel: SoundsViewModel by sharedViewModel()
    private val mixesViewModel: MixesViewModel by sharedViewModel()
    private var _binding: FragmentSaveCustomBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveCustomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.customNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                binding.applyBtn.isEnabled = binding.customNameEt.text.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.photoRemoveIv.setOnClickListener { }

        binding.photoEditIv.setOnClickListener { }

        binding.uploadImageBtn.setOnClickListener { }

        binding.closeBtn.setOnClickListener { requireActivity().onBackPressed() }

        binding.applyBtn.setOnClickListener {
            val mix = Mix(
                title = binding.customNameEt.text.toString(),
                sounds = soundsViewModel.selectedSounds.value?.toMutableList()!!,
                picturePath = "bubbling-stream.png",
                category = MixCategory.Others
            )
            mixesViewModel.handleEvent(MixesEvent.OnMixSave(mix))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}