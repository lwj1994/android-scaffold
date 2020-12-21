package com.lwjlol.scaffold.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : Any> mmkv(key: String, def: T, fileName: String? = null) = object : ReadWriteProperty<Any, T> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        MMKVUtils.put(key, value, fileName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return MMKVUtils.get(key, def, fileName)
    }
}
