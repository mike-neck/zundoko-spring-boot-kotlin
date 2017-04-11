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

import org.junit.Test

class ZundokoControllerTest {

    val controller = ZundokoSpringBootKotlinApplication()

    @Test fun ズンだけ渡す(): Unit =
            controller.post(ZundokoRequest()).model["zundoko"]!! shouldBe ZundokoResponse(ZnDk.ズン, listOf())

    @Test fun 最初がズンで次にドコ(): Unit =
            controller.post(ZundokoRequest(zndk = ZnDk.ドコ, previous = ZnDk.ズン, list = emptyList()))
                    .model["zundoko"]!! shouldBe ZundokoResponse(ZnDk.ドコ, listOf(ZnDk.ズン))
}

infix fun <T: Any> T.shouldBe(expected: T): Unit =
        if (this != expected) throw AssertionError("expected[$expected] but actual[$this]") else Unit
