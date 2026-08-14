"""

2468. Split Message Based on Limit
Hard

You are given a string, message, and a positive integer, limit.

You must split message into one or more parts based on limit. Each resulting part should have the suffix "<a/b>", where "b" is to be replaced with the total number of parts and "a" is to be replaced with the index of the part, starting from 1 and going up to b. Additionally, the length of each resulting part (including its suffix) should be equal to limit, except for the last part whose length can be at most limit.

The resulting parts should be formed such that when their suffixes are removed and they are all concatenated in order, they should be equal to message. Also, the result should contain as few parts as possible.

Return the parts message would be split into as an array of strings. If it is impossible to split message as required, return an empty array.


Example 1:

Input: message = "this is really a very awesome message", limit = 9
Output: ["thi<1/14>","s i<2/14>","s r<3/14>","eal<4/14>","ly <5/14>","a v<6/14>","ery<7/14>"," aw<8/14>","eso<9/14>","me<10/14>"," m<11/14>","es<12/14>","sa<13/14>","ge<14/14>"]
Explanation:
The first 9 parts take 3 characters each from the beginning of message.
The next 5 parts take 2 characters each from the remaining message.
In this example, each part, including the last, has length 9.
It can be shown it is not possible to split message into less than 14 parts.

Example 2:

Input: message = "short message", limit = 15
Output: ["short mess<1/2>","age<2/2>"]
Explanation:
Under the given constraints, the string can be split into two parts:
- The first part comprises of the first 10 characters, and has a length 15.
- The next part comprises of the last 3 characters, and has a length 8.


Constraints:

1 <= message.length <= 10^4
message consists of only lowercase English letters and ' '.
1 <= limit <= 10^4

"""

# V0
# IDEA : TRY EACH PART COUNT k IN ORDER AND CHECK THE TOTAL CAPACITY
#
#   with k parts, part i carries the suffix "<i/k>" of length
#       len(str(i)) + len(str(k)) + 3
#   so its payload room is  limit - 3 - len(str(k)) - len(str(i)).
#
#   the total room over all k parts is
#       k * (limit - 3 - len(str(k)))  -  sum of len(str(i)) for i = 1..k
#   and the second term is computed in O(log k) by grouping the indices by
#   digit length — summing it naively per k would be quadratic at k = 10^4.
#
#   k is valid when the WIDEST suffix (part k) still leaves positive room and
#   the total capacity covers the message. scanning k upward and stopping at
#   the first hit gives the fewest parts.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def splitMessage(self, message, limit):
        n = len(message)

        def digits_total(k):
            """sum of len(str(i)) for i = 1..k"""
            total = 0
            width = 1
            start = 1
            while start <= k:
                end = min(k, start * 10 - 1)
                total += (end - start + 1) * width
                width += 1
                start *= 10
            return total

        for k in range(1, n + 1):
            tail = len(str(k))
            if limit - 3 - tail - tail <= 0:
                continue                      # the last part has no room left
            capacity = k * (limit - 3 - tail) - digits_total(k)
            if capacity >= n:
                res = []
                pos = 0
                for i in range(1, k + 1):
                    room = limit - 3 - tail - len(str(i))
                    res.append(message[pos:pos + room] + '<%d/%d>' % (i, k))
                    pos += room
                return res
        return []
