/*
 * This file is part of Project Aurora Minecraft Mod, licensed under the MIT License.
 *
 * Copyright (c) 2024 TreyRuffy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.treyruffy.projectaurora

/**
 * Represents a key binding in Project Aurora Minecraft Mod.
 */
@Suppress("MagicNumber", "LongMethod", "CyclomaticComplexMethod", "LibraryEntitiesShouldNotBePublic")
fun toAuroraKeyCode(a: String): Int {
  val oldKeycode = a.replace("key.keyboard.", "")
  return when (oldKeycode) {
    "esc" -> 0x01
    "1" -> 0x02
    "2" -> 0x03
    "3" -> 0x04
    "4" -> 0x05
    "5" -> 0x06
    "6" -> 0x07
    "7" -> 0x08
    "8" -> 0x09
    "9" -> 0x0A
    "0" -> 0x0B
    "minus" -> 0x0C
    "equal" -> 0x0D
    "backspace" -> 0x0E
    "tab" -> 0x0F
    "q" -> 0x10
    "w" -> 0x11
    "e" -> 0x12
    "r" -> 0x13
    "t" -> 0x14
    "y" -> 0x15
    "u" -> 0x16
    "i" -> 0x17
    "o" -> 0x18
    "p" -> 0x19
    "left.bracket" -> 0x1A
    "right.bracket" -> 0x1B
    "enter" -> 0x1B
    "left.control" -> 0x1D
    "a" -> 0x1E
    "s" -> 0x1F
    "d" -> 0x20
    "f" -> 0x21
    "g" -> 0x22
    "h" -> 0x23
    "j" -> 0x24
    "k" -> 0x25
    "l" -> 0x26
    "semicolon" -> 0x27
    "apostrophe" -> 0x28
    "tilde" -> 0x29 // ???
    "left.shift" -> 0x2A
    "backslash" -> 0x2B
    "z" -> 0x2C
    "x" -> 0x2D
    "c" -> 0x2E
    "v" -> 0x2F
    "b" -> 0x30
    "n" -> 0x31
    "m" -> 0x32
    "comma" -> 0x33
    "period" -> 0x34
    "slash" -> 0x35
    "right.shift" -> 0x36
    "keypad.multiply" -> 0x37
    "left.alt" -> 0x38
    "space" -> 0x39
    "caps.lock" -> 0x3A
    "f1" -> 0x3B
    "f2" -> 0x3C
    "f3" -> 0x3D
    "f4" -> 0x3E
    "f5" -> 0x3F
    "f6" -> 0x40
    "f7" -> 0x41
    "f8" -> 0x42
    "f9" -> 0x43
    "f10" -> 0x44
    "num.lock" -> 0x45
    "scroll.lock" -> 0x46
    "keypad.7" -> 0x47
    "keypad.8" -> 0x48
    "keypad.9" -> 0x49
    "keypad.subtract" -> 0x4A
    "keypad.4" -> 0x4B
    "keypad.5" -> 0x4C
    "keypad.6" -> 0x4D
    "keypad.add" -> 0x4E
    "keypad.1" -> 0x4F
    "keypad.2" -> 0x50
    "keypad.3" -> 0x51
    "keypad.0" -> 0x52
    "keypad.decimal" -> 0x53
    "f11" -> 0x57
    "f12" -> 0x58
    "keypad.enter" -> 0x9C
    "right.control" -> 0x9D
    "keypad.divide" -> 0xB5
    "right.alt" -> 0xB8
    "pause" -> 0xC5
    "home" -> 0xC7
    "up" -> 0xC8
    "page.up" -> 0xC9
    "left" -> 0xCB
    "right" -> 0xCD
    "end" -> 0xCF
    "down" -> 0xCF
    "page.down" -> 0xD1
    "insert" -> 0xD2
    "delete" -> 0xD3
    "left.win" -> 0xDB
    else -> 0x00
  }
}
