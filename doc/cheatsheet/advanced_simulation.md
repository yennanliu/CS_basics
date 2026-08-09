# Advanced Simulation

## LeetCode Problem Lists

- [Simulation](https://leetcode.com/problem-list/simulation/)
- [Design](https://leetcode.com/problem-list/design/)

## Overview
**Simulation** problems require step-by-step execution of a process, following specific rules and state transitions. These problems test your ability to model real-world scenarios, manage complex state, and implement precise logic flow.

### Key Properties
- **Time Complexity**: Often O(steps × operations) where steps is simulation length
- **Space Complexity**: O(state_size) for maintaining current state
- **Core Idea**: Model the process accurately and execute step by step
- **When to Use**: Process simulation, robot movement, game mechanics, state machines
- **Key Skills**: State management, rule implementation, edge case handling

### Core Characteristics
- **State Tracking**: Maintain current system state accurately
- **Rule Following**: Implement precise rules and transitions
- **Step-by-Step**: Execute process incrementally
- **Edge Case Handling**: Boundary conditions and special states
- **Optimization**: Detect cycles, precompute, or mathematical shortcuts

## Problem Categories

### **Category 1: Robot/Movement Simulation**
- **Description**: Simulate robot movement following commands
- **Examples**: LC 2061 (Robot Cleaning), LC 2069 (Walking Robot Simulation), LC 657 (Robot Return to Origin)
- **Pattern**: Track position, direction, and state changes

### **Category 2: Game/Process Simulation**
- **Description**: Simulate game rules or multi-step processes
- **Examples**: LC 2532 (Time to Cross Bridge), LC 1823 (Find Winner of Circular Game), LC 874 (Walking Robot Simulation)
- **Pattern**: State machine with rule-based transitions

### **Category 3: System/Environment Simulation**
- **Description**: Model complex systems with multiple components
- **Examples**: LC 1701 (Average Waiting Time), LC 1603 (Design Parking System)
- **Pattern**: Component interaction and resource management

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Time Complexity | When to Use |
|---------------|----------|-----------------|-------------|
| **Basic Movement** | Robot navigation | O(steps) | Simple movement simulation |
| **State Machine** | Game mechanics | O(steps × rules) | Rule-based processes |
| **Event Queue** | System simulation | O(events log events) | Timeline-based simulation |
| **Grid World** | 2D environment | O(steps) | Spatial simulation |

### Template 1: Robot Movement Simulation
```python
class RobotSimulation:
    """Template for robot movement simulation"""

    def __init__(self):
        # Robot state
        self.x = 0
        self.y = 0
        self.direction = 0  # 0: North, 1: East, 2: South, 3: West

        # Direction vectors: North, East, South, West
        self.dx = [0, 1, 0, -1]
        self.dy = [1, 0, -1, 0]

    def turn_left(self):
        """Turn robot left (counterclockwise)"""
        self.direction = (self.direction - 1) % 4

    def turn_right(self):
        """Turn robot right (clockwise)"""
        self.direction = (self.direction + 1) % 4

    def move_forward(self, steps=1):
        """Move robot forward by steps"""
        for _ in range(steps):
            new_x = self.x + self.dx[self.direction]
            new_y = self.y + self.dy[self.direction]

            # Check if move is valid (implement based on problem constraints)
            if self.is_valid_position(new_x, new_y):
                self.x = new_x
                self.y = new_y
            else:
                break  # Stop on obstacle

    def is_valid_position(self, x, y):
        """Check if position is valid (override based on problem)"""
        return True  # Default: all positions valid

    def execute_commands(self, commands):
        """Execute a sequence of commands"""
        for command in commands:
            if command == 'L':
                self.turn_left()
            elif command == 'R':
                self.turn_right()
            elif command == 'G':
                self.move_forward()
            # Add more commands as needed

        return (self.x, self.y)
```

### Template 2: State Machine Simulation
```python
class StateMachineSimulation:
    """Template for state machine based simulation"""

    def __init__(self, initial_state):
        self.current_state = initial_state
        self.state_history = [initial_state]
        self.step_count = 0

        # Define state transition rules (override in subclasses)
        self.transition_rules = {}

    def add_transition(self, from_state, event, to_state, action=None):
        """Add a state transition rule"""
        if from_state not in self.transition_rules:
            self.transition_rules[from_state] = {}
        self.transition_rules[from_state][event] = (to_state, action)

    def process_event(self, event):
        """Process an event and transition state"""
        if (self.current_state in self.transition_rules and
            event in self.transition_rules[self.current_state]):

            next_state, action = self.transition_rules[self.current_state][event]

            # Execute action if provided
            if action:
                action()

            # Transition to next state
            self.current_state = next_state
            self.state_history.append(next_state)
            self.step_count += 1

            return True
        return False

    def simulate_steps(self, events):
        """Simulate multiple steps with events"""
        results = []
        for event in events:
            success = self.process_event(event)
            results.append({
                'event': event,
                'success': success,
                'state': self.current_state,
                'step': self.step_count
            })
        return results

    def detect_cycle(self):
        """Detect if we've entered a cycle"""
        seen_states = {}
        for i, state in enumerate(self.state_history):
            if state in seen_states:
                cycle_start = seen_states[state]
                cycle_length = i - cycle_start
                return cycle_start, cycle_length
            seen_states[state] = i
        return None, 0
```

### Template 3: Event Queue Simulation
```python
import heapq
from collections import defaultdict

class EventQueueSimulation:
    """Template for timeline-based simulation with events"""

    def __init__(self):
        self.current_time = 0
        self.event_queue = []  # Min-heap: (time, event_type, event_data)
        self.system_state = {}

    def schedule_event(self, time, event_type, event_data):
        """Schedule an event to happen at specific time"""
        heapq.heappush(self.event_queue, (time, event_type, event_data))

    def process_event(self, event_type, event_data):
        """Process a specific event type (override in subclasses)"""
        # Default implementation - override for specific event types
        pass

    def simulate_until(self, end_time):
        """Run simulation until specified time"""
        results = []

        while self.event_queue and self.event_queue[0][0] <= end_time:
            event_time, event_type, event_data = heapq.heappop(self.event_queue)

            # Update current time
            self.current_time = event_time

            # Process the event
            result = self.process_event(event_type, event_data)
            results.append({
                'time': event_time,
                'type': event_type,
                'data': event_data,
                'result': result
            })

        return results

    def get_state_at_time(self, time):
        """Get system state at specific time"""
        # Save current state
        saved_time = self.current_time
        saved_queue = self.event_queue.copy()
        saved_state = self.system_state.copy()

        # Simulate to target time
        self.simulate_until(time)
        result_state = self.system_state.copy()

        # Restore state
        self.current_time = saved_time
        self.event_queue = saved_queue
        self.system_state = saved_state

        return result_state
```

### Template 4: Grid World Simulation
```python
class GridWorldSimulation:
    """Template for 2D grid-based simulation"""

    def __init__(self, rows, cols):
        self.rows = rows
        self.cols = cols
        self.grid = [[0 for _ in range(cols)] for _ in range(rows)]
        self.entities = {}  # Track entities and their positions

        # Direction mappings
        self.directions = {
            'UP': (-1, 0),
            'DOWN': (1, 0),
            'LEFT': (0, -1),
            'RIGHT': (0, 1)
        }

    def is_valid_position(self, row, col):
        """Check if position is within grid bounds"""
        return 0 <= row < self.rows and 0 <= col < self.cols

    def is_free_position(self, row, col):
        """Check if position is free (no obstacles)"""
        return (self.is_valid_position(row, col) and
                self.grid[row][col] == 0)

    def add_entity(self, entity_id, row, col):
        """Add entity to specific position"""
        if self.is_free_position(row, col):
            self.entities[entity_id] = (row, col)
            self.grid[row][col] = entity_id
            return True
        return False

    def move_entity(self, entity_id, direction):
        """Move entity in specified direction"""
        if entity_id not in self.entities:
            return False

        curr_row, curr_col = self.entities[entity_id]
        dr, dc = self.directions[direction]
        new_row, new_col = curr_row + dr, curr_col + dc

        if self.is_free_position(new_row, new_col):
            # Clear old position
            self.grid[curr_row][curr_col] = 0
            # Set new position
            self.grid[new_row][new_col] = entity_id
            self.entities[entity_id] = (new_row, new_col)
            return True
        return False

    def simulate_moves(self, entity_id, moves):
        """Simulate sequence of moves for entity"""
        path = [self.entities.get(entity_id)]

        for move in moves:
            success = self.move_entity(entity_id, move)
            if success:
                path.append(self.entities[entity_id])
            else:
                break  # Stop on invalid move

        return path

    def get_neighbors(self, row, col, include_diagonal=False):
        """Get valid neighbor positions"""
        neighbors = []
        directions = [(0,1), (0,-1), (1,0), (-1,0)]
        if include_diagonal:
            directions.extend([(1,1), (1,-1), (-1,1), (-1,-1)])

        for dr, dc in directions:
            new_row, new_col = row + dr, col + dc
            if self.is_valid_position(new_row, new_col):
                neighbors.append((new_row, new_col))

        return neighbors
```

## Problems by Pattern

### **Robot Movement Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Robot Return to Origin | 657 | Simple position tracking | Easy |
| Walking Robot Simulation | 874 | Direction + obstacle handling | Easy |
| Walking Robot Simulation II | 2069 | Circular path optimization | Medium |
| Number of Spaces Cleaning Robot Cleaned | 2061 | Grid traversal simulation | Medium |

### **Game Simulation Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Find Winner of Circular Game | 1823 | Josephus problem simulation | Medium |
| Time to Cross a Bridge | 2532 | Priority queue simulation | Hard |
| Snakes and Ladders | 909 | BFS + board simulation | Medium |

### **System Simulation Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Design Parking System | 1603 | Resource management | Easy |
| Average Waiting Time | 1701 | Queue simulation | Medium |
| Design Phone Directory | 379 | State management | Medium |

## LC Examples

### 2-1) Walking Robot Simulation II — LC 2069
> Map position to perimeter index (mod perimeter); track direction at each corner.

```java
// LC 2069 - Walking Robot Simulation II
// IDEA: Map perimeter as 1D array (mod perimeter); corners change direction
// time = O(1) per step, space = O(1)
class Robot {
    int w, h, perimeter, pos = 0;
    // perimeter positions: 0=bottom, w-1=bottom-right, w+h-2=top-right, 2w+h-3=top-left
    String[] DIRS = {"East","North","West","South"};
    int[] dx = {1,0,-1,0}, dy = {0,1,0,-1};
    public Robot(int width, int height) {
        w = width; h = height;
        perimeter = 2 * (w + h - 2);
    }
    public void step(int num) { pos = (pos + num) % perimeter; }
    public int[] getPos() {
        if (pos < w)            return new int[]{pos, 0};
        if (pos < w + h - 1)   return new int[]{w - 1, pos - w + 1};
        if (pos < 2*w + h - 2) return new int[]{2*w + h - 3 - pos, h - 1};
        return new int[]{0, 2*w + 2*h - 4 - pos};
    }
    public String getDir() {
        // NOTE: at pos==0 this returns "East" (start dir). After a full loop back to the
        // origin the true LC 2069 answer is "South"; distinguishing requires tracking whether
        // step() was ever called. The Python version below tracks direction and handles this.
        if (pos == 0)             return "East"; // start facing East
        if (pos < w)              return "East";
        if (pos < w + h - 1)     return "North";
        if (pos < 2*w + h - 2)   return "West";
        return "South";
    }
}
```

```python
class Robot:
    """Optimized robot simulation with circular path detection"""

    def __init__(self, width, height):
        self.width = width
        self.height = height

        # Robot state
        self.pos = 0  # Position on perimeter (0 to perimeter-1)
        self.direction = 0  # 0: East, 1: North, 2: West, 3: South

        # Calculate perimeter and key positions
        self.perimeter = 2 * (width + height - 2)
        self.corners = [0, width - 1, width + height - 2, 2 * width + height - 3]

        # Direction names
        self.dir_names = ["East", "North", "West", "South"]

    def position_to_coords(self, pos):
        """Convert perimeter position to (x, y) coordinates"""
        if pos < self.width:  # Bottom edge
            return [pos, 0]
        elif pos < self.width + self.height - 1:  # Right edge
            return [self.width - 1, pos - self.width + 1]
        elif pos < 2 * self.width + self.height - 2:  # Top edge
            return [2 * self.width + self.height - 3 - pos, self.height - 1]
        else:  # Left edge
            return [0, 2 * self.width + 2 * self.height - 4 - pos]

    def step(self, num):
        """Move robot num steps"""
        if num == 0:
            return

        # Handle full loops
        num = num % self.perimeter if self.perimeter > 0 else 0

        self.pos = (self.pos + num) % self.perimeter

        # Update direction if at corner
        if self.pos in self.corners and num > 0:
            self.direction = (self.corners.index(self.pos) + 1) % 4

    def getPos(self):
        """Get current position"""
        return self.position_to_coords(self.pos)

    def getDir(self):
        """Get current direction"""
        return self.dir_names[self.direction]
```

### 2-2) Time to Cross a Bridge — LC 2532
> Four priority queues: waiting left/right + working left/right; simulate worker assignments by time.

```java
// LC 2532 - Time to Cross a Bridge
// IDEA: 4 heaps (waitL, waitR, workL, workR); pick highest-efficiency waiting worker each step
// time = O(N log K), space = O(K)  K = workers
public int findCrossingTime(int n, int k, int[][] time) {
    // Max-heaps for waiting (by efficiency = leftTime + rightTime, higher = worse = higher priority)
    PriorityQueue<int[]> waitL = new PriorityQueue<>((a,b) -> b[0]-a[0]); // [eff, id]
    PriorityQueue<int[]> waitR = new PriorityQueue<>((a,b) -> b[0]-a[0]);
    // Min-heaps for working (by finish time)
    PriorityQueue<int[]> workL = new PriorityQueue<>((a,b) -> a[0]-b[0]); // [finishTime, id]
    PriorityQueue<int[]> workR = new PriorityQueue<>((a,b) -> a[0]-b[0]);
    for (int i = 0; i < k; i++) waitL.offer(new int[]{time[i][0]+time[i][2], i});
    int cur = 0;
    for (int boxes = 0; boxes < n; boxes++) {
        // Advance time if no one is waiting
        while (waitL.isEmpty() || waitR.isEmpty()) {
            int nextFinish = Integer.MAX_VALUE;
            if (!workL.isEmpty()) nextFinish = Math.min(nextFinish, workL.peek()[0]);
            if (!workR.isEmpty()) nextFinish = Math.min(nextFinish, workR.peek()[0]);
            cur = Math.max(cur, nextFinish);
            while (!workL.isEmpty() && workL.peek()[0] <= cur) { int[] w = workL.poll(); waitL.offer(new int[]{time[w[1]][0]+time[w[1]][2], w[1]}); }
            while (!workR.isEmpty() && workR.peek()[0] <= cur) { int[] w = workR.poll(); waitR.offer(new int[]{time[w[1]][0]+time[w[1]][2], w[1]}); }
            if (waitL.isEmpty() && waitR.isEmpty()) cur = nextFinish;
        }
        // Priority: right->left over left->right
        if (!waitR.isEmpty()) {
            int[] w = waitR.poll();
            cur += time[w[1]][2];
            workL.offer(new int[]{cur + time[w[1]][3], w[1]});
        } else {
            int[] w = waitL.poll();
            cur += time[w[1]][0];
            workR.offer(new int[]{cur + time[w[1]][1], w[1]});
        }
    }
    // Wait for all right-side workers to come back
    int ans = cur;
    while (!waitR.isEmpty()) { int[] w = waitR.poll(); ans = Math.max(ans, cur) + time[w[1]][2]; cur = ans; }
    while (!workR.isEmpty()) { int[] w = workR.poll(); ans = Math.max(ans, w[0]) + time[w[1]][2]; }
    return ans;
}
```

```python
import heapq

def findCrossingTime(n, k, time):
    """Simulate workers crossing bridge with priority queues"""

    # Priority queues: (efficiency, worker_id)
    left_wait = [(time[i][0] + time[i][2], i) for i in range(k)]
    right_wait = []
    left_work = []  # (available_time, efficiency, worker_id)
    right_work = []

    heapq.heapify(left_wait)

    boxes_left = n
    boxes_right = 0
    current_time = 0

    def move_available_workers(current_time):
        # Move workers from work to wait queues when they become available
        while left_work and left_work[0][0] <= current_time:
            _, eff, worker_id = heapq.heappop(left_work)
            heapq.heappush(left_wait, (eff, worker_id))

        while right_work and right_work[0][0] <= current_time:
            _, eff, worker_id = heapq.heappop(right_work)
            heapq.heappush(right_wait, (eff, worker_id))

    while boxes_right < n:
        move_available_workers(current_time)

        # Priority: right to left > left to right
        if right_wait and boxes_right > 0:
            # Worker crosses from right to left
            eff, worker_id = heapq.heappop(right_wait)
            current_time += time[worker_id][3]

            # Worker goes to put box and work on left
            work_end_time = current_time + time[worker_id][2]
            heapq.heappush(left_work, (work_end_time, eff, worker_id))
            boxes_right -= 1

        elif left_wait and boxes_left > 0:
            # Worker crosses from left to right
            eff, worker_id = heapq.heappop(left_wait)
            current_time += time[worker_id][1]

            # Worker goes to pick box and work on right
            work_end_time = current_time + time[worker_id][0]
            heapq.heappush(right_work, (work_end_time, eff, worker_id))
            boxes_left -= 1
            boxes_right += 1

        else:
            # No workers available, advance time to next available worker
            next_time = float('inf')
            if left_work:
                next_time = min(next_time, left_work[0][0])
            if right_work:
                next_time = min(next_time, right_work[0][0])
            current_time = next_time

    return current_time
```

### 2-3) Number of Spaces Cleaning Robot Cleaned — LC 2061
> DFS from (0,0) facing East; stop when revisiting the same (row, col, direction) state.

```java
// LC 2061 - Number of Spaces Cleaning Robot Cleaned
// IDEA: DFS simulation; stop when (row, col, dir) state repeats (cycle detected)
// time = O(M*N*4), space = O(M*N*4)
public int numberOfCleanRooms(int[][] room) {
    int m = room.length, n = room[0].length;
    boolean[][][] visited = new boolean[m][n][4];
    int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}}; // E,S,W,N
    int row = 0, col = 0, dir = 0, count = 0;
    Set<String> cleaned = new HashSet<>();
    while (!visited[row][col][dir]) {
        visited[row][col][dir] = true;
        if (cleaned.add(row + "," + col)) count++;
        int nr = row + dirs[dir][0], nc = col + dirs[dir][1];
        if (nr >= 0 && nr < m && nc >= 0 && nc < n && room[nr][nc] == 0) {
            row = nr; col = nc;
        } else {
            dir = (dir + 1) % 4; // turn right
        }
    }
    return count;
}
```

```python
def numberOfCleanRooms(room):
    """Simulate robot cleaning with cycle detection"""
    m, n = len(room), len(room[0])

    # Robot starts at (0, 0) facing right
    x, y, direction = 0, 0, 0

    # Directions: right, down, left, up
    dx = [0, 1, 0, -1]
    dy = [1, 0, -1, 0]

    cleaned = set()
    visited_states = set()  # (x, y, direction)

    while True:
        # Clean current position
        cleaned.add((x, y))

        # Check if we've been in this state before (cycle detection)
        state = (x, y, direction)
        if state in visited_states:
            break
        visited_states.add(state)

        # Try to move forward
        next_x = x + dx[direction]
        next_y = y + dy[direction]

        # Check if next position is valid and not blocked
        if (0 <= next_x < m and 0 <= next_y < n and
            room[next_x][next_y] == 0):
            # Move forward
            x, y = next_x, next_y
        else:
            # Turn right (clockwise)
            direction = (direction + 1) % 4

    return len(cleaned)
```

## Advanced Techniques

### Cycle Detection in Simulations
```python
def detect_simulation_cycle(state_sequence):
    """Detect cycles in simulation using Floyd's algorithm"""

    def get_next_state(state):
        # Implement state transition logic
        pass

    # Floyd's cycle detection
    slow = fast = initial_state

    # Phase 1: Detect if cycle exists
    while True:
        slow = get_next_state(slow)
        fast = get_next_state(get_next_state(fast))
        if slow == fast:
            break

    # Phase 2: Find cycle start
    slow = initial_state
    while slow != fast:
        slow = get_next_state(slow)
        fast = get_next_state(fast)

    # Phase 3: Find cycle length
    cycle_length = 1
    current = get_next_state(slow)
    while current != slow:
        current = get_next_state(current)
        cycle_length += 1

    return slow, cycle_length
```

### State Compression Techniques
```python
class StateCompression:
    """Techniques for compressing simulation states"""

    def hash_state(self, state):
        """Create hash for complex state objects"""
        # For tuples/lists
        return hash(tuple(state) if isinstance(state, list) else state)

    def compress_grid_state(self, grid):
        """Compress 2D grid into single value"""
        # Bit manipulation for binary grids
        result = 0
        for i, row in enumerate(grid):
            for j, val in enumerate(row):
                if val:
                    result |= (1 << (i * len(row) + j))
        return result

    def compress_position_direction(self, x, y, direction, max_x, max_y):
        """Compress position and direction into single value"""
        return x * max_y * 4 + y * 4 + direction
```

### Optimization Strategies
```python
class SimulationOptimizations:
    """Various optimization techniques for simulations"""

    def precompute_cycles(self, initial_states):
        """Precompute common cycles for faster simulation"""
        cycle_cache = {}
        for state in initial_states:
            if state not in cycle_cache:
                cycle_start, cycle_length = self.detect_cycle(state)
                cycle_cache[state] = (cycle_start, cycle_length)
        return cycle_cache

    def mathematical_shortcuts(self, steps, cycle_length, cycle_start_pos):
        """Use math to skip repetitive cycles"""
        if steps <= cycle_start_pos:
            return steps

        remaining_steps = steps - cycle_start_pos
        full_cycles = remaining_steps // cycle_length
        final_position = remaining_steps % cycle_length

        return cycle_start_pos + final_position

    def parallel_simulation(self, entities):
        """Simulate multiple entities in parallel"""
        # Use threading or multiprocessing for independent entities
        pass
```

## Performance Optimization Tips

### Memory Management
```python
def memory_optimization_techniques():
    """Optimize memory usage in simulations"""

    # 1. Use generators for large sequences
    def simulate_steps_generator(initial_state, max_steps):
        current_state = initial_state
        for step in range(max_steps):
            yield current_state
            current_state = get_next_state(current_state)

    # 2. Limit history tracking
    def limited_history_simulation(state, max_history=1000):
        history = []
        while True:
            history.append(state)
            if len(history) > max_history:
                history.pop(0)  # Remove oldest
            state = get_next_state(state)

    # 3. Use bitwise operations for boolean states
    def bitwise_state_management(width, height):
        state = 0  # Single integer instead of 2D array
        # Set bit: state |= (1 << (row * width + col))
        # Check bit: (state >> (row * width + col)) & 1
        # Clear bit: state &= ~(1 << (row * width + col))
```

## Summary & Quick Reference

### Common Simulation Patterns

| Pattern | Template | Use Case | Example |
|---------|----------|----------|---------|
| **Robot Movement** | Position + direction tracking | Navigation problems | Walking robot |
| **State Machine** | Rule-based transitions | Game mechanics | Josephus problem |
| **Event Queue** | Timeline simulation | System modeling | Bridge crossing |
| **Grid World** | 2D environment | Spatial problems | Robot cleaning |

### Time Complexity Guide
| Problem Type | Time Complexity | Space Complexity | Notes |
|--------------|-----------------|------------------|-------|
| Basic Movement | O(steps) | O(1) | Simple position tracking |
| State Machine | O(steps × rules) | O(states) | Rule complexity matters |
| Event Simulation | O(events log events) | O(events) | Priority queue overhead |
| Grid Simulation | O(steps × grid_ops) | O(grid_size) | Grid operation complexity |

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Not handling boundary conditions properly
- Missing cycle detection for infinite loops
- Inefficient state representation
- Forgetting to update all state variables

**✅ Best Practices:**
- Always validate moves/transitions before applying
- Implement cycle detection for potentially infinite simulations
- Use appropriate data structures for state representation
- Consider mathematical shortcuts for repetitive patterns
- Test edge cases thoroughly

### Interview Tips
1. **Model the problem accurately**: Understand all rules and constraints
2. **Design clean state representation**: Easy to update and compare
3. **Implement cycle detection**: Prevent infinite loops
4. **Consider optimization**: Mathematical shortcuts, precomputation
5. **Handle edge cases**: Boundaries, invalid states, empty inputs
6. **Test systematically**: Trace through examples step by step

This comprehensive simulation cheatsheet covers the essential patterns and techniques for solving complex process modeling problems efficiently.

---

## Additional Templates (High-Frequency Interview Patterns)

### Quick Decision Table

| Goal | Template | LC examples |
|------|----------|-------------|
| All cells update **at the same time**, no scratch copy allowed | **Template 5** — In-place state encoding | 289 |
| Elements **interact with the most recent survivor** (collide / cancel / undo) | **Template 6** — Stack as simulation state | 735, 682, 844, 946, 1910 |
| Simulate **grade-school arithmetic** on strings too big for `long` | **Template 7** — Column-by-column carry | 43, 415, 67 |
| Process repeats forever — need the **answer, not the loop** | **Template 8** — Algebraic loop detection | 1041 |

---

### Template 5: In-Place State Encoding (Simultaneous Update) ⭐⭐⭐⭐⭐

**Key Idea**: when every cell must transition **based on the ORIGINAL board**, writing the new
value directly destroys data the *later* cells still need to read. Instead of allocating a copy,
store both states in the same integer: **bit 0 = current state, bit 1 = next state**. Reads use
`x & 1` (untouched), writes use `x |= next << 1`. A final pass `x >>= 1` commits every cell at once.

**Generalization**: any value domain works, not just bits — e.g. use `old + 2*new`, or mark
"was 1 now 0" as `-1` and "was 0 now 1" as `2`. The bit trick is just the cleanest encoding.

```java
// java
// LC 289 - Game of Life
// time = O(M*N), space = O(1)  -- no second board
// IDEA: bit0 = current state, bit1 = next state. Neighbours are read with (x & 1) so they
//       still report their ORIGINAL value even after their next state has been written.
public void gameOfLife(int[][] board) {
    int m = board.length, n = board[0].length;
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            int live = 0;
            for (int di = -1; di <= 1; di++) {
                for (int dj = -1; dj <= 1; dj++) {
                    if (di == 0 && dj == 0) continue;
                    int r = i + di, c = j + dj;
                    if (r < 0 || r >= m || c < 0 || c >= n) continue;
                    live += board[r][c] & 1;              // read ORIGINAL state
                }
            }
            int cur  = board[i][j] & 1;
            int next = (cur == 1) ? ((live == 2 || live == 3) ? 1 : 0)   // survive
                                  : ((live == 3) ? 1 : 0);              // reproduce
            board[i][j] |= next << 1;                     // stash next state in bit1
        }
    }
    // commit: shift every cell down so bit1 becomes the live value
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            board[i][j] >>= 1;
}
```

```python
# python
# LC 289 - Game of Life
# time = O(M*N), space = O(1)
# IDEA: bit0 = current state, bit1 = next state; commit with a final >>= 1 sweep
def gameOfLife(board):
    m, n = len(board), len(board[0])
    for i in range(m):
        for j in range(n):
            live = 0
            for di in (-1, 0, 1):
                for dj in (-1, 0, 1):
                    if di == 0 and dj == 0:
                        continue
                    r, c = i + di, j + dj
                    if 0 <= r < m and 0 <= c < n:
                        live += board[r][c] & 1          # read ORIGINAL state
            cur = board[i][j] & 1
            nxt = 1 if (live == 3 or (cur == 1 and live == 2)) else 0
            board[i][j] |= nxt << 1                       # stash next state in bit1
    for i in range(m):
        for j in range(n):
            board[i][j] >>= 1                             # commit
```

> **Follow-up they always ask**: "what if the board is infinite?" — answer: store only the live
> cells in a hash set of `(r, c)`, count neighbours by iterating live cells and incrementing a
> counter map for their 8 neighbours, then rebuild the live set. Memory becomes O(live cells).

---

### Template 6: Stack as Simulation State (Collision / Cancel / Undo) ⭐⭐⭐⭐⭐

**Key Idea**: when a new element only ever interacts with **the most recently surviving element**,
the survivors form a stack. Push each new item, then run a `while` loop that resolves the conflict
against `stack.top()` — the loop may pop several victims, and the new item itself may die.

**Recognition signal**: "the last one still standing", "they annihilate each other", "characters
undo the previous character". Any such rule is O(N) with a stack, not O(N²) with re-scans.

```java
// java
// LC 735 - Asteroid Collision
// time = O(N), space = O(N)
// IDEA: stack holds surviving asteroids. A collision only happens when the incoming asteroid
//       moves left (a < 0) and the top of the stack moves right (top > 0).
public int[] asteroidCollision(int[] asteroids) {
    Deque<Integer> st = new ArrayDeque<>();
    for (int a : asteroids) {
        boolean alive = true;
        while (alive && a < 0 && !st.isEmpty() && st.peek() > 0) {
            if (st.peek() < -a) { st.pop(); continue; }   // top explodes, `a` keeps going
            if (st.peek() == -a) st.pop();                // both explode
            alive = false;                                // `a` explodes (or both did)
        }
        if (alive) st.push(a);
    }
    int[] res = new int[st.size()];
    for (int i = res.length - 1; i >= 0; i--) res[i] = st.pop();
    return res;
}
```

```python
# python
# LC 735 - Asteroid Collision
# time = O(N), space = O(N)
# IDEA: collision only when incoming a < 0 and stack top > 0 (moving toward each other)
def asteroidCollision(asteroids):
    st = []
    for a in asteroids:
        alive = True
        while alive and a < 0 and st and st[-1] > 0:
            if st[-1] < -a:
                st.pop()          # top explodes, `a` survives and keeps colliding
                continue
            if st[-1] == -a:
                st.pop()          # both explode
            alive = False         # `a` explodes
        if alive:
            st.append(a)
    return st
```

#### Variations of Template 6

**Variation A — LC 682 Baseball Game**: *twist: the stack is the record, and ops read/rewrite the top few entries.*

```python
# python
# LC 682 - Baseball Game
# time = O(N), space = O(N)
def calPoints(operations):
    st = []
    for op in operations:
        if op == "C":   st.pop()                    # undo last
        elif op == "D": st.append(st[-1] * 2)
        elif op == "+": st.append(st[-1] + st[-2])  # sum of previous two
        else:           st.append(int(op))
    return sum(st)
```

**Variation B — LC 844 Backspace String Compare**: *twist: `#` pops instead of colliding; compare two independently-built stacks.* (An O(1)-space version walks both strings backwards, counting pending backspaces.)

```python
# python
# LC 844 - Backspace String Compare
# time = O(M+N), space = O(M+N)   (O(1) possible by scanning from the right)
def backspaceCompare(s, t):
    def build(x):
        st = []
        for ch in x:
            if ch == '#':
                if st: st.pop()
            else:
                st.append(ch)
        return st
    return build(s) == build(t)
```

**Variation C — LC 946 Validate Stack Sequences**: *twist: you replay the push order and greedily pop whenever the top matches the next expected pop — valid iff the stack drains.*

```java
// java
// LC 946 - Validate Stack Sequences
// time = O(N), space = O(N)
public boolean validateStackSequences(int[] pushed, int[] popped) {
    Deque<Integer> st = new ArrayDeque<>();
    int j = 0;
    for (int v : pushed) {
        st.push(v);
        while (!st.isEmpty() && j < popped.length && st.peek() == popped[j]) { st.pop(); j++; }
    }
    return st.isEmpty();
}
```

**Variation D — LC 1910 Remove All Occurrences of a Substring**: *twist: the "collision" is the last `k` chars matching `part`; pop all `k` at once so newly-adjacent text can match again.*

```python
# python
# LC 1910 - Remove All Occurrences of a Substring
# time = O(N*K), space = O(N)
def removeOccurrences(s, part):
    st, k = [], len(part)
    for ch in s:
        st.append(ch)
        if len(st) >= k and "".join(st[-k:]) == part:
            del st[-k:]          # removing may expose a NEW match on the next push
    return "".join(st)
```

---

### Template 7: Column-by-Column Arithmetic Simulation (Carry) ⭐⭐⭐⭐

**Key Idea**: the inputs are strings/lists of digits far too long for `int`/`long`, so you simulate
the grade-school algorithm. Two invariants make it painless:

1. **Index math**: `num1[i] * num2[j]` lands in result positions `i+j` (carry) and `i+j+1` (digit),
   for a result buffer of size `m + n`.
2. **Carry loop condition**: keep going `while i >= 0 or j >= 0 or carry > 0` — the trailing
   `carry > 0` is what produces the extra leading digit (`999 + 1 = 1000`).

```java
// java
// LC 43 - Multiply Strings
// time = O(M*N), space = O(M+N)
// IDEA: num1[i]*num2[j] contributes to result[i+j+1] (units) and result[i+j] (carry).
//       Accumulate everything first, normalize as you go, strip leading zeros at the end.
public String multiply(String num1, String num2) {
    if (num1.equals("0") || num2.equals("0")) return "0";
    int m = num1.length(), n = num2.length();
    int[] pos = new int[m + n];
    for (int i = m - 1; i >= 0; i--) {
        for (int j = n - 1; j >= 0; j--) {
            int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
            int p1 = i + j, p2 = i + j + 1;
            int sum = mul + pos[p2];
            pos[p2] = sum % 10;
            pos[p1] += sum / 10;      // safe: pos[p1] stays small, normalized on a later pass
        }
    }
    StringBuilder sb = new StringBuilder();
    for (int p : pos) if (!(sb.length() == 0 && p == 0)) sb.append(p);  // skip leading zeros
    return sb.length() == 0 ? "0" : sb.toString();
}
```

```python
# python
# LC 43 - Multiply Strings
# time = O(M*N), space = O(M+N)
# IDEA: digit (i,j) writes units at i+j+1 and carry at i+j
def multiply(num1, num2):
    if num1 == "0" or num2 == "0":
        return "0"
    m, n = len(num1), len(num2)
    pos = [0] * (m + n)
    for i in range(m - 1, -1, -1):
        for j in range(n - 1, -1, -1):
            mul = (ord(num1[i]) - 48) * (ord(num2[j]) - 48)
            p1, p2 = i + j, i + j + 1
            s = mul + pos[p2]
            pos[p2] = s % 10
            pos[p1] += s // 10
    res = "".join(map(str, pos)).lstrip("0")
    return res if res else "0"
```

#### Variations of Template 7

**Variation A — LC 415 Add Strings**: *twist: single pass with two pointers from the right; the `or carry` in the loop condition handles the overflow digit.*

```java
// java
// LC 415 - Add Strings
// time = O(max(M,N)), space = O(max(M,N))
public String addStrings(String num1, String num2) {
    StringBuilder sb = new StringBuilder();
    int i = num1.length() - 1, j = num2.length() - 1, carry = 0;
    while (i >= 0 || j >= 0 || carry > 0) {           // `carry > 0` emits the leading digit
        int d1 = i >= 0 ? num1.charAt(i--) - '0' : 0; // pad short number with 0
        int d2 = j >= 0 ? num2.charAt(j--) - '0' : 0;
        int sum = d1 + d2 + carry;
        sb.append(sum % 10);
        carry = sum / 10;
    }
    return sb.reverse().toString();
}
```

**Variation B — LC 67 Add Binary**: *twist: identical loop, only the base changes — divide/mod by 2 instead of 10.*

```python
# python
# LC 67 - Add Binary  (same skeleton as LC 415, base 2)
# time = O(max(M,N)), space = O(max(M,N))
def addBinary(a, b):
    i, j, carry, out = len(a) - 1, len(b) - 1, 0, []
    while i >= 0 or j >= 0 or carry:
        d1 = ord(a[i]) - 48 if i >= 0 else 0
        d2 = ord(b[j]) - 48 if j >= 0 else 0
        carry, d = divmod(d1 + d2 + carry, 2)   # base 10 -> divmod(..., 10)
        out.append(str(d))
        i -= 1
        j -= 1
    return "".join(reversed(out))
```

---

### Template 8: Algebraic Loop Detection — Skip the Simulation ⭐⭐⭐⭐

**Key Idea**: a simulation that runs "forever" cannot be run forever. Two escapes:

| Escape | How | Example |
|--------|-----|---------|
| **State cycle** | Hash the full state `(pos, dir, ...)`; repeat ⇒ loop | LC 2061 (above) |
| **Algebraic** | Run **one** pass of the script, then reason about net displacement + net rotation | LC 1041 |

For LC 1041 the whole infinite process collapses to one observation: after one pass of the
instructions the robot has some **net displacement** `(x, y)` and some **net rotation** `d`.

- If `d != 0` (the robot is not facing its original direction), then 4 passes rotate the total
  displacement by 0°/90°/180°/270°, which **always sums to the zero vector** ⇒ bounded.
- If `d == 0` and `(x, y) != (0, 0)`, the same offset repeats forever ⇒ escapes to infinity.

```java
// java
// LC 1041 - Robot Bounded In Circle
// time = O(N), space = O(1)
// IDEA: simulate ONE pass. Bounded iff back at origin, OR not facing north (rotation cancels
//       the drift over 4 passes). No need to simulate repeated cycles at all.
public boolean isRobotBounded(String instructions) {
    int x = 0, y = 0, dir = 0;                       // 0=N, 1=E, 2=S, 3=W
    int[] dx = {0, 1, 0, -1}, dy = {1, 0, -1, 0};
    for (char c : instructions.toCharArray()) {
        if (c == 'L')      dir = (dir + 3) % 4;      // -1 mod 4
        else if (c == 'R') dir = (dir + 1) % 4;
        else { x += dx[dir]; y += dy[dir]; }
    }
    return (x == 0 && y == 0) || dir != 0;
}
```

```python
# python
# LC 1041 - Robot Bounded In Circle
# time = O(N), space = O(1)
# IDEA: one pass; bounded iff net displacement is zero OR net rotation != 0
def isRobotBounded(instructions):
    x = y = d = 0                                    # 0=N, 1=E, 2=S, 3=W
    dx, dy = [0, 1, 0, -1], [1, 0, -1, 0]
    for c in instructions:
        if c == 'L':
            d = (d + 3) % 4
        elif c == 'R':
            d = (d + 1) % 4
        else:
            x += dx[d]
            y += dy[d]
    return (x == 0 and y == 0) or d != 0
```

> **Direction-vector convention reminder** (used by every robot template here): keep `dx`/`dy`
> in **clockwise** order so `R` is `(dir + 1) % 4` and `L` is `(dir + 3) % 4`. Writing `(dir - 1) % 4`
> is fine in Python but yields a negative index in Java — use `+3` in both for portability.

---

### Reference: Other High-Frequency Simulation Problems

| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Text Justification | 68 | Greedy line packing: fit words while `len + words <= maxWidth`, then distribute `maxWidth - lettersLen` spaces across `gaps` as `q, r = divmod(spaces, gaps)` (left gaps get one extra); last line + single-word lines are left-justified | Hard |
| Contain Virus | 749 | Multi-phase grid simulation: each round, flood-fill every virus region, quarantine only the region threatening the most fresh cells, then spread all the others | Hard |
| Where Will the Ball Fall | 1706 | Per-ball column walk; a ball falls only if `grid[r][c] == grid[r][c + grid[r][c]]` (no V-shaped wall) | Medium |