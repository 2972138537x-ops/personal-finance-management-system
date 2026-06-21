# User Management API

## 项目介绍 / プロジェクト概要

这是一个基于 Spring Boot 的用户管理后端 API 项目，用于练习 Java 后端开发中的 RESTful 接口、三层结构、MyBatis、MySQL、统一返回结果、参数校验、统一异常处理、token 登录校验和简单角色权限控制。

このプロジェクトは、Spring Boot を使用したユーザー管理 API です。RESTful API、三層構成、MyBatis、MySQL、共通レスポンス、入力チェック、例外処理、トークン認証、簡単なロール権限管理を学習するために作成しました。

当前项目定位：

```text
Spring Boot + MyBatis + MySQL 的后端 REST API 项目
```

## 技术栈 / 使用技術

- Java 17
- Spring Boot 3.5.15
- Maven
- MySQL 5.7
- MyBatis
- MySQL Connector/J
- Spring Validation
- Git
- IntelliJ IDEA HTTP Client (`test.http`)

## 功能列表 / 機能一覧

### 公共功能 / Public APIs

- 用户注册 / ユーザー登録
- 用户登录并返回 token / ログインして token を返す
- 用户退出登录并清空 token / ログアウトして token を削除する
- 参数校验 / 入力チェック
- 统一异常处理 / 共通例外処理
- 统一 Result 返回格式 / 共通レスポンス形式

### 普通用户功能 / User APIs

- 查看自己的信息 `GET /me`
- 修改自己的密码 `PUT /me/password`
- 普通用户只能操作自己的数据

### 管理员功能 / Admin APIs

- 查询所有用户 `GET /admin/users`
- 根据用户名查询用户 `GET /admin/users/{username}`
- 重置用户密码 `PUT /admin/users/{username}/password`
- 删除用户 `DELETE /admin/users/{username}`
- 只有 `ADMIN` 角色可以访问 `/admin/**`

## 项目结构 / プロジェクト構成

```text
src/main/java/com/study/usermanagement
  common       通用返回结果 Result
  config       Spring MVC 配置，注册拦截器
  controller   接收 HTTP 请求
  dto          接收请求体数据
  entity       数据库实体对象
  exception    统一异常处理
  interceptor  登录校验和权限校验
  mapper       MyBatis Mapper 接口
  service      处理业务逻辑
  vo           返回给前端的展示对象

src/main/resources
  mapper       MyBatis XML SQL 映射文件
  application.properties
```

## 分层说明 / Layer Design

```text
Controller
接收 HTTP 请求，获取路径参数、请求体和当前登录用户。

Service
处理业务逻辑，例如用户名是否存在、密码是否合法、是否允许操作。

Mapper
通过 MyBatis 操作 MySQL 数据库。

XML
编写具体 SQL 语句。
```

## 主要接口 / Main APIs

| 方法 / Method | 路径 / Path | 说明 / Description | 是否需要 token |
|---|---|---|---|
| POST | `/users` | 注册用户 / Register user | 否 |
| POST | `/login` | 登录并返回 token / Login and return token | 否 |
| POST | `/logout` | 退出登录 / Logout | 是 |
| GET | `/me` | 查看自己的信息 / Get current user info | 是 |
| PUT | `/me/password` | 修改自己的密码 / Change own password | 是 |
| GET | `/admin/users` | 管理员查询所有用户 / Admin get all users | 是，ADMIN |
| GET | `/admin/users/{username}` | 管理员查询单个用户 / Admin get one user | 是，ADMIN |
| PUT | `/admin/users/{username}/password` | 管理员重置密码 / Admin reset password | 是，ADMIN |
| DELETE | `/admin/users/{username}` | 管理员删除用户 / Admin delete user | 是，ADMIN |

## 登录和 token / Login and Token

登录成功后，后端会生成 token，并保存到数据库 `user.token` 字段。

```text
POST /login
```

返回示例：

```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "username": "tom",
    "role": "ADMIN",
    "token": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
  }
}
```

访问需要登录的接口时，请把 token 放到请求头：

```http
Authorization: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

拦截器会在请求进入 Controller 之前检查 token：

```text
1. 从请求头 Authorization 取 token
2. 根据 token 查询用户
3. 找到用户则放行
4. 找不到用户则返回登录状态无效
```

## 权限控制 / Permission Control

项目中使用 `role` 字段区分用户身份：

```text
USER   普通用户
ADMIN  管理员
```

权限规则：

```text
/me/**      需要登录
/admin/**   需要登录，并且 role 必须是 ADMIN
```

如果普通用户访问管理员接口，会返回失败结果。

## 数据库 / Database

数据库名：

```sql
java_study
```

用户表：

```sql
user
```

主要字段：

```text
id
username
password
register_date
role
token
```

示例建表 SQL：

```sql
CREATE TABLE `user` (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL UNIQUE,
  password VARCHAR(20) NOT NULL,
  register_date DATE,
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  token VARCHAR(100)
);
```

## 配置 / Configuration

`src/main/resources/application.properties`

```properties
spring.application.name=user-management-api

spring.datasource.url=jdbc:mysql://localhost:3306/java_study?useSSL=false&serverTimezone=Asia/Tokyo
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

mybatis.mapper-locations=classpath:mapper/*.xml
```

## MyBatis 说明 / MyBatis

Mapper 接口：

```text
src/main/java/com/study/usermanagement/mapper/UserMapper.java
```

Mapper XML：

```text
src/main/resources/mapper/UserMapper.xml
```

MyBatis 关键点：

```text
@Mapper                 声明 Mapper 接口
@Param                  给 SQL 参数命名
namespace               对应 Mapper 接口完整类名
id                      对应 Mapper 接口方法名
resultType              每一行结果要封装成的 Java 类型
#{参数名}               安全占位符，类似 JDBC 的 ?
```

## 启动方式 / How to Run

1. 启动 MySQL。
2. 确认数据库 `java_study` 存在。
3. 确认 `user` 表存在，并包含 `role` 和 `token` 字段。
4. 确认 `application.properties` 数据库配置正确。
5. 启动 `UserManagementApiApplication`。
6. 使用 `test.http` 测试接口。

Maven 编译：

```powershell
.\mvnw.cmd -DskipTests compile
```

如果看到：

```text
BUILD SUCCESS
```

说明项目编译成功。

## 测试方式 / API Test

项目根目录有：

```text
test.http
```

推荐测试顺序：

```text
1. 注册用户
2. 登录普通用户，复制 USER token
3. 登录管理员，复制 ADMIN token
4. 测试 /me
5. 测试 /me/password
6. 测试 USER 访问 /admin/** 应该失败
7. 测试 ADMIN 访问 /admin/** 应该成功
8. 测试 logout 后旧 token 失效
```

## 请求示例 / Request Examples

注册用户：

```http
POST http://localhost:8080/users
Content-Type: application/json

{
  "username": "spring01",
  "password": "123456"
}
```

登录：

```http
POST http://localhost:8080/login
Content-Type: application/json

{
  "username": "tom",
  "password": "123456"
}
```

查看自己的信息：

```http
GET http://localhost:8080/me
Authorization: your_token_here
```

管理员查询所有用户：

```http
GET http://localhost:8080/admin/users
Authorization: admin_token_here
```

## 返回格式 / Response Format

项目使用统一返回对象 `Result`：

```json
{
  "success": true,
  "message": "查询成功",
  "data": {
    "username": "tom",
    "role": "ADMIN"
  }
}
```

字段说明：

```text
success  是否成功
message  返回信息
data     返回数据
```

## 安全处理 / Security Notes

- 查询接口不会直接返回完整 `User`。
- 使用 `UserVO` 控制返回给前端的数据。
- 登录接口使用 `LoginVO` 返回用户名、角色和 token。
- 密码不会在普通查询接口中返回。
- 通过 Interceptor 统一检查 token。
- 通过 role 区分普通用户和管理员。

## 学习重点 / Learning Points

- Spring Boot 项目创建和启动
- RESTful API
- Controller / Service / Mapper 分层
- MyBatis Mapper 接口和 XML
- MySQL 数据库操作
- 参数校验 `@Valid` / `@NotBlank` / `@Size`
- 统一异常处理 `@RestControllerAdvice`
- 统一返回对象 `Result`
- token 登录状态
- Interceptor 拦截器
- 简单角色权限控制
- Git 本地版本管理

## 后续计划 / Next Steps

后续计划在用户系统基础上增加个人收支管理模块：

```text
Personal Finance API
```

计划功能：

- 新增收入记录
- 新增支出记录
- 查询当前用户自己的收支记录
- 修改收支记录
- 删除收支记录
- 按月份统计收入、支出、结余
- 按类型统计支出

