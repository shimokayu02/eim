# eim

management tool of employee info.
---

Spring boot と Vue.js を元にした、お勉強レベルでの実装サンプルです。

# prerequisites

* Java11 or later.
* PostgreSQL 9.5 or later.
* Spring boot + Spring Security + Lombok + Gradle
* Node.js + Vue CLI + Yarn or npm + core-js

# PostgreSQL prerequisite details

次の手順で ① database ② role ③ schema を作成してください。

０．PostgreSQLへ接続する

```
C:\Users\5h1m0kayu02>psql -U postgres
```

## create database

<details><summary>Click to expand</summary><br>

１．事前確認

```sql
postgres=#  \l
                                             データベース一覧
   名前    |  所有者  | エンコーディング |      照合順序      | Ctype(変換演算子)  |     アクセス権限
-----------+----------+------------------+--------------------+--------------------+-----------------------
 postgres  | postgres | UTF8             | Japanese_Japan.932 | Japanese_Japan.932 |
 template0 | postgres | UTF8             | Japanese_Japan.932 | Japanese_Japan.932 | =c/postgres          +
           |          |                  |                    |                    | postgres=CTc/postgres
 template1 | postgres | UTF8             | Japanese_Japan.932 | Japanese_Japan.932 | =c/postgres          +
           |          |                  |                    |                    | postgres=CTc/postgres
(3 行)
```
* `hosdb` というデータベースが既に作成済みで無いこと。

２．データベースを作成する

```sql
postgres=# create database hosdb;
CREATE DATABASE
```

３．事後確認

```sql
postgres=# \l
                                             データベース一覧
   名前    |  所有者  | エンコーディング |      照合順序      | Ctype(変換演算子)  |     アクセス権限
-----------+----------+------------------+--------------------+--------------------+-----------------------
 hosdb     | postgres | UTF8             | Japanese_Japan.932 | Japanese_Japan.932 |
 postgres  | postgres | UTF8             | Japanese_Japan.932 | Japanese_Japan.932 |
 template0 | postgres | UTF8             | Japanese_Japan.932 | Japanese_Japan.932 | =c/postgres          +
           |          |                  |                    |                    | postgres=CTc/postgres
 template1 | postgres | UTF8             | Japanese_Japan.932 | Japanese_Japan.932 | =c/postgres          +
           |          |                  |                    |                    | postgres=CTc/postgres
(4 行)
```
* `hosdb` というデータベースが作成されていること。

</details>

## create role

<details><summary>Click to expand</summary><br>

１．事前確認

```sql
postgres=# \du
                                             ロール一覧
 ロール名 |                                   属性                                   | 所属グループ
----------+--------------------------------------------------------------------------+--------------
 postgres | スーパユーザ, ロール作成可, DB作成可, レプリケーション可, RLS のバイパス | {}
```
* `hosuser` というロールが既に作成済みで無いこと。

２．ロールを作成する

```sql
postgres=# create ROLE hosuser login password '54321';
CREATE ROLE
```

３．事後確認

```sql
postgres=# \du
                                             ロール一覧
 ロール名 |                                   属性                                   | 所属グループ
----------+--------------------------------------------------------------------------+--------------
 hosuser  |                                                                          | {}
 postgres | スーパユーザ, ロール作成可, DB作成可, レプリケーション可, RLS のバイパス | {}
```
* `hosuser` というロールが作成されていること。

</details>

## create schema

<details><summary>Click to expand</summary><br>

１．hosdb に接続

```sql
postgres=# \c hosdb
データベース"hosdb"にユーザ"postgres"として接続しました。
```

２．事前確認

```sql
hosdb=# \dn
   スキーマ一覧
  名前  |  所有者
--------+----------
 public | postgres
(1 行)
```
* `eim` というスキーマが既に作成済みで無いこと。

３．`hosuser` を指定してスキーマを作成

```sql
hosdb=# create schema eim authorization hosuser;
CREATE SCHEMA
```

４．事後確認

```sql
hosdb=# \dn
   スキーマ一覧
  名前  |  所有者
--------+----------
 eim    | hosuser
 public | postgres
(2 行)
```
* 所有者が `hosuser` の `eim` スキーマが作成されていること。

</details>

# Getting Started (動作確認)

１．コンソール (Command Prompt) で `eim` ディレクトリへ移動し、`gradlew bootRun` を実行

２．コンソール (Node.js command prompt) で `eim\ui` ディレクトリへ移動し、`yarn serve` を実行

３．ブラウザを立ち上げて `http://localhost:8080/hos-web/eim/login` へアクセス
* 以下の username と password を入力してログイン
    - usr : `b@b`
    - pwd : `54321`

# FYI

本サンプルをビルドするには、事前に以下のインストールを行っておく必要があります。
> 以降はコンソール (Node.js command prompt) の利用を前提としています。

<details><summary>Click to expand</summary><br>

① Yarnをインストール

```
C:\Users\5h1m0kayu02>npm install -g yarn
```

② core-jsをインストール

```
C:\Users\5h1m0kayu02>npm install --save core-js
```

③ `eim\ui` ディレクトリへ移動し、`package.json` 内のライブラリをインストール

```
C:\Users\5h1m0kayu02>cd C:\Users\5h1m0kayu02\Documents\GitHub\eim\ui

C:\Users\5h1m0kayu02\Documents\GitHub\eim\ui>dir | findstr package.json

C:\Users\5h1m0kayu02\Documents\GitHub\eim\ui>yarn install
```

</details>

# License

本サンプルのライセンスは *MIT License* です。
