
# Explanation of Overlapping Booking Logic in MyCalendar

- (LC 729)

This document explains the logic used in the following code snippet from the `MyCalendar` class:

```java
if (start < date.get(1) && end > date.get(0)) {
    return false;  // There's an overlap
}
```

## Purpose
The purpose of this logic is to check whether a new booking overlaps with an existing booking in the calendar. If the new booking overlaps with an existing booking, the function will return `false`, indicating that the booking cannot be made.

## Visual Explanation

Imagine a timeline where each event has a start and an end time. The new event is represented by `start` and `end`, while an existing event is represented by `date.get(0)` (start) and `date.get(1)` (end).

### Cases:

### 1. **No Overlap - New Event is Completely Before the Existing Event**

```
New:      |-----|   
Existing:          |-----|
```

- **Condition**: `end <= date.get(0)`
- Explanation: The new event ends before the existing event starts, so no overlap.

### 2. **No Overlap - New Event is Completely After the Existing Event**

```
New:              |-----|
Existing:  |-----|
```

- **Condition**: `start >= date.get(1)`
- Explanation: The new event starts after the existing event ends, so no overlap.

### 3. **Overlap - New Event Partially Overlaps with the Existing Event**

```
New:       |-------|
Existing:     |------|
```

- **Condition**: `start < date.get(1)` and `end > date.get(0)`
- Explanation: The new event starts before the existing event ends and ends after the existing event starts, causing a partial overlap.

### 4. **Complete Overlap - New Event Starts and Ends Inside the Existing Event**

```
New:         |---|
Existing:   |-------|
```

- **Condition**: `start < date.get(1)` and `end > date.get(0)`
- Explanation: Both conditions are true, meaning the new event is entirely within the bounds of the existing event.

### 5. **New Event Engulfs the Existing Event (Starts Before and Ends After)**

```
New:     |-----------|
Existing:   |-----|
```

- **Condition**: `start < date.get(1)` and `end > date.get(0)`
- Explanation: The new event starts before and ends after the existing event, completely overlapping it.

## Breakdown of the Condition:

- **`start < date.get(1)`**: This checks if the new event starts before the existing event ends.
- **`end > date.get(0)`**: This checks if the new event ends after the existing event starts.

If **both conditions are true**, there is an overlap, and the function returns `false` to indicate that the booking cannot be made.

