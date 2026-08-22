"""

1491. Average Salary Excluding the Minimum and Maximum Salary
Easy

You are given an array of unique integers salary where salary[i] is the salary of the ith employee.

Return the average salary of employees excluding the minimum and maximum salary. Answers within 10^-5 of the actual answer will be accepted.


Example 1:

Input: salary = [4000,3000,1000,2000]
Output: 2500.00000
Explanation: Minimum salary and maximum salary are 1000 and 4000 respectively.
Average salary excluding minimum and maximum salary is (2000+3000) / 2 = 2500

Example 2:

Input: salary = [1000,2000,3000]
Output: 2000.00000
Explanation: Minimum salary and maximum salary are 1000 and 3000 respectively.
Average salary excluding minimum and maximum salary is (2000) / 1 = 2000


Constraints:

3 <= salary.length <= 100
1000 <= salary[i] <= 10^6
All the integers of salary are unique.

"""

# V0
# IDEA : SUM MINUS THE EXTREMES (no sorting needed)
#
#   subtract min and max from the total, then divide by n - 2.
#   the values are unique, so exactly one entry is dropped at each end.
#   NOTE : n >= 3 is guaranteed, so the denominator is never 0.
#   NOTE : force a float division - integer division would truncate on py2.
#
# time = O(n), space = O(1)
class Solution(object):
    def average(self, salary):
        total = sum(salary) - min(salary) - max(salary)
        return float(total) / (len(salary) - 2)


# V0-1
# IDEA : SORT, THEN DROP THE TWO ENDS
#
#   after sorting, the excluded salaries are exactly the first and the last
#   element, so a slice does the filtering. slower than V0, but it generalises
#   to "exclude the k lowest and k highest".
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def average(self, salary):
        s = sorted(salary)
        return float(sum(s[1:-1])) / (len(s) - 2)


# V0-2
# IDEA : ONE PASS - ACCUMULATE total / lo / hi TOGETHER
#
#   V0 traverses the input three times (sum, min, max). here a single traversal
#   is enough, so it also works when `salary` is a one-shot iterator instead of
#   a list.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def average(self, salary):
        total = 0
        n = 0
        lo = hi = None
        for v in salary:
            n += 1
            total += v
            if lo is None or v < lo:
                lo = v
            if hi is None or v > hi:
                hi = v
        return float(total - lo - hi) / (n - 2)
