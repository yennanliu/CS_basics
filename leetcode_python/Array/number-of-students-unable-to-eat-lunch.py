"""

1700. Number of Students Unable to Eat Lunch
Easy

The school cafeteria offers circular and square sandwiches at lunch break, referred to by numbers 0
and 1 respectively. All students stand in a queue. Each student either prefers square or circular
sandwiches.

The number of sandwiches in the cafeteria is equal to the number of students. The sandwiches are
placed in a stack. At each step:

- If the student at the front of the queue prefers the sandwich on the top of the stack, they will
  take it and leave the queue.
- Otherwise, they will leave it and go to the queue's end.

This continues until none of the queue students want to take the top sandwich and are thus unable to
eat.

You are given two integer arrays students and sandwiches where sandwiches[i] is the type of the ith
sandwich in the stack (i = 0 is the top of the stack) and students[j] is the preference of the jth
student in the initial queue (j = 0 is the front of the queue). Return the number of students that
are unable to eat.


Example 1:

Input: students = [1,1,0,0], sandwiches = [0,1,0,1]
Output: 0
Explanation:
- Front student leaves the top sandwich and returns to the end of the line making students =
  [1,0,0,1].
- Front student leaves the top sandwich and returns to the end of the line making students =
  [0,0,1,1].
- Front student takes the top sandwich and leaves the line making students = [0,1,1] and sandwiches =
  [1,0,1].
- Front student leaves the top sandwich and returns to the end of the line making students = [1,1,0].
- Front student takes the top sandwich and leaves the line making students = [1,0] and sandwiches =
  [0,1].
- Front student leaves the top sandwich and returns to the end of the line making students = [0,1].
- Front student takes the top sandwich and leaves the line making students = [1] and sandwiches = [1].
- Front student takes the top sandwich and leaves the line making students = [] and sandwiches = [].
Hence all students are able to eat.

Example 2:

Input: students = [1,1,1,0,0,1], sandwiches = [1,0,0,0,1,1]
Output: 3


Constraints:

1 <= students.length, sandwiches.length <= 100
students.length == sandwiches.length
sandwiches[i] is 0 or 1
students[i] is 0 or 1

"""

# V0
# IDEA : COUNTING (the queue ORDER is irrelevant -- only how many of each type)
#
#   a student who does not want the top sandwich just cycles to the back, so the
#   top sandwich is taken as long as ANY remaining student prefers its type.
#   -> only the two counts matter, not the arrangement of the queue.
#
#   walk the stack top to bottom: if nobody left wants this type, the process is
#   stuck forever and the answer is the number of students still waiting.
#
#   NOTE : cnt[v ^ 1] is the count of the OTHER type, which is exactly everyone
#          still in the queue at the moment we stall (cnt[v] is 0 there).
#
# time = O(n), space = O(1)
from collections import Counter
class Solution(object):
    def countStudents(self, students, sandwiches):
        cnt = Counter(students)
        for v in sandwiches:
            if cnt[v] == 0:
                return cnt[v ^ 1]
            cnt[v] -= 1
        return 0
