package com.worldsnas.daggercore.navigator

import android.app.Application
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.worldsnas.daggercore.navigator.changehandler.ArcFadeMoveChangeHandlerCompat
import com.worldsnas.daggercore.navigator.changehandler.CircularRevealChangeHandlerCompat
import com.worldsnas.daggercore.navigator.changehandler.FlipChangeHandler
import com.worldsnas.daggercore.navigator.changehandler.ScaleFadeChangeHandler
import com.worldsnas.navigation.Navigation
import com.worldsnas.navigation.NavigationAnimation
import com.worldsnas.navigation.Navigator
import com.worldsnas.navigation.Screens

class ControllerNavigator(
    private val app: Application,
    private val router: Router
) : Navigator {
    override fun goTo(screen: Screens) {
        val to = Navigation.createController(app, screen)
        router.pushController(
            RouterTransaction.with(to)
                .pushChangeHandler(getAnimation(screen.pushAnimation))
                .popChangeHandler(getAnimation(screen.popAnimation))
        )
    }

    override fun getAnimation(anim: NavigationAnimation?): ControllerChangeHandler? {
        if (anim == null) {
            return null
        }

        return when (anim) {
            is NavigationAnimation.ArcFadeMove ->
                ArcFadeMoveChangeHandlerCompat(
                    *anim.transitionNames
                )
            is NavigationAnimation.CircularReveal ->
                if (anim.duration == -1L){
                    CircularRevealChangeHandlerCompat(
                        anim.fromCX,
                        anim.fromCY
                    )
                }else{
                    CircularRevealChangeHandlerCompat(
                        anim.fromCX,
                        anim.fromCY,
                        anim.duration
                    )
                }
            NavigationAnimation.Flip ->
                FlipChangeHandler()
            NavigationAnimation.ScaleFade ->
                ScaleFadeChangeHandler()
        }
    }
}