package com.lwjlol.scaffold.core.ktx

inline fun <T> List<T>.mapModel(index: Int, map: (T) -> T): List<T> {
  val newly = ArrayList(this)
  val old = get(index)
  newly.removeAt(index)
  newly.add(index, map(old))
  return newly
}

inline fun <T> List<T>.mapModel(predicate: (T) -> Boolean, map: (T) -> T): List<T> {
  val newly = toMutableList()
  val iterator = newly.listIterator()
  while (iterator.hasNext()) {
    val next = iterator.next()
    if (predicate(next)) {
      val t = map(next)
      iterator.remove()
      iterator.add(t)
      return newly
    }
  }
  return newly
}