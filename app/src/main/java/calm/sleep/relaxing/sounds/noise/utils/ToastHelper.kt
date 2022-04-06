package calm.sleep.relaxing.sounds.noise.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import calm.sleep.relaxing.sounds.noise.databinding.ToastBinding

object ToastHelper {
    fun showToast(context: Context?,message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun showSetSuccessfullyToast(activity: FragmentActivity) {
        val widthOfToast = activity.window.decorView.width
        Toast(activity).apply {
            duration = Toast.LENGTH_SHORT
            setGravity(Gravity.TOP,0,100)
            view = ToastBinding.inflate(LayoutInflater.from(activity)).root.apply { minWidth = widthOfToast-40 }
        }.show()
    }
}