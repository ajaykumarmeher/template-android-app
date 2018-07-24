package com.dwarvesv.mvvm.view.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.OnCompositionLoadedListener
import com.dwarvesv.mvvm.R
import com.dwarvesv.mvvm.base.BaseFragment
import com.dwarvesv.mvvm.utils.disposebag.DisposeBag
import com.dwarvesv.mvvm.utils.disposebag.disposedBy
import com.jakewharton.rxbinding2.view.RxView

import kotlinx.android.synthetic.main.fragment_splash.*


class SplashFragment : BaseFragment(), OnCompositionLoadedListener {


    private var listener: InteractionListener? = null
    private lateinit var viewModel: SplashViewModel
    private val bag = DisposeBag(this)
    internal var mAnimFiles = arrayOf("empty_status.json", "letter_b_monster.json", "permission.json", "ice_cream_animation.json")
    internal var mCurrentAnim = 0
    private var isLogin = false

    companion object {
        fun getInstance() = SplashFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement Fragment.InteractionListener")
        }
    }

    override fun setUpView(view: View, savedInstanceState: Bundle?) {
        viewModel = SplashViewModel()
        RxView.clicks(btnNext)
                .subscribe { _ ->
                    if (isLogin)
                        listener?.navigateToLogin()
                    else {
                        btnNext.setEnabled(false)
                        changeAnim()
                    }
                }
                .disposedBy(bag)
        viewModel.outputs.enableLogin.subscribe {
            btnNext.setText(getString(R.string.login))
            isLogin = true
        }.disposedBy(bag)
    }

    private fun changeAnim() {
        Handler().post {
            val animFileName = mAnimFiles[++mCurrentAnim % mAnimFiles.size]
            viewModel.inputs.animationeName.onNext(animFileName)
            LottieComposition.Factory.fromAssetFileName(context, animFileName, this)
        }
    }

    override fun onCompositionLoaded(composition: LottieComposition?) {
        composition?.let { animView.setComposition(it) }
        animView.playAnimation()
        btnNext.setEnabled(true)
    }

    interface InteractionListener {

        fun navigateToLogin()
    }
}