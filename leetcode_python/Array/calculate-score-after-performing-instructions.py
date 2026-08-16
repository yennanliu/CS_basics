"""

3522. Calculate Score After Performing Instructions
Medium

You are given two arrays, instructions and values, both of size n.

You start with an initial score of 0, and you follow a sequence of instructions.

At each step i, the instruction at index i determines your action:

If instructions[i] == "add": Add values[i] to your score. Move to the next
instruction, i.e. i + 1.
If instructions[i] == "jump": Jump to the instruction at index i + values[i].
Do not add anything to your score at this step.

The process ends when you either:

Go out of bounds (i.e. i < 0 or i >= n), or
Attempt to revisit an instruction that has been previously executed. Revisiting
is not allowed.

Return your score at the end of the process.


Example 1:

Input: instructions = ["jump","add","add","jump","add","jump"], values = [2,1,3,1,-2,-3]
Output: 1
Explanation:
Simulate the process starting at instruction 0:
At index 0: instruction is "jump", so jump to index 0 + 2 = 2.
At index 2: instruction is "add", so add values[2] = 3, score becomes 3, move to
index 3.
At index 3: instruction is "jump", so jump to index 3 + 1 = 4.
At index 4: instruction is "add", so add values[4] = -2, score becomes 1, move to
index 5.
At index 5: instruction is "jump", so jump to index 5 + (-3) = 2. Index 2 has
already been visited, so the process ends.
The final score is 1.

Example 2:

Input: instructions = ["jump","add","add"], values = [3,1,1]
Output: 0
Explanation:
Simulate the process starting at instruction 0:
At index 0: instruction is "jump", so jump to index 0 + 3 = 3.
Index 3 is out of bounds, so the process ends.
The final score is 0.

Example 3:

Input: instructions = ["jump"], values = [0]
Output: 0
Explanation:
Simulate the process starting at instruction 0:
At index 0: instruction is "jump", so jump to index 0 + 0 = 0.
Index 0 has already been visited, so the process ends.
The final score is 0.


Constraints:

n == instructions.length == values.length
1 <= n <= 10^5
-10^5 <= values[i] <= 10^5
instructions[i] is either "add" or "jump".

"""

# V0
# IDEA : SIMULATION WITH A VISITED SET
#
#   the walk is deterministic, so once an index repeats the process would loop
#   forever — that is exactly why the problem stops there.  marking visited
#   indices bounds the walk at n steps.
#
#   note the visit is marked when the instruction is *executed*, so the very
#   first index counts as visited before any jump can land on it again.
#
# time = O(n), space = O(n)
class Solution(object):
    def calculateScore(self, instructions, values):
        n = len(instructions)
        seen = [False] * n
        i = 0
        score = 0
        while 0 <= i < n and not seen[i]:
            seen[i] = True
            if instructions[i] == "add":
                score += values[i]
                i += 1
            else:
                i += values[i]
        return score
