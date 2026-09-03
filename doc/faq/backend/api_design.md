# API Design FAQ (REST, and when not to use it)

> **Scope** — designing an HTTP API: resource modelling, status codes, idempotency, pagination, versioning, error shape, auth, and how REST compares with GraphQL and gRPC.
> **See also**: [`authentication.md`](./authentication.md) — sessions and JWT;
> [`be_programming_notes.md`](./be_programming_notes.md) — rate limiting, retries,
> idempotency keys in code; [`web_long_connections.md`](./web_long_connections.md) —
> streaming and push.

"Design an API for X" is a warm-up in most backend interviews. What is being scored is
whether you think about the **contract** — its evolution, its failure modes, and what a
client has to do to use it correctly.

---

## 1) Resources, Not Verbs ⭐⭐⭐⭐⭐

A REST resource is a **noun** with a stable URI; the HTTP method is the verb.

```text
GET    /orders?status=open&limit=50   list
POST   /orders                        create           → 201 + Location
GET    /orders/1042                   read one
PUT    /orders/1042                   replace whole
PATCH  /orders/1042                   partial update
DELETE /orders/1042                   remove           → 204
POST   /orders/1042/cancel            a state transition that isn't CRUD
```

Rules that hold up in review: plural nouns, lower-case with hyphens, nest only one level
(`/orders/1042/items`, then link rather than nest deeper), no verbs in paths
(`/getOrders` ❌) — except for genuine actions that are not resource mutations
(`/cancel`, `/search`, `/reindex`), which everyone accepts as `POST` sub-resources.

### Method semantics ⭐⭐⭐⭐⭐

| Method | Safe (no side effect) | Idempotent (N calls == 1 call) | Cacheable |
|--------|----------------------|-------------------------------|-----------|
| `GET` | ✅ | ✅ | ✅ |
| `HEAD` | ✅ | ✅ | ✅ |
| `PUT` | ❌ | ✅ | ❌ |
| `DELETE` | ❌ | ✅ (the second call returns 404/204) | ❌ |
| `PATCH` | ❌ | ⚠️ Only if you design it that way | ❌ |
| `POST` | ❌ | ❌ | Rarely |

**Idempotency is the property that makes retries safe**, and networks make retries
unavoidable. Since `POST` is not idempotent, give it an **idempotency key**:

```http
POST /payments
Idempotency-Key: 7f1c9e2a-...
```

The server stores the key with the first response and replays it for any duplicate — the
pattern is written out in [`be_programming_notes.md`](./be_programming_notes.md) and, for
AI tool calls, in [`llm_tool_idempotency.md`](./llm_tool_idempotency.md).

---

## 2) Status Codes ⭐⭐⭐⭐

| Code | Use it for |
|------|-----------|
| **200** OK | Success with a body |
| **201** Created | A resource was created — return a `Location` header |
| **202** Accepted | Async: accepted for processing, not done yet |
| **204** No Content | Success with no body (a `DELETE`, a `PUT` returning nothing) |
| **400** Bad Request | Malformed syntax / failed validation |
| **401** Unauthorized | **Not authenticated** (bad or missing credentials) |
| **403** Forbidden | Authenticated but **not allowed** |
| **404** Not Found | No such resource — also the polite answer when 403 would leak existence |
| **409** Conflict | Version conflict, duplicate, state machine violation |
| **422** Unprocessable | Syntactically fine, semantically invalid (when you want it distinct from 400) |
| **429** Too Many Requests | Rate limited — send `Retry-After` |
| **500** Internal Server Error | An unhandled bug on your side |
| **502 / 503 / 504** | Bad upstream / not available (send `Retry-After`) / upstream timeout |

Two mistakes interviewers listen for: returning `200 {"error": ...}` for failures (it
breaks every client, proxy and monitor that reads the status line), and using `500` for a
client's bad input.

---

## 3) The Error Shape ⭐⭐⭐⭐

One shape for every error, machine-readable first:

```json
{
  "type":     "https://api.acme.com/errors/insufficient-funds",
  "title":    "Insufficient funds",
  "status":   409,
  "detail":   "Balance 12.50 is below the required 30.00",
  "instance": "/accounts/42/withdrawals",
  "traceId":  "b7ad6b7169203331",
  "errors":   [{ "field": "amount", "message": "must be <= balance" }]
}
```

That is RFC 9457 (`application/problem+json`), and Spring's `ProblemDetail` produces it
directly. Whatever shape you pick:

- a **stable error code** clients can branch on — never force them to parse prose;
- a **trace id** that also appears in your logs, so a user report becomes one query;
- field-level detail for validation errors;
- **never** a stack trace, SQL fragment or internal hostname in the response.

---

## 4) Pagination, Filtering, Sorting ⭐⭐⭐⭐

| Style | Request | Good | Bad |
|-------|---------|------|-----|
| **Offset** | `?page=3&size=50` | Jump to any page, show totals | `OFFSET 100000` gets slow; items shift between pages as data changes |
| **Cursor / keyset** | `?limit=50&cursor=eyJpZCI6MTA0Mn0` | Stable under writes; cost is one index seek plus the page (`O(log n + limit)`) no matter how deep you are | No page numbers, no total |

Prefer **cursor** pagination for feeds, logs and anything large or live; offset is fine
for small admin tables. Always cap `limit` server-side (a client asking for 1,000,000 is
a denial of service), always return a default, and return the next cursor in the body or a
`Link` header.

```json
{ "data": [ ... ], "nextCursor": "eyJpZCI6MTA0Mn0", "hasMore": true }
```

Filtering and sorting belong in the query string (`?status=open&sort=-createdAt`), with a
**whitelist** of sortable fields — sorting by an unindexed column is a table scan, and
interpolating the field name into SQL is an injection.

---

## 5) Versioning & Evolution ⭐⭐⭐⭐

| Approach | Example | Notes |
|----------|---------|-------|
| **URI path** | `/v1/orders` | Ugliest, clearest, cache- and log-friendly. The common choice |
| Header / media type | `Accept: application/vnd.acme.v2+json` | "Purer", harder to test and cache |
| Query parameter | `?version=2` | Easy, easy to lose |

The cheaper skill is **not needing a new version**. Backward-compatible changes: adding an
optional field, adding an endpoint, adding an enum value *if clients are told to ignore
unknown ones*. Breaking changes: removing or renaming a field, tightening validation,
changing a type or the meaning of a value, changing defaults.

So: clients must ignore unknown fields, servers must not repurpose existing ones, and a
removal goes through **deprecate → announce with a sunset date (`Deprecation`/`Sunset`
headers) → measure remaining traffic → remove**.

---

## 6) Concurrency & Caching ⭐⭐⭐

**Lost updates**: two clients read version 1, both write, the second silently overwrites.
Fix with an optimistic-concurrency token:

```http
GET  /orders/1042        → 200, ETag: "v7"
PUT  /orders/1042        If-Match: "v7"
                         → 200 if still v7, 412 Precondition Failed if not
```

Caching headers worth knowing: `Cache-Control: public, max-age=60` (and `no-store` for
anything personal), `ETag` + `If-None-Match` → `304 Not Modified` for cheap revalidation,
and `Vary` when the response differs by header (`Accept-Encoding`, `Authorization`).

---

## 7) Security Checklist ⭐⭐⭐⭐⭐

- **HTTPS only**, HSTS on. No credentials or tokens in the URL — URLs land in logs,
  proxies and referrers.
- **Authenticate then authorise**, and authorise **per object**, not just per endpoint.
  "Broken object-level authorisation" (`GET /orders/1043` returning someone else's order)
  is the number-one API vulnerability in practice.
- **Validate every input** server-side against a schema — length, type, range, enum. Client
  validation is UX, not security.
- **Never trust client-supplied identity, prices or roles.** Recompute the price.
- **Rate limit** per principal and per IP; return `429` with `Retry-After`.
- **Don't leak** in errors: no stack traces, no "user not found" vs "wrong password"
  distinction on login, no internal ids that are guessable if that matters (use UUID/ULID).
- Set a **request size limit** and timeouts; parse JSON with depth limits.
- CORS explicitly allow-listed; no `Access-Control-Allow-Origin: *` on an authenticated
  API.
- Log who did what, without logging secrets or PII payloads.

Token mechanics — sessions vs JWT, refresh tokens, revocation — are in
[`authentication.md`](./authentication.md).

---

## 8) Long-Running and Bulk Operations ⭐⭐⭐

For anything slower than a request should block on, return a job:

```text
POST /reports            → 202 Accepted, Location: /jobs/9f2
GET  /jobs/9f2           → { "status": "running", "progress": 0.4 }
                         → { "status": "done", "result": "/reports/551" }
```

Then let the client poll, or push with SSE/webhooks
([`web_long_connections.md`](./web_long_connections.md)). Webhooks you send need the same
discipline you ask of others: signed payloads, at-least-once delivery with an event id so
the receiver can dedupe, and retries with backoff.

---

## 9) REST vs GraphQL vs gRPC ⭐⭐⭐

| | REST/JSON | GraphQL | gRPC |
|---|-----------|---------|------|
| Shape | Resources over HTTP | One endpoint, client-specified queries | Typed RPC over HTTP/2 + protobuf |
| Over/under-fetching | Common | Solved by design | Solved per method |
| Caching | HTTP caching for free | Hard (POST, per-query) | Manual |
| Schema | OpenAPI (optional) | Mandatory, introspectable | Mandatory `.proto` |
| Streaming | SSE/WebSocket bolted on | Subscriptions | Native bidirectional |
| Browser support | Native | Native | Needs grpc-web |
| Pain | Endpoint sprawl | Query cost/depth limits, N+1 resolvers | Debuggability, binary payloads |

Rule of thumb: **REST** for public and CRUD-ish APIs, **gRPC** for internal
service-to-service calls where latency and typed contracts matter, **GraphQL** when many
different clients need many different projections of the same graph.

---

## 10) Common Interview Q&A

**Q: PUT vs PATCH vs POST?**
`PUT` replaces the whole resource and is idempotent; `PATCH` applies a partial change (and
is only idempotent if you design it so — "set status to shipped" is, "add 1 to quantity"
is not); `POST` creates or triggers, and is not idempotent.

**Q: How do you make a payment API safe to retry?**
Idempotency key on the request, stored with the outcome; the same key returns the original
response instead of charging twice. Combine with a unique constraint in the database as the
last line of defence.

**Q: 401 vs 403?**
401 = we do not know who you are (or your credentials expired). 403 = we know, and you may
not.

**Q: How do you paginate a feed that is being written to?**
Cursor/keyset pagination on an immutable, monotonically ordered key — offsets duplicate and
skip items when rows are inserted mid-scroll.

**Q: How do you change a field's type without breaking clients?**
Add the new field beside the old one, write both, migrate clients, watch usage, then remove
the old field in a new version with a sunset window.

**Q: Where do you enforce rate limits?**
At the edge (gateway/CDN) for coarse IP limits, and in the service for per-user/per-plan
limits, which need the identity. The algorithms — token bucket, sliding window — are in
[`be_programming_notes.md`](./be_programming_notes.md).

**Q: How do you document it?**
OpenAPI, generated from or verified against the code so it cannot drift, with examples per
endpoint and per error.

---

## 11) Recap Checklist

```text
[ ] Nouns for resources, methods for verbs; sensible nesting
[ ] Safe / idempotent / cacheable per method — and an idempotency key for POST
[ ] Correct status codes; never 200-with-error
[ ] One error shape with a stable code and a trace id
[ ] Cursor pagination, capped limits, whitelisted sort fields
[ ] Backward-compatible evolution; deprecate-then-remove
[ ] ETag + If-Match for lost updates; Cache-Control/304 for reads
[ ] Object-level authorisation, server-side validation, rate limits
[ ] 202 + job resource for long operations; signed, dedupable webhooks
[ ] When gRPC or GraphQL beats REST
```

---

## References

- [RFC 9110 — HTTP semantics](https://www.rfc-editor.org/rfc/rfc9110.html)
- [RFC 9457 — Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc9457.html)
- [OWASP API Security Top 10](https://owasp.org/API-Security/editions/2023/en/0x11-t10/)
- [`authentication.md`](./authentication.md) · [`be_programming_notes.md`](./be_programming_notes.md) · [`web_long_connections.md`](./web_long_connections.md)
