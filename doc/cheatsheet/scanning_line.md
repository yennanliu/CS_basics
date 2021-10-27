# Scanning Line 

## 0) Concept  
1. Find the "max" overlap in intervals

### 0-1) Types

### 0-2) Pattern
```python
open_close = []
for interval in intervals:
    open_close.append(interval[0], True)
    open_close.append(interval[1], False)

# note : array.sort() will do `in place` (e.g. return nothing)
open_close.sort( key = lambda x : (x[1], x[0]) )

open_close

n = 0
max_num = 0

for i in open_close:
    if i[1]:
        n += 1
    else:
        n -= 1
    max_num = max(max_num, n)
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Meeting Rooms II
```python
# LC 253 Meeting Rooms II
# V0
# IDEA : SCANNING LINE
# Step 1 : split intervals to points, and label start, end point
# Step 2 : reorder the points
# Step 3 : go through every point, if start : result + 1, if end : result -1, and record the maximum result in every iteration
# https://www.1point3acres.com/bbs/thread-295648-1-1.html
class Solution:
    """
    @param intervals: an array of meeting time intervals
    @return: the minimum number of conference rooms required
    """
    def minMeetingRooms(self, intervals):
        open_close = []   
        needed_room = 0  
        res = 0  
        for interval in intervals:
            # "open" the room
            open_close.append((interval[0], "open"))
            # "close" the room
            open_close.append((interval[1], "close"))

        # sort the time 
        open_close_ = open_close.sort(lambda x : x[0])
        # go through every start-end time slot
        for i in open_close_:
            # if there is a "open" => open 2 new room 
            if i[1] == "open":
                needed_room += 2
                res = max(res, needed_room)
            # if there is a "close" => close 1 new room 
            elif i[1] == "close":
                needed_room -= 1
        return res
```