package com.pocketimps.extlib


/**
 * `Registrator` pattern implementation which allows to maintain the list of listeners,
 * offers safe adding/removing of listeners during iteration.
 * NOT thread-safe.
 */
class Listeners<T> {
  private val listeners = LinkedHashSet<T>()
  private val listenersToAdd = LinkedHashSet<T>()
  private val listenersToRemove = LinkedHashSet<T>()
  var iterating = false
    private set

  private fun startIteration() {
    if (iterating)
      throw RuntimeException("finishIteration() must be called before new iteration")

    iterating = true
  }

  /** Completes listeners iteration. Must be called when iteration is done */
  private fun finishIteration() {
    if (listenersToRemove.isNotEmpty())
      listeners.removeAll(listenersToRemove)

    if (listenersToAdd.isNotEmpty())
      listeners.addAll(listenersToAdd)

    listenersToAdd.clear()
    listenersToRemove.clear()
    iterating = false
  }

  /**
   * Safely registers new listener. If registered during iteration,
   * new listener will NOT be called before current iteration is complete.
   * @return true if the listener was registered, false otherwise.
   */
  fun register(listener: T): Boolean {
    if (iterating) {
      if (listenersToRemove.remove(listener))
        return true

      if (listeners.contains(listener))
        return false

      return listenersToAdd.add(listener)
    }

    return listeners.add(listener)
  }

  /**
   * Safely unregisters listener. If unregistered during iteration,
   * old listener WILL be called in the current iteration.
   * @return true if the listener was unregistered, false otherwise.
   */
  fun unregister(listener: T): Boolean {
    if (iterating) {
      if (listenersToAdd.remove(listener))
        return true

      if (!listeners.contains(listener))
        return false

      return listenersToRemove.add(listener)
    }

    return listeners.remove(listener)
  }

  /**
   * Safely removes all listeners. If unregistered during iteration,
   * old listeners WILL be called in the current iteration.
   */
  fun clear() {
    if (iterating) {
      listenersToAdd.clear()
      listenersToRemove.clear()
      listenersToRemove.addAll(listeners)
    } else
      listeners.clear()
  }

  fun first() = listeners.firstOrNull()

  fun size(): Int {
    var res = listeners.size
    if (iterating) {
      res += listenersToAdd.size
      res -= listenersToRemove.size
    }

    return res
  }

  fun isEmpty() = (size() <= 0)
  fun isNotEmpty() = (size() > 0)

  fun notify(proc: (T) -> Unit) {
    startIteration()

    for (listener in listeners)
      proc.invoke(listener)

    finishIteration()
  }
}
