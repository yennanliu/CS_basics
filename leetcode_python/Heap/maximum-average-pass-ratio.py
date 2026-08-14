"""

1792. Maximum Average Pass Ratio
Medium

There is a school that has classes of students and each class will be having a final exam. You are given a 2D integer array classes, where classes[i] = [passi, totali]. You know beforehand that in the i^th class, there are totali total students, but only passi number of students will pass the exam.

You are also given an integer extraStudents. There are another extraStudents brilliant students that are guaranteed to pass the exam of any class they are assigned to. You want to assign each of the extraStudents students to a class in a way that maximizes the average pass ratio across all the classes.

The pass ratio of a class is equal to the number of students of the class that will pass the exam divided by the total number of students of the class. The average pass ratio is the sum of pass ratios of all the classes divided by the number of the classes.

Return the maximum possible average pass ratio after assigning the extraStudents students. Answers within 10^-5 of the actual answer will be accepted.

Example 1:

Input: classes = [[1,2],[3,5],[2,2]], extraStudents = 2
Output: 0.78333
Explanation: You can assign the two extra students to the first class. The average pass ratio will be equal to (3/4 + 3/5 + 2/2) / 3 = 0.78333.

Example 2:

Input: classes = [[2,4],[3,9],[4,5],[2,10]], extraStudents = 4
Output: 0.53485

Constraints:

1 <= classes.length <= 10^5
classes[i].length == 2
1 <= passi <= totali <= 10^5
1 <= extraStudents <= 10^5

"""

# V0
# IDEA : MAX HEAP ON THE MARGINAL GAIN (the gain is strictly decreasing per class)
#
#   putting one extra student into class (a, b) raises its ratio by
#       gain = (a + 1) / (b + 1) - a / b
#   this gain shrinks as the class grows, so greedily handing each student to
#   the class with the currently largest gain is optimal (exchange argument /
#   the gains form a decreasing sequence per class).
#   NOTE : python only has a min-heap, so the gain is pushed negated.
#
# time = O((n + k) log n), space = O(n)
from heapq import heapify, heappush, heappop
class Solution(object):
    def maxAverageRatio(self, classes, extraStudents):
        def gain(a, b):
            return float(a + 1) / (b + 1) - float(a) / b

        h = [(-gain(a, b), a, b) for a, b in classes]
        heapify(h)
        for _ in range(extraStudents):
            _, a, b = heappop(h)
            a, b = a + 1, b + 1
            heappush(h, (-gain(a, b), a, b))
        return sum(float(a) / b for _, a, b in h) / len(classes)
