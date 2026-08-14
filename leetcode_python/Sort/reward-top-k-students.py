"""

2512. Reward Top K Students
Medium

You are given two string arrays positive_feedback and negative_feedback, containing the words denoting positive and negative feedback, respectively. Note that no word is both positive and negative.

Initially every student has 0 points. Each positive word in a feedback report increases the points of a student by 3, whereas each negative word decreases the points by 1.

You are given n feedback reports, represented by a 0-indexed string array report and a 0-indexed integer array student_id, where student_id[i] represents the ID of the student who has received the feedback report report[i]. The ID of each student is unique.

Given an integer k, return the top k students after ranking them in non-increasing order by their points. In case more than one student has the same points, the one with the lower ID ranks higher.


Example 1:

Input: positive_feedback = ["smart","brilliant","studious"], negative_feedback = ["not"], report = ["this student is studious","the student is smart"], student_id = [1,2], k = 2
Output: [1,2]
Explanation:
Both the students have 1 positive feedback and 3 points but since student 1 has a lower ID he ranks higher.

Example 2:

Input: positive_feedback = ["smart","brilliant","studious"], negative_feedback = ["not"], report = ["this student is not studious","the student is smart"], student_id = [1,2], k = 2
Output: [2,1]
Explanation:
- The student with ID 1 has 1 positive feedback and 1 negative feedback, so he has 3-1=2 points.
- The student with ID 2 has 1 positive feedback, so he has 3 points.
Since student 2 has more points, [2,1] is returned.


Constraints:

1 <= positive_feedback.length, negative_feedback.length <= 10^4
1 <= positive_feedback[i].length, negative_feedback[j].length <= 100
Both positive_feedback[i] and negative_feedback[j] consists of lowercase English letters.
No word is present in both positive_feedback and negative_feedback.
n == report.length == student_id.length
1 <= n <= 10^4
report[i] consists of lowercase English letters and spaces ' '.
There is a single space between consecutive words of report[i].
1 <= report[i].length <= 100
1 <= student_id[i] <= 10^9
All the values of student_id[i] are unique.
1 <= k <= n

"""

# V0
# IDEA : HASH SET SCORING + CUSTOM SORT
#
#   put the positive / negative vocabularies in sets so word lookup is O(1),
#   then score each report by splitting it on spaces (+3 / -1 per hit).
#
#   ranking is "points descending, then id ascending" — a mixed direction, so
#   sort on the key (-points, id) which turns both into ascending order.
#
#   NOTE : each student appears in exactly one report (ids are unique), so no
#          accumulation across reports is needed.
#
# time = O(n * L + n * log n), space = O(P + N + n)
class Solution(object):
    def topStudents(self, positive_feedback, negative_feedback, report, student_id, k):
        pos = set(positive_feedback)
        neg = set(negative_feedback)

        scored = []
        for sid, text in zip(student_id, report):
            point = 0
            for w in text.split():
                if w in pos:
                    point += 3
                elif w in neg:
                    point -= 1
            scored.append((-point, sid))

        scored.sort()
        return [sid for _, sid in scored[:k]]
