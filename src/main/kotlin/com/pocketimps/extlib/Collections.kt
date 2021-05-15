package com.pocketimps.extlib


fun <T> List<T>.fastCompare(other: List<T>): Boolean {
  if (size != other.size)
    return false

  forEachIndexed { idx, item ->
    if (item !== other[idx])
      return false
  }

  return true
}

fun <T> MutableList<T>.setItems(newData: Collection<T>?) = apply {
  clear()
  newData?.let(::addAll)
}

fun <K, V> MutableMap<K, V>.setItems(newData: Map<K, V>?) = apply {
  clear()
  newData?.let(::putAll)
}

fun <T> MutableSet<T>.setItems(newData: Collection<T>?) = apply {
  clear()
  newData?.let(::addAll)
}
