# Software Runtime FAQ

How a program is loaded, laid out in memory, and executed — the concepts that
come up in systems and language-internals interviews.

## 1) Process vs Thread

| | Process | Thread |
|---|---------|--------|
| Definition | An instance of a running program | A unit of execution within a process |
| Memory | Own isolated address space | Shares the process's heap & code |
| Owns | Code, heap, file handles, PCB | Own stack, registers, program counter |
| Communication | IPC (pipes, sockets, shared mem) | Shared memory (cheap, but needs sync) |
| Cost | Heavy to create/switch | Light to create/switch |
| Failure | Crash isolated to that process | A crash can take down the whole process |

**Key points**
- Threads share the heap → they need synchronization (locks, atomics) to avoid
  data races. Each thread has its **own stack**.
- Context switching between processes is more expensive (address space / TLB flush)
  than between threads of the same process.
- **Concurrency** = tasks make progress in overlapping time windows;
  **parallelism** = tasks literally run at the same instant on multiple cores.

---

## 2) Memory Layout of a Program

A typical process address space, low → high address:

```
+------------------------+  high address
|        Stack           |  grows down ↓  (local vars, call frames, return addrs)
+------------------------+
|          ↓             |
|                        |
|          ↑             |
+------------------------+
|         Heap           |  grows up ↑    (dynamic allocation: malloc/new)
+------------------------+
|         BSS            |  uninitialized globals/statics (zeroed)
+------------------------+
|         Data           |  initialized globals/statics
+------------------------+
|         Text           |  program code (read-only, executable)
+------------------------+  low address
```

- **Stack**: fast, automatic, LIFO. Each function call pushes a frame; return pops
  it. Fixed max size → deep/infinite recursion causes a **stack overflow**.
- **Heap**: manually or GC-managed, dynamic lifetime, slower, can fragment.
  Leaks live here (reachable-but-unused, or unfreed, memory).
- **Text/Data/BSS**: fixed at load time.

**Stack vs Heap (interview staple)**

| | Stack | Heap |
|---|-------|------|
| Allocation | Automatic (scope) | Explicit / GC |
| Speed | Very fast (pointer bump) | Slower |
| Lifetime | Until function returns | Until freed / unreachable |
| Size | Small, fixed | Large, flexible |
| Thread | One per thread | Shared |

---

## 3) How a Program Loads and Runs

```
Source code
   │  compile / assemble
   ▼
Object files ──link──► Executable (or bytecode / script)
   │
   ▼  OS loader
Loaded into memory: text, data segments mapped; stack & heap set up
   │
   ▼
Entry point (e.g. main) → CPU fetch-decode-execute cycle
```

1. **Compile/build**: source → machine code or bytecode. Linking resolves symbols
   (static = baked in; dynamic = resolved at load/run via shared libraries).
2. **Load**: the OS loader creates the address space, maps segments, sets up the
   stack, loads dynamic libraries, and jumps to the entry point.
3. **Run**: the CPU executes the fetch–decode–execute cycle; the OS scheduler
   time-slices threads onto cores; syscalls cross into kernel mode for I/O etc.

---

## 4) Compiled vs Interpreted vs JIT

| Model | How | Pros | Cons |
|-------|-----|------|------|
| **Compiled** (AOT) | Source → native machine code before running | Fast startup & execution, no runtime needed | Platform-specific, slower build cycle |
| **Interpreted** | Instructions read & executed at runtime | Portable, fast dev loop, flexible | Slower execution |
| **JIT** | Bytecode interpreted, then hot code compiled to native at runtime | Near-native speed + portability | Warm-up cost, higher memory |

- **Java** compiles source → **bytecode** (`.class`), which the **JVM** runs.
  The JVM starts by interpreting, profiles execution, then the **JIT** compiler
  (C1/C2) compiles hot methods to native code and applies aggressive
  optimizations (inlining, loop unrolling, escape analysis).
- This is why Java has a "warm-up" period before reaching peak throughput.

---

## 5) Garbage Collection Basics

Automatic memory management: reclaims heap objects that are no longer reachable.

- **Reachability**: an object is live if reachable from **GC roots** (stack refs,
  static fields, active threads). Everything else is garbage.
- **Generational hypothesis**: most objects die young. Heap is split into
  **young** (Eden + survivor) and **old** generations.
  - Minor GC collects the young gen (frequent, cheap).
  - Major/full GC collects the old gen (rarer, more expensive).
- **Common algorithms**: mark-and-sweep, mark-compact, copying collectors.
- **Stop-the-world (STW)** pauses freeze application threads during (parts of) GC;
  modern collectors (G1, ZGC, Shenandoah) minimize pause times.
- **Trade-off**: GC removes whole classes of bugs (use-after-free, double-free)
  at the cost of throughput, memory overhead, and pause-time unpredictability.

Languages without GC (C, C++, Rust) manage memory manually or via ownership,
trading safety/effort for control and predictable latency.

---

## 6) Runtime Environments

The runtime is the software layer providing services a program needs while running:
memory management, scheduling, standard library, and platform abstraction.

- **JVM (Java)**: loads bytecode, verifies it, JIT-compiles, manages GC and threads.
  "Write once, run anywhere" — bytecode is portable across JVM implementations.
- **Language VMs / interpreters**: e.g. CPython, V8 (JavaScript) — parse and
  execute, often with their own JIT and GC.
- **OS as a runtime**: provides processes, threads, virtual memory, and syscalls.
- **Containers**: package the app + its runtime + dependencies for consistent
  execution across environments (isolation via namespaces/cgroups, not a full VM).

**Key idea**: the same program can behave differently depending on its runtime
(GC settings, JIT warm-up, available cores/memory), so performance tests must run
in a representative environment.
