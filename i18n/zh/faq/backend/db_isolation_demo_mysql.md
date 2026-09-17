<!-- e3219a836834 -->
# MySQL 隔離級別設定與實際演示

<!-- e8d6d491b623 -->
## 設定隔離級別

<!-- a3ea06cf8bb0 -->
### Session 級別（當前連線）

<!--CODE-->

<!-- 2ce8a4e4277d -->
### 全域級別（影響所有新連線）

<!--CODE-->

<!-- 657884dc1af0 -->
### 持久化設定（`my.cnf`）

<!--CODE-->

<!--CODE-->

<!-- cdb893db9530 -->
### 查看當前隔離級別

<!--CODE-->

<!-- 1de962e5299e -->
### Java JDBC 設定

<!--CODE-->

---

<!-- d988072a6fc3 -->
## 隔離級別演示

> 使用 `accounts` 表，示範各隔離級別下不同異常的發生與防止。

<!-- dbbe5b1652f1 -->
## 準備：建立 accounts 表

<!--CODE-->

---

<!-- fb6528e03608 -->
## ✅ 演示 1：髒讀（READ UNCOMMITTED）

開啟 **Session A**：

<!--CODE-->

接著在 **Session B**：

<!--CODE-->

> 🔴 **發生髒讀**。如果 Session A 之後回滾，Session B 讀到的就是不存在的資料。

---

<!-- 8a8095b5bb4a -->
## ✅ 演示 2：防止髒讀（READ COMMITTED）

在 **Session A**：

<!--CODE-->

在 **Session B**：

<!--CODE-->

> ✅ 比較安全 —— 只看得到已提交的資料。

---

<!-- ed1204893d4b -->
## ✅ 演示 3：不可重複讀（由 REPEATABLE READ 解決）

在 **Session A**：

<!--CODE-->

接著在 **Session B**：

<!--CODE-->

回到 **Session A**：

<!--CODE-->

> ✅ 避免**不可重複讀** —— 同一個查詢在同一個交易裡回傳同樣的結果。

---

<!-- a5297f96e3b3 -->
## ✅ 演示 4：幻讀（由 SERIALIZABLE 解決）

在 **Session A**：

<!--CODE-->

在 **Session B**：

<!--CODE-->

> ✅ 以範圍鎖（range lock）避免**幻讀**。

---

<!-- a3bac143d913 -->
### 🧠 總結

| 隔離級別         | 能防止什麼             | 適用場景                                  |
| ---------------- | ---------------------- | ----------------------------------------- |
| Read Uncommitted | 什麼都不防             | 求快的分析查詢（不安全）                  |
| Read Committed   | 髒讀                   | 一般網站應用（PostgreSQL 預設）           |
| Repeatable Read  | 髒讀 + 不可重複讀      | 庫存、轉帳（MySQL 預設）                  |
| Serializable     | 所有異常               | 訂票、金融等關鍵操作                      |

---
