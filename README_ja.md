[English version](README.md)

# Spring Ai Computer Use Demo

このプロジェクトはSpring AIでの[こちらのPR](https://github.com/spring-projects/spring-ai/pull/5335)で実装した、Geminiの[Computer Use機能](https://ai.google.dev/gemini-api/docs/computer-use)を使ったデモアプリケーションです。

## 実行例

引数にプロンプトを与えると、その内容に基づいて自動でブラウザ操作を行います。
`GOOGLE_GENAI_PROJECT_ID`の環境変数にGCPのプロジェクトIDの設定が必要です。

```shell
./gradlew bootRun --args="https://testautomationpractice.blogspot.com にアクセスして、Pagination Web Tableの3ページ目で高い商品を2点選択してください"
```
