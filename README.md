zundoko-spring-boot-kotlin
===

ズンドコのwebアプリ

動かし方
---

spring-bootアプリなので、 `./gradlew bootRun` ですれば動くと思うよ。
その後は `http://localhost:8080` にアクセスすれば画面が出てくる。

ポート番号
---

デフォルトは `8080` だけど、 `src/main/resources/application.properties` で `server.port` の値を書き換えられるよ。

```properties
# suppress inspection "UnusedProperty" for whole file
server.port=8080
```

動かし方(その2)
---

「ズン」ボタンと「ドコ」ボタンをクリックして君だけのズンドコを作ろう！
