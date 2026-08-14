"""

1125. Smallest Sufficient Team
Hard

In a project, you have a list of required skills req_skills, and a list of people.
The ith person people[i] contains a list of skills that the person has.

Consider a sufficient team: a set of people such that for every required skill in req_skills,
there is at least one person in the team who has that skill.
We can represent these teams by the index of each person.

For example, team = [0, 1, 3] represents the people with skills people[0], people[1], and people[3].

Return any sufficient team of the smallest possible size, represented by the index of each person.
You may return the answer in any order.

It is guaranteed an answer exists.


Example 1:

Input: req_skills = ["java","nodejs","reactjs"], people = [["java"],["nodejs"],["nodejs","reactjs"]]
Output: [0,2]

Example 2:

Input: req_skills = ["algorithms","math","java","reactjs","csharp","aws"], people = [["algorithms","math","java"],["algorithms","math","reactjs"],["java","csharp","aws"],["reactjs","csharp"],["csharp","math"],["aws","java"]]
Output: [1,2]


Constraints:

1 <= req_skills.length <= 16
1 <= req_skills[i].length <= 16
req_skills[i] consists of lowercase English letters.
All the strings of req_skills are unique.
1 <= people.length <= 60
0 <= people[i].length <= 16
1 <= people[i][j].length <= 16
people[i][j] consists of lowercase English letters.
All the strings of people[i] are unique.
Every skill in people[i] is a skill in req_skills.
It is guaranteed a sufficient team exists.

"""

# V0
# IDEA : BITMASK DP over skill sets (m <= 16 -> only 2^16 states)
#
#   encode each person as a bitmask of the skills he/she covers.
#   dp[state] = minimum team size that covers exactly the skill set `state`.
#
#   transition : from a reachable state, hiring person j moves us to
#                state | mask[j] with cost + 1.
#
#   to rebuild the actual team we store, for every state, which person
#   was hired last (`who`) and the state we came from (`prev`), then walk
#   backwards from the full mask.
#   NOTE : a person with a skill set already implied by the state still
#          gets tried, but dp never improves so it is harmless.
#
# time = O(2^m * n), space = O(2^m)   m = len(req_skills), n = len(people)
class Solution(object):
    def smallestSufficientTeam(self, req_skills, people):
        m, n = len(req_skills), len(people)
        skill_id = {}
        for i, s in enumerate(req_skills):
            skill_id[s] = i

        masks = [0] * n
        for i, skills in enumerate(people):
            for s in skills:
                masks[i] |= 1 << skill_id[s]

        full = (1 << m) - 1
        INF = float('inf')
        dp = [INF] * (1 << m)
        who = [0] * (1 << m)
        prev = [0] * (1 << m)
        dp[0] = 0

        for state in range(1 << m):
            if dp[state] == INF:
                continue
            for j in range(n):
                nxt = state | masks[j]
                if dp[state] + 1 < dp[nxt]:
                    dp[nxt] = dp[state] + 1
                    who[nxt] = j
                    prev[nxt] = state

        res = []
        state = full
        while state:
            res.append(who[state])
            state = prev[state]
        return res
