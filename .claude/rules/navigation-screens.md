---
paths:
  - "app/src/main/java/com/iroha71/koigoi_client/navigation/**"
  - "app/src/main/java/com/iroha71/koigoi_client/views/**"
---

新しい画面を追加する場合は、必ず以下の両方を行う。

- `navigation/Routes.kt` に `@Serializable object` としてルートを追加する。
- `navigation/AppNavHost.kt` の `NavHost` ブロックに `composable<...>` を追加する。

画面(`views/` 配下)は state を持たない。必要な値とコールバックは引数で受け取る設計にする。
