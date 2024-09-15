package com.example.timer2.data

class AppRouter {

    private object Route{
        const val TIMERSCREEN = "Start"
        const val PAUSESCREEN = "Pause"
    }
    sealed class Screen (val route: String){
        object timerScreen: Screen(Route.TIMERSCREEN)
        object pauseScreen: Screen(Route.PAUSESCREEN)
    }
}