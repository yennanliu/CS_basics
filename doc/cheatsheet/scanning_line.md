# Scanning Line  (AKA `Line Sweep`)

## 0) Concept  

> Find the "max" overlap in intervals

- https://web.ntnu.edu.tw/~algo/Point2.html
- https://hackmd.io/@meyr543/SkrRZCwfj

### 0-1) Types

- Meeting room
    - LC 253
    - LC 759
    - LC 2021
- Calendar
    - LC 731
- Others
    - LC 1851
    - LC 218

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
# NOTE : there're also priority queue, sorting approaches

# V0
# IDEA : SCANNING LINE : Sort all time points and label the start and end points. Move a vertical line from left to right.
class Solution:
     def minMeetingRooms(self, intervals):
            lst = []
            """
            NOTE THIS !!!
            """
            for start, end in intervals:
                lst.append((start, 1))
                lst.append((end, -1))
            # all of below sort work
            #lst.sort()
            lst.sort(key = lambda x : [x[0], x[1]])
            res, curr_rooms = 0, 0
            for t, n in lst:
                curr_rooms += n
                res = max(res, curr_rooms)
            return res

# V0''
# IDEA : SCANNING LINE
# Step 1 : split intervals to points, and label start, end point
# Step 2 : reorder the points
# Step 3 : go through every point, if start : result + 1, if end : result -1, and record the maximum result in every iteration
class Solution:
    def minMeetingRooms(self, intervals):
        if intervals is None or len(intervals) == 0:
            return 0

        tmp = []

        # set up start and end points 
        for inter in intervals:
            tmp.append((inter[0], True))
            tmp.append((inter[1], False))

        # sort 
        tmp = sorted(tmp, key=lambda v: (v[0], v[1]))

        n = 0
        max_num = 0
        for arr in tmp:
            # start point +1 
            if arr[1]:
                n += 1
            # end point -1 
            else:
                n -= 1 # release the meeting room
            max_num = max(n, max_num)
        return max_num
```

### 2-2) Brightest Position on Street
```python
# LC 2021. Brightest Position on Street
# V0
# IDEA : Scanning line, LC 253 MEETING ROOM II
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        # light range array
        light_r = []
        for p,r in lights:
            light_r.append((p-r,'start'))
            light_r.append((p+r+1,'end'))
        light_r.sort(key = lambda x:x[0])
        # focus on the boundary of light range 
        
        bright = collections.defaultdict(int)
        power = 0
        for l in light_r:
            if 'start' in l:
                power += 1
            else:
                power -= 1
            bright[l[0]] = power # NOTE : we update "power" in each iteration
                
        list_bright = list(bright.values())
        list_position = list(bright.keys())
        
        max_bright = max(list_bright)
        max_bright_index = list_bright.index(max_bright)
        
        return list_position[max_bright_index]

# V0'
# IDEA : Scanning line, meeting room
from collections import defaultdict
class Solution(object):
    def brightestPosition(self, lights):
        # edge case
        if not lights:
            return
        _lights = []
        for x in lights:
            """
            NOTE this !!!
             -> 1) scanning line trick
             -> 2) we add 1 to idx for close session (_lights.append([x[0]+x[1]+1, -1]))
            """
            _lights.append([x[0]-x[1], 1])
            _lights.append([x[0]+x[1]+1, -1])
        _lights.sort(key = lambda x : x)
        #print ("_lights = " + str(_lights))
        d = defaultdict(int)
        up = 0
        for a, b in _lights:
            if b == 1:
                up += 1
            else:
                up -= 1
            d[a] = up
        print ("d = " + str(d))
        _max = max(d.values())
        res = [i for i in d if d[i] == _max]
        #print ("res = " + str(res))
        return min (res)

# V1
# IDEA : LC 253 MEETING ROOM II
# https://leetcode.com/problems/brightest-position-on-street/discuss/1494005/Python%3A-Basically-meeting-room-II
# IDEA :
# So, the only difference in this problem in comparison to meeting room II is that we have to convert our input into intervals, which is straightforward and basically suggested to use by the first example. So, here is my code and here is meeting rooms II https://leetcode.com/problems/meeting-rooms-ii/
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        intervals, heap, res, best = [], [], 0, 0
        for x, y in lights:
            intervals.append([x-y, x+y])
            
        intervals.sort()

        for left, right in intervals:            
            while heap and heap[0] < left: 
                heappop(heap)
            heappush(heap, right)
            if len(heap) > best:
                best = len(heap)
                res = left
        return res
```

### 2-2) My Calendar II

```java
// java
// LC 731 My Calendar II
  // V1-1
  // https://leetcode.com/problems/my-calendar-ii/editorial/
  // IDEA:  Line Sweep (Scanning line)
  /**
   *  IDEA:
   *
   *
   *  1) Class `MyCalendarTwo` will have two data members,
   *     `maxOverlappedBooking` which is the maximum number of
   *     concurrent bookings possible at a time,
   *     and `bookingCount` which is a map from integer to integer
   *     with the time point as the key and number of bookings as the value.
   *
   *
   *  2) Initialize `maxOverlappedBooking` as 2, as we need to check for triple booking.
   *
   *  3) Define the function book(start, end) as:
   *
   *    - Increase the number of bookings for the time start and decrease
   *      the number of bookings for end by 1 in the map bookingCount.
   *
   *    - Iterate over each key-value pair in the map
   *      in ascending order of keys to find the prefix sum.
   *      Add the value in the map to the count overlappedBooking.
   *
   *    - If overlappedBooking is more than two, it implies that this
   *      is triple booking. Hence, we should return false.
   *      Also, we need to revert the changes in the map as this booking shouldn't be added.
   *
   *    - If we reach here, it implies no triple booking and hence returns true.
   *
   */
  class MyCalendarTwo_1_1 {



        private TreeMap<Integer, Integer> bookingCount;
        private int maxOverlappedBooking;

        public MyCalendarTwo_1_1() {
            bookingCount = new TreeMap<>();
            maxOverlappedBooking = 2;
        }

        public boolean book(int start, int end) {
            // Increase the booking count at 'start' and decrease at 'end'.
            bookingCount.put(start, bookingCount.getOrDefault(start, 0) + 1);
            bookingCount.put(end, bookingCount.getOrDefault(end, 0) - 1);

            int overlappedBooking = 0;

            // Calculate the prefix sum of bookings.
            for (Map.Entry<Integer, Integer> entry : bookingCount.entrySet()) {
                overlappedBooking += entry.getValue();

                // If the number of overlaps exceeds the allowed limit, rollback and
                // return false.
                if (overlappedBooking > maxOverlappedBooking) {
                    // Rollback changes.
                    bookingCount.put(start, bookingCount.get(start) - 1);
                    bookingCount.put(end, bookingCount.get(end) + 1);

                    // Clean up if the count becomes zero to maintain the map clean.
                    if (bookingCount.get(start) == 0) {
                        bookingCount.remove(start);
                    }

                    return false;
                }
            }

            return true;
        }
    }
```