In **MySQL**, you can configure the **transaction isolation level** at several scopes:

* **Global** (affects all sessions, until MySQL restarts or changes)
* **Session** (affects just the current connection)
* **Per Transaction** (using SQL directly)

---

## ✅ 1. 🔧 Set Isolation Level for a Session

Before starting a transaction:

```sql
SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
START TRANSACTION;
-- your queries here
COMMIT;
```

Supported values:

* `READ UNCOMMITTED`
* `READ COMMITTED`
* `REPEATABLE READ` (default for InnoDB)
* `SERIALIZABLE`

---

## ✅ 2. 🔧 Set Isolation Level Globally (Permanent Until Restart)

```sql
SET GLOBAL TRANSACTION ISOLATION LEVEL READ COMMITTED;
```

> ⚠️ Requires `SUPER` or `SYSTEM_VARIABLES_ADMIN` privilege.

This affects **new sessions**, not existing ones.

---

## ✅ 3. 🔧 Set Isolation Level in `my.cnf` (Persistent Across Restarts)

Edit your MySQL config file (usually `/etc/mysql/my.cnf` or `/etc/my.cnf`):

```ini
[mysqld]
transaction-isolation = READ-COMMITTED
```

Then restart MySQL:

```bash
sudo systemctl restart mysql
```

---

## 🧪 Check Current Isolation Level

* **Session**:

  ```sql
  SELECT @@session.tx_isolation;  -- deprecated
  SELECT @@session.transaction_isolation;
  ```

* **Global**:

  ```sql
  SELECT @@global.transaction_isolation;
  ```

---

## 🛠️ Example: Use Isolation Per Query Session in Java (JDBC)

```java
Connection conn = dataSource.getConnection();
conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
conn.setAutoCommit(false);

// ... perform queries

conn.commit();
```
