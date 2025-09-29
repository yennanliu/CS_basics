# Advanced Simulation

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

### 1. Walking Robot Simulation II (LC 2069)
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

### 2. Time to Cross a Bridge (LC 2532)
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

### 3. Number of Spaces Cleaning Robot Cleaned (LC 2061)
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