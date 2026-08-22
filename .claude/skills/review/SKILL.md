---
name: review
description: This skill should be used when the user asks to review this project's (koigoi-client) pending code changes from multiple perspectives at once, e.g. "レビューして", "このプロジェクトをレビューして", "/review", "Kotlinとセキュリティの観点でレビューして", or wants a combined Kotlin/performance + security review with results grouped and ranked by severity (重大/警告/情報).
---

# review スキル

このプロジェクト(koigoi-client)向けの複合レビュー。次の2つのサブエージェントを並行して実行し、結果を重大度別にまとめて出力する。

- `kotlin-perf-reviewer`: Kotlin コードレビューとパフォーマンスレビューを行う
- `security-reviewer`: `security-review` スキルを呼び出してセキュリティ観点のレビューを行う

## レビュー対象の決定

1. 引数で対象(PR番号・ブランチ名・パス等)が指定されていれば、それを対象にする。
2. 指定が無ければ、まず `git status` でこのディレクトリが git リポジトリかどうかを確認する。
   - git リポジトリであれば、未コミット・未ステージの変更(`git diff` / `git diff --staged`)を対象にする。
   - git リポジトリでなければ、レビュー対象のファイル/ディレクトリをユーザーに確認する(推測でファイルを選ばない)。
3. レビューに入る前に、対象を一言で明示する。

## 実行手順

1. Agent ツールで以下の2つのサブエージェントを **同一メッセージ内で並列に** 起動する(互いに依存関係が無いため)。レビュー対象(diff の範囲、対象ファイル)を両方に同じ内容で渡す。
   - `subagent_type: "kotlin-perf-reviewer"`
   - `subagent_type: "security-reviewer"`
2. 各サブエージェントは `- [severity] file:line — summary` 形式の1行ずつのリスト(指摘が無ければ `(該当なし)` の1行)を返す約束になっている。両方の結果を集める。
3. 各指摘に、どちらのサブエージェントの結果かを示すラベルを付ける(`[Kotlin]` または `[Security]`)。
4. 重大度でグルーピングする。順序は 重大 → 警告 → 情報。指摘が0件の重大度の見出しは出力しない。
5. 各行に重大度に応じたプレフィックス記号を付けて出力する:
   - 重大 → `🔴`(赤)
   - 警告 → `🟡`(黄)
   - 情報 → プレフィックス記号なし。先頭は `-` の箇条書きのみ
6. 最終的に以下の形式で出力する:

```
## 重大
🔴 [Kotlin] file:line — summary
🔴 [Security] file:line — summary

## 警告
🟡 [Kotlin] file:line — summary

## 情報
- [Security] file:line — summary
```

7. 両サブエージェントとも指摘が無かった場合は、見出しを立てずにその旨を一言で伝える。
8. サブエージェントが返した指摘の要約以上に、憶測での深掘りや追加の改善提案を付け足さない。
