package white.noise.sounds.baby.sleep.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import white.noise.sounds.baby.sleep.databinding.DialogTimerBinding

class TimerDialog : DialogFragment() {
    private var _binding: DialogTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DialogTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.offBtn.setOnClickListener {  }
        binding.customBtn.setOnClickListener {  }
        binding.minutes10Btn.setOnClickListener {  }
        binding.minutes15Btn.setOnClickListener {  }
        binding.minutes20Btn.setOnClickListener {  }
        binding.minutes30Btn.setOnClickListener {  }
        binding.minutes40Btn.setOnClickListener {  }
        binding.minutes50Btn.setOnClickListener {  }
        binding.hour1Btn.setOnClickListener {  }
        binding.hour2Btn.setOnClickListener {  }
        binding.hour3Btn.setOnClickListener {  }
        binding.hour4Btn.setOnClickListener {  }
        binding.hour8Btn.setOnClickListener {  }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}