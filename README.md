# 个人财务管理系统

## 项目介绍 / プロジェクト概要

这是一个基于 **Spring Boot + MyBatis + MySQL** 的个人财务管理系统，用于练习 Java 后端开发、RESTful API、三层架构、MyBatis XML、MySQL 数据库操作、Token 登录校验、角色权限控制、统一返回结果、参数校验、统一异常处理、Swagger 接口文档以及简单前端页面开发。

系统实现了用户注册登录、个人信息管理、收支分类管理、收支记录管理、分页查询、月度统计、分类统计、管理员用户管理以及 AI 助手功能。

このプロジェクトは、**Spring Boot + MyBatis + MySQL** を使用した個人財務管理システムです。Java バックエンド開発、RESTful API、三層構成、MyBatis XML、MySQL、Token 認証、権限管理、共通レスポンス、入力チェック、例外処理、Swagger API ドキュメント、簡単なフロントエンド実装を学習するために作成しました。

---

## 项目定位 / Project Position

```text
Spring Boot + MyBatis + MySQL + HTML/CSS/JavaScript 的个人财务管理系统
```

项目特点：

```text
1. 前后端分离思想的练习项目
2. 后端使用 Spring Boot 提供 REST API
3. 前端使用 HTML、CSS、JavaScript 调用后端接口
4. 使用 MyBatis XML 编写 SQL
5. 使用 Token 实现登录状态校验
6. 使用 role 字段实现简单管理员权限控制
7. 接入 OpenRouter API，实现 AI 助手功能
```

---

## 技术栈 / 使用技術

* Java 17
* Spring Boot 3.5.15
* Maven
* MySQL 8.0.34
* MyBatis
* MyBatis XML
* MySQL Connector/J
* Spring Validation
* Springdoc OpenAPI / Swagger UI
* Spring MVC Interceptor
* RestTemplate
* OpenRouter API
* HTML
* CSS
* JavaScript
* Git
* IntelliJ IDEA

---

## 功能列表 / 機能一覧

### 1. 用户认证功能 / User Authentication

* 用户注册
* 用户登录
* 登录成功后生成 Token
* Token 保存到数据库
* 退出登录时清空 Token
* 通过 `Authorization` 请求头携带 Token
* 拦截器统一校验登录状态

### 2. 当前用户功能 / Current User

* 查看当前登录用户信息
* 修改自己的登录密码
* 修改密码时校验当前密码
* 新密码不能和当前密码相同
* 返回用户信息时不返回密码

### 3. 收支分类管理 / Transaction Category Management

* 新增当前用户自己的收入或支出分类
* 查询当前用户自己的全部分类
* 修改当前用户自己的分类
* 删除当前用户自己的分类
* 同一个用户不能重复创建同名同类型分类
* 普通用户只能操作自己的分类数据

### 4. 收支记录管理 / Transaction Record Management

* 新增收入或支出记录
* 查询当前用户自己的全部收支记录
* 修改当前用户自己的收支记录
* 删除当前用户自己的收支记录
* 按收入或支出类型查询
* 按日期范围查询
* 按分类查询
* 分页查询
* 组合条件分页搜索
* 新增和修改记录时，会校验分类是否属于当前用户
* 记录类型必须和分类类型一致

### 5. 统计分析功能 / Statistics

* 查询指定月份的收入合计
* 查询指定月份的支出合计
* 查询指定月份的结余
* 按分类统计某月收入金额
* 按分类统计某月支出金额
* 按类型统计某月总金额
* 前端使用图表形式展示分类统计

### 6. 管理员功能 / Admin Management

* 管理员查询所有用户
* 管理员根据用户名查询用户
* 管理员重置指定用户密码
* 管理员删除用户
* 只有 `ADMIN` 角色可以访问 `/admin/**`
* 普通用户访问管理员接口会被拦截

### 7. AI 助手功能 / AI Assistant

* 前端提供 AI 助手页面
* 用户可以输入自然语言问题
* 前端调用后端 `/ai/chat` 接口
* 后端 `AiController` 接收请求
* 后端 `AiService` 使用 `RestTemplate` 调用 OpenRouter API
* API Key 通过环境变量 `OPENROUTER_API_KEY` 配置
* 前端不会直接接触 API Key
* AI 可回答记账、消费分析、省钱建议等问题

---

## 项目结构 / プロジェクト構成

```text
src/main/java/com/study/usermanagement
  common
    Result.java                     统一返回结果

  config
    WebConfig.java                  注册拦截器
    OpenApiConfig.java              Swagger / OpenAPI 配置

  controller
    UserController.java             用户注册
    LoginController.java            登录和退出
    MeController.java               当前用户信息和修改密码
    AdminUserController.java        管理员用户管理
    TransactionCategoryController.java  收支分类接口
    TransactionRecordController.java    收支记录接口
    TransactionStatsController.java     统计接口
    AiController.java               AI 助手接口

  dto
    AiRequest.java                  AI 请求体
    PasswordRequest.java            修改密码请求体

  entity
    User.java                       用户实体
    TransactionCategory.java        收支分类实体
    TransactionRecord.java          收支记录实体

  exception
    GlobalExceptionHandler.java     统一异常处理

  interceptor
    AuthInterceptor.java            Token 登录校验和管理员权限校验

  mapper
    UserMapper.java                 用户 Mapper 接口
    TransactionCategoryMapper.java  分类 Mapper 接口
    TransactionRecordMapper.java    记录 Mapper 接口
    TransactionStatsMapper.java     统计 Mapper 接口

  service
    UserService.java                用户业务逻辑
    TransactionCategoryService.java 分类业务逻辑
    TransactionRecordService.java   记录业务逻辑
    TransactionStatsService.java    统计业务逻辑
    AiService.java                  AI 调用业务逻辑

  vo
    LoginVO.java                    登录结果展示对象
    UserVO.java                     用户展示对象
    TransactionCategoryVO.java      分类展示对象
    TransactionRecordVO.java        记录展示对象
    MonthlyStatsVO.java             月度统计展示对象
    CategoryStatsVO.java            分类统计展示对象
    PageVO.java                     分页展示对象

src/main/resources
  mapper
    UserMapper.xml
    TransactionCategoryMapper.xml
    TransactionRecordMapper.xml
    TransactionStatsMapper.xml

  static
    index.html
    styles.css
    app.js

  application.properties
```

---

## 分层设计 / Layer Design

```text
Controller
接收 HTTP 请求，获取路径参数、请求体和当前登录用户。

Service
处理业务逻辑，例如参数校验、用户名是否存在、分类是否属于当前用户、记录类型是否正确等。

Mapper
定义数据库操作方法。

XML
编写具体 SQL 语句。

VO
控制返回给前端的数据结构，避免直接暴露数据库实体中的敏感字段。

DTO
接收前端请求体数据。
```

---

## 主要接口 / Main APIs

| 方法 / Method | 路径 / Path                                    | 说明 / Description                     | 是否需要 Token |
| ----------- | -------------------------------------------- | ------------------------------------ | ---------- |
| POST        | `/users`                                     | 注册用户 / Register user                 | 否          |
| POST        | `/login`                                     | 登录并返回 Token / Login and return Token | 否          |
| POST        | `/logout`                                    | 退出登录 / Logout                        | 是          |
| GET         | `/me`                                        | 查看当前用户信息 / Get current user info     | 是          |
| PUT         | `/me/password`                               | 修改自己的密码 / Change own password        | 是          |
| GET         | `/admin/users`                               | 管理员查询所有用户 / Admin get all users      | 是，ADMIN    |
| GET         | `/admin/users/{username}`                    | 管理员查询单个用户 / Admin get one user       | 是，ADMIN    |
| PUT         | `/admin/users/{username}/password`           | 管理员重置密码 / Admin reset password       | 是，ADMIN    |
| DELETE      | `/admin/users/{username}`                    | 管理员删除用户 / Admin delete user          | 是，ADMIN    |
| POST        | `/transaction-categories`                    | 新增当前用户的收支分类                          | 是          |
| GET         | `/transaction-categories`                    | 查询当前用户的全部分类                          | 是          |
| PUT         | `/transaction-categories/{id}`               | 修改当前用户自己的分类                          | 是          |
| DELETE      | `/transaction-categories/{id}`               | 删除当前用户自己的分类                          | 是          |
| POST        | `/transaction-records`                       | 新增当前用户的收支记录                          | 是          |
| GET         | `/transaction-records`                       | 查询当前用户的全部收支记录                        | 是          |
| PUT         | `/transaction-records/{id}`                  | 修改当前用户自己的收支记录                        | 是          |
| DELETE      | `/transaction-records/{id}`                  | 删除当前用户自己的收支记录                        | 是          |
| GET         | `/transaction-records/type/{type}`           | 按 income / expense 查询收支记录            | 是          |
| GET         | `/transaction-records/range`                 | 按日期范围查询收支记录                          | 是          |
| GET         | `/transaction-records/category/{categoryId}` | 按分类查询收支记录                            | 是          |
| GET         | `/transaction-records/page`                  | 分页查询收支记录                             | 是          |
| GET         | `/transaction-records/search`                | 组合条件分页搜索收支记录                         | 是          |
| GET         | `/transaction-stats/month`                   | 查询月度收入、支出和结余                         | 是          |
| GET         | `/transaction-stats/category/{type}`         | 按分类统计月度金额                            | 是          |
| GET         | `/transaction-stats/type-total`              | 统计某类型月度总金额                           | 是          |
| POST        | `/ai/chat`                                   | AI 助手聊天接口                            | 是          |

---

## 登录和 Token / Login and Token

登录成功后，后端会生成 Token，并保存到数据库 `user.token` 字段。

```text
POST /login
```

请求示例：

```json
{
  "username": "tom",
  "password": "123456"
}
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

访问需要登录的接口时，需要把 Token 放到请求头：

```http
Authorization: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

拦截器处理流程：

```text
1. 从请求头 Authorization 中获取 Token
2. 根据 Token 查询用户
3. 如果用户存在，把 currentUser 放入 request
4. 如果用户不存在，返回“登录状态无效”
5. 如果访问 /admin/**，继续判断 role 是否为 ADMIN
```

---

## 权限控制 / Permission Control

项目中使用 `role` 字段区分用户身份：

```text
USER   普通用户
ADMIN  管理员
```

权限规则：

```text
/me/**                    需要登录
/admin/**                 需要登录，并且 role 必须是 ADMIN
/transaction-categories/** 需要登录
/transaction-records/**    需要登录
/transaction-stats/**      需要登录
/ai/**                     需要登录
```

普通用户访问管理员接口时，会返回失败结果。

---

## AI 助手说明 / AI Assistant

AI 助手模块用于让用户通过自然语言获取记账、消费分析和省钱建议。

调用流程：

```text
前端 AI助手页面
→ POST /ai/chat
→ AiController
→ AiService
→ RestTemplate
→ OpenRouter API
→ 返回 AI 回复
```

请求示例：

```http
POST http://localhost:8080/ai/chat
Content-Type: application/json
Authorization: your_token_here

{
  "message": "请给我一些控制生活支出的建议"
}
```

返回示例：

```json
{
  "success": true,
  "message": "AI回复成功",
  "data": "控制生活支出可以从记录每月固定支出、减少冲动消费、设置预算等方面开始。"
}
```

AI API Key 不写在前端，也不直接写死在代码里，而是通过环境变量配置：

```text
OPENROUTER_API_KEY
```

这样可以避免 API Key 暴露在浏览器中。

---

## Swagger / OpenAPI 文档

项目已接入 `springdoc-openapi-starter-webmvc-ui`，并在 Controller 中使用 `@Tag`、`@Operation`、`@Parameter` 补充接口说明。

启动项目后可以访问：

```text
Swagger UI:
http://localhost:8080/swagger-ui/index.html

OpenAPI JSON:
http://localhost:8080/v3/api-docs
```

常用注解：

```text
@Tag        写在 Controller 类上，说明这一组接口属于哪个模块
@Operation  写在 Controller 方法上，说明这个接口做什么
@Parameter  写在路径参数或请求参数上，说明参数含义
```

---

## 数据库 / Database

当前开发环境使用：

```text
MySQL 8.0.34
```

数据库名：

```sql
java_study
```

主要数据表：

```text
user
transaction_category
transaction_record
```

### user 表

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

### transaction_category 表

```sql
CREATE TABLE transaction_category (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  type VARCHAR(20) NOT NULL
);
```

### transaction_record 表

```sql
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

## 配置 / Configuration

`src/main/resources/application.properties`

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

注意：

```text
真实的 OpenRouter API Key 不要写进 application.properties。
需要在运行配置或系统环境变量中设置 OPENROUTER_API_KEY。
```

---

## MyBatis 说明 / MyBatis

Mapper 接口示例：

```text
src/main/java/com/study/usermanagement/mapper/UserMapper.java
```

Mapper XML 示例：

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

项目中使用 XML 编写 SQL，方便练习复杂查询、分页查询、条件查询和统计查询。

---

## 前端页面 / Frontend

前端文件放在：

```text
src/main/resources/static
```

主要文件：

```text
index.html    页面结构
styles.css    页面样式
app.js        前端交互和接口请求
```

前端功能：

```text
1. 登录 / 注册页面
2. 首页月度总览
3. 个人信息和修改密码
4. 分类管理
5. 收支记录管理
6. 分页查询
7. 统计图表
8. 管理员页面
9. AI 助手页面
10. 中文 / 日语 / 英语切换
```

---

## 启动方式 / How to Run

1. 启动 MySQL。
2. 确认数据库 `java_study` 存在。
3. 确认 `user`、`transaction_category`、`transaction_record` 表存在。
4. 确认 `application.properties` 数据库配置正确。
5. 设置环境变量 `OPENROUTER_API_KEY`。
6. 启动 `UserManagementApiApplication`。
7. 打开前端页面：

```text
http://localhost:8080/
```

8. 或打开 Swagger UI：

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Maven 编译 / Build

Maven 编译：

```powershell
.\mvnw.cmd -DskipTests compile
```

如果看到：

```text
BUILD SUCCESS
```

说明项目编译成功。

---

## 测试顺序 / Test Flow

推荐测试顺序：

```text
1. 注册普通用户
2. 登录普通用户，复制 USER Token
3. 登录管理员，复制 ADMIN Token
4. 测试 /me
5. 测试 /me/password
6. 测试分类新增、查询、修改、删除
7. 测试收支记录新增、查询、修改、删除
8. 测试分页和组合条件查询
9. 测试月度统计和分类统计
10. 测试 USER 访问 /admin/** 应该失败
11. 测试 ADMIN 访问 /admin/** 应该成功
12. 测试 /ai/chat AI 助手接口
13. 测试 logout 后旧 Token 失效
```

---

## 请求示例 / Request Examples

### 注册用户

```http
POST http://localhost:8080/users
Content-Type: application/json

{
  "username": "spring01",
  "password": "123456"
}
```

### 登录

```http
POST http://localhost:8080/login
Content-Type: application/json

{
  "username": "spring01",
  "password": "123456"
}
```

### 查看自己的信息

```http
GET http://localhost:8080/me
Authorization: your_token_here
```

### 新增分类

```http
POST http://localhost:8080/transaction-categories
Content-Type: application/json
Authorization: your_token_here

{
  "name": "餐饮",
  "type": "expense"
}
```

### 新增收支记录

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

### 查询月度统计

```http
GET http://localhost:8080/transaction-stats/month?year=2026&month=6
Authorization: your_token_here
```

### AI 助手

```http
POST http://localhost:8080/ai/chat
Content-Type: application/json
Authorization: your_token_here

{
  "message": "请分析一下怎么减少生活支出"
}
```

---

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

---

## 安全处理 / Security Notes

* 查询用户信息时不返回密码。
* 使用 `UserVO` 控制返回给前端的用户数据。
* 登录接口使用 `LoginVO` 返回用户名、角色和 Token。
* 通过 `AuthInterceptor` 统一检查 Token。
* 通过 `role` 区分普通用户和管理员。
* 普通用户只能操作自己的分类、记录和统计数据。
* SQL 中使用 `user_id` 限制数据范围。
* AI API Key 不放在前端，不写死在代码里。
* OpenRouter API Key 通过环境变量配置。

---

## 项目亮点 / Project Highlights

```text
1. 使用 Spring Boot + MyBatis + MySQL 实现完整的个人财务管理系统。
2. 实现用户注册、登录、Token 校验和管理员权限控制。
3. 使用 Controller / Service / Mapper / XML 分层结构，职责清晰。
4. 使用 VO 控制返回数据，避免直接返回密码等敏感信息。
5. 使用 Interceptor 统一处理登录校验和管理员权限判断。
6. 使用 GlobalExceptionHandler 统一处理参数校验和异常返回。
7. 收支记录支持分页查询和组合条件搜索。
8. 统计模块支持月度收入、支出、结余和分类金额统计。
9. 前端使用 HTML、CSS、JavaScript 实现完整页面交互。
10. 接入 OpenRouter API，实现 AI 助手功能。
11. API Key 通过环境变量配置，避免前端泄露。
12. 接入 Swagger UI，方便查看和测试接口文档。
```

---

## 学习重点 / Learning Points

* Spring Boot 项目创建和启动
* RESTful API 设计
* Controller / Service / Mapper 分层
* MyBatis Mapper 接口和 XML
* MySQL 数据库操作
* 参数校验 `@Valid` / `@NotBlank` / `@Size`
* 统一异常处理 `@RestControllerAdvice`
* 统一返回对象 `Result`
* Token 登录状态管理
* Interceptor 拦截器
* 简单角色权限控制
* VO / DTO 的使用
* 分页查询
* 条件查询
* 月度统计 SQL
* Swagger / OpenAPI 接口文档
* 前端 HTML / CSS / JavaScript
* 调用外部 AI API
* 环境变量保存 API Key
* Git 本地版本管理

---

## 后续优化方向 / Next Steps

后续可以继续优化：

```text
1. 删除分类前检查该分类下是否已有收支记录。
2. 密码加密保存，例如使用 BCrypt。
3. Token 改为 JWT。
4. AI 助手结合当前用户真实收支数据进行分析。
5. 增加更多接口测试用例。
6. 增加数据库建表 SQL 文件。
7. 优化前端页面样式和移动端适配。
8. 为项目整理简历用介绍和面试回答。
```
