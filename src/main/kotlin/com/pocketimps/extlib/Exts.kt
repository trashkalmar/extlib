@file:Suppress("NOTHING_TO_INLINE")
package com.pocketimps.extlib

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


typealias Proc = () -> Unit
typealias BoolProc = (result: Boolean) -> Unit

inline fun <T> uiLazy(noinline initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

inline fun Boolean.alsoIfTrue(proc: Proc) = apply {
  if (this)
    proc.invoke()
}

inline fun Boolean.alsoIfFalse(proc: Proc) = apply {
  if (!this)
    proc.invoke()
}

inline fun <T> T.make(block: (T) -> Unit) = block.invoke(this)

@OptIn(ExperimentalContracts::class)
inline fun <T> Boolean.makeIfTrue(block: () -> T?): T? {
  contract {
    callsInPlace(block, InvocationKind.AT_LEAST_ONCE)
    returns(null) implies (!this@makeIfTrue)
  }

  return if (this)
    block.invoke()
  else
    null
}

inline fun <T> Boolean.makeIfFalse(block: () -> T?) = (!this).makeIfTrue(block)
