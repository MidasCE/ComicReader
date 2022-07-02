package com.example.app.core

import io.reactivex.Scheduler

interface SchedulerFactory {
    fun io() : Scheduler

    fun main() : Scheduler
}
