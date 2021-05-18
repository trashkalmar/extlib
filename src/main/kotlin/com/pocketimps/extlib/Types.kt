package com.pocketimps.extlib


typealias Proc = () -> Unit
typealias BoolProc = (result: Boolean) -> Unit

interface Tagged {
  val tag: String
}
