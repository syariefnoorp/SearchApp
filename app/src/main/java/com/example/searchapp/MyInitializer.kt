package com.example.searchapp

import android.content.Context
import androidx.startup.Initializer
import io.isfaaghyth.rak.Rak

class MyInitializer: Initializer<Unit> {

    override fun create(context: Context) : Unit = Rak.initialize(context)

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}