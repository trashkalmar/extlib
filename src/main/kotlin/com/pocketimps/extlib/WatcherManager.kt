package com.pocketimps.extlib


typealias WatcherCallback<T> = Unwatchable.(T?) -> Unit

fun interface Watchable<out T> {
  fun watch(callback: WatcherCallback<T>): Unwatchable
}


fun interface Unwatchable {
  fun unwatch()
}


open class BaseWatchable<T>(protected val watcherManager: WatcherManager) : Watchable<T> {
  final override fun watch(callback: WatcherCallback<T>) = watcherManager.watch(this, callback)
  fun notifyWatchers(data: T?) = watcherManager.notifyWatchers(this, data)
}


class WatcherManager {
  private val entries = HashMap<Watchable<*>, Entry>()

  private class InternalUnwatchable(@Volatile private var entry: Entry?,
                                    val callback: WatcherCallback<*>)
    : Unwatchable {
    override fun unwatch() {
      entry?.make {
        entry = null
        it.unregister(this)
      }
    }
  }


  private inner class Entry(val watchable: Watchable<*>) {
    private val items = Listeners<InternalUnwatchable>()

    fun register(callback: WatcherCallback<*>) = synchronized(items) {
      InternalUnwatchable(this, callback).also(items::register)
    }

    fun unregister(unwatchable: InternalUnwatchable) = synchronized(items) {
      items.unregister(unwatchable)
      if (items.isEmpty())
        unregister(this)
    }

    fun <T> notifyWatchers(data: T?) = synchronized(items) {
      items.isNotEmpty().alsoIfTrue {
        items.notify {
          @Suppress("UNCHECKED_CAST")
          (it.callback as WatcherCallback<T>).invoke(it, data)
        }
      }
    }
  }


  private fun unregister(entry: Entry) = synchronized(entries) {
    entries -= entry.watchable
  }

  fun <T> watch(watchable: Watchable<T>, callback: WatcherCallback<T>): Unwatchable = synchronized(entries) {
    val entry = entries[watchable] ?: Entry(watchable).also {
      entries[watchable] = it
    }

    entry.register(callback)
  }

  fun <T> notifyWatchers(watchable: Watchable<T>, data: T?) = synchronized(entries) {
    entries[watchable]?.notifyWatchers(data) == true
  }
}
