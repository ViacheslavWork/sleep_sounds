package white.noise.sounds.baby.sleep

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import white.noise.sounds.baby.sleep.databinding.ActivityMainBinding
import white.noise.sounds.baby.sleep.service.PlayerService

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    companion object {
        const val ACTION_SHOW_SOUNDS = "ACTION_SHOW_SOUNDS"
        const val ACTION_SHOW_MIX = "ACTION_SHOW_MIX"
        const val FINISH = "finish_key_extra"
    }

    private lateinit var binding: ActivityMainBinding
    var time: LocalTime = LocalTime.of(0, 0, 0)
    private val _mutableTime = MutableLiveData<LocalTime>(time)
    val timeLD: LiveData<LocalTime> = _mutableTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (finishActivityIfNeeded()) return
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fr_activity_main) as NavHostFragment

        val navController = navHostFragment.navController

        setTouchListener(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_settings, R.id.navigation_mixes, R.id.navigation_sounds ->
                    navView.visibility = View.VISIBLE
                else -> navView.visibility = View.GONE
            }
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /*this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }*/

        /*  window.addFlags(
              WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
          )*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }*/

//        navHostFragment.findNavController().navigate(R.id.action_global_to_mixFragment)
        navView.setupWithNavController(navController)
        navigate(intent, navController)
    }

    private fun finishActivityIfNeeded(): Boolean {
        val finish = intent.getBooleanExtra(FINISH, false) //default false if not set by argument
        if (finish) {
            finish()
            return true
        }
        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        time = LocalTime.of(0, 0, 0)
        return super.dispatchTouchEvent(ev)
    }

    private fun setTouchListener(navController: NavController) {
        tickerFlow(Duration.ofSeconds(1))
            .onEach {
                Log.i(TAG, "time: $it")
                _mutableTime.postValue(it)
                if (it == LocalTime.of(0, 0, 10)) {
                    Log.i(TAG, "10 seconds have passed")
                    if (PlayerService.isPlayable.value == true
                        && PlayerService.isPause.value != true
                    ) {
                        navController.navigate(R.id.action_global_to_sleepTimerFragment)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun tickerFlow(period: Duration) = flow {
        while (true) {
            time = time.plusSeconds(1)
            emit(time)
            delay(period.toMillis())
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val navController = (supportFragmentManager
            .findFragmentById(R.id.nav_host_fr_activity_main) as NavHostFragment).navController
        navigate(intent, navController)
    }

    private fun navigate(intent: Intent?, navController: NavController) {
        when (intent?.action) {
            ACTION_SHOW_MIX -> {
                navController.navigate(R.id.action_global_to_mixFragment)
            }
            ACTION_SHOW_SOUNDS -> {
                val bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view);
                bottomNavigationView.selectedItemId = R.id.navigation_sounds
            }
            else -> {
                navController.navigate(R.id.action_global_to_greetingFragment)
            }
        }
    }
}