"""

682. Baseball Game
Easy

You are keeping score for a baseball game with strange rules. The game consists of several rounds, where the scores of past rounds may affect future rounds' scores.

At the beginning of the game, you start with an empty record. You are given a list of strings ops, where ops[i] is the ith operation you must apply to the record and is one of the following:

An integer x - Record a new score of x.
"+" - Record a new score that is the sum of the previous two scores. It is guaranteed there will always be two previous scores.
"D" - Record a new score that is double the previous score. It is guaranteed there will always be a previous score.
"C" - Invalidate the previous score, removing it from the record. It is guaranteed there will always be a previous score.
Return the sum of all the scores on the record. The test cases are generated so that the answer fits in a 32-bit integer.

 

Example 1:

Input: ops = ["5","2","C","D","+"]
Output: 30
Explanation:
"5" - Add 5 to the record, record is now [5].
"2" - Add 2 to the record, record is now [5, 2].
"C" - Invalidate and remove the previous score, record is now [5].
"D" - Add 2 * 5 = 10 to the record, record is now [5, 10].
"+" - Add 5 + 10 = 15 to the record, record is now [5, 10, 15].
The total sum is 5 + 10 + 15 = 30.
Example 2:

Input: ops = ["5","-2","4","C","D","9","+","+"]
Output: 27
Explanation:
"5" - Add 5 to the record, record is now [5].
"-2" - Add -2 to the record, record is now [5, -2].
"4" - Add 4 to the record, record is now [5, -2, 4].
"C" - Invalidate and remove the previous score, record is now [5, -2].
"D" - Add 2 * -2 = -4 to the record, record is now [5, -2, -4].
"9" - Add 9 to the record, record is now [5, -2, -4, 9].
"+" - Add -4 + 9 = 5 to the record, record is now [5, -2, -4, 9, 5].
"+" - Add 9 + 5 = 14 to the record, record is now [5, -2, -4, 9, 5, 14].
The total sum is 5 + -2 + -4 + 9 + 5 + 14 = 27.
Example 3:

Input: ops = ["1","C"]
Output: 0
Explanation:
"1" - Add 1 to the record, record is now [1].
"C" - Invalidate and remove the previous score, record is now [].
Since the record is empty, the total sum is 0.
 

Constraints:

1 <= ops.length <= 1000
ops[i] is "C", "D", "+", or a string representing an integer in the range [-3 * 104, 3 * 104].
For operation "+", there will always be at least two previous scores on the record.
For operations "C" and "D", there will always be at least one previous score on the record.

"""

# V0
# IDEA : STACK
class Solution(object):
    def calPoints(self, ops):
        cache = []
        for op in ops:
            if op == "C":
                if cache:
                    cache.pop(-1)
            elif op == "D":
                if cache:
                    tmp = cache[-1] * 2
                    cache.append(tmp)
            elif op == "+":
                if len(cache) >= 2:
                    tmp1 = cache[-1]
                    tmp2 = cache[-2]
                    cache.append(tmp1+tmp2)
            else:
                cache.append(int(op))

        if not cache:
            return 0
        return sum(cache)

# V0' 
# IDEA : STACK
class Solution(object):
    def calPoints(self, ops):
        stack=[]
        for op in ops:
            if op=='C':
                stack.pop()
            elif op=='D':
                stack.append(stack[-1]*2)
            elif op=='+':
                stack.append(stack[-2] + stack[-1]) 
            else:
                stack.append(int(op))
        return sum(stack)

# V1
# IDEA : STACK
# https://leetcode.com/problems/baseball-game/solution/
class Solution(object):
    def calPoints(self, ops):
        stack = []
        for op in ops:
            if op == '+':
                stack.append(stack[-1] + stack[-2])
            elif op == 'C':
                stack.pop()
            elif op == 'D':
                stack.append(2 * stack[-1])
            else:
                stack.append(int(op))

        return sum(stack)

# V1 
# http://bookshadow.com/weblog/2017/09/24/leetcode-baseball-game/
class Solution(object):
    def calPoints(self, ops):
        """
        :type ops: List[str]
        :rtype: int
        """
        stack = []
        for op in ops:
            if op == 'C':
                stack.pop()
            elif op == 'D':
                stack.append(stack[-1] * 2)
            elif op == '+':
                stack.append(stack[-1] + stack[-2])
            else:
                stack.append(int(op))
        return sum(stack)

### Test case
s=Solution()
assert s.calPoints(["5","2","C","D","+"]) == 30
assert s.calPoints(["5","-2","4","C","D","9","+","+"]) == 27
assert s.calPoints([]) == 0
assert s.calPoints(["10","10","10"]) == 30
assert s.calPoints(["10","10","+"]) == 40
assert s.calPoints(["10","10","C"]) == 10
assert s.calPoints(["10","10","D"]) == 40

# V2
class Solution(object):
    def calPoints(self, ops):
        history = []
        for op in ops:
            if op == '+':
                history.append(history[-1] + history[-2])
            elif op == 'D':
                history.append(history[-1] * 2)
            elif op == 'C':
                history.pop()
            else:
                history.append(int(op))
            print (history)
        return sum(history)

# V3
class Solution(object):
    def calPoints(self, ops):
        """
        :type ops: List[str]
        :rtype: int
        """
        history = []
        for op in ops:
            if op == '+':
                history.append(history[-1] + history[-2])
            elif op == 'D':
                history.append(history[-1] * 2)
            elif op == 'C':
                history.pop()
            else:
                history.append(int(op))
        return sum(history)

# V4 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def calPoints(self, ops):
        """
        :type ops: List[str]
        :rtype: int
        """
        history = []
        for op in ops:
            if op == '+':
                history.append(history[-1] + history[-2])
            elif op == 'D':
                history.append(history[-1] * 2)
            elif op == 'C':
                history.pop()
            else:
                history.append(int(op))
        return sum(history)