"""

732. My Calendar III
Hard

A k-booking happens when k events have some non-empty intersection
(i.e., there is some time that is common to all k events.)

You are given some events [startTime, endTime), after each given event, return an
integer k representing the maximum k-booking between all the previous events.

Implement the MyCalendarThree class:

  - MyCalendarThree() Initializes the object.
  - int book(int startTime, int endTime) Returns an integer k representing the largest
    integer such that there exists a k-booking in the calendar.


Example 1:

Input
["MyCalendarThree", "book", "book", "book", "book", "book", "book"]
[[], [10, 20], [50, 60], [10, 40], [5, 15], [5, 10], [25, 55]]
Output
[null, 1, 1, 2, 3, 3, 3]

Explanation
MyCalendarThree myCalendarThree = new MyCalendarThree();
myCalendarThree.book(10, 20); // return 1
myCalendarThree.book(50, 60); // return 1
myCalendarThree.book(10, 40); // return 2
myCalendarThree.book(5, 15); // return 3
myCalendarThree.book(5, 10); // return 3
myCalendarThree.book(25, 55); // return 3


Constraints:

0 <= startTime < endTime <= 10^9
At most 400 calls will be made to book.

"""

# V0
# IDEA : SWEEP LINE (BOUNDARY DELTA COUNTING)
#
#   Store only the event boundaries as +1 / -1 deltas keyed by time.
#   Sweeping the keys in ascending order and accumulating the deltas gives the
#   number of simultaneously active events at each boundary; the max of that
#   running sum is the answer.
#
#   Since intervals are half-open [start, end), the -1 at `end` is applied at the
#   same time point as any +1 starting there, so touching intervals never overlap.
#
# time = O(n log n) per book call, n = number of bookings so far
# space = O(n)
class MyCalendarThree(object):

    def __init__(self):
        self.delta = {}  # time -> net change in number of active events

    def book(self, startTime, endTime):
        self.delta[startTime] = self.delta.get(startTime, 0) + 1
        self.delta[endTime] = self.delta.get(endTime, 0) - 1

        active = 0
        best = 0
        for t in sorted(self.delta):
            active += self.delta[t]
            best = max(best, active)

        return best


# Your MyCalendarThree object will be instantiated and called as such:
# obj = MyCalendarThree()
# param_1 = obj.book(startTime,endTime)
