# Implement `Leaderboard` via Redis


##  Sorted set (ZSET)

- https://systemdesignschool.io/problems/leetcode/solution



Using **Redis for a leaderboard** is a common and efficient pattern, especially when you need **fast reads, writes, and ranking operations**. Redis provides a built-in data structure perfect for this: **Sorted Sets** (`ZSET`).

---

## 🧠 Redis Sorted Set Primer

A **Sorted Set** in Redis is a collection of unique strings (like user IDs or usernames), each associated with a floating-point **score**. The set is automatically ordered by score.

For a leaderboard:

* The **user/player ID** is the **member**.
* The **score (e.g. points, kills, rating)** is the **score** in the sorted set.

---

## ✅ Basic Leaderboard Operations with Redis

### 1. Add or Update a Player's Score

```bash
ZADD leaderboard <score> <user_id>
```

✅ Adds the user if not present, or updates the score if already in the set.

**Example:**

```bash
ZADD leaderboard 1500 "user:123"
ZADD leaderboard 1800 "user:456"
```

### 2. Get Top N Players

```bash
ZREVRANGE leaderboard 0 N-1 WITHSCORES
```

* Use `ZREVRANGE` for **descending order** (highest score first).
* `WITHSCORES` returns both user IDs and scores.

**Example:**

```bash
ZREVRANGE leaderboard 0 9 WITHSCORES  # Top 10 players
```

### 3. Get Player Rank

```bash
ZREVRANK leaderboard <user_id>
```

* Returns the 0-based **rank** of the player in descending order.

**Example:**

```bash
ZREVRANK leaderboard "user:123"  # e.g., returns 5
```

### 4. Get Player Score

```bash
ZSCORE leaderboard <user_id>
```

**Example:**

```bash
ZSCORE leaderboard "user:123"  # e.g., returns 1500.0
```

### 5. Remove a Player

```bash
ZREM leaderboard <user_id>
```

**Example:**

```bash
ZREM leaderboard "user:123"
```

---

## 🧱 Optional Enhancements

### 🕐 Time-based Leaderboards

For weekly/monthly leaderboards:

* Use **separate keys** per time period:

  * `leaderboard:weekly:2025-W20`
  * `leaderboard:monthly:2025-05`
* Use a scheduled job to **archive or delete old leaderboards**.

### 🧾 Metadata Support

You can’t directly store extra data (e.g., username, avatar) in Redis Sorted Sets. Solutions:

* Store just the user ID in the sorted set.
* Store user metadata in a **Redis hash or another DB**:

  ```bash
  HGETALL user:123  # e.g., {"username": "Alice", "avatar": "..."}
  ```

### 🔁 Score Incrementation

```bash
ZINCRBY leaderboard 100 "user:123"
```

Adds 100 to the existing score of `user:123`.

---

## 🧪 Sample Use Case (Gaming)

```bash
ZADD game:leaderboard 2000 "player:alice"
ZADD game:leaderboard 1900 "player:bob"
ZREVRANGE game:leaderboard 0 4 WITHSCORES  # top 5
ZREVRANK game:leaderboard "player:alice"   # alice's rank
```

---

## 🧩 Scalability Notes

* Redis is **in-memory**: super fast, but data is volatile unless persistence (RDB/AOF) is enabled.
* For very large user bases, consider **sharding** leaderboards or using **Redis Cluster**.
* If precision isn’t critical, you can **batch updates** or use **approximate leaderboards** (e.g., with periodic sync to a database).

---