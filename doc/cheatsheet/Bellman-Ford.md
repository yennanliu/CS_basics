# Bellman-Ford Algorithm

## Overview
**Bellman-Ford algorithm** is a single-source shortest path algorithm that can handle graphs with negative edge weights. Unlike Dijkstra's algorithm, it can detect negative cycles and is guaranteed to find the shortest path if no negative cycles exist.

### Key Properties
- **Time Complexity**: O(V·E) where V is vertices and E is edges
- **Space Complexity**: O(V) for distance array
- **Core Idea**: Relaxation of all edges V-1 times
- **When to Use**: Single-source shortest path with negative weights
- **Special Feature**: `Can detect` negative cycles

### Core Characteristics
- **Simple Implementation**: Two nested loops over edges
- **Handles Negative Weights**: Unlike Dijkstra, works with negative edges
- **Cycle Detection**: Can identify negative cycles in the graph
- **Relaxation-Based**: Repeatedly relaxes edges until convergence
- **Guaranteed Optimal**: Finds shortest path if no negative cycles

### References
- [Bellman-Ford Visualization](https://www.cs.usfca.edu/~galles/visualization/BellmanFord.html)
- [CP Algorithms - Bellman-Ford](https://cp-algorithms.com/graph/bellman_ford.html)
- [Dijkstra Cheatsheet](./Dijkstra.md) - For non-negative weight comparison
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md) - For all-pairs comparison


## Problem Categories

### **Category 1: Classic Shortest Path with Negative Weights**
- **Description**: Standard single-source shortest path with negative edges
- **Examples**: LC 787 (Cheapest Flights K Stops), Currency conversion
- **Pattern**: Direct application of Bellman-Ford

### **Category 2: Negative Cycle Detection**
- **Description**: Detect if graph contains negative cycles
- **Examples**: LC 1334 (arbitrage detection), Currency arbitrage
- **Pattern**: Run V-th iteration to check for updates

### **Category 3: Constrained Shortest Path**
- **Description**: Shortest path with at most K edges/hops
- **Examples**: LC 787 (K stops), LC 1928 (Limited connections)
- **Pattern**: Modified Bellman-Ford with iteration limit

### **Category 4: Currency Exchange and Arbitrage**
- **Description**: Convert log weights to detect arbitrage opportunities
- **Examples**: Currency trading, price arbitrage
- **Pattern**: Log transformation + negative cycle detection

### **Category 5: Network Routing with Costs**
- **Description**: Find cheapest route with possible negative costs (discounts)
- **Examples**: Delivery with discounts, routing with rewards
- **Pattern**: Standard Bellman-Ford with cost tracking


## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Iterations | When to Use |
|---------------|----------|------------|-------------|
| **Basic Bellman-Ford** | Standard shortest path | V-1 | Negative weights |
| **With Cycle Detection** | Detect negative cycles | V | Arbitrage, validation |
| **Constrained K-Edges** | Path with edge limit | K | Limited hops/stops |
| **SPFA (Queue-Optimized)** | Faster average case | Variable | Sparse graphs |
| **Path Reconstruction** | Track actual paths | V-1 | Need path details |

### Template 1: Basic Bellman-Ford
```python
def bellman_ford(n, edges, src):
    """
    Find shortest paths from src to all vertices
    n: number of vertices (0-indexed)
    edges: list of (u, v, weight)
    src: source vertex
    Returns: distance array or None if negative cycle exists
    """
    # Initialize distances
    dist = [float('inf')] * n
    dist[src] = 0

    # Relax all edges V-1 times
    for i in range(n - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True

        # Early termination: no updates in this iteration
        if not updated:
            break

    # Check for negative cycles (V-th iteration)
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return None  # Negative cycle detected

    return dist
```

### Template 2: Bellman-Ford with Negative Cycle Detection
```python
def bellman_ford_with_cycle_detection(n, edges, src):
    """
    Bellman-Ford with detailed negative cycle detection
    Returns: (distances, has_negative_cycle, cycle_nodes)
    """
    dist = [float('inf')] * n
    dist[src] = 0
    parent = [-1] * n

    # Relax edges V-1 times
    for i in range(n - 1):
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                parent[v] = u

    # Check for negative cycle and track affected nodes
    negative_cycle_node = -1
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            dist[v] = dist[u] + w
            parent[v] = u
            negative_cycle_node = v

    if negative_cycle_node == -1:
        return dist, False, []

    # Extract negative cycle
    cycle = []
    visited = set()
    current = negative_cycle_node

    # Walk back to find cycle
    for _ in range(n):
        current = parent[current]

    # Now current is definitely in the cycle
    cycle_start = current
    while True:
        cycle.append(current)
        current = parent[current]
        if current == cycle_start:
            cycle.append(current)
            break

    cycle.reverse()
    return dist, True, cycle
```

### Template 3: Bellman-Ford with K Edges Constraint
```python
def bellman_ford_k_edges(n, edges, src, dst, k):
    """
    Find shortest path using at most k edges
    Useful for problems like "Cheapest Flights Within K Stops"
    """
    # dist[i] = shortest distance to vertex i
    dist = [float('inf')] * n
    dist[src] = 0

    # Relax edges exactly k times (at most k edges)
    for iteration in range(k):
        # Use temporary array to avoid using updated values in same iteration
        temp_dist = dist.copy()

        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < temp_dist[v]:
                temp_dist[v] = dist[u] + w

        dist = temp_dist

        # Early termination
        if iteration > 0 and dist[dst] == prev_dist:
            break
        prev_dist = dist[dst]

    return dist[dst] if dist[dst] != float('inf') else -1
```

### Template 4: SPFA (Shortest Path Faster Algorithm)
```python
from collections import deque

def spfa(n, edges, src):
    """
    Queue-optimized Bellman-Ford (SPFA)
    Average case: O(E), Worst case: O(V·E)
    Only relax edges from vertices that were updated
    """
    # Build adjacency list
    graph = [[] for _ in range(n)]
    for u, v, w in edges:
        graph[u].append((v, w))

    dist = [float('inf')] * n
    dist[src] = 0

    # Queue of vertices to process
    queue = deque([src])
    in_queue = [False] * n
    in_queue[src] = True

    # Count times each vertex is added to queue
    count = [0] * n
    count[src] = 1

    while queue:
        u = queue.popleft()
        in_queue[u] = False

        for v, w in graph[u]:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

                if not in_queue[v]:
                    queue.append(v)
                    in_queue[v] = True
                    count[v] += 1

                    # Negative cycle detection
                    if count[v] >= n:
                        return None  # Negative cycle

    return dist
```

### Template 5: Bellman-Ford with Path Reconstruction
```python
def bellman_ford_with_path(n, edges, src, dst):
    """
    Find shortest path and reconstruct the actual path
    Returns: (distance, path)
    """
    dist = [float('inf')] * n
    dist[src] = 0
    parent = [-1] * n

    # Relax edges V-1 times
    for i in range(n - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                parent[v] = u
                updated = True

        if not updated:
            break

    # Check for negative cycle
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return float('inf'), []  # Negative cycle

    # Reconstruct path
    if dist[dst] == float('inf'):
        return float('inf'), []

    path = []
    current = dst
    while current != -1:
        path.append(current)
        current = parent[current]

    path.reverse()
    return dist[dst], path
```

### Template 6: Currency Arbitrage Detection
```python
import math

def detect_arbitrage(n, exchange_rates):
    """
    Detect arbitrage opportunity in currency exchange
    exchange_rates[i][j] = rate from currency i to currency j
    Returns: True if arbitrage exists, False otherwise
    """
    # Convert to logarithmic weights
    # If product of rates > 1, arbitrage exists
    # Take negative log to use Bellman-Ford for negative cycles
    edges = []
    for u in range(n):
        for v in range(n):
            if u != v and exchange_rates[u][v] > 0:
                # Negative log to convert product to sum
                weight = -math.log(exchange_rates[u][v])
                edges.append((u, v, weight))

    # Run Bellman-Ford from any source
    dist = [float('inf')] * n
    dist[0] = 0

    # Relax edges V-1 times
    for _ in range(n - 1):
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

    # Check for negative cycle (arbitrage)
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return True  # Arbitrage detected

    return False
```


## Algorithm Comparison

### Bellman-Ford vs Dijkstra vs Floyd-Warshall

| Feature | Bellman-Ford | Dijkstra | Floyd-Warshall |
|---------|--------------|----------|----------------|
| **Problem Type** | Single-source | Single-source | All-pairs |
| **Time Complexity** | O(V·E) | O((V+E) log V) | O(V³) |
| **Space Complexity** | O(V) | O(V) | O(V²) |
| **Negative Weights** | ✅ Yes | ❌ No | ✅ Yes |
| **Negative Cycles** | ✅ Detects | N/A | ✅ Detects |
| **Implementation** | Simple (2 loops) | Moderate (priority queue) | Very simple (3 loops) |
| **Early Termination** | ✅ Possible | ✅ Can stop at target | ❌ Must complete |
| **Average Performance** | Slow for dense graphs | Fast for sparse graphs | Good for small graphs |
| **Best Graph Type** | Any (especially with negatives) | Sparse, non-negative | Dense, small |
| **Optimization** | SPFA variant | A* with heuristic | None practical |

### When to Use Each Algorithm

```
Shortest Path Algorithm Selection:

1. Does graph have negative edge weights?
   ├── YES → Continue to 2
   │   ├── Single-source? → Use Bellman-Ford
   │   ├── All-pairs? → Use Floyd-Warshall
   │   └── Need cycle detection? → Use Bellman-Ford
   │
   └── NO → Continue to 3

2. Single-source or all-pairs?
   ├── Single-source → Use Dijkstra
   │   └── Very sparse graph? → Consider SPFA
   │
   └── All-pairs → Continue to 4

3. What's the graph size?
   ├── Small (V ≤ 400) → Floyd-Warshall
   └── Large → Run Dijkstra V times

4. Special considerations:
   ├── Need to detect negative cycles? → Bellman-Ford or Floyd-Warshall
   ├── Edge count constraint (K edges)? → Modified Bellman-Ford
   ├── Unweighted graph? → BFS
   └── Tree structure? → DFS/BFS
```

### Performance Comparison

**Example: Graph with V=1000 vertices**

| Graph Density | Edges | Bellman-Ford | Dijkstra | SPFA (avg) |
|---------------|-------|--------------|----------|------------|
| Sparse | 2,000 | 2,000,000 ops | ~20,000 ops ⚡ | ~40,000 ops |
| Medium | 10,000 | 10,000,000 ops | ~100,000 ops ⚡ | ~200,000 ops |
| Dense | 100,000 | 100,000,000 ops | ~1,000,000 ops ⚡ | ~2,000,000 ops |

**Note**: Dijkstra is typically 50-100x faster than Bellman-Ford for non-negative weights.

### Algorithm Selection Examples

| Scenario | Best Algorithm | Why |
|----------|----------------|-----|
| GPS navigation (road network) | **Dijkstra** | Non-negative weights, sparse graph |
| Currency exchange with fees | **Bellman-Ford** | Negative weights possible |
| Arbitrage detection | **Bellman-Ford** | Need negative cycle detection |
| Network delay time | **Dijkstra** | Non-negative, single-source |
| Course prerequisites (all pairs) | **Floyd-Warshall** | Small graph, transitive closure |
| Flight with at most K stops | **Bellman-Ford (K iterations)** | Edge count constraint |
| Internet routing (OSPF) | **Dijkstra** | Non-negative costs |
| Forex trading opportunities | **Bellman-Ford** | Detect arbitrage cycles |


## LC Examples

### Example 1: Cheapest Flights Within K Stops (LC 787)

```python
# LC 787 - Cheapest Flights Within K Stops
# Classic Bellman-Ford with K edges constraint

def findCheapestPrice(n, flights, src, dst, k):
    """
    Find cheapest price with at most k stops (k+1 edges)
    """
    # Initialize distances
    prices = [float('inf')] * n
    prices[src] = 0

    # Relax edges at most k+1 times (k stops = k+1 edges)
    for i in range(k + 1):
        # Use temp array to ensure we only use distances from previous iteration
        temp_prices = prices.copy()

        for u, v, price in flights:
            if prices[u] != float('inf'):
                temp_prices[v] = min(temp_prices[v], prices[u] + price)

        prices = temp_prices

    return prices[dst] if prices[dst] != float('inf') else -1


# Alternative: SPFA with stops tracking
def findCheapestPrice_SPFA(n, flights, src, dst, k):
    """
    SPFA variant with stops tracking
    """
    from collections import deque

    # Build adjacency list
    graph = [[] for _ in range(n)]
    for u, v, price in flights:
        graph[u].append((v, price))

    # (cost, node, stops)
    queue = deque([(0, src, 0)])
    # best_cost[node] = minimum cost to reach node
    best = {src: 0}

    min_cost = float('inf')

    while queue:
        cost, node, stops = queue.popleft()

        # Reached destination
        if node == dst:
            min_cost = min(min_cost, cost)
            continue

        # Exceeded stops limit
        if stops > k:
            continue

        # Explore neighbors
        for neighbor, price in graph[node]:
            new_cost = cost + price

            # Pruning: only continue if this is a better path
            if new_cost < best.get(neighbor, float('inf')) or new_cost < min_cost:
                best[neighbor] = new_cost
                queue.append((new_cost, neighbor, stops + 1))

    return min_cost if min_cost != float('inf') else -1
```

### Example 2: Network Delay Time (LC 743)

```python
# LC 743 - Network Delay Time
# Can use Bellman-Ford but Dijkstra is more efficient

def networkDelayTime(times, n, k):
    """
    Bellman-Ford approach (Dijkstra is better for this problem)
    """
    # Initialize distances (1-indexed)
    dist = [float('inf')] * (n + 1)
    dist[k] = 0

    # Relax edges n-1 times
    for _ in range(n - 1):
        updated = False
        for u, v, w in times:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True

        if not updated:
            break

    # Find maximum delay
    max_delay = max(dist[1:])
    return max_delay if max_delay != float('inf') else -1
```

### Example 3: Currency Arbitrage Detection

```python
# Detect arbitrage opportunity in currency exchange
import math

def detect_arbitrage_opportunity(rates):
    """
    rates: 2D array where rates[i][j] = exchange rate from currency i to j
    Returns: True if arbitrage exists

    Example:
    rates = [
        [1, 0.5, 2.0],    # Currency 0: 1->1, 0.5->1, 2.0->2
        [2.0, 1, 0.25],   # Currency 1
        [0.5, 4.0, 1]     # Currency 2
    ]
    If 0 -> 1 -> 2 -> 0 gives > 1, arbitrage exists
    """
    n = len(rates)

    # Convert to negative log weights
    edges = []
    for i in range(n):
        for j in range(n):
            if i != j and rates[i][j] > 0:
                # -log(rate): if product > 1, sum of -logs < 0
                weight = -math.log(rates[i][j])
                edges.append((i, j, weight))

    # Bellman-Ford from any starting currency
    dist = [float('inf')] * n
    dist[0] = 0

    # Relax edges n-1 times
    for _ in range(n - 1):
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

    # Check for negative cycle (arbitrage)
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return True

    return False


# Example usage
def find_arbitrage_path(rates):
    """
    Not only detect but find the arbitrage path
    """
    n = len(rates)
    edges = []

    for i in range(n):
        for j in range(n):
            if i != j and rates[i][j] > 0:
                weight = -math.log(rates[i][j])
                edges.append((i, j, weight))

    dist = [0] * n  # Start with 0 (log(1) = 0)
    parent = [-1] * n

    # Relax edges
    for _ in range(n - 1):
        for u, v, w in edges:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                parent[v] = u

    # Find node in negative cycle
    cycle_node = -1
    for u, v, w in edges:
        if dist[u] + w < dist[v]:
            cycle_node = v
            parent[v] = u
            break

    if cycle_node == -1:
        return None  # No arbitrage

    # Extract cycle
    visited = set()
    current = cycle_node
    while current not in visited:
        visited.add(current)
        current = parent[current]

    # Build cycle path
    path = [current]
    node = parent[current]
    while node != current:
        path.append(node)
        node = parent[node]
    path.reverse()

    return path
```

### Example 4: Minimum Cost to Reach Destination (Custom)

```python
def min_cost_with_discounts(n, roads, src, dst, discounts):
    """
    Find minimum cost where some roads have discounts (negative weights)
    roads: [(u, v, cost)]
    discounts: {(u, v): discount_amount}
    """
    # Apply discounts to create edges with potentially negative weights
    edges = []
    for u, v, cost in roads:
        actual_cost = cost - discounts.get((u, v), 0)
        edges.append((u, v, actual_cost))
        # If undirected
        actual_cost_rev = cost - discounts.get((v, u), 0)
        edges.append((v, u, actual_cost_rev))

    # Bellman-Ford
    dist = [float('inf')] * n
    dist[src] = 0

    for _ in range(n - 1):
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

    # Check for negative cycle
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return "Infinite arbitrage possible"

    return dist[dst] if dist[dst] != float('inf') else -1
```

### Example 5: Time Travel Problem (Theoretical)

```python
def shortest_path_with_time_machine(n, edges, src, dst):
    """
    Some edges go back in time (negative weight)
    Need to detect if infinite time travel loop exists

    edges: [(u, v, time_delta)]  # negative = go back in time
    """
    dist = [float('inf')] * n
    dist[src] = 0
    parent = [-1] * n

    # Bellman-Ford
    for _ in range(n - 1):
        for u, v, time in edges:
            if dist[u] != float('inf') and dist[u] + time < dist[v]:
                dist[v] = dist[u] + time
                parent[v] = u

    # Check if destination is affected by negative cycle
    affected = set()
    for u, v, time in edges:
        if dist[u] != float('inf') and dist[u] + time < dist[v]:
            # Mark all reachable nodes from v as affected
            affected.add(v)
            # BFS/DFS to find all reachable from v
            queue = [v]
            visited = {v}
            while queue:
                node = queue.pop(0)
                for u2, v2, _ in edges:
                    if u2 == node and v2 not in visited:
                        visited.add(v2)
                        queue.append(v2)
            affected.update(visited)

    if dst in affected:
        return "Can arrive arbitrarily early (time loop)"

    return dist[dst] if dist[dst] != float('inf') else "Unreachable"
```


## Problems by Pattern

### **Classic Shortest Path with Negatives**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Cheapest Flights Within K Stops | 787 | K-edge Bellman-Ford | Medium |
| Network Delay Time | 743 | Basic Bellman-Ford (Dijkstra better) | Medium |
| Minimum Cost to Reach Destination | 1928 | Constrained path | Hard |
| Path with Maximum Probability | 1514 | Modified weights | Medium |

### **Negative Cycle Detection**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Currency Arbitrage | N/A | Log transformation | Hard |
| Detect Cycle in Graph | N/A | V-th iteration check | Medium |
| Find Negative Weight Cycle | N/A | Track parent pointers | Hard |

### **Constrained Path Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Cheapest Flights K Stops | 787 | Iteration limit | Medium |
| Maximum Probability Path | 1514 | Modified Bellman-Ford | Medium |
| Minimum Cost K Edges | N/A | K iterations | Medium |

### **Graph Metrics with Negatives**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Evaluate Division | 399 | Weighted graph | Medium |
| Accounts Merge | 721 | Union-Find better | Medium |


## Decision Framework

### When to Use Bellman-Ford

✅ **Use Bellman-Ford when:**
- Graph has negative edge weights
- Need to detect negative cycles
- Path must use at most K edges
- Currency exchange or arbitrage problems
- Simple implementation is priority over speed
- Working with distributed systems (can be parallelized)
- Graph structure changes frequently (easier to update)

❌ **Don't use Bellman-Ford when:**
- All weights are non-negative (use Dijkstra instead)
- Need all-pairs shortest path on small graph (use Floyd-Warshall)
- Graph is very large and dense (too slow)
- Real-time performance critical and no negative weights
- Working with unweighted graph (use BFS)

### Implementation Checklist

```python
# Bellman-Ford Implementation Checklist:

# 1. Initialize distances
dist = [float('inf')] * n
dist[src] = 0

# 2. Relax all edges V-1 times
for iteration in range(n - 1):
    updated = False
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            dist[v] = dist[u] + w
            updated = True

    # Early termination optimization
    if not updated:
        break

# 3. Check for negative cycles (V-th iteration)
has_negative_cycle = False
for u, v, w in edges:
    if dist[u] != float('inf') and dist[u] + w < dist[v]:
        has_negative_cycle = True
        break

# 4. Handle results
if has_negative_cycle:
    return None  # or handle appropriately
return dist
```

### Common Optimizations

1. **Early Termination**
   ```python
   # If no updates in an iteration, done early
   if not updated:
       break
   ```

2. **SPFA (Queue-Based)**
   ```python
   # Only process vertices that had updates
   # Average O(E), worst O(V·E)
   ```

3. **Bidirectional Search**
   ```python
   # Run from both src and dst simultaneously
   # Can reduce iterations by half
   ```

4. **Iteration Limiting**
   ```python
   # For K-edge constraint, stop after K iterations
   for i in range(min(k, n-1)):
   ```


## Summary & Quick Reference

### Time/Space Complexity

| Aspect | Complexity | Notes |
|--------|------------|-------|
| Time (Standard) | O(V·E) | V-1 iterations over E edges |
| Time (SPFA avg) | O(E) | Queue-optimized average case |
| Time (SPFA worst) | O(V·E) | Degrades to standard |
| Space | O(V) | Distance array + parent array |
| Cycle Detection | +O(E) | Additional V-th iteration |

### Core Algorithm Structure

```python
# Standard Bellman-Ford Pattern
dist[src] = 0
for _ in range(V - 1):                    # V-1 iterations
    for each edge (u, v, w):              # All edges
        if dist[u] + w < dist[v]:         # Relaxation
            dist[v] = dist[u] + w

# Negative cycle check
for each edge (u, v, w):
    if dist[u] + w < dist[v]:
        return "Negative cycle exists"
```

### Key Differences from Other Algorithms

| Aspect | Bellman-Ford | Dijkstra | Floyd-Warshall |
|--------|--------------|----------|----------------|
| **Edge Relaxation** | All edges, V-1 times | Only shortest distance nodes | All pairs via intermediate |
| **Data Structure** | Simple array | Priority queue | 2D matrix |
| **Order Matters** | No (relaxes all edges) | Yes (greedy selection) | Yes (k loop outer) |
| **Parallelizable** | ✅ Yes (within iteration) | ❌ No (sequential) | ✅ Yes (with modifications) |

### Common Patterns & Tricks

#### **Pattern 1: Negative Cycle Detection**
```python
# After V-1 iterations, one more check
for u, v, w in edges:
    if dist[u] + w < dist[v]:
        return "Has negative cycle"
```

#### **Pattern 2: K-Edge Constraint**
```python
# Use temp array to ensure using previous iteration values
for _ in range(k):
    temp = dist.copy()
    for u, v, w in edges:
        temp[v] = min(temp[v], dist[u] + w)
    dist = temp
```

#### **Pattern 3: Path Reconstruction**
```python
parent = [-1] * n
# During relaxation:
if dist[u] + w < dist[v]:
    dist[v] = dist[u] + w
    parent[v] = u

# Reconstruct path
path = []
while current != -1:
    path.append(current)
    current = parent[current]
path.reverse()
```

#### **Pattern 4: Arbitrage Detection**
```python
# Convert multiplicative to additive
weight = -math.log(exchange_rate)
# Negative cycle = arbitrage opportunity
```

#### **Pattern 5: Early Termination**
```python
for _ in range(n - 1):
    updated = False
    for u, v, w in edges:
        if relax(u, v, w):
            updated = True
    if not updated:
        break  # No more updates possible
```

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Using Bellman-Ford when Dijkstra would work (wasting time)
- Forgetting to check `dist[u] != inf` before relaxation
- Not using temp array for K-edge constraint problems
- Wrong number of iterations (should be V-1 for standard)
- Not handling disconnected components
- Forgetting early termination optimization

**✅ Best Practices:**
- Always check if negative weights actually exist
- Use SPFA for better average performance on sparse graphs
- Implement early termination for efficiency
- Use temp array for K-edge problems to avoid wrong updates
- Handle infinity values carefully in comparisons
- Consider Dijkstra first if no negative weights
- For all-pairs, compare with Floyd-Warshall cost

### Interview Tips

1. **When to mention Bellman-Ford**:
   - "Are there negative edge weights?" → If yes, mention Bellman-Ford
   - "Need to detect negative cycles?" → Bellman-Ford is the answer
   - "Path with at most K edges?" → Modified Bellman-Ford

2. **Complexity discussion**:
   - State O(V·E) time complexity upfront
   - Mention it's slower than Dijkstra for non-negative weights
   - Discuss SPFA as optimization for sparse graphs

3. **Implementation notes**:
   - Simpler to implement than Dijkstra (no priority queue)
   - Easy to modify for constraints (K edges)
   - Can detect negative cycles with one extra iteration

4. **Alternative approaches**:
   - If no negative weights → "Dijkstra would be faster"
   - If all-pairs needed → "Floyd-Warshall might be simpler"
   - If very large graph → "SPFA optimization recommended"

5. **Edge cases to discuss**:
   - Disconnected components
   - Negative cycles (how to handle)
   - Source unreachable from some vertices
   - Multiple edges between same vertices

### Related Algorithms

- **[Dijkstra](./Dijkstra.md)**: Faster single-source, no negative weights
- **[Floyd-Warshall](./Floyd-Warshall.md)**: All-pairs, handles negatives
- **SPFA**: Queue-optimized Bellman-Ford variant
- **Johnson's Algorithm**: Reweighting + Dijkstra for all-pairs
- **Yen's Algorithm**: K-shortest paths
- **Eppstein's Algorithm**: K-shortest paths (faster)

### Quick Decision Matrix

| Your Situation | Choose |
|----------------|--------|
| Single-source, no negative weights | **Dijkstra** ⚡ |
| Single-source, negative weights | **Bellman-Ford** ✅ |
| Need to detect negative cycles | **Bellman-Ford** ✅ |
| At most K edges/hops | **Bellman-Ford (K iterations)** ✅ |
| All-pairs, small graph | **Floyd-Warshall** |
| All-pairs, large graph | **Dijkstra V times** or **Johnson's** |
| Unweighted graph | **BFS** ⚡⚡ |
| Currency arbitrage | **Bellman-Ford** ✅ |
| Real-time navigation | **Dijkstra** or **A*** ⚡ |
