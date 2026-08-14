"""

2169. Count Operations to Obtain Zero
Easy

You are given two non-negative integers num1 and num2.

In one operation, if num1 >= num2, you must subtract num2 from num1, otherwise subtract num1 from num2.

For example, if num1 = 5 and num2 = 4, subtract num2 from num1, thus obtaining num1 = 1 and num2 = 4. However, if num1 = 4 and num2 = 5, after one operation, num1 = 4 and num2 = 1.

Return the number of operations required to make either num1 = 0 or num2 = 0.


Example 1:

Input: num1 = 2, num2 = 3
Output: 3
Explanation:
- Operation 1: num1 = 2, num2 = 3. Since num1 < num2, we subtract num1 from num2 and get num1 = 2, num2 = 3 - 2 = 1.
- Operation 2: num1 = 2, num2 = 1. Since num1 > num2, we subtract num2 from num1.
- Operation 3: num1 = 1, num2 = 1. Since num1 == num2, we subtract num2 from num1.
Now num1 = 0 and num2 = 1. Since num1 == 0, we do not need to perform any further operations.
So the total number of operations required is 3.

Example 2:

Input: num1 = 10, num2 = 10
Output: 1
Explanation:
- Operation 1: num1 = 10, num2 = 10. Since num1 == num2, we subtract num2 from num1 and get num1 = 10 - 10 = 0.
Now num1 = 0 and num2 = 10. Since num1 == 0, we are done.
So the total number of operations required is 1.


Constraints:

0 <= num1, num2 <= 10^5

"""

# V0
# IDEA : THIS IS THE SUBTRACTIVE EUCLIDEAN ALGORITHM — BATCH THE SUBTRACTIONS
#
#   repeatedly subtracting the smaller from the larger is exactly Euclid's
#   original GCD procedure. instead of one subtraction per step, do the whole
#   run at once with divmod : subtracting num2 from num1 as many times as it
#   fits costs  num1 // num2  operations and leaves  num1 % num2.
#
#   that turns a potentially 10^5-step loop into O(log(min)) iterations.
#
#   NOTE : a zero on either side means no operations are needed at all.
#
# time = O(log(min(num1, num2))), space = O(1)
class Solution(object):
    def countOperations(self, num1, num2):
        res = 0
        while num1 and num2:
            if num1 >= num2:
                q, num1 = divmod(num1, num2)
            else:
                q, num2 = divmod(num2, num1)
            res += q
        return res
