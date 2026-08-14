"""

1953. Maximum Number of Weeks for Which You Can Work
Medium

There are n projects numbered from 0 to n - 1. You are given an integer array milestones where each milestones[i] denotes the number of milestones the ith project has.

You can work on the projects following these two rules:

Every week, you will finish exactly one milestone of one project. You must work every week.
You cannot work on two milestones from the same project for two consecutive weeks.

Once all the milestones of all the projects are finished, or if the only milestones that you can work on will cause you to violate the above rules, you will stop working. Note that you may not be able to finish every project's milestones due to these constraints.

Return the maximum number of weeks you would be able to work on the projects without violating the rules mentioned above.


Example 1:

Input: milestones = [1,2,3]
Output: 6
Explanation: One possible scenario is:
- During the 1st week, you will work on a milestone of project 0.
- During the 2nd week, you will work on a milestone of project 2.
- During the 3rd week, you will work on a milestone of project 1.
- During the 4th week, you will work on a milestone of project 2.
- During the 5th week, you will work on a milestone of project 1.
- During the 6th week, you will work on a milestone of project 2.
The total number of weeks is 6.

Example 2:

Input: milestones = [5,2,1]
Output: 7
Explanation: One possible scenario is:
- During the 1st week, you will work on a milestone of project 0.
- During the 2nd week, you will work on a milestone of project 1.
- During the 3rd week, you will work on a milestone of project 0.
- During the 4th week, you will work on a milestone of project 1.
- During the 5th week, you will work on a milestone of project 0.
- During the 6th week, you will work on a milestone of project 2.
- During the 7th week, you will work on a milestone of project 0.
The total number of weeks is 7.
Note that you cannot work on the last milestone of project 0 on 8th week because it would violate the rules.
Thus, one milestone in project 0 will remain unfinished.


Constraints:

n == milestones.length
1 <= n <= 10^5
1 <= milestones[i] <= 10^9

"""

# V0
# IDEA : GREEDY / "TASK REARRANGEMENT" BOUND (only the biggest project matters)
#
#   let mx = the largest project and rest = total - mx.
#
#   the biggest project's milestones must be separated by milestones of OTHER
#   projects, so the schedule looks like  M _ M _ M ... with `rest` fillers.
#
#     - if mx <= rest + 1 : the fillers are enough to interleave everything and
#       the whole total is achievable -> answer = total
#     - if mx >  rest + 1 : we run out of fillers; the best possible schedule is
#       M f M f ... M using every filler once -> rest * 2 + 1 weeks
#
# time = O(n), space = O(1)
class Solution(object):
    def numberOfWeeks(self, milestones):
        total = sum(milestones)
        mx = max(milestones)
        rest = total - mx
        if mx > rest + 1:
            return rest * 2 + 1
        return total
