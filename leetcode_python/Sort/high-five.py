"""

1086. High Five
Easy

Given a list of the scores of different students, items, where items[i] = [IDi, scorei]
represents one score from a student with IDi, calculate each student's top five average.

Return the answer as an array of pairs result, where result[j] = [IDj, topFiveAveragej]
represents the student with IDj and their top five average. Sort result by IDj in
increasing order.

A student's top five average is calculated by taking the sum of their top five
scores and dividing it by 5 using integer division.


Example 1:

Input: items = [[1,91],[1,92],[2,93],[2,97],[1,60],[2,77],[1,65],[1,87],[1,100],[2,100],[2,76]]
Output: [[1,87],[2,88]]
Explanation:
The student with ID = 1 got scores 91, 92, 60, 65, 87, and 100. Their top five average is (100 + 92 + 91 + 87 + 65) / 5 = 87.
The student with ID = 2 got scores 93, 97, 77, 100, and 76. Their top five average is (100 + 97 + 93 + 77 + 76) / 5 = 88.6, but with integer division their average converts to 88.

Example 2:

Input: items = [[1,100],[7,100],[1,100],[7,100],[1,100],[7,100],[1,100],[7,100],[1,100],[7,100]]
Output: [[1,100],[7,100]]


Constraints:

1 <= items.length <= 1000
items[i].length == 2
1 <= IDi <= 1000
0 <= scorei <= 100
For each IDi, there will be at least five scores.

"""

# V0
# IDEA : GROUP BY id (hash map) + keep the 5 biggest scores per student
#
#  bucket every score under its student id, then for each id (visited in
#  increasing order) take the 5 largest scores and integer-divide their sum by 5
# time = O(n log n)
# space = O(n)
from collections import defaultdict
from heapq import nlargest
class Solution(object):
    def highFive(self, items):
        scores = defaultdict(list)
        for _id, score in items:
            scores[_id].append(score)

        res = []
        for _id in sorted(scores):
            top5 = nlargest(5, scores[_id])
            res.append([_id, sum(top5) // 5])
        return res

# V1
# IDEA : MIN HEAP of fixed size 5 per student
#        (keeps only the current top 5, so no full sort per student)
# time = O(n log 5 + k log k), k = number of distinct ids
# space = O(k)
from collections import defaultdict
import heapq
class Solution(object):
    def highFive(self, items):
        heaps = defaultdict(list)
        for _id, score in items:
            h = heaps[_id]
            heapq.heappush(h, score)
            if len(h) > 5:
                heapq.heappop(h)        # drop the smallest -> keep top 5

        return [[_id, sum(heaps[_id]) // 5] for _id in sorted(heaps)]
