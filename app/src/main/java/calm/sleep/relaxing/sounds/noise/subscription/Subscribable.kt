package calm.sleep.relaxing.sounds.noise.subscription

import androidx.lifecycle.LiveData
import com.anjlab.android.iab.v3.BillingProcessor

interface Subscribable : BillingProcessor.IBillingHandler {
    fun isPremium(): Boolean
    fun subscribe(subscription: Subscription)
    fun getIsPremiumLD(): LiveData<Boolean>
    fun getSubscriptionsToPricesLD(): LiveData<Map<Subscription, SubscriptionPrice>>
}