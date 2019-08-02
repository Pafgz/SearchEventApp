package com.paf.londonevents.app.utils

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class WeakReference<T> {

    private var ref: WeakReference<T>? = null

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newListener: T?) {
        newListener?.let { ref = WeakReference(it) }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return ref?.get()
    }
}