# Python Concurrency FAQ

> **Scope** — how Python actually runs concurrent work: the GIL, threads vs processes vs `asyncio`, the synchronisation primitives, and how to pick between them.
> **See also**: [`faq_python.md`](./faq_python.md) — the language itself;
> [`cs_basic.md`](./cs_basic.md) — processes, threads and scheduling at the OS level.

---

## 1) The GIL ⭐⭐⭐⭐⭐

The **Global Interpreter Lock** is a single mutex in CPython that lets **one thread
execute Python bytecode at a time**, even on a 16-core machine.

**Why it exists**: CPython's memory management is reference counting, and refcount
updates are not atomic. One coarse lock made the interpreter (and every C extension
written against it) correct and single-thread-fast, at the cost of parallel bytecode
execution.

**What it does and does not block:**

| Work | Scales with threads? | Why |
|------|---------------------|-----|
| CPU-bound Python (loops, parsing, math in pure Python) | ❌ No | Only one thread holds the GIL |
| Blocking I/O (network, disk, `subprocess`) | ✅ Yes | The GIL is **released** around the syscall |
| `time.sleep` | ✅ Yes | Releases the GIL |
| NumPy / pandas / `hashlib` / compression heavy work | ✅ Often | C extensions release the GIL around long computations |

So the rule that matters: **threads for waiting, processes for computing.**

```python
# python
# 4 threads on a CPU-bound loop  -> about as slow as 1 thread (sometimes slower,
#                                   from GIL hand-off contention)
# 4 threads on 4 HTTP requests   -> about 4x faster
```

The interpreter switches threads every ~5 ms (`sys.setswitchinterval`) or when a thread
blocks. Python 3.13 ships an **experimental free-threaded build** with no GIL; until it
is the default, assume the table above.

---

## 2) The Three Models ⭐⭐⭐⭐⭐

| | `threading` | `multiprocessing` | `asyncio` |
|---|-------------|-------------------|-----------|
| Unit | OS thread | OS process | Coroutine (one thread) |
| Parallel CPU | ❌ (GIL) | ✅ true parallelism | ❌ |
| Concurrent I/O | ✅ | ✅ | ✅✅ (thousands of sockets) |
| Memory | Shared | Separate — data is **pickled** across | Shared |
| Cost per unit | ~8 MB stack, µs to start | ~MBs, ms to start | ~KB, ns to switch |
| Switching | Pre-emptive (any bytecode boundary) | OS scheduler | **Cooperative** — only at `await` |
| Failure mode | Data races | Serialisation cost, no shared state | One blocking call freezes everything |
| Use for | Blocking I/O, legacy libs | CPU-bound work | Many concurrent network calls |

**Choosing, in one line each:**

- **Lots of network/disk waiting, library is blocking** → threads (or a thread pool).
- **Lots of network waiting, library is async** → `asyncio`.
- **Number crunching in pure Python** → processes (or NumPy, or move the loop into C).
- **Both** → `asyncio` for the I/O with a `ProcessPoolExecutor` for the CPU parts.

---

## 3) Threads ⭐⭐⭐⭐

### `concurrent.futures` is the API to use

```python
# python
from concurrent.futures import ThreadPoolExecutor, as_completed

urls = [...]
with ThreadPoolExecutor(max_workers=16) as pool:          # shutdown on exit
    futures = {pool.submit(fetch, u): u for u in urls}
    for fut in as_completed(futures):
        url = futures[fut]
        try:
            handle(fut.result())     # result() re-raises the worker's exception
        except TimeoutError:
            log.warning("timed out: %s", url)
```

- `pool.map(fn, items)` keeps input order and is fine when you want all results.
- `as_completed` yields whichever finishes first — better for progress and early exit.
- **A future's exception is only raised when you call `.result()`.** Forgetting to call it
  silently swallows failures; this is the most common bug with executors.
- Size the pool for **waiting**, not cores: `max_workers` in the tens is normal for I/O.

### Races still happen

The GIL does **not** make your code thread-safe — it only serialises bytecode execution,
and `counter += 1` is a read-modify-write compiled into several bytecodes (how many
depends on the version), so a switch can land in the middle of it.

```python
# python
import threading

lock = threading.Lock()
with lock:                 # always `with`, never manual acquire/release
    counter += 1
```

| Primitive | Use |
|-----------|-----|
| `Lock` | Mutual exclusion |
| `RLock` | Re-entrant — the same thread can acquire it again |
| `Semaphore(n)` | Limit concurrency (e.g. 5 simultaneous API calls) |
| `Event` | One-shot broadcast flag (`set()` / `wait()`) |
| `Condition` | Wait for a predicate, then be notified |
| `Barrier(n)` | Release n threads together |
| `queue.Queue` | **Thread-safe hand-off** — prefer this over shared lists + locks |
| `threading.local()` | Per-thread state (a request id, a DB connection) |

The producer/consumer shape with `queue.Queue` needs no explicit locking at all — it is
the idiomatic answer to most "share data between threads" questions.

### Daemon threads and shutdown

A daemon thread does not keep the process alive and is killed abruptly at exit — never
put a `finally` you care about in one. Prefer a sentinel value on a queue or an
`Event` to shut workers down cleanly.

---

## 4) Processes ⭐⭐⭐⭐

```python
# python
from concurrent.futures import ProcessPoolExecutor

if __name__ == "__main__":                 # REQUIRED on macOS/Windows (spawn)
    with ProcessPoolExecutor() as pool:    # defaults to the usable CPU count
                                          # (os.process_cpu_count() on 3.13+,
                                          #  os.cpu_count() before that)
        for result in pool.map(crunch, chunks, chunksize=8):
            ...
```

Things that bite:

- **Everything crossing the boundary is pickled.** Lambdas, closures, open sockets and DB
  handles cannot cross. Send plain data, not objects wrapping resources.
- **Start methods**: `fork` (Linux default until 3.14 — fast, but unsafe with threads),
  `spawn` (macOS/Windows default — a fresh interpreter, so module-level code re-runs),
  `forkserver`. Behaviour differences here explain most "works on my Linux box" reports.
- **Chunk the work.** Per-item IPC overhead can dwarf the computation; `chunksize` is the
  usual fix.
- Sharing state: `multiprocessing.Queue`/`Pipe` for messages, `Value`/`Array` or
  `shared_memory` for bulk data, a `Manager` for convenience (slow — it proxies through a
  server process).

---

## 5) asyncio ⭐⭐⭐⭐⭐

One thread, one event loop, many coroutines. A coroutine runs until it hits `await`, then
yields control to the loop, which runs whatever is ready.

```python
# python
import asyncio, aiohttp

async def fetch(session, url):
    async with session.get(url, timeout=5) as resp:
        return await resp.json()

async def main(urls):
    async with aiohttp.ClientSession() as session:
        async with asyncio.TaskGroup() as tg:              # 3.11+
            tasks = [tg.create_task(fetch(session, u)) for u in urls]
    return [t.result() for t in tasks]

asyncio.run(main(urls))
```

### The rules

- **`await` is the only place a task can be suspended.** Cooperative scheduling means one
  slow synchronous call blocks *every* other task.
- **Never call blocking code in a coroutine** — `time.sleep`, `requests.get`, heavy CPU
  loops. Use the async equivalent (`asyncio.sleep`, `httpx`/`aiohttp`), or push it off the
  loop: `await asyncio.to_thread(blocking_fn, arg)`.
- Calling a coroutine function does nothing until it is awaited or scheduled — a bare
  `fetch(url)` with no `await` is the classic "why did nothing happen?".
- Concurrency comes from **tasks**, not from `await` itself: awaiting in a loop is
  sequential; `TaskGroup` / `asyncio.gather` is concurrent.

| Tool | Does |
|------|------|
| `asyncio.run(main())` | Owns the loop for the program's lifetime |
| `TaskGroup` (3.11+) | Structured concurrency: waits for all, cancels siblings on error, raises an `ExceptionGroup` |
| `gather(*aws)` | Older equivalent; `return_exceptions=True` to collect rather than fail fast |
| `wait_for(aw, timeout)` / `asyncio.timeout()` | Deadlines — always put one on network calls |
| `Semaphore` | Cap in-flight requests so you don't melt the peer |
| `Queue` | Async producer/consumer |
| `to_thread` / `loop.run_in_executor` | Escape hatch for blocking or CPU work |

`async for` / `async with` are the async iterator and context-manager protocols
(`__aiter__`/`__anext__`, `__aenter__`/`__aexit__`).

### Cancellation

Cancelling a task raises `CancelledError` **inside** it at the next `await`. Let it
propagate — catching it to "clean up and continue" breaks shutdown. Put cleanup in
`finally`, and use `asyncio.shield` only for the rare operation that must complete.

---

## 6) Worked Comparison

The same job — 100 HTTP calls plus a hash of each response — three ways:

```text
sequential          : 100 x (latency + hash)          slowest
ThreadPoolExecutor  : latency overlapped, hash under the GIL (one at a time)
asyncio             : latency overlapped, lowest overhead; hash still serialises
asyncio + ProcessPool: latency overlapped AND hashing parallel across cores  ← best
```

The lesson interviewers look for: **name what you are waiting on.** Overlap waiting with
threads or `asyncio`; parallelise computing with processes.

---

## 7) Common Interview Q&A

**Q: Why doesn't multithreading speed up my CPU-bound Python?**
The GIL serialises bytecode execution. Use `multiprocessing`, a C-extension library that
releases the GIL (NumPy), or a different runtime.

**Q: If the GIL exists, why do I still need locks?**
Because the GIL is released between bytecodes. `x += 1`, check-then-act, and any
multi-step invariant can interleave.

**Q: `asyncio` vs threads for 10k concurrent connections?**
`asyncio`. 10k OS threads means gigabytes of stacks and heavy context switching; 10k
coroutines is kilobytes each and switching is a function return.

**Q: What is a race condition, and how do you find one in Python?**
Two threads interleaving on shared mutable state. Reproduce with contention (more threads,
smaller `sys.setswitchinterval`), then fix by narrowing shared state — prefer message
passing over locks.

**Q: What is a deadlock and how do you avoid it?**
Two threads each holding a lock the other needs. Acquire locks in a **global order**, use
`acquire(timeout=…)`, and keep critical sections short. See
[`cs_basic.md`](./cs_basic.md) for the four Coffman conditions.

**Q: How do you time-limit work?**
`asyncio.timeout()` / `wait_for` in async code; `future.result(timeout=…)` with an
executor. Note a thread cannot be forcibly killed in Python — design for a cooperative
stop flag, or use a process you can terminate.

**Q: What is `threading.local()` for?**
Per-thread state that must not be shared: a request id for logs, a DB session. The
`asyncio` equivalent is `contextvars.ContextVar`, which also survives across `await`.

---

## 8) Recap Checklist

```text
[ ] Explain the GIL, and exactly which workloads it blocks
[ ] Threads for waiting, processes for computing, asyncio for many sockets
[ ] ThreadPoolExecutor + as_completed, and why .result() must be called
[ ] queue.Queue producer/consumer without explicit locks
[ ] Pickling boundary and spawn vs fork for multiprocessing
[ ] async/await: tasks give concurrency, await alone does not
[ ] Never block the event loop; asyncio.to_thread as the escape hatch
[ ] TaskGroup, timeouts, cancellation and CancelledError
[ ] contextvars vs threading.local
```

---

## References

- [`asyncio` — Python docs](https://docs.python.org/3/library/asyncio.html)
- [`concurrent.futures` — Python docs](https://docs.python.org/3/library/concurrent.futures.html)
- [PEP 703 — making the GIL optional](https://peps.python.org/pep-0703/)
- [`faq_python.md`](./faq_python.md) — the language itself
