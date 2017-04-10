/*
 * Copyright 2017 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

typealias RandomZundokoService = () -> ZnDk
typealias ZundokoService = (String, List<ZnDk>) -> Pair<Zundoko, List<Zundoko>>

@Controller
@RequestMapping("/")
class ZundokoController {

    @GetMapping
    fun get(): ModelAndView = ModelAndView("zundoko", mapOf("zundoko" to ZundokoResponse(null, emptyList())), HttpStatus.OK)

    @PostMapping
    fun post(@ModelAttribute zndk: ZundokoRequest): ModelAndView =
            zndk.list.map<ZnDk, Zundoko> { it }
                    .toMutableList()
                    .apply { (zndk.previous to this).apply { if (this.first != null) this.second.add(this.first as Zundoko) } }
                    .apply { this.add(zndk.zndk as Zundoko) }
                    .apply { if (this.endsWith(finish)) this.add(ZnDk.Companion) }
                    .let { it.last() to it.apply { this.removeAt(this.lastIndex) }.toList() }
                    .let { ModelAndView("zundoko", mapOf("zundoko" to ZundokoResponse(it.first, it.second)), HttpStatus.OK) }

    companion object {
        val finish: List<Zundoko> = listOf(ZnDk.ズン, ZnDk.ズン, ZnDk.ズン, ZnDk.ズン, ZnDk.ドコ)
    }
}

interface Zundoko {
    val value: String
}

enum class ZnDk: Zundoko {
    ズン, ドコ;
    override val value: String get() = name
    companion object: Zundoko {
        override val value: String get() = "きよし"
    }
}

data class ZundokoRequest(var zndk: ZnDk = ZnDk.ズン, var previous: ZnDk? = null, var list: List<ZnDk> = emptyList())

data class ZundokoResponse(val zndk: Zundoko?, val list: List<Zundoko>) {
    fun isEmpty(): Boolean = zndk == null
    fun isNotEmpty(): Boolean = zndk != null
    fun isFinish(): Boolean = when (zndk) {
        null -> false
        is ZnDk.Companion -> true
        else -> false
    }
    fun isNotFinish(): Boolean = !isFinish()
}

fun <T> List<T>.endsWith(list: List<T>): Boolean = if (this.size < list.size) false else eq(this, list, list.size)

tailrec fun <T> eq(l: List<T>, r: List<T>, i: Int): Boolean =
        if (i == 0) true
        else if (r[i - 1] != l[l.size + i - r.size - 1]) false
        else eq(l, r, i - 1)
