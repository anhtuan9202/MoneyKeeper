package com.example.moneykeeper.presenter.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.moneykeeper.presenter.interfaces.OnActionCallback
import com.example.moneykeeper.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.platform.MaterialContainerTransform

abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity(), OnActionCallback {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getLayout()
        setContentView(binding.root)
        initView()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

    }

    abstract fun initView()

    abstract fun getLayout():VB

    override fun showFragment(
        fromFragment: Class<*>,
        toFragment: Class<*>,
        enterAnim: Int,
        exitAnim: Int,
        data: Any?,
        isBack: Boolean
    ) {
        val clazz = Class.forName(toFragment.name)
        val fragment = clazz.getConstructor().newInstance() as BaseFragment<*>
        fragment.callback = this
        fragment.data = data
//        fragment.sharedElementEnterTransition = MaterialContainerTransform().apply {
//            drawingViewId = R.id.frMain
//            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//            scrimColor = Color.TRANSPARENT
//            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
//        }
        val trans = supportFragmentManager.beginTransaction()
        if(isBack){
            trans.addToBackStack(null)
        }
        trans.setCustomAnimations(enterAnim, exitAnim)
        trans.replace(R.id.frMain, fragment, toFragment.name)
        trans.commit()
    }

    override fun backToPrevious() {
        onBackPressed()
    }

    fun notify(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
//    fun hiddenBNV(hidden:Boolean){
//        if(hidden){
//            findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = View.GONE
//            findViewById<FloatingActionButton>(R.id.fAB)?.visibility = View.GONE
//        }
//        else {
//            findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = View.VISIBLE
//            findViewById<FloatingActionButton>(R.id.fAB)?.visibility = View.VISIBLE
//        }
//    }



}