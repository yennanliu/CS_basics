# Web Long Connections

> How to maintain persistent / long-lived connections in web applications

## Table of Contents

1. [HTTP Keep-Alive (Persistent Connection)](#1-http-keep-alive-persistent-connection)
2. [HTTP Pipelining](#2-http-pipelining)
3. [Long Polling](#3-long-polling)
4. [Streaming (Chunked Transfer)](#4-streaming-chunked-transfer)
5. [Server-Sent Events (SSE)](#5-server-sent-events-sse)
6. [WebSocket](#6-websocket-upgrade-from-http)
7. [HTTP/2 Multiplexing](#7-http2-multiplexing)
8. [Summary](#summary)

---

In HTTP, a "long connection" usually refers to keeping a TCP connection open for multiple requests instead of closing it after each one (often called **persistent connections** or **keep-alive**). There are several ways this concept shows up in practice depending on what you're trying to achieve.

---

## 1. HTTP Keep-Alive (Persistent Connection)

* Default in HTTP/1.1.
* The client and server reuse the same TCP connection for multiple requests/responses.
* Reduces latency and overhead from repeatedly opening/closing connections.

**Header example:**
```
Connection: keep-alive
```

**Flow:**
```
Client ──── request 1 ────▶ Server
Client ◀─── response 1 ─── Server
Client ──── request 2 ────▶ Server   (same TCP connection)
Client ◀─── response 2 ─── Server
```

---

## 2. HTTP Pipelining

* Multiple HTTP requests are sent without waiting for previous responses.
* Responses must still come back in order.
* Rarely used today due to **head-of-line blocking** issues.

**Flow:**
```
Client ──── request 1 ────▶
Client ──── request 2 ────▶ Server
Client ──── request 3 ────▶
Client ◀─── response 1, 2, 3 (in order)
```

---

## 3. Long Polling

* Client sends a request.
* Server **holds** the request open until new data is available.
* Once data is sent, the connection closes and the client immediately reconnects.
* Common for near–real-time updates (older technique).

**Flow:**
```
Client ──── request ─────────────────────▶ Server
                                           (server waits for new data)
Client ◀─── response (when data ready) ── Server
Client ──── reconnect immediately ───────▶ Server
```

**Use case:** Chat apps, notification systems (legacy approach).

---

## 4. Streaming (Chunked Transfer)

* Server keeps the connection open and continuously sends data in chunks.
* Useful for logs, live feeds, progress updates.

**Header:**
```
Transfer-Encoding: chunked
```

**Example (Node.js):**
```javascript
res.writeHead(200, { 'Transfer-Encoding': 'chunked' });
setInterval(() => res.write('data chunk\n'), 1000);
```

---

## 5. Server-Sent Events (SSE)

* One-way communication: **server → client** only.
* Browser keeps a long-lived HTTP connection open.
* Simpler than WebSockets for real-time updates (push notifications, dashboards).
* Automatically reconnects on disconnect.

**Header:**
```
Content-Type: text/event-stream
```

**Server example (Node.js):**
```javascript
res.setHeader('Content-Type', 'text/event-stream');
res.setHeader('Cache-Control', 'no-cache');
setInterval(() => res.write(`data: ${JSON.stringify({ time: Date.now() })}\n\n`), 1000);
```

**Client example:**
```javascript
const source = new EventSource('/events');
source.onmessage = (e) => console.log(e.data);
```

---

## 6. WebSocket (Upgrade from HTTP)

* Starts as HTTP, then upgrades to a **persistent full-duplex** connection.
* Enables real-time bidirectional communication.
* Best for chat, gaming, live collaboration.

**Upgrade headers:**
```
Upgrade: websocket
Connection: Upgrade
```

**Server example (Node.js with `ws`):**
```javascript
const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 8080 });
wss.on('connection', (ws) => {
  ws.on('message', (msg) => ws.send(`echo: ${msg}`));
});
```

**Client example:**
```javascript
const ws = new WebSocket('ws://localhost:8080');
ws.onmessage = (e) => console.log(e.data);
ws.send('hello');
```

---

## 7. HTTP/2 Multiplexing

* Multiple streams share a **single long-lived connection**.
* Solves head-of-line blocking at the HTTP layer (unlike HTTP/1.1 pipelining).
* More efficient: headers are compressed (HPACK), streams are independent.

**Key features vs HTTP/1.1:**

| Feature | HTTP/1.1 | HTTP/2 |
|---|---|---|
| Connections per host | Multiple | One (multiplexed) |
| Head-of-line blocking | Yes | No (at HTTP layer) |
| Header compression | No | Yes (HPACK) |
| Server push | No | Yes |

---

## Summary

| Method | Direction | Connection | Use Case |
|---|---|---|---|
| **Keep-Alive** | Request/Response | Reused TCP | General HTTP efficiency |
| **Pipelining** | Request/Response | Reused TCP | Batch requests (rarely used) |
| **Long Polling** | Server → Client | Reopened each time | Legacy real-time updates |
| **Chunked Streaming** | Server → Client | Long-lived | Logs, live feeds |
| **SSE** | Server → Client | Long-lived | Dashboards, notifications |
| **WebSocket** | Full-duplex | Long-lived | Chat, gaming, collaboration |
| **HTTP/2** | Request/Response | Multiplexed | Modern APIs, reduced latency |

**Quick decision guide:**
- **Connection reuse:** Keep-Alive, HTTP/2
- **Client waiting for data:** Long Polling
- **Continuous data flow from server:** Streaming, SSE
- **Full-duplex (bidirectional) communication:** WebSocket

---

## Pros, Cons & Use Cases Comparison

| Method | Pros | Cons | Best Use Cases |
|---|---|---|---|
| **HTTP Keep-Alive** | • Reduces TCP handshake overhead<br>• Default in HTTP/1.1, widely supported<br>• Simple — no extra code needed | • Still request/response only<br>• Server holds idle connections<br>• Head-of-line blocking within connection | • REST APIs<br>• Static asset serving<br>• Any general HTTP traffic |
| **HTTP Pipelining** | • Sends multiple requests without waiting<br>• Reduces round-trip latency | • Responses must arrive in order (head-of-line blocking)<br>• Poor proxy/server support<br>• Largely deprecated | • Rarely used; superseded by HTTP/2 |
| **Long Polling** | • Works with any HTTP stack<br>• Firewall/proxy friendly<br>• No special browser API needed | • High server-side resource usage (held connections)<br>• Reconnect latency after each response<br>• Not truly real-time | • Legacy chat systems<br>• Notification polling in older environments<br>• When WebSocket/SSE not available |
| **Chunked Streaming** | • Simple server-side implementation<br>• Works over plain HTTP<br>• Good for large or unbounded responses | • No built-in reconnect<br>• Clients must handle partial chunks<br>• Less structured than SSE | • File downloads<br>• Log tailing<br>• Progress reporting |
| **Server-Sent Events (SSE)** | • Built-in auto-reconnect<br>• Native browser `EventSource` API<br>• Lightweight — plain HTTP, no upgrade<br>• Easy to scale with standard load balancers | • Server → client only (unidirectional)<br>• Limited to text data<br>• Some proxies may buffer and delay | • Live dashboards<br>• News/score feeds<br>• Push notifications<br>• Activity streams |
| **WebSocket** | • Full-duplex (bidirectional)<br>• Low latency after handshake<br>• Binary and text support<br>• Wide browser support | • Requires protocol upgrade<br>• Harder to scale (sticky sessions or pub/sub needed)<br>• Some firewalls/proxies block WS<br>• More complex error handling | • Chat applications<br>• Online gaming<br>• Live collaboration (e.g. Google Docs)<br>• Financial trading feeds |
| **HTTP/2 Multiplexing** | • Multiple streams over one connection<br>• No head-of-line blocking (HTTP layer)<br>• Header compression (HPACK)<br>• Server push capability | • TCP head-of-line blocking still exists (solved by HTTP/3)<br>• Requires TLS in most browsers<br>• More complex to debug | • Modern REST/gRPC APIs<br>• Asset-heavy web pages<br>• Microservice communication |

### Scalability Considerations

| Method | Server Resources | Horizontal Scaling |
|---|---|---|
| Keep-Alive | Low (short-lived idle) | Easy |
| Long Polling | High (threads held per client) | Moderate |
| Chunked Streaming | Medium (open response streams) | Moderate |
| SSE | Medium (open response streams) | Easy with stateless design |
| WebSocket | Medium-High (persistent connections) | Needs sticky sessions or message broker (Redis pub/sub) |
| HTTP/2 | Low-Medium (one conn, many streams) | Easy |


### Ref

- https://systemdesignschool.io/problems/google-doc/solution
