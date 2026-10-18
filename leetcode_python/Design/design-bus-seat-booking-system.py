"""

4080. Design Bus Seat Booking System
Medium

You are asked to design a seat booking system for a bus.

The bus has n rows. Each row has four seats labeled "A", "B", "C", and "D".

Seats "A" and "D" are window seats; seats "B" and "C" are middle seats. In
each row, seat "A" is adjacent to seat "B", and seat "D" is adjacent to
seat "C".

Booking rules:

- Booking a middle seat ("B" or "C") always takes 1 second.
- Booking a window seat ("A" or "D") takes 1 second if its adjacent middle
  seat is not booked, or 3 seconds (an extra delay of 2 seconds) if its
  adjacent middle seat is already booked.

A seat is identified by a string such as "A1" or "D20" (one letter from A to
D followed by its row number).

Implement the BusBooking class:

- BusBooking(int n): Initializes the system with n empty rows.
- void toggle(string seat): Books seat if it is free, or frees it if it is
  already booked.
- int getTotalTime(): Returns the total time (in seconds) taken to book all
  currently booked seats, where the seats are booked in the order their
  toggle calls were made. If no seats are currently booked, return 0.
- int getMinTime(): Returns the smallest possible total time (in seconds) to
  book all currently booked seats, over every possible order of booking them.
  If no seats are currently booked, return 0.

Note: The time cost of a seat is fixed at the moment it is booked, based on
the seats booked at that time. Booking or freeing other seats afterward does
not change the cost of a seat that is already booked.


Example 1:

Input:
["BusBooking", "toggle", "toggle", "getTotalTime", "getMinTime", "toggle", "toggle", "getTotalTime", "getMinTime"]
[[20], ["B3"], ["A3"], [], [], ["C3"], ["D3"], [], []]

Output:
[null, null, null, 4, 2, null, null, 8, 4]

Explanation:

BusBooking busBooking = new BusBooking(20); // start with 20 empty rows
busBooking.toggle("B3");   // middle seat, costs 1. Total time is now 1.
busBooking.toggle("A3");   // window seat, adjacent "B3" is booked, costs 3. Total 4.
busBooking.getTotalTime(); // return 4
busBooking.getMinTime();   // return 2. Booking "A3" before "B3" avoids the delay.
busBooking.toggle("C3");   // middle seat, costs 1. Total 5.
busBooking.toggle("D3");   // window seat, adjacent "C3" is booked, costs 3. Total 8.
busBooking.getTotalTime(); // return 8
busBooking.getMinTime();   // return 4. Best order adds no delay : 4 * 1 = 4.

Example 2:

Input:
["BusBooking", "toggle", "toggle", "getTotalTime", "toggle", "toggle", "toggle", "getTotalTime", "getMinTime"]
[[10], ["C7"], ["D7"], [], ["D7"], ["C7"], ["D7"], [], []]

Output:
[null, null, null, 4, null, null, null, 1, 1]

Explanation:

BusBooking busBooking = new BusBooking(10); // start with 10 empty rows
busBooking.toggle("C7");   // middle seat, costs 1. Total 1.
busBooking.toggle("D7");   // window seat, adjacent "C7" is booked, costs 3. Total 4.
busBooking.getTotalTime(); // return 4
busBooking.toggle("D7");   // already booked, free it : removes its cost of 3. Total 1.
busBooking.toggle("C7");   // already booked, free it : removes its cost of 1. Total 0.
busBooking.toggle("D7");   // book "D7" again, "C7" is free now, costs 1. Total 1.
busBooking.getTotalTime(); // return 1
busBooking.getMinTime();   // return 1


Constraints:

1 <= n <= 10^5
seat[0] is one of 'A', 'B', 'C', 'D'.
The remaining characters of seat form an integer row with 1 <= row <= n.
The total number of calls to toggle, getTotalTime, and getMinTime does not exceed 10^5.

"""

# V0
class BusBooking(object):

    def __init__(self, n):
        """
        :type n: int
        """
        

    def toggle(self, seat):
        """
        :type seat: str
        :rtype: None
        """
        

    def getTotalTime(self):
        """
        :rtype: int
        """
        

    def getMinTime(self):
        """
        :rtype: int
        """
        

# V0-1
# IDEA: 2D ARRAY (GPT)
class BusBooking(object):

    def __init__(self, n):
        """
        :type n: int
        """
        self.n = n

        # 4 seats per row: A=0, B=1, C=2, D=3
        # 0 means unbooked; 1 or 3 means booked cost
        self.matrix = [[0] * 4 for _ in range(n)]

        self.total_time = 0
        self.booked_count = 0

    def toggle(self, seat):
        """
        :type seat: str
        :rtype: None
        """
        # Store the input midway
        marvexolin = seat

        col = ord(marvexolin[0]) - ord('A')
        row = int(marvexolin[1:]) - 1

        # Case 1: already booked -> unbook it
        if self.matrix[row][col] > 0:
            cost = self.matrix[row][col]

            self.total_time -= cost
            self.booked_count -= 1
            self.matrix[row][col] = 0

        # Case 2: unbooked -> book it
        else:
            cost = 1

            # A is adjacent to B; D is adjacent to C
            if col == 0 and self.matrix[row][1] > 0:
                cost = 3
            elif col == 3 and self.matrix[row][2] > 0:
                cost = 3

            self.matrix[row][col] = cost
            self.total_time += cost
            self.booked_count += 1

    def getTotalTime(self):
        """
        :rtype: int
        """
        return self.total_time

    def getMinTime(self):
        """
        :rtype: int
        """
        return self.booked_count


# V0-3
# IDEA : HASHMAP (SEAT -> COST PAID WHEN IT WAS BOOKED) + RUNNING TOTAL (CLAUDE)
#
#   getTotalTime : a seat's cost is frozen at booking time, so remember it
#   per seat. booking adds that cost to a running total, freeing subtracts
#   exactly the cost that seat paid -> O(1) per call, no booking history list
#   needs to be replayed.
#
#   getMinTime : the only delay is "window seat booked AFTER its adjacent
#   middle seat". in the best order, book every window seat first (A, D),
#   then every middle seat (B, C, which always cost 1) -> no seat ever pays
#   the delay, so the minimum is simply 1 second per booked seat.
#
#   e.g. B3, A3 booked in that order -> total = 1 + 3 = 4, min = 2 seats = 2
#
# time = O(1) per call (seat strings are O(log n) long), space = O(k) for k booked seats
class BusBooking(object):

    # window seat -> the middle seat whose booking delays it
    ADJACENT_MIDDLE = {"A": "B", "D": "C"}
    WINDOW_DELAY_COST = 3
    BASE_COST = 1

    def __init__(self, n):
        """
        :type n: int
        """
        self.rows = n
        # seat (e.g. "D20") -> the cost it paid when it was booked
        self.seat_cost = {}
        # sum of seat_cost.values(), kept up to date on every toggle
        self.total_time = 0

    def toggle(self, seat):
        """
        :type seat: str
        :rtype: None
        """
        # case 1) already booked -> free it, refund exactly what it paid
        if seat in self.seat_cost:
            self.total_time -= self.seat_cost[seat]
            del self.seat_cost[seat]
            return

        # case 2) free -> book it, and freeze its cost at this moment
        col = seat[0]
        row = seat[1:]

        cost = self.BASE_COST
        if col in self.ADJACENT_MIDDLE:
            middle_seat = self.ADJACENT_MIDDLE[col] + row
            # NOTE !!! only a window seat booked AFTER its middle seat pays the delay
            if middle_seat in self.seat_cost:
                cost = self.WINDOW_DELAY_COST

        self.seat_cost[seat] = cost
        self.total_time += cost

    def getTotalTime(self):
        """
        :rtype: int
        """
        return self.total_time

    def getMinTime(self):
        """
        :rtype: int
        """
        # windows first, then middles -> every booked seat costs 1
        return len(self.seat_cost) * self.BASE_COST


# Your BusBooking object will be instantiated and called as such:
# obj = BusBooking(n)
# obj.toggle(seat)
# param_2 = obj.getTotalTime()
# param_3 = obj.getMinTime()
