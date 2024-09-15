package com.example.timer2.timer

enum class TimerType{
    DEFAULT,
    WORK,
    REFRESH,
    BREAK
}

class SetTimer (var duration: Long, val timerType: TimerType) {
}