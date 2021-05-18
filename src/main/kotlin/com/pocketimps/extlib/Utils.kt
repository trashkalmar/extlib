@file:Suppress("NOTHING_TO_INLINE")
package com.pocketimps.extlib


inline fun Int.isBitSet(bit: Int) = ((this and bit) == bit)
inline fun Int.isBitCleared(bit: Int) = ((this and bit) == 0)

inline fun <T> uiLazy(noinline initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

inline fun <reified E> String?.toTagged(): E? where E : Enum<E>, E : Tagged = this?.let {
  enumValues<E>().find {
    it.tag == this
  }
}
