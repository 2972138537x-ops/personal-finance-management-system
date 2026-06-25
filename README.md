# 个人财务管理系统 / 個人財務管理システム

## 项目简介 / プロジェクト概要

中文说明：

这是一个基于 **Java 17 + Spring Boot + MyBatis + MySQL** 的个人财务管理系统。项目实现了用户认证、收支分类、收支记录、分页筛选、统计分析、管理员管理、Swagger / OpenAPI 文档、AI 财务助手和简单前端页面，适合作为初级 Java 后端求职展示项目。

日本語説明：

このプロジェクトは **Java 17 + Spring Boot + MyBatis + MySQL** を使用した個人財務管理システムです。ユーザー認証、収支カテゴリ、収支記録、ページング検索、統計、管理者機能、Swagger / OpenAPI、AI 財務アシスタント、簡単なフロント画面を実装しており、初級 Java バックエンドエンジニア向けのポートフォリオとして整理しています。

---

## 技术栈 / 技術スタック

| 中文 | 日本語 |
|---|---|
| 后端：Java 17, Spring Boot 3.5.15, Spring MVC | バックエンド：Java 17, Spring Boot 3.5.15, Spring MVC |
| 数据库：MySQL 8.0.34 | データベース：MySQL 8.0.34 |
| ORM / SQL：MyBatis, MyBatis XML | ORM / SQL：MyBatis, MyBatis XML |
| 构建工具：Maven | ビルドツール：Maven |
| 参数校验：Service 层业务校验 / Spring Validation | 入力チェック：Service 層の業務チェック / Spring Validation |
| 登录认证：Token, Spring MVC Interceptor | ログイン認証：Token, Spring MVC Interceptor |
| 接口文档：Swagger / OpenAPI | API ドキュメント：Swagger / OpenAPI |
| AI API：OpenRouter AI API, RestTemplate | AI API：OpenRouter AI API, RestTemplate |
| 前端：HTML / CSS / JavaScript | フロントエンド：HTML / CSS / JavaScript |
| 开发工具：IntelliJ IDEA, Git | 開発ツール：IntelliJ IDEA, Git |

---

## 主要功能 / 主な機能

| 中文 | 日本語 |
|---|---|
| 用户注册、登录、退出 | ユーザー登録・ログイン・ログアウト |
| Token 登录认证 | Token によるログイン認証 |
| 个人信息查看 | ログイン中ユーザー情報の確認 |
| 修改密码 | パスワード変更 |
| 管理员用户管理 | 管理者によるユーザー管理 |
| 收支分类管理 | 収支カテゴリ管理 |
| 收支记录管理 | 収支記録管理 |
| 分页查询和条件筛选 | ページング検索と条件検索 |
| 月度统计 | 月次統計 |
| 收入 / 支出分类统计 | 収入 / 支出のカテゴリ別集計 |
| AI 财务助手 | AI 財務アシスタント |
| 中日英多语言切换 | 中国語・日本語・英語の切り替え |
| 密码显示 / 隐藏 | パスワード表示 / 非表示 |
| 金额输入校验 | 金額入力チェック |

---

## 项目结构 / プロジェクト構成

中文说明：

```text
src/main/java/com/study/usermanagement
  common       统一返回结果 Result
  config       WebConfig, OpenApiConfig
  controller   接收 HTTP 请求
  dto          接收请求体数据
  entity       数据库实体对象
  exception    统一异常处理
  interceptor  Token 登录校验和管理员权限校验
  mapper       MyBatis Mapper 接口
  service      业务逻辑
  vo           返回给前端的展示对象

src/main/resources
  mapper       MyBatis XML SQL 文件
  static       HTML / CSS / JavaScript 前端页面
  application.properties
```

日本語説明：

```text
controller  HTTP リクエストを受け取る層
service     業務ロジックを処理する層
mapper      MyBatis の Mapper インターフェース
mapper XML  SQL を記述するファイル
dto         リクエストボディ用オブジェクト
vo          フロントへ返す表示用オブジェクト
```

---

## 后端功能说明 / バックエンド機能説明

### 用户认证 / ユーザー認証

| 中文 | 日本語 |
|---|---|
| `POST /users` 注册普通用户 | `POST /users` で一般ユーザーを登録 |
| `POST /login` 登录成功后生成 Token | `POST /login` 成功時に Token を生成 |
| Token 保存到数据库 `user.token` 字段 | Token は DB の `user.token` に保存 |
| `POST /logout` 清空当前用户 Token | `POST /logout` で現在ユーザーの Token をクリア |
| 后续请求通过 `Authorization` 请求头携带 Token | 以降のリクエストは `Authorization` ヘッダーで Token を送信 |

### 当前用户 / ログイン中ユーザー

| 中文 | 日本語 |
|---|---|
| `GET /me` 查看当前登录用户信息 | `GET /me` でログイン中ユーザー情報を取得 |
| `PUT /me/password` 修改自己的密码 | `PUT /me/password` で自分のパスワードを変更 |
| 修改密码需要旧密码和新密码 | パスワード変更には現在のパスワードと新しいパスワードが必要 |
| 返回用户信息时使用 `UserVO`，不返回密码 | ユーザー情報は `UserVO` で返し、パスワードは返さない |

### 管理员用户管理 / 管理者ユーザー管理

| 中文 | 日本語 |
|---|---|
| 管理员可以查询全部用户 | 管理者は全ユーザーを取得できる |
| 管理员可以按用户名查询用户 | 管理者はユーザー名でユーザーを検索できる |
| 管理员可以重置用户密码 | 管理者はユーザーのパスワードをリセットできる |
| 管理员可以删除用户 | 管理者はユーザーを削除できる |
| `/admin/**` 只有 `ADMIN` 角色可以访问 | `/admin/**` は `ADMIN` ロールのみアクセス可能 |

### 收支分类管理 / 収支カテゴリ管理

| 中文 | 日本語 |
|---|---|
| 当前用户可以新增收入 / 支出分类 | ログイン中ユーザーは収入 / 支出カテゴリを追加できる |
| 当前用户可以查询、修改、删除自己的分类 | 自分のカテゴリを取得・更新・削除できる |
| 分类类型使用 `income` 和 `expense` | カテゴリタイプは `income` と `expense` |
| 同一用户下同类型同名称分类不能重复 | 同じユーザー内で同じタイプ・同じ名前のカテゴリは重複不可 |
| 分类下已有收支记录时，不允许删除分类 | 収支記録が紐づいているカテゴリは削除不可 |

### 收支记录管理 / 収支記録管理

| 中文 | 日本語 |
|---|---|
| 当前用户可以新增、查询、修改、删除自己的收支记录 | ログイン中ユーザーは自分の収支記録を追加・取得・更新・削除できる |
| 新增和修改记录时，会校验分类是否属于当前用户 | 追加・更新時にカテゴリが本人のものか確認する |
| 记录类型必须和分类类型一致 | 記録タイプとカテゴリタイプは一致する必要がある |
| 支持按类型、日期范围、分类查询 | タイプ、日付範囲、カテゴリで検索できる |
| 支持分页查询和组合条件筛选 | ページング検索と複合条件検索に対応 |

### 统计功能 / 統計機能

| 中文 | 日本語 |
|---|---|
| 查询指定年月的收入合计 | 指定年月の収入合計を取得 |
| 查询指定年月的支出合计 | 指定年月の支出合計を取得 |
| 查询指定年月的结余 | 指定年月の残高を取得 |
| 按收入分类统计金额 | 収入カテゴリ別に金額を集計 |
| 按支出分类统计金额 | 支出カテゴリ別に金額を集計 |
| 前端用图表展示分类统计 | フロントでカテゴリ別集計をグラフ表示 |

### AI 财务助手 / AI 財務アシスタント

| 中文 | 日本語 |
|---|---|
| 前端提供 AI 助手页面 | フロントに AI アシスタント画面を用意 |
| 用户可以提问记账、消费分析、省钱建议 | 記帳、支出分析、節約アドバイスについて質問可能 |
| 后端 `AiService` 调用 OpenRouter AI API | バックエンドの `AiService` が OpenRouter AI API を呼び出す |
| 使用 `RestTemplate` 发送 HTTP 请求 | `RestTemplate` で HTTP リクエストを送信 |
| API Key 通过 `OPENROUTER_API_KEY` 环境变量读取 | API Key は `OPENROUTER_API_KEY` 環境変数から読み込む |
| 对提示词泄露类问题做了简单防护 | プロンプト漏えい系の質問に対する簡単な防御を実装 |

### 前端功能 / フロントエンド機能

| 中文 | 日本語 |
|---|---|
| 使用 HTML / CSS / JavaScript 实现页面 | HTML / CSS / JavaScript で画面を実装 |
| 支持中文、日语、英语切换 | 中国語・日本語・英語の切り替えに対応 |
| 登录、注册、首页、分类、记录、统计、管理员、AI 页面 | ログイン、登録、ホーム、カテゴリ、記録、統計、管理者、AI 画面 |
| 密码输入框支持显示 / 隐藏 | パスワード入力欄は表示 / 非表示を切り替え可能 |
| 金额输入框设置最小值、最大值和小数位 | 金額入力欄に最小値、最大値、小数桁を設定 |

---

## 安全和业务校验 / セキュリティと業務チェック

| 中文 | 日本語 |
|---|---|
| 普通用户只能操作自己的数据 | 一般ユーザーは自分のデータだけ操作できる |
| 后端通过 Token 查询当前用户，不依赖前端传入 `userId` | バックエンドは Token から現在ユーザーを取得し、フロントの `userId` に依存しない |
| 管理员可以查看用户、重置密码、删除用户 | 管理者はユーザー確認、パスワードリセット、削除ができる |
| `/admin/**` 接口需要 `ADMIN` 角色 | `/admin/**` API は `ADMIN` ロールが必要 |
| 金额字段按 `DECIMAL(10,2)` 设计 | 金額項目は `DECIMAL(10,2)` を想定 |
| 前端金额输入限制为 `0.01` 到 `99999999.99`，小数步长 `0.01` | フロントでは `0.01` から `99999999.99`、小数ステップ `0.01` に制限 |
| 后端校验金额必须大于 0，并保留范围和小数位校验逻辑 | バックエンドでは金額が 0 より大きいことを確認し、範囲・小数桁チェックのロジックも保持 |
| 分类下已有收支记录时，不允许删除分类 | 収支記録があるカテゴリは削除不可 |
| 收支记录的分类必须属于当前登录用户 | 収支記録のカテゴリはログイン中ユーザー本人のものに限る |
| 收支记录类型必须和分类类型一致 | 収支記録タイプとカテゴリタイプは一致が必要 |
| OpenRouter API Key 使用环境变量 `OPENROUTER_API_KEY` | OpenRouter API Key は環境変数 `OPENROUTER_API_KEY` を使用 |
| API Key 不写死在代码中，也不暴露给前端 | API Key はコードに直書きせず、フロントにも公開しない |
| AI 助手加入简单的提示词泄露防护 | AI アシスタントには簡単なプロンプト漏えい防止処理を追加 |

---

## 主要接口 / 主な API

| Method | Path | 中文 | 日本語 |
|---|---|---|---|
| POST | `/users` | 用户注册 | ユーザー登録 |
| POST | `/login` | 用户登录 | ログイン |
| POST | `/logout` | 退出登录 | ログアウト |
| GET | `/me` | 查看当前用户信息 | ログイン中ユーザー情報取得 |
| PUT | `/me/password` | 修改当前用户密码 | パスワード変更 |
| GET | `/admin/users` | 管理员查询全部用户 | 管理者：全ユーザー取得 |
| GET | `/admin/users/{username}` | 管理员查询指定用户 | 管理者：指定ユーザー取得 |
| PUT | `/admin/users/{username}/password` | 管理员重置密码 | 管理者：パスワードリセット |
| DELETE | `/admin/users/{username}` | 管理员删除用户 | 管理者：ユーザー削除 |
| POST | `/transaction-categories` | 新增收支分类 | 収支カテゴリ追加 |
| GET | `/transaction-categories` | 查询自己的分类 | 自分のカテゴリ取得 |
| PUT | `/transaction-categories/{id}` | 修改自己的分类 | 自分のカテゴリ更新 |
| DELETE | `/transaction-categories/{id}` | 删除自己的分类 | 自分のカテゴリ削除 |
| POST | `/transaction-records` | 新增收支记录 | 収支記録追加 |
| GET | `/transaction-records` | 查询自己的全部记录 | 自分の全記録取得 |
| PUT | `/transaction-records/{id}` | 修改自己的记录 | 自分の記録更新 |
| DELETE | `/transaction-records/{id}` | 删除自己的记录 | 自分の記録削除 |
| GET | `/transaction-records/page` | 分页查询 | ページング検索 |
| GET | `/transaction-records/search` | 条件筛选分页查询 | 条件付きページング検索 |
| GET | `/transaction-stats/month` | 月度统计 | 月次統計 |
| GET | `/transaction-stats/category/{type}` | 收入 / 支出分类统计 | 収入 / 支出カテゴリ別集計 |
| POST | `/ai/chat` | AI 财务助手 | AI 財務アシスタント |

---

## 数据库 / データベース

中文说明：

当前开发环境使用 **MySQL 8.0.34**，数据库名为 `java_study`。项目主要使用三张表：`user`、`transaction_category`、`transaction_record`。

日本語説明：

現在の開発環境では **MySQL 8.0.34** を使用しています。データベース名は `java_study` です。主に `user`、`transaction_category`、`transaction_record` の3つのテーブルを使用します。

参考 SQL：

```sql
CREATE DATABASE IF NOT EXISTS java_study
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE java_study;

CREATE TABLE `user` (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL UNIQUE,
  password VARCHAR(20) NOT NULL,
  register_date DATE,
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  token VARCHAR(100)
);

CREATE TABLE transaction_category (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  type VARCHAR(20) NOT NULL
);

CREATE TABLE transaction_record (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  category_id INT NOT NULL,
  type VARCHAR(20) NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  remark VARCHAR(255),
  record_date DATE NOT NULL
);
```

---

## 运行方法 / 実行方法

### 1. 创建 MySQL 数据库 / MySQL データベース作成

中文说明：

先创建数据库 `java_study`，再创建 `user`、`transaction_category`、`transaction_record` 三张表。

日本語説明：

まず `java_study` データベースを作成し、その後 `user`、`transaction_category`、`transaction_record` の3テーブルを作成します。

```sql
CREATE DATABASE IF NOT EXISTS java_study
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

### 2. 修改数据库配置 / DB 設定の変更

中文说明：

修改 `src/main/resources/application.properties` 中的数据库连接信息。

日本語説明：

`src/main/resources/application.properties` のデータベース接続情報を自分の環境に合わせて変更します。

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/java_study?useSSL=false&serverTimezone=Asia/Tokyo
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 3. 设置 OPENROUTER_API_KEY 环境变量 / OPENROUTER_API_KEY 環境変数の設定

中文说明：

OpenRouter API Key 不写死在代码里，需要通过环境变量设置。

日本語説明：

OpenRouter API Key はコードに直接書かず、環境変数として設定します。

PowerShell 示例：

```powershell
$env:OPENROUTER_API_KEY="your_api_key_here"
```

IntelliJ IDEA 中也可以在 Run Configuration 的 Environment variables 中设置：

```text
OPENROUTER_API_KEY=your_api_key_here
```

### 4. 启动 Spring Boot / Spring Boot 起動

中文说明：

可以在 IntelliJ IDEA 中运行 `UserManagementApiApplication`，也可以使用 Maven 启动。

日本語説明：

IntelliJ IDEA で `UserManagementApiApplication` を実行するか、Maven で起動します。

```powershell
.\mvnw.cmd spring-boot:run
```

### 5. 浏览器访问 / ブラウザでアクセス

| 中文 | 日本語 | URL |
|---|---|---|
| 前端页面 | フロント画面 | http://localhost:8080/ |
| Swagger UI | Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | OpenAPI JSON | http://localhost:8080/v3/api-docs |

---

## 配置文件 / 設定ファイル

中文说明：

项目主要配置在 `src/main/resources/application.properties`。

日本語説明：

主な設定は `src/main/resources/application.properties` にあります。

```properties
spring.application.name=user-management-api

spring.datasource.url=jdbc:mysql://localhost:3306/java_study?useSSL=false&serverTimezone=Asia/Tokyo
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

mybatis.mapper-locations=classpath:mapper/*.xml

openrouter.api-key=${OPENROUTER_API_KEY}
openrouter.api-url=https://openrouter.ai/api/v1/chat/completions
openrouter.model=openai/gpt-oss-20b:free
```

---

## 请求示例 / リクエスト例

### 登录 / ログイン

```http
POST http://localhost:8080/login
Content-Type: application/json

{
  "username": "tom",
  "password": "123456"
}
```

### 新增收支分类 / 収支カテゴリ追加

```http
POST http://localhost:8080/transaction-categories
Content-Type: application/json
Authorization: your_token_here

{
  "name": "餐饮",
  "type": "expense"
}
```

### 新增收支记录 / 収支記録追加

```http
POST http://localhost:8080/transaction-records
Content-Type: application/json
Authorization: your_token_here

{
  "categoryId": 1,
  "type": "expense",
  "amount": 120.50,
  "remark": "晚饭",
  "recordDate": "2026-06-25"
}
```

### 条件筛选分页查询 / 条件付きページング検索

```http
GET http://localhost:8080/transaction-records/search?type=expense&categoryId=1&startRecordDate=2026-06-01&endOfRecordDate=2026-06-30&page=1&size=5
Authorization: your_token_here
```

### AI 财务助手 / AI 財務アシスタント

```http
POST http://localhost:8080/ai/chat
Content-Type: application/json
Authorization: your_token_here

{
  "message": "请给我一些控制生活支出的建议"
}
```

---

## 统一返回格式 / 共通レスポンス形式

中文说明：

后端接口统一返回 `Result` 对象。

日本語説明：

バックエンド API は共通の `Result` オブジェクトを返します。

```json
{
  "success": true,
  "message": "查询成功",
  "data": {}
}
```

| 字段 | 中文 | 日本語 |
|---|---|---|
| success | 是否成功 | 成功したかどうか |
| message | 返回提示信息 | レスポンスメッセージ |
| data | 返回数据，可以是对象、数组或 null | 返却データ。オブジェクト、配列、null の場合があります |

---

## Swagger / OpenAPI

中文说明：

项目已接入 Swagger / OpenAPI，可以在浏览器中查看接口分组、路径、参数和说明。

日本語説明：

Swagger / OpenAPI を導入しており、ブラウザで API のグループ、パス、パラメータ、説明を確認できます。

```text
http://localhost:8080/swagger-ui/index.html
```

---

## GitHub 展示用项目亮点 / GitHub 表示用ポイント

| 中文 | 日本語 |
|---|---|
| 使用 Spring Boot + MyBatis + MySQL 实现个人财务管理系统 | Spring Boot + MyBatis + MySQL で個人財務管理システムを実装 |
| 使用 Controller / Service / Mapper / XML 分层结构 | Controller / Service / Mapper / XML の層構成を採用 |
| 使用 Token 和 Interceptor 实现登录认证 | Token と Interceptor でログイン認証を実装 |
| 使用 role 字段实现 ADMIN / USER 权限控制 | role 項目で ADMIN / USER の権限制御を実装 |
| 普通用户只能操作自己的收支数据 | 一般ユーザーは自分の収支データのみ操作可能 |
| 使用 VO 控制返回数据，避免返回密码 | VO でレスポンスを制御し、パスワードを返さない |
| 使用统一异常处理返回错误信息 | 共通例外処理でエラーレスポンスを返す |
| 收支记录支持分页查询和条件筛选 | 収支記録はページング検索と条件検索に対応 |
| 统计模块支持月度统计和分类统计 | 統計機能は月次統計とカテゴリ別集計に対応 |
| 前端支持中日英切换和密码显示 / 隐藏 | フロントは中日英切り替えとパスワード表示 / 非表示に対応 |
| 接入 Swagger / OpenAPI，方便查看接口 | Swagger / OpenAPI で API を確認しやすい |
| 接入 OpenRouter AI API，实现简单 AI 财务助手 | OpenRouter AI API と連携し、簡単な AI 財務アシスタントを実装 |
| API Key 使用环境变量管理 | API Key は環境変数で管理 |

---

## 本地验证 / ローカル確認

中文说明：

可以使用 Maven 编译项目，确认代码可以正常通过编译。

日本語説明：

Maven でプロジェクトをコンパイルし、コードが正常にビルドできることを確認できます。

```powershell
.\mvnw.cmd -DskipTests compile
```

成功时会看到：

```text
BUILD SUCCESS
```

---

## 后续改进方向 / 今後の改善

| 中文 | 日本語 |
|---|---|
| 密码改为加密保存，例如 BCrypt | パスワードを BCrypt などでハッシュ化して保存 |
| Token 改为 JWT 或更完整的认证方案 | Token を JWT などより整った認証方式に変更 |
| 增加数据库外键和索引 | DB に外部キーやインデックスを追加 |
| 给 Service 层补充单元测试 | Service 層の単体テストを追加 |
| 增加 SQL 初始化脚本 | SQL 初期化スクリプトを追加 |
| AI 助手结合当前用户真实收支数据分析 | AI アシスタントが実際の収支データを使って分析できるようにする |
| 继续优化前端页面和移动端适配 | フロント画面とモバイル対応を改善 |
