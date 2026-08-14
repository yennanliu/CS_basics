"""

2526. Find Consecutive Integers from a Data Stream
Medium

For a stream of integers, implement a data structure that checks if the last k
integers parsed in the stream are equal to value.

Implement the DataStream class:

DataStream(int value, int k) Initializes the object with an empty integer stream
and the two integers value and k.
boolean consec(int num) Adds num to the stream of integers. Returns true if the
last k integers are equal to value, and false otherwise. If there are less than
k integers, the condition does not hold true, so returns false.


Example 1:

Input
["DataStream", "consec", "consec", "consec", "consec"]
[[4, 3], [4], [4], [4], [3]]
Output
[null, false, false, true, false]

Explanation
DataStream dataStream = new DataStream(4, 3); //value = 4, k = 3
dataStream.consec(4); // Only 1 integer is parsed, so returns False.
dataStream.consec(4); // Only 2 integers are parsed.
                      // Since 2 is less than k, returns False.
dataStream.consec(4); // The 3 integers parsed are all equal to value, so returns True.
dataStream.consec(3); // The last k integers parsed in the stream are [4,4,3].
                      // Since 3 is not equal to value, it returns False.


Constraints:

1 <= value, num <= 10^9
1 <= k <= 10^5
At most 10^5 calls will be made to consec.

"""

# V0
# IDEA : RUNNING STREAK COUNTER (no queue needed)
#
#   the question "are the last k items all == value?" only depends on the
#   length of the CURRENT suffix run of `value`. So keep a single counter:
#     - num == value -> streak += 1
#     - num != value -> streak = 0   (the run is broken, nothing before it
#                                     can help any future window either)
#   then answer streak >= k.
#
#   NOTE : the naive design keeps a deque of the last k items; that works but
#          costs O(k) memory. The counter collapses it to O(1) because a run
#          longer than k still satisfies the predicate -- we never need to
#          "forget" earlier matches, only to reset on a mismatch.
#
#   NOTE : the "fewer than k integers seen" case needs no special handling --
#          the streak simply cannot have reached k yet.
#
# time = O(1) per consec call, space = O(1)
class DataStream(object):

    def __init__(self, value, k):
        self.value = value
        self.k = k
        self.streak = 0

    def consec(self, num):
        if num == self.value:
            self.streak += 1
        else:
            self.streak = 0
        return self.streak >= self.k


# Your DataStream object will be instantiated and called as such:
# obj = DataStream(value, k)
# param_1 = obj.consec(num)
