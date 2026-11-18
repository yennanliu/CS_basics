# Case template

The shape every `system_design/<case>/README.md` written by `case` mode takes. Fill every
section; a heading with nothing under it is a stub, and the directory already has too many.
Lead each section with the sentence you would say out loud, then the detail.

````markdown
# Design <System>

> **Crux** — <the one thing this problem is really testing, in one sentence>.
> **Lens**: backend | data engineering | both. **See also**: <the repo sections it builds on>.

## 1) Requirements

### Functional
- <what users or consumers can do>

### Non-functional
| Property | Target | Why it matters here |
|---|---|---|
| Scale | <DAU / events per day> | |
| Latency or freshness | <p99 / SLA> | |
| Availability | <e.g. 99.9%> | |
| Consistency or correctness | <strong / eventual / reconciled> | |

### Out of scope
- <what you are deliberately not designing>

## 2) Estimation

```text
<per-day → per-second → peak → storage → bandwidth, each line ending in "→ <decision>">
```

## 3) API and data model

```text
<endpoints or events; tables or keys>
```

**Key choice**: <shard / partition / primary key> — <why, and the hot spot it risks>.

## 4) High-level design

```text
<the request path or the data path, plus the control plane>
```

<One paragraph per component: which requirement it serves.>

## 5) Deep dive: <the crux>

<The mechanism, the alternatives, and why this one. Most of the case lives here.>

## 6) Deep dive: <the second hardest part>

## 7) Trade-offs

| Decision | Chose | Over | Because | Flips if |
|---|---|---|---|---|

## 8) Scale and failure

| Scenario | What breaks | Design answer |
|---|---|---|
| 10× traffic | | |
| <a node / a region / a job down> | | |
| <bad or duplicate input> | | |

## 9) Monitoring

| Signal | Alert when |
|---|---|

## 10) Follow-up questions

- **<question an interviewer pushes on>** — <the answer in two sentences>.

## References

- <links kept from the stub, plus the repo deep dives used>
````

Rules the template does not show:

- Every fence is tagged; diagrams are ` ```text `.
- Numbers are labelled assumptions or rules of thumb, never measurements.
- A mechanism the repo already explains (CDC, watermarks, isolation levels, consistent hashing
  in `sql_vs_nosql.md`) is one sentence plus a link.
