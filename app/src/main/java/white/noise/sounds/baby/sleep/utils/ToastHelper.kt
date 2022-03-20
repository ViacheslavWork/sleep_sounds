package white.noise.sounds.baby.sleep.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.koin.java.KoinJavaComponent.inject
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.databinding.ToastBinding

object ToastHelper {
    fun showToast(context: Context?,message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun showCustomToast(activity: FragmentActivity) {
        val widthOfToast = activity.window.decorView.width
        Toast(activity).apply {
            duration = Toast.LENGTH_SHORT
            setGravity(Gravity.TOP,0,100)
            view = ToastBinding.inflate(LayoutInflater.from(activity)).root.apply { minWidth = widthOfToast-40 }
        }.show()
    }
}