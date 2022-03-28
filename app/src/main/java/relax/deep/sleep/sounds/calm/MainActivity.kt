package relax.deep.sleep.sounds.calm

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.anjlab.android.iab.v3.SkuDetails
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import relax.deep.sleep.sounds.calm.databinding.ActivityMainBinding
import relax.deep.sleep.sounds.calm.service.PlayerService
import relax.deep.sleep.sounds.calm.subscription.Subscribable
import relax.deep.sleep.sounds.calm.subscription.Subscription
import relax.deep.sleep.sounds.calm.subscription.SubscriptionPrice
import relax.deep.sleep.sounds.calm.utils.Constants.SUBSCRIPTION_ID_MONTH
import relax.deep.sleep.sounds.calm.utils.Constants.SUBSCRIPTION_ID_YEAR
import relax.deep.sleep.sounds.calm.utils.MyLog.showLog
import relax.deep.sleep.sounds.calm.utils.PremiumPreferences
import relax.deep.sleep.sounds.calm.utils.ToastHelper.showToast


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), Subscribable {
    companion object {
        const val ACTION_SHOW_SOUNDS = "ACTION_SHOW_SOUNDS"
        const val ACTION_SHOW_MIX = "ACTION_SHOW_MIX"
        const val FINISH = "finish_key_extra"

        private const val LICENCE_KEY = R.string.licence_key
        private const val SUBSCRIPTION_ID_TEST = R.string.purchase_id_test
    }

    private var isActivityLetShowTimeFragment = true

    private var bp: BillingProcessor? = null

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    var time: LocalTime = LocalTime.of(0, 0, 0)
    private val _mutableTime = MutableLiveData(time)
    val timeLD: LiveData<LocalTime> = _mutableTime

    private var isPremium = false

    private val _isPremiumLd = MutableLiveData<Boolean>()
    private val isPremiumLd: LiveData<Boolean> = _isPremiumLd
    private val _mutableSubscriptionToPriceLD =
        MutableLiveData<Map<Subscription, SubscriptionPrice>>()
    private val mapSubscriptionToPriceLD: LiveData<Map<Subscription, SubscriptionPrice>> get() = _mutableSubscriptionToPriceLD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (finishActivityIfNeeded()) return
        _binding = ActivityMainBinding.inflate(layoutInflater)

        bp = BillingProcessor.newBillingProcessor(this, getString(LICENCE_KEY), this)
        bp?.initialize()

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fr_activity_main) as NavHostFragment

        val navController = navHostFragment.navController

        setTouchListener(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_settings,
                R.id.navigation_sounds,
                R.id.navigation_mixes,
                R.id.customMixDialog,
                R.id.ratingDialog,
                R.id.timerDialog,
                R.id.unlockForFreeFragment -> navView.visibility = View.VISIBLE
                else -> navView.visibility = View.GONE
            }
            when (destination.id) {
                R.id.playerFragment -> letActivityShowTimeFragment(true)
                else -> letActivityShowTimeFragment(false)
            }
        }

        navView.setupWithNavController(navController)
        navigate(intent, navController)
    }

    override fun onDestroy() {
        _binding = null
        bp?.release()
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val navController = (supportFragmentManager
            .findFragmentById(R.id.nav_host_fr_activity_main) as NavHostFragment).navController
        navigate(intent, navController)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        time = LocalTime.of(0, 0, 0)
        return super.dispatchTouchEvent(ev)
    }

    private fun letActivityShowTimeFragment(isLet: Boolean) {
        isActivityLetShowTimeFragment = isLet
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

    private fun finishActivityIfNeeded(): Boolean {
        val finish = intent.getBooleanExtra(FINISH, false) //default false if not set by argument
        if (finish) {
            finish()
            return true
        }
        return false
    }

    private fun setTouchListener(navController: NavController) {
        tickerFlow(Duration.ofSeconds(1))
            .onEach {
//                Log.i(TAG, "time: $it")
//                Log.i(TAG, "is service running: ${isMyServiceRunning(PlayerService::class.java)}")
                _mutableTime.postValue(it)
                if (it == LocalTime.of(0, 0, 10)) {
                    Log.i(TAG, "10 seconds have passed")
                    if (PlayerService.isPlayable.value == true
                        && PlayerService.isPause.value != true
                        && isActivityLetShowTimeFragment
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

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    /**billing*/
    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        showLog("onProductPurchased: ", TAG)
        updateSubscriptionStatus()
    }

    override fun onPurchaseHistoryRestored() {
        showLog("onPurchaseHistoryRestored: ", TAG)
        updateSubscriptionStatus()
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
//        getSubscriptionsPrice()
        showLog("onBillingError: ", TAG)
    }

    override fun onBillingInitialized() {
        updateSubscriptionStatus()
        //??TODO
        getSubscriptionsPrice()
    }

    override fun isPremium(): Boolean {
        return isPremium
    }

    override fun subscribe(subscription: Subscription) {
        when (subscription) {
            Subscription.MONTH -> {
//                showToast(this, "Month subscription")
                bp?.subscribe(this, SUBSCRIPTION_ID_MONTH)
            }
            Subscription.YEAR -> {
//                showToast(this, "Year subscription")
                bp?.subscribe(this, SUBSCRIPTION_ID_YEAR)
            }
        }
    }

    override fun getIsPremiumLD(): LiveData<Boolean> {
        return isPremiumLd
    }

    override fun getSubscriptionsToPricesLD(): LiveData<Map<Subscription, SubscriptionPrice>> {
        return mapSubscriptionToPriceLD
    }

    private fun hasSubscription(): Boolean {
        val isMonthSubscribed = bp?.isSubscribed(SUBSCRIPTION_ID_MONTH)
        val isYearSubscribed = bp?.isSubscribed(SUBSCRIPTION_ID_YEAR)
        return isMonthSubscribed == true || isYearSubscribed == true
    }

    private fun updateSubscriptionStatus() {
        showLog("updateSubscriptionStatus: ", TAG)
        bp?.loadOwnedPurchasesFromGoogleAsync(object : BillingProcessor.IPurchasesResponseListener {
            override fun onPurchasesSuccess() {
//                showToast(this@MainActivity, "Subscriptions updated.")
            }

            override fun onPurchasesError() {
//                showToast(this@MainActivity, "Subscriptions update error.")
            }
        })
        PremiumPreferences.setStoredPremiumStatus(this,hasSubscription())
        //for test
//        PremiumPreferences.setStoredPremiumStatus(this, false)

        isPremium = hasSubscription()
        _isPremiumLd.postValue(hasSubscription())
    }

    private fun getSubscriptionsPrice() {
        var monthPrice = SubscriptionPrice("-", "-")
        var yearPrice = SubscriptionPrice("-", "-")

        bp?.getSubscriptionListingDetailsAsync(SUBSCRIPTION_ID_MONTH,
            object : BillingProcessor.ISkuDetailsResponseListener {
                override fun onSkuDetailsResponse(products: MutableList<SkuDetails>?) {
                    try {
                        val product = products?.get(0)
                        monthPrice = SubscriptionPrice(product?.currency!!, product.priceText)
                    } catch (e: IndexOutOfBoundsException) {
                        showLog("onSkuDetailsResponse: empty products", TAG)
                    }
                }

                override fun onSkuDetailsError(error: String?) {
//                    showToast(this@MainActivity, "Can't download actual price")
                }
            })
        bp?.getSubscriptionListingDetailsAsync(SUBSCRIPTION_ID_YEAR,
            object : BillingProcessor.ISkuDetailsResponseListener {
                override fun onSkuDetailsResponse(products: MutableList<SkuDetails>?) {
                    try {
                        val product = products?.get(0)
                        yearPrice = SubscriptionPrice(product?.currency!!, product.priceText)
                    } catch (e: IndexOutOfBoundsException) {
                        showLog("onSkuDetailsResponse: empty products", TAG)
                    }
                }

                override fun onSkuDetailsError(error: String?) {
//                    showToast(this@MainActivity, "Can't download actual price")
                }
            })
        val mapSubscriptionToPrice = mutableMapOf<Subscription, SubscriptionPrice>()
        mapSubscriptionToPrice[Subscription.MONTH] = monthPrice
        mapSubscriptionToPrice[Subscription.YEAR] = yearPrice
        _mutableSubscriptionToPriceLD.postValue(mapSubscriptionToPrice.toMap())
    }
}