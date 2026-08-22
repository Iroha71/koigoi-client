# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

Kotlin + Jetpack Compose で書かれた Android アプリ(`koigoi-client`)。
Auth0 を使った認証機能を中心に開発が進んでいる。

- namespace / applicationId: `com.iroha71.koigoi_client`
- minSdk 33 / targetSdk 37 / compileSdk 37
- Kotlin 2.2.10, AGP 9.3.1

## よく使うコマンド

Windows 環境のため `gradlew.bat` を使用する(PowerShell)。

```powershell
# デバッグビルド
.\gradlew.bat assembleDebug

# 単体テスト(app/src/test)を実行
.\gradlew.bat testDebugUnitTest

# 単一のテストクラスのみ実行
.\gradlew.bat testDebugUnitTest --tests "com.iroha71.koigoi_client.ExampleUnitTest"

# インストルメンテーションテスト(app/src/androidTest, 実機/エミュレータが必要)
.\gradlew.bat connectedDebugAndroidTest

# Lint
.\gradlew.bat lint
```

## アーキテクチャ

### 画面遷移と認証状態の流れ

- `MainActivity` が認証状態(`credentials`, `isLoading`)を保持する唯一の場所。
  - 起動時に `SecureCredentialsManager` から保存済み認証情報の復元を試みる。
  - `login()` / `logout()` は Auth0 の `WebAuthProvider` を呼び出し、結果を `MainActivity` の state に反映する。
  - これらの state とコールバック(`onLogin`, `onLogout`)を `AppNavHost` に渡すだけで、画面側は Auth0 の API を直接触らない。
- `navigation/AppNavHost.kt` が `credentials` の有無を見て `Landing` ⇔ `Home` を自動的に切り替える(`LaunchedEffect(credentials)` で `popUpTo` しながら遷移)。
  - ルートは `navigation/Routes.kt` の `@Serializable object`(型安全ナビゲーション)で定義する。新しい画面を追加する場合はここにルートを足し、`AppNavHost` の `NavHost` ブロックに `composable<...>` を追加する。
- `views/` 配下が画面(Landing, Home など)。画面は state を持たず、必要な値とコールバックを引数で受け取る設計になっている。

### Auth0 設定

- クライアント ID / ドメイン / スキームは `app/src/main/res/values/strings.xml` の `com_auth0_*` に定義し、`AndroidManifest.xml` の `manifestPlaceholders`(`build.gradle.kts` の `defaultConfig`)経由でコールバック用の intent-filter に反映される。
- `libs/Auth.kt` はまだ未実装のスタブ(現状 `MainActivity` に認証ロジックが直書きされている)。Auth 関連のリファクタを行う際はここへの切り出しが意図されている可能性がある点に留意する。

### UI

- Material3 + Compose。テーマは `ui/theme/`(Color.kt, Theme.kt, Type.kt)にまとまっている。
- `material-icons-core`(`androidx.compose.material:material-icons-core`)は依存に追加せず利用しない方針。
  アイコンが必要な箇所(例: BottomTab)はラベルのみで表現する。
