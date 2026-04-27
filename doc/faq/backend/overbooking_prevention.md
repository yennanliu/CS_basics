# Overbooking Prevention in Booking Systems

## Problem

In high-concurrency booking systems (flash sales, ticket booking, hotel reservations), multiple requests can simultaneously read the same available count and all proceed — leading to **overselling/overbooking**.

```
Thread A: reads stock=1 → proceeds
Thread B: reads stock=1 → proceeds   ← race condition
Both confirm → stock goes to -1
```

---

## Strategies

### 1. Atomic DECR (Redis)

Use Redis `DECR` or `DECRBY` — atomic by nature, no locking needed.

```lua
-- Single command, atomic
result = redis.call('DECR', 'stock:item_123')
if result < 0 then
    redis.call('INCR', 'stock:item_123')  -- rollback
    return 0  -- out of stock
end
return 1  -- success
```

**Flow:**
```
Client → Redis DECR → if result >= 0: proceed to DB write
                     if result < 0:  rollback INCR, reject
```

**Pros:** Ultra-high throughput, minimal latency, no lock contention  
**Cons:** Simple logic only — no per-user limits, no complex conditions  
**Use case:** Flash sales, simple inventory (e.g., "100 items, first come first served")

---

### 2. Lua Script (Redis)

Execute a multi-step check atomically inside Redis. Lua scripts run as a single atomic operation — no other command can interleave.

```lua
-- lua script: limit 1 booking per user
local stock_key  = KEYS[1]   -- e.g. "stock:event_42"
local user_key   = KEYS[2]   -- e.g. "booked:event_42:user_99"

-- check if user already booked
if redis.call('EXISTS', user_key) == 1 then
    return -1  -- duplicate booking
end

-- check and decrement stock
local stock = tonumber(redis.call('GET', stock_key))
if stock <= 0 then
    return 0  -- out of stock
end

redis.call('DECR', stock_key)
redis.call('SET', user_key, 1, 'EX', 86400)  -- mark user as booked (TTL 1 day)
return 1  -- success
```

**Java call:**
```java
String lua = "...";  // script above
List<String> keys = Arrays.asList("stock:event_42", "booked:event_42:" + userId);
Long result = jedis.eval(lua, keys, Collections.emptyList());
// -1 = duplicate, 0 = sold out, 1 = success
```

**Pros:** Atomic multi-condition check, handles per-user dedup  
**Cons:** Redis single-threaded during script — keep scripts short; harder to debug  
**Use case:** "Limit 1 per user", coupon redemption, event registration

---

### 3. Distributed Lock (Redis / Zookeeper)

Acquire a lock before entering the critical section. Ensures only one process executes the booking logic at a time.

```java
String lockKey = "lock:booking:event_42";
String lockValue = UUID.randomUUID().toString();
int expireMs = 3000;

// try acquire lock (SET NX PX)
boolean acquired = redis.set(lockKey, lockValue, "NX", "PX", expireMs) != null;
if (!acquired) {
    throw new BusyException("System busy, please retry");
}

try {
    // safe to run complex logic now
    int stock = db.queryStock(eventId);
    if (stock <= 0) throw new SoldOutException();

    boolean alreadyBooked = db.hasBooked(userId, eventId);
    if (alreadyBooked) throw new DuplicateException();

    db.deductStock(eventId);
    db.createOrder(userId, eventId);
} finally {
    // release lock only if we still own it (Lua for atomicity)
    String releaseLua = "if redis.call('GET',KEYS[1])==ARGV[1] then return redis.call('DEL',KEYS[1]) else return 0 end";
    redis.eval(releaseLua, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
}
```

**Pros:** Full flexibility — arbitrary business logic, multi-DB transactions  
**Cons:** Serialized throughput (one lock holder at a time), lock timeout tuning required, risk of lock holder dying mid-execution  
**Use case:** Hotel/flight booking with complex validation, multi-table DB writes, payment flows

---

## Comparison

| Strategy | Throughput | Complexity | Best For |
|---|---|---|---|
| Atomic DECR | Ultra High | Low | High-concurrency flash sales (simple stock) |
| Lua Script | High | Medium | Logic-gated booking (e.g., limit 1 per person) |
| Distributed Lock | Medium | High | Complex business logic spanning multiple DBs |

---

## Layered Architecture (Production)

In practice, combine all three layers:

```
Request
  │
  ▼
[Rate Limiter]          ← reject obvious abuse early
  │
  ▼
[Redis Lua Script]      ← fast atomic pre-check (stock + dedup)
  │ success
  ▼
[DB Optimistic Lock]    ← final guard with version/CAS
  │
  ▼
[Order Created]
```

**DB optimistic lock as final safety net:**
```sql
UPDATE inventory
SET stock = stock - 1, version = version + 1
WHERE item_id = ? AND version = ? AND stock > 0;
-- if affected rows = 0 → lost the race, retry or reject
```

This pattern gives you Redis-level speed for 99% of requests while the DB prevents any edge-case oversell from Redis failures (e.g., crash between DECR and DB write).

---

## Common Pitfalls

- **Forgetting rollback**: If DB write fails after Redis DECR, restore Redis stock — otherwise inventory leaks.
- **Lock expiry too short**: If business logic takes longer than lock TTL, another thread acquires it mid-execution → use async heartbeat or extend TTL.
- **Redis cluster**: Lua scripts and atomic ops are per-slot; if stock key and user key hash to different slots, use hash tags `{event_42}:stock` / `{event_42}:user:99` to co-locate.
- **Thundering herd on lock**: If lock fails, add random jitter before retry to avoid stampede.
