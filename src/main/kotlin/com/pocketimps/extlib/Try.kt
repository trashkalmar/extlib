package com.pocketimps.extlib


inline fun <R> tryOr(default: R, block: () -> R) = try {
  block.invoke()
} catch (e: Exception) {
  default
}

inline fun tryResultOrFalse(block: () -> Boolean) =
  tryOr(false, block)

inline fun tryOrFalse(block: Proc) =
  tryOr(false) {
    block.invoke()
    true
  }

inline fun <R> tryOrNull(block: () -> R) =
  tryOr(null, block)

inline fun trySilent(block: Proc) =
  tryOr(Unit, block)
