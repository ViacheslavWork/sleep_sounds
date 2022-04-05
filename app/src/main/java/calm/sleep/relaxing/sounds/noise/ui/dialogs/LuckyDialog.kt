package calm.sleep.relaxing.sounds.noise.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import calm.sleep.relaxing.sounds.noise.databinding.DialogLuckyBinding

class LuckyDialog : DialogFragment() {
    private var _binding: DialogLuckyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogLuckyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.closeBtn.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}