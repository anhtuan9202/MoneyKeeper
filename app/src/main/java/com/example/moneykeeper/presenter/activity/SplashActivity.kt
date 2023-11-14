package com.example.moneykeeper.presenter.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.ActivitySplashBinding
import com.example.moneykeeper.presenter.application.MoneyKepperApplication

import com.example.moneykeeper.presenter.base.BaseActivity
import com.example.moneykeeper.presenter.utils.PrefsUtils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {


    override fun initView() {
        loadLocale()
        PrefsUtils.initialize(applicationContext)
        when(PrefsUtils.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)){
            AppCompatDelegate.MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }




        val application = application as MoneyKepperApplication
        Handler(Looper.myLooper()!!).postDelayed({
            application.showAdIfAvailable(
                this, object : MoneyKepperApplication.OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        startActivity()
                        finish()
                    }
                }
            )
        }, 2000)
    }


    override fun getLayout(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)

    }

    private fun startActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setLocale(lang: String){
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        PrefsUtils.initialize(baseContext)
        PrefsUtils.putString("language",lang)
    }

    private fun loadLocale(){
        PrefsUtils.initialize(baseContext)
        PrefsUtils.getString("language")?.let { setLocale(it) }
    }


}