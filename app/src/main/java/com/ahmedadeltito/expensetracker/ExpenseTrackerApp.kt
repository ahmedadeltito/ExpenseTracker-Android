package com.ahmedadeltito.expensetracker

import android.app.Application
import com.ahmedadeltito.expensetracker.di.AppContainer

class ExpenseTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        AppContainer.initialize(this)
    }
}