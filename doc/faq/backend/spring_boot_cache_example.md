Implementing caching via **Redis** in a **Spring Boot + MySQL** application can **massively boost performance** and **reduce DB load**, but it requires a careful design to avoid **stale data**, **inconsistency**, and **cache stampede**.

---

## ✅ 1. Design Considerations

### 🔧 General Design

* **Read-heavy** queries → cacheable in Redis
* Cache **key-value pairs**, typically:

  * Key: `user:123` or `product:sku:abc`
  * Value: JSON-serialized object (or Hash/String)

### 🧱 Common Caching Patterns

| Pattern                     | Description                                                                |
| --------------------------- | -------------------------------------------------------------------------- |
| **Read-Through**            | App checks cache first → DB fallback on miss → populate cache              |
| **Write-Through**           | Writes to cache and DB at the same time                                    |
| **Write-Behind (Async)**    | Writes to cache, flushes to DB later (riskier)                             |
| **Cache-Aside (Lazy Load)** | App reads cache, falls back to DB, populates cache if missed (most common) |

### ✅ Recommended: **Cache-Aside** (Spring Boot default pattern)

```java
@Cacheable(value = "user", key = "#userId")
public User getUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow();
}
```

---

## ✅ 2. Ensuring Consistency (DB <-> Cache)

The key challenge: **When DB changes, how do we keep Redis cache in sync?**

### 🔁 Sync Strategies

| Strategy                                | How It Works                           | Pros                   | Cons                                               |
| --------------------------------------- | -------------------------------------- | ---------------------- | -------------------------------------------------- |
| **Evict on Write** (`@CacheEvict`)      | After DB write, remove the cache entry | Simple, consistent     | Requires cache reload on next read                 |
| **Update Cache on Write** (`@CachePut`) | Write to DB and cache together         | Fast reads             | Risk of partial failure (DB updated but cache not) |
| **Message Queue** (e.g. Kafka)          | Async sync via event stream            | Decoupled, scalable    | Complexity, potential lag                          |
| **Double Write Transaction**            | Transactionally update both            | Reliable if done right | Harder to manage rollback                          |

### 🔐 Spring Boot Code Example: Cache Eviction on Update

```java
@CacheEvict(value = "user", key = "#user.id")
public void updateUser(User user) {
    userRepository.save(user); // DB write
}
```

---

## ⚠️ 3. Bottlenecks & Edge Cases

### 💥 Cache Stampede

**Many requests hit the DB when cache expires**

* ✅ Solution: Use **randomized TTL** + **mutex locks** (`SETNX`)
* Libraries: [Caffeine](https://github.com/ben-manes/caffeine) (local), [Redisson](https://github.com/redisson/redisson)

### 🔁 Cache Inconsistency

**DB updated, cache not updated**

* Use `@CacheEvict`, `@CachePut`, or transactional messaging
* Avoid parallel updates — lock or debounce as needed

### 🧊 Cold Start

**Cache empty after restart / deployment**

* ✅ Pre-warm cache during boot (optional batch loaders)

### 🔓 Serialization Format

* Redis needs **fast serialization**
* Prefer **JSON** (e.g. `Jackson2JsonRedisSerializer`) or **String** over Java native serialization

```java
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    return template;
}
```

---

## 🧪 Example Caching Flow in Spring Boot (Cache-Aside):

```java
@GetMapping("/user/{id}")
@Cacheable(value = "user", key = "#id")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);  // Calls DB only if cache miss
}
```

```java
@PutMapping("/user")
@CacheEvict(value = "user", key = "#user.id")
public void updateUser(@RequestBody User user) {
    userService.update(user); // Removes stale cache
}
```

---

## 🧠 Summary Checklist

| Goal                        | Technique                           |
| --------------------------- | ----------------------------------- |
| Improve read performance    | Cache-Aside w/ Redis                |
| Keep cache & DB in sync     | Use `@CacheEvict` on write          |
| Avoid stampede / cold start | Pre-warm or use lock/mutex          |
| Handle failures             | Use retry logic, fallbacks          |
| Choose right TTL            | Add random jitter to avoid stampede |
| Fast serialization          | Use JSON or String serializers      |

