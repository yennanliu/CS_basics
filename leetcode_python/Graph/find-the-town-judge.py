"""

997. Find the Town Judge
Solved
Easy
Topics
premium lock icon
Companies
In a town, there are n people labeled from 1 to n. There is a rumor that one of these people is secretly the town judge.

If the town judge exists, then:

The town judge trusts nobody.
Everybody (except for the town judge) trusts the town judge.
There is exactly one person that satisfies properties 1 and 2.
You are given an array trust where trust[i] = [ai, bi] representing that the person labeled ai trusts the person labeled bi. If a trust relationship does not exist in trust array, then such a trust relationship does not exist.

Return the label of the town judge if the town judge exists and can be identified, or return -1 otherwise.

 

Example 1:

Input: n = 2, trust = [[1,2]]
Output: 2
Example 2:

Input: n = 3, trust = [[1,3],[2,3]]
Output: 3
Example 3:

Input: n = 3, trust = [[1,3],[2,3],[3,1]]
Output: -1
 

Constraints:

1 <= n <= 1000
0 <= trust.length <= 104
trust[i].length == 2
All the pairs of trust are unique.
ai != bi
1 <= ai, bi <= n

"""

# V0
class Solution(object):
    def findJudge(self, n, trust):
        """
        :type n: int
        :type trust: List[List[int]]
        :rtype: int
        """
        pass


# V1
# IDEA: in-degree + out-degree (gpt)
# LC 997 = 找一個 node：indegree = n-1，outdegree = 0。
class Solution(object):
    def findJudge(self, n, trust):
        """
        :type n: int
        :type trust: List[List[int]]
        :rtype: int
        """

        # out_degree[i] = how many people i trusts
        # in_degree[i]  = how many people trust i
        in_degree = [0] * (n + 1)
        out_degree = [0] * (n + 1)

        for a, b in trust:
            out_degree[a] += 1
            in_degree[b] += 1

        # Judge:
        # - trusts nobody -> out_degree == 0
        # - trusted by everyone else -> in_degree == n - 1
        for person in range(1, n + 1):
            if out_degree[person] == 0 and in_degree[person] == n - 1:
                return person

        return -1


# V2
# IDEA: set + prob understanding (gpt)
class Solution(object):
    def findJudge(self, n, trust):
        """
        :type n: int
        :type trust: List[List[int]]
        :rtype: int
        """

        # Nobody trusts anyone
        # Only person 1 can be the judge
        if n == 1:
            return 1

        # people who trust someone
        non_judge = set()

        # people who are trusted
        judge_candidate = set()

        for a, b in trust:
            # a trusts b
            # Therefore a cannot be the judge
            non_judge.add(a)

            # b could be the judge
            judge_candidate.add(b)

        # Judge:
        # 1. is trusted by someone
        # 2. does not trust anyone
        candidates = judge_candidate - non_judge

        if len(candidates) != 1:
            return -1

        judge = candidates.pop()

        # Judge must be trusted by EVERY other person
        if len([x for x in trust if x[1] == judge]) != n - 1:
            return -1

        return judge
