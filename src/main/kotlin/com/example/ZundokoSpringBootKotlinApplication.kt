package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@SpringBootApplication
@Controller
@RequestMapping("/")
class ZundokoSpringBootKotlinApplication {

    @GetMapping
    fun get(): ModelAndView = ok("zundoko", mapOf("zundoko" to ZundokoResponse(null, emptyList())))

    @PostMapping
    fun post(@ModelAttribute zndk: ZundokoRequest): ModelAndView =
            zndk.list.map<ZnDk, Zundoko> { it }
                    .toMutableList()
                    .apply { ((zndk.previous as Zundoko) to this).tap() }
                    .apply { this.add(zndk.zndk as Zundoko) }
                    .apply { if (this.endsWith(finish)) this.add(ZnDk.Companion) }
                    .let { it.last() to it.apply { this.removeAt(this.lastIndex) }.toList() }
                    .let { ok("zundoko", mapOf("zundoko" to ZundokoResponse(it.first, it.second))) }

    companion object {
        val finish: List<Zundoko> = listOf(ZnDk.ズン, ZnDk.ズン, ZnDk.ズン, ZnDk.ズン, ZnDk.ドコ)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ZundokoSpringBootKotlinApplication::class.java, *args)
}

interface Zundoko {
    val value: String
}

enum class ZnDk : Zundoko {
    ズン, ドコ;

    override val value: String get() = name

    companion object : Zundoko {
        override val value: String get() = "き・よ・し！"
    }
}

data class ZundokoRequest(
        var zndk: ZnDk = ZnDk.ズン,
        var previous: ZnDk? = null,
        var list: List<ZnDk> = emptyList())

data class ZundokoResponse(val zndk: Zundoko?, val list: List<Zundoko>) {
    fun isNotEmpty(): Boolean = zndk != null
    fun isFinish(): Boolean = when (zndk) {
        null -> false
        is ZnDk.Companion -> true
        else -> false
    }

    fun isNotFinish(): Boolean = !isFinish()
}

fun <T> List<T>.endsWith(list: List<T>): Boolean =
        if (this.size < list.size) false
        else eq(this, list, list.size)

tailrec fun <T> eq(l: List<T>, r: List<T>, i: Int): Boolean =
        if (i == 0) true
        else if (r[i - 1] != l[l.size + i - r.size - 1]) false
        else eq(l, r, i - 1)

val <T : Any> T.unit: Unit get() = Unit

fun ok(view: String, model: Map<String, Any>): ModelAndView = ModelAndView(view, model, HttpStatus.OK)

fun <T> Pair<T, MutableList<T>>.tap(): Unit =
        if (this.first != null) unit else Unit
