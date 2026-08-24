"""

2167. Minimum Time to Remove All Cars Containing Illegal Goods
Hard

You are given a 0-indexed binary string s which represents a sequence of train cars. s[i] = '0' denotes that the ith car does not contain illegal goods and s[i] = '1' denotes that the ith car does contain illegal goods.

As the train conductor, you would like to get rid of all the cars containing illegal goods. You can do any of the following three operations any number of times:

Remove a train car from the left end (i.e., remove s[0]) which takes 1 unit of time.
Remove a train car from the right end (i.e., remove s[s.length - 1]) which takes 1 unit of time.
Remove a train car from anywhere in the sequence which takes 2 units of time.

Return the minimum time to remove all the cars containing illegal goods.

Note that an empty sequence of cars is considered to have no cars containing illegal goods.


Example 1:

Input: s = "1100101"
Output: 5
Explanation:
One way to remove all the cars containing illegal goods from the sequence is to
- remove a car from the left end 2 times. Time taken is 2 * 1 = 2.
- remove a car from the right end. Time taken is 1.
- remove the car containing illegal goods found in the middle. Time taken is 2.
This obtains a total time of 2 + 1 + 2 = 5.

An alternative way is to
- remove a car from the left end 2 times. Time taken is 2 * 1 = 2.
- remove a car from the right end 3 times. Time taken is 3 * 1 = 3.
This also obtains a total time of 2 + 3 = 5.

5 is the minimum time taken to remove all the cars containing illegal goods.
There are no other ways to remove them with less time.

Example 2:

Input: s = "0010"
Output: 2
Explanation:
One way to remove all the cars containing illegal goods from the sequence is to
- remove a car from the left end 3 times. Time taken is 3 * 1 = 3.
This obtains a total time of 3.

Another way to remove all the cars containing illegal goods from the sequence is to
- remove the car containing illegal goods found in the middle. Time taken is 2.
This obtains a total time of 2.

Another way to remove all the cars containing illegal goods from the sequence is to
- remove a car from the right end 2 times. Time taken is 2 * 1 = 2.
This obtains a total time of 2.

2 is the minimum time taken to remove all the cars containing illegal goods.
There are no other ways to remove them with less time.


Constraints:

1 <= s.length <= 2 * 10^5
s[i] is either '0' or '1'.

"""

# V0
# IDEA : PREFIX / SUFFIX DP AROUND A SPLIT POINT
#
#   any solution clears some prefix (using left-end removals and/or middle
#   removals) and some suffix (right-end and/or middle). so fix a split i and
#   add the two independent costs.
#
#   left[i] = min time to clear every '1' in s[:i]
#       left[i] = min( left[i-1] + 2 * (s[i-1] == '1'),   # pay 2 for that car
#                      i )                                # or sweep from the left
#
#   right[i] = min time to clear every '1' in s[i:]
#       right[i] = min( right[i+1] + 2 * (s[i] == '1'),
#                       n - i )
#
#   answer = min over i in [0, n] of left[i] + right[i].
#
"""

DP def
    any solution clears some PREFIX (left-end and/or middle removals) and some
    SUFFIX (right-end and/or middle) - so fix a split and add two independent
    costs

    left[i] : MIN time to clear every '1' in s[:i]

    right[i]: MIN time to clear every '1' in s[i:]

DP eq

     left[i]  = min( left[i-1] + 2 * (s[i-1] == '1'),   # middle-remove that car
                     i )                               # or sweep from the LEFT

     right[i] = min( right[i+1] + 2 * (s[i] == '1'),
                     n - i )                           # or sweep from the RIGHT


    -> e.g. a middle removal costs 2, an end removal costs 1 - which is why
              "just sweep i cars off the left end" (cost i) is the competing
              option

     ans = min over i in [0, n] of ( left[i] + right[i] )

"""
# time = O(n), space = O(n)
class Solution(object):
    def minimumTime(self, s):
        n = len(s)

        left = [0] * (n + 1)
        for i in range(1, n + 1):
            left[i] = min(left[i - 1] + (2 if s[i - 1] == '1' else 0), i)

        right = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            right[i] = min(right[i + 1] + (2 if s[i] == '1' else 0), n - i)

        return min(left[i] + right[i] for i in range(n + 1))
