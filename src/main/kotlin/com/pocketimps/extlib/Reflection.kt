package com.pocketimps.extlib

import java.lang.reflect.Field


fun <R> accessClassField(cls: Class<*>, fieldName: String, proc: (field: Field) -> R?): R? = try {
  cls.getDeclaredField(fieldName).run {
    val wasInaccessible = !isAccessible
    if (wasInaccessible)
      isAccessible = true

    proc.invoke(this).also {
      if (wasInaccessible)
        isAccessible = false
    }
  }
} catch (e: NoSuchFieldException) {
  null
} catch (e: IllegalAccessException) {
  null
}

@Suppress("UNCHECKED_CAST")
inline fun <reified C, R> C.getFieldValue(name: String) = accessClassField(C::class.java, name) {
  it.get(this) as? R?
}

inline fun <reified C> C.setFieldValue(name: String, value: Any?) {
  accessClassField(C::class.java, name) {
    it.set(this, value)
  }
}
