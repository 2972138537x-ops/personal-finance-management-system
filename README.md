# User Management API

## 项目介绍 / プロジェクト概要

这是一个基于 Spring Boot 的用户管理 API 项目，用于练习 Java 后端开发中的三层架构、RESTful 接口、MySQL 数据库操作和统一返回结果。

このプロジェクトは、Spring Boot を使用したユーザー管理 API です。Java バックエンド開発における三層構成、RESTful API、MySQL 操作、共通レスポンス形式を学習するために作成しました。

## 技术栈 / 使用技術

- Java 17
- Spring Boot 3.5.15
- Maven
- MySQL 5.7
- JdbcTemplate
- Git

## 功能列表 / 機能一覧

- 用户注册 / ユーザー登録
- 用户登录 / ユーザーログイン
- 查询所有用户 / ユーザー一覧取得
- 根据用户名查询用户 / ユーザー名による検索
- 修改用户密码 / パスワード変更
- 删除用户 / ユーザー削除
- 查询接口不返回密码，使用 UserVO 控制响应数据 / UserVO を使用し、レスポンスにパスワードを含めない

## 项目结构 / プロジェクト構成

```text
controller  接收 HTTP 请求 / HTTP リクエストを受け取る
service     处理业务逻辑 / 業務ロジックを処理する
dao         操作 MySQL 数据库 / MySQL データベースを操作する
entity      数据库实体对象 / データベース用のエンティティ
vo          返回给前端的展示对象 / フロントエンドに返す表示用オブジェクト
common      通用返回结果 Result / 共通レスポンス Result
```

## 主要接口 / 主な API

| 方法 / Method | 路径 / Path | 说明 / Description |
|---|---|---|
| GET | `/users` | 查询所有用户 / ユーザー一覧取得 |
| GET | `/users/{username}` | 根据用户名查询用户 / ユーザー名で検索 |
| POST | `/users` | 注册用户 / ユーザー登録 |
| POST | `/login` | 用户登录 / ユーザーログイン |
| PUT | `/users/{username}/password` | 修改密码 / パスワード変更 |
| DELETE | `/users/{username}` | 删除用户 / ユーザー削除 |

## 数据库 / データベース

数据库名 / Database：

```sql
java_study
```

用户表 / User table：

```sql
user
```

主要字段 / Main columns：

```text
id
username
password
register_date
```

## 配置示例 / 設定例

`src/main/resources/application.properties`

```properties
spring.application.name=user-management-api

spring.datasource.url=jdbc:mysql://localhost:3306/java_study?useSSL=false&serverTimezone=Asia/Tokyo
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## 启动方式 / 起動方法

1. 启动 MySQL / MySQL を起動する
2. 创建或确认数据库 `java_study` / `java_study` データベースを作成または確認する
3. 确认 `application.properties` 中的数据库配置 / `application.properties` のデータベース設定を確認する
4. 启动 `UserManagementApiApplication` / `UserManagementApiApplication` を起動する
5. 访问接口 / API にアクセスする

示例 / Example：

```text
http://localhost:8080/users
```

## 请求示例 / リクエスト例

用户登录 / Login：

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/login" -ContentType "application/json" -Body '{"username":"tom","password":"123456"}'
```

用户注册 / Register：

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/users" -ContentType "application/json" -Body '{"username":"spring01","password":"123456"}'
```

修改密码 / Change password：

```powershell
Invoke-RestMethod -Method Put -Uri "http://localhost:8080/users/spring01/password" -ContentType "application/json" -Body '{"password":"abcdef"}'
```

删除用户 / Delete user：

```powershell
Invoke-RestMethod -Method Delete -Uri "http://localhost:8080/users/spring01"
```

## 返回格式 / レスポンス形式

项目使用统一返回对象 `Result`。

このプロジェクトでは、共通レスポンスオブジェクト `Result` を使用しています。

```json
{
  "success": true,
  "message": "查询成功",
  "data": {
    "username": "tom"
  }
}
```

字段说明 / Fields：

```text
success  是否成功 / 成功したかどうか
message  返回信息 / メッセージ
data     返回数据 / レスポンスデータ
```

## 安全处理 / セキュリティ対応

查询接口不会直接返回数据库实体 `User`，而是使用 `UserVO` 控制返回字段，避免把 `password` 暴露给前端。

検索 API では、データベースエンティティである `User` をそのまま返さず、`UserVO` を使用して返却フィールドを制御しています。これにより、`password` がフロントエンドに公開されないようにしています。

## 学习重点 / 学習ポイント

- RESTful API 设计 / RESTful API 設計
- Controller / Service / Dao 三层结构 / Controller・Service・Dao の三層構成
- JdbcTemplate 操作 MySQL / JdbcTemplate による MySQL 操作
- Result 统一返回格式 / Result による共通レスポンス
- UserVO 隐藏敏感字段 / UserVO による機密情報の非表示
- Git 本地版本管理 / Git によるローカルバージョン管理

