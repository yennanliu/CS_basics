"""

2303. Calculate Amount Paid in Taxes
Easy

You are given a 0-indexed 2D integer array brackets where brackets[i] = [upperi, percenti] means that the ith tax bracket has an upper bound of upperi and is taxed at a rate of percenti. The brackets are sorted by upper bound (i.e. upperi-1 < upperi for 0 < i < brackets.length).

Tax is calculated as follows:

The first upper0 dollars earned are taxed at a rate of percent0.
The next upper1 - upper0 dollars earned are taxed at a rate of percent1.
The next upper2 - upper1 dollars earned are taxed at a rate of percent2.
And so on.

You are given an integer income representing the amount of money you earned. Return the amount of money that you have to pay in taxes. Answers within 10^-5 of the actual answer will be accepted.


Example 1:

Input: brackets = [[3,50],[7,10],[12,25]], income = 10
Output: 2.65000
Explanation:
Based on your income, you have 3 dollars in the 1st tax bracket, 4 dollars in the 2nd tax bracket, and 3 dollars in the 3rd tax bracket.
The tax rate for the three tax brackets is 50%, 10%, and 25%, respectively.
In total, you pay $3 * 50% + $4 * 10% + $3 * 25% = $2.65 in taxes.

Example 2:

Input: brackets = [[1,0],[4,25],[5,50]], income = 2
Output: 0.25000
Explanation:
Based on your income, you have 1 dollar in the 1st tax bracket and 1 dollar in the 2nd tax bracket.
The tax rate for the two tax brackets is 0% and 25%, respectively.
In total, you pay $1 * 0% + $1 * 25% = $0.25 in taxes.

Example 3:

Input: brackets = [[2,50]], income = 0
Output: 0.00000
Explanation:
You have no income to tax, so you have to pay a total of $0 in taxes.


Constraints:

1 <= brackets.length <= 100
1 <= upperi <= 1000
0 <= percenti <= 100
0 <= income <= 1000
upperi is sorted in ascending order.
All the values of upperi are unique.
The upper bound of the last tax bracket is greater than or equal to income.

"""

# V0
# IDEA : SIMULATION (walk the brackets, clip each slice by the income)
#
#   bracket i covers the dollars in the half-open range (prev, upper].
#   the part of it that is actually earned is
#       max(0, min(income, upper) - prev)
#   once min(income, upper) <= prev the term is 0, so the loop naturally
#   stops contributing after the income runs out.
#
#   NOTE : accumulate in "percent dollars" and divide by 100 only at the
#          end -> the running total stays an exact integer.
#
# time = O(n), space = O(1)
class Solution(object):
    def calculateTax(self, brackets, income):
        total = 0
        prev = 0
        for upper, percent in brackets:
            taxable = min(income, upper) - prev
            if taxable > 0:
                total += taxable * percent
            prev = upper
        return total / 100.0


# V0-1
# IDEA : CUMULATIVE TAX TABLE + BINARY SEARCH
#
#   cum[i] = the total tax owed by someone earning exactly upper_i, built once:
#       cum[i] = cum[i-1] + (upper_i - upper_(i-1)) * percent_i
#
#   then any income is answered by locating its bracket k (the first upper that
#   reaches the income) and adding the partial slice inside it :
#       tax = cum[k-1] + (income - upper_(k-1)) * percent_k
#
#   V0 must walk every bracket for each income; here the walk is paid once and
#   each further income costs only a bisect -- the useful shape when the same
#   bracket table is queried many times.
#
# time  = O(n) to build the table + O(log n) per income query
# space = O(n)
import bisect

class Solution(object):
    def calculateTax(self, brackets, income):
        uppers = [u for u, _ in brackets]
        cum = []
        run = 0
        prev_u = 0
        for u, p in brackets:
            run += (u - prev_u) * p
            cum.append(run)
            prev_u = u

        k = min(bisect.bisect_left(uppers, income), len(brackets) - 1)
        base = cum[k - 1] if k > 0 else 0
        low = uppers[k - 1] if k > 0 else 0
        return (base + (income - low) * brackets[k][1]) / 100.0


# V0-2
# IDEA : MARGINAL RATE DIFFERENCES (no slice widths at all)
#
#   each slice can be written as a difference of two "income above a
#   threshold" terms :
#       min(income, u_i) - u_(i-1)  clipped at 0
#           = max(0, income - u_(i-1)) - max(0, income - u_i)
#
#   summing percent_i times that and collecting equal terms telescopes into
#
#       tax = sum_i (p_i - p_(i-1)) * max(0, income - u_(i-1))
#
#   with u_(-1) = 0 and p_(-1) = 0.  every bracket now contributes only its
#   RATE INCREASE, applied to all income above the PREVIOUS threshold -- the
#   identity accountants use for marginal-rate tables.  the dropped tail term
#   p_last * max(0, income - u_last) is 0 because the last upper bound is
#   guaranteed to be >= income.
#
# time = O(n), space = O(1)
class Solution(object):
    def calculateTax(self, brackets, income):
        total = 0
        prev_u = 0
        prev_p = 0
        for u, p in brackets:
            total += (p - prev_p) * max(0, income - prev_u)
            prev_u, prev_p = u, p
        return total / 100.0
