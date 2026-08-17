"""

1124. Longest Well-Performing Interval
Solved
Medium
Topics
premium lock icon
Companies
Hint
We are given hours, a list of the number of hours worked per day for a given employee.

A day is considered to be a tiring day if and only if the number of hours worked is (strictly) greater than 8.

A well-performing interval is an interval of days for which the number of tiring days is strictly larger than the number of non-tiring days.

Return the length of the longest well-performing interval.

 

Example 1:

Input: hours = [9,9,6,0,6,6,9]
Output: 3
Explanation: The longest well-performing interval is [9,9,6].
Example 2:

Input: hours = [6,6,6]
Output: 0
 

Constraints:

1 <= hours.length <= 104
0 <= hours[i] <= 16
 

"""


# V0
class Solution(object):
    def longestWPI(self, hours):
        """
        :type hours: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: PREFIX + HASHMAP (gemini)
class Solution(object):
    def longestWPI(self, hours):
        """
        :type hours: List[int]
        :rtype: int
        """
        max_len = 0
        prefix_sum = 0
        first_seen = {}

        for i, h in enumerate(hours):
            # +1 for tiring day (> 8 hrs), -1 for non-tiring day
            prefix_sum += 1 if h > 8 else -1

            # Case 1: Entire prefix from index 0 to i has more tiring days
            if prefix_sum > 0:
                max_len = i + 1
            else:
                # Case 2: Look for earliest index where prefix_sum was (prefix_sum - 1)
                # This guarantees a subarray sum of +1 between that index and i
                """

                NOTE !!!

                Q: why ONLY check `prefix_sum - 1` ?


                V1:

                  We need an earlier prefix that is smaller than current prefix.
                  -> 
	                # Since prefix changes by exactly +/- 1, prefix - 1
	                # is enough to find the longest valid interval.


	            V2:

	            Because the prefix sum changes by at most +-1 at each step, 
	            smaller target values (like prefix_sum - 2, prefix_sum - 3)
	            are guaranteed to appear later in time than prefix_sum - 1.


                """
                if (prefix_sum - 1) in first_seen:
                    max_len = max(max_len, i - first_seen[prefix_sum - 1])

            # Store ONLY the first occurrence of each prefix_sum to maximize window length
            if prefix_sum not in first_seen:
                first_seen[prefix_sum] = i

        return max_len

# V0-2
# IDEA: PREFIX + HASHMAP (gpt)
class Solution(object):
    def longestWPI(self, hours):
        """
        :type hours: List[int]
        :rtype: int
        """
        prefix = 0

        # {prefix_sum: first index where it appeared}
        first = {}

        max_len = 0

        for i, h in enumerate(hours):

            # Tiring day = +1
            # Non-tiring day = -1
            if h > 8:
                prefix += 1
            else:
                prefix -= 1

            # If prefix > 0, the entire [0..i] is well-performing
            if prefix > 0:
                max_len = i + 1

            # We need an earlier prefix that is smaller than current prefix.
            #
            # Since prefix changes by exactly +/- 1, prefix - 1
            # is enough to find the longest valid interval.
            elif prefix - 1 in first:
                max_len = max(
                    max_len,
                    i - first[prefix - 1]
                )

            # Only store the FIRST occurrence.
            if prefix not in first:
                first[prefix] = i

        return max_len


# V0-3
# IDEA: Monotonic Stack (gemini)
class Solution(object):
    def longestWPI(self, hours):
        """
        :type hours: List[int]
        :rtype: int
        """
        n = len(hours)

        # 1. Build Prefix Sum Array P (size n + 1)
        P = [0] * (n + 1)
        for i in range(n):
            P[i + 1] = P[i] + (1 if hours[i] > 8 else -1)

        # 2. Build strictly decreasing stack of candidate left indices (j)
        stack = []
        for j in range(n + 1):
            if not stack or P[j] < P[stack[-1]]:
                stack.append(j)

        # 3. Traverse right-to-left with index i to maximize (i - j)
        max_len = 0
        for i in range(n, -1, -1):
            while stack and P[stack[-1]] < P[i]:
                j = stack.pop()
                max_len = max(max_len, i - j)

        return max_len


# V1


# V2
