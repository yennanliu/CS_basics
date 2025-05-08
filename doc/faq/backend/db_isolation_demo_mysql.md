Here's a **runnable SQL snippet** using a simple `accounts` table to demonstrate **isolation levels in MySQL**, particularly how anomalies can occur and be prevented with different settings.

---

## 🧪 Setup: Accounts Table

```sql
DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    balance INT
);

INSERT INTO accounts VALUES (1, 'Alice', 100), (2, 'Bob', 200);
```

---

## ✅ Demo 1: Dirty Read (READ UNCOMMITTED)

Open **Session A**:

```sql
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
START TRANSACTION;

UPDATE accounts SET balance = balance - 50 WHERE id = 1;
-- Don't commit yet
```

Then, in **Session B**:

```sql
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
START TRANSACTION;

SELECT * FROM accounts WHERE id = 1;
-- Sees Alice’s balance as 50 (even though it's uncommitted)
```

> 🔴 **Dirty read occurred**. If Session A rolls back, Session B saw incorrect data.

---

## ✅ Demo 2: Prevent Dirty Read (READ COMMITTED)

In **Session A**:

```sql
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
START TRANSACTION;

UPDATE accounts SET balance = balance - 50 WHERE id = 1;
-- Don't commit yet
```

In **Session B**:

```sql
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
START TRANSACTION;

SELECT * FROM accounts WHERE id = 1;
-- Sees balance still 100 (no dirty read)
```

> ✅ Safer — only committed data is visible.

---

## ✅ Demo 3: Non-Repeatable Read (Fixed by REPEATABLE READ)

In **Session A**:

```sql
SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
START TRANSACTION;

SELECT balance FROM accounts WHERE id = 2;
-- returns 200
```

Then in **Session B**:

```sql
START TRANSACTION;
UPDATE accounts SET balance = 250 WHERE id = 2;
COMMIT;
```

Back in **Session A**:

```sql
SELECT balance FROM accounts WHERE id = 2;
-- Still sees 200 (repeatable read)
COMMIT;
```

> ✅ Prevents **non-repeatable reads** — same query returns the same result.

---

## ✅ Demo 4: Phantom Read (Fixed by SERIALIZABLE)

In **Session A**:

```sql
SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;
START TRANSACTION;

SELECT * FROM accounts WHERE balance > 100;
-- Returns Bob only
```

In **Session B**:

```sql
START TRANSACTION;
INSERT INTO accounts VALUES (3, 'Charlie', 150);
-- This will block until Session A commits or rolls back
```

> ✅ Prevents **phantom reads** by locking ranges.

---

### 🧠 Summary

| Level            | Prevents               | Use Case Example                          |
| ---------------- | ---------------------- | ----------------------------------------- |
| Read Uncommitted | None                   | Fast analytics (unsafe)                   |
| Read Committed   | Dirty reads            | Common web apps (PostgreSQL default)      |
| Repeatable Read  | Dirty + non-repeatable | Inventory, money transfer (MySQL default) |
| Serializable     | All anomalies          | Critical booking, finance ops             |

---