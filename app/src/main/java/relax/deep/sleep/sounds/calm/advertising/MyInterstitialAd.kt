package relax.deep.sleep.sounds.calm.advertising

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import relax.deep.sleep.sounds.calm.utils.MyLog.showLog

private const val TAG = "MyInterstitialAd"

class MyInterstitialAd(private val context: Context, private val interstitialId: String) {
    private var interAd: InterstitialAd? = null

    fun loadInterAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            interstitialId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    showLog("onAdFailedToLoad: $adError", TAG)
                    interAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    showLog("onAdLoaded: ", TAG)
                    interAd = interstitialAd
                }
            })
    }

    fun showInterAd(activity: Activity, functionAfterAd: () -> Unit): Boolean {
        if (interAd != null) {
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    showLog("onAdDismissedFullScreenContent: ", TAG)
                    functionAfterAd()
                    interAd = null
                    loadInterAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    showLog("onAdFailedToShowFullScreenContent: ", TAG)
                    functionAfterAd()
                    interAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    showLog("onAdShowedFullScreenContent: ", TAG)
                    functionAfterAd()
                    interAd = null
                    loadInterAd()
                }
            }
            interAd?.show(activity)
            return true
        } else {
            showLog("interId = null", TAG)
            functionAfterAd()
            return false
        }
    }
}