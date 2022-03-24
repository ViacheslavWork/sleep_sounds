package relax.deep.sleep.sounds.calm.subscription

import android.content.Context
import androidx.lifecycle.LiveData
import com.anjlab.android.iab.v3.BillingProcessor
import relax.deep.sleep.sounds.calm.utils.PremiumPreferences

interface Subscribable : BillingProcessor.IBillingHandler {
    fun isPremium(): Boolean
    fun subscribe(subscription: Subscription)
    fun getIsPremiumLD(): LiveData<Boolean>
    fun getSubscriptionsToPricesLD(): LiveData<Map<Subscription, SubscriptionPrice>>
}