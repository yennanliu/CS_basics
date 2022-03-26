"""

1209. Remove All Adjacent Duplicates in String II
Medium

You are given a string s and an integer k, a k duplicate removal consists of choosing k adjacent and equal letters from s and removing them, causing the left and the right side of the deleted substring to concatenate together.

We repeatedly make k duplicate removals on s until we no longer can.

Return the final string after all such duplicate removals have been made. It is guaranteed that the answer is unique.

 

Example 1:

Input: s = "abcd", k = 2
Output: "abcd"
Explanation: There's nothing to delete.
Example 2:

Input: s = "deeedbbcccbdaa", k = 3
Output: "aa"
Explanation: 
First delete "eee" and "ccc", get "ddbbbdaa"
Then delete "bbb", get "dddaa"
Finally delete "ddd", get "aa"
Example 3:

Input: s = "pbbcggttciiippooaais", k = 2
Output: "ps"
 

Constraints:

1 <= s.length <= 105
2 <= k <= 104
s only contains lower case English letters.

"""

# V0
# IDEA : STACK
class Solution:
     def removeDuplicates(self, x, k):
          # edge case
          if not x:
            return None
          stack = []
          """
          NOTE !!!
            1) we use [[element, _count]] format for below op
            2) note the case when deal with duplicated elements

               if stack and stack[-1][0] == x[i]:
                    if stack[-1][1] < k-1:
                         stack[-1][1] += 1
                    else:
                         stack.pop(-1)
          """
          for i in range(len(x)):
               if stack and stack[-1][0] == x[i]:
                    if stack[-1][1] < k-1:
                         stack[-1][1] += 1
                    else:
                         stack.pop(-1)
               else:
                    stack.append([x[i], 1])
          #print (">> stack = " + str(stack))
          tmp = [x[0]*x[1] for x in stack]
          #print (">> tmp = " + str(tmp))
          return "".join(tmp)

# V0'
# IDEA : STACK
# NOTE !!! we DON'T need to modify original s, (but maintain an extra stack for duplicated checks)
class Solution:
     def removeDuplicates(self, s, k):
            stack = [['#', 0]]
            for c in s:
            	#print ("c = " + str(c) + " stack = " + str(stack))
                if stack[-1][0] == c:
                    stack[-1][1] += 1
                    if stack[-1][1] == k:
                        stack.pop()
                else:
                    stack.append([c, 1])
            return ''.join(c * k for c, k in stack)

# V0''
# TODO : fix below
# from collections import Counter
# class Solution(object):
#     def removeDuplicates(self, s, k):
#         def check_dup(_s):
#             cnt = Counter(s)
#             if all( a < k for a in cnt.values()):
#                 return True         
#         # edge case
#         if not s:
#             return
#         if k == len(s) or check_dup(s):
#             return s
#         # sliding window
#         # "abbd"
#         #  ij
#         #   ij
#         i = 0
#         j = 1
#         s = list(s)
#         while s:
#             print ("s = " + str(s) + " i = " + str(i) + " j = " + str(j))
#             if check_dup(s):
#                 return s
#             if s[i] != s[j]:
#                 #i += 1
#                 j += 1
#                 i = j-1
#             elif s[i] == s[j] and j-i+1 < k:
#                 j += 1
#             elif s[i] == s[j] and j-i+1 >= k:
#                 _j = j-i+1
#                 while _j:
#                     print ("_j = " + str(_j))
#                     s.pop(_j)
#                     _j -= 1
#                 i = 0
#                 j = 1
#                 # i = 0
#                 # j = 1
#
#         print ("s = " + str(s))
#         return s

# V1
# IDEA : Stack
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/discuss/392933/JavaC%2B%2BPython-Two-Pointers-and-Stack-Solution
# DEMO:
# c = d stack = [['#', 0]]
# c = e stack = [['#', 0], ['d', 1]]
# c = e stack = [['#', 0], ['d', 1], ['e', 1]]
# c = e stack = [['#', 0], ['d', 1], ['e', 2]]
# c = d stack = [['#', 0], ['d', 1]]
# c = b stack = [['#', 0], ['d', 2]]
# c = b stack = [['#', 0], ['d', 2], ['b', 1]]
# c = c stack = [['#', 0], ['d', 2], ['b', 2]]
# c = c stack = [['#', 0], ['d', 2], ['b', 2], ['c', 1]]
# c = c stack = [['#', 0], ['d', 2], ['b', 2], ['c', 2]]
# c = b stack = [['#', 0], ['d', 2], ['b', 2]]
# c = d stack = [['#', 0], ['d', 2]]
# c = a stack = [['#', 0]]
# c = a stack = [['#', 0], ['a', 1]]
# aa
class Solution:
     def removeDuplicates(self, s, k):
            stack = [['#', 0]]
            for c in s:
            	#print ("c = " + str(c) + " stack = " + str(stack))
                if stack[-1][0] == c:
                    stack[-1][1] += 1
                    if stack[-1][1] == k:
                        stack.pop()
                else:
                    stack.append([c, 1])
            return ''.join(c * k for c, k in stack)

# V1'
# IDEA : TWO POINTERS
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/discuss/628576/Python-3-solutions-(Greedy-Stack-Two-pointers)-with-explanations
# IDEA :
# Explanations: stack will only store int. We will have two pointers fast and slow. Both of them run in the same direction, and each time a list of arr (let's say, it is arr) will copy s[fast] to arr[slow]. However when a character show-up time reachs k, slow will move backword k, so it will be replaced by next non-removable character that fast point to.
class Solution:
    def removeDuplicates(self, s: str, k: int) -> str:
        slow, stack, s = 0, [], [c for c in s]
        for fast, c in enumerate(s):
            s[slow] = c
            if slow == 0 or s[slow - 1] != s[slow]:
                stack.append(1)
            else:
                stack[-1] += 1
                if stack[-1] == k:
                    slow -= k
                    stack.pop()
            slow += 1
        return "".join(s[:slow])

# V1''
# IDEA : STACK
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/discuss/396006/Python-(Stack)
class Solution:
    def removeDuplicates(self, s: str, k: int) -> str:
        stack = []
        for e in s:
            if not stack:
                stack.append([e])
            else:
                if e == stack[-1][-1]:
                    stack[-1].append(e)
                else:
                    stack.append([e])
            if stack:
                if len(stack[-1]) == k:
                    stack.pop()
        
        ret = ''
        for e in stack:
            ret += ''.join(e)
        return ret

# V1'''
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/solution/
# JAVA
# public String removeDuplicates(String s, int k) {
#     StringBuilder sb = new StringBuilder(s);
#     int length = -1;
#     while (length != sb.length()) {
#         length = sb.length();
#         for (int i = 0, count = 1; i < sb.length(); ++i) {
#             if (i == 0 || sb.charAt(i) != sb.charAt(i - 1)) {
#                 count = 1;
#             } else if (++count == k) {
#                 sb.delete(i - k + 1, i + 1);
#                 break;
#             }
#         }
#     }
#     return sb.toString();
# }

# V1''''
# IDEA : Memoise Count
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/solution/
# JAVA
# public String removeDuplicates(String s, int k) {
#     StringBuilder sb = new StringBuilder(s);
#     int count[] = new int[sb.length()];
#     for (int i = 0; i < sb.length(); ++i) {
#         if (i == 0 || sb.charAt(i) != sb.charAt(i - 1)) {
#             count[i] = 1;
#         } else {
#             count[i] = count[i - 1] + 1;
#             if (count[i] == k) {
#                 sb.delete(i - k + 1, i + 1);
#                 i = i - k;
#             }
#         }
#     }
#     return sb.toString();
# }

# V1
# IDEA : STACK
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/solution/
# JAVA
# public String removeDuplicates(String s, int k) {
#     StringBuilder sb = new StringBuilder(s);
#     Stack<Integer> counts = new Stack<>();
#     for (int i = 0; i < sb.length(); ++i) {
#         if (i == 0 || sb.charAt(i) != sb.charAt(i - 1)) {
#             counts.push(1);
#         } else {
#             int incremented = counts.pop() + 1;
#             if (incremented == k) {
#                 sb.delete(i - k + 1, i + 1);
#                 i = i - k;
#             } else {
#                 counts.push(incremented);
#             }
#         }
#     }
#     return sb.toString();
# }

# V1''''
# IDEA : Stack with Reconstruction
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/solution/
# JAVA
# class Pair {
#     int cnt;
#     char ch;
#     public Pair(int cnt, char ch) {
#         this.ch = ch;
#         this.cnt = cnt;
#     }
# }
# public String removeDuplicates(String s, int k) {
#     Stack<Pair> counts = new Stack<>();
#     for (int i = 0; i < s.length(); ++i) {
#         if (counts.empty() || s.charAt(i) != counts.peek().ch) {
#             counts.push(new Pair(1, s.charAt(i)));
#         } else {
#             if (++counts.peek().cnt == k) {
#                 counts.pop();
#             }
#         }
#     }
#     StringBuilder b = new StringBuilder();
#     while (!counts.empty()) {
#         Pair p = counts.pop();
#         for (int i = 0; i < p.cnt; i++) {
#             b.append(p.ch);
#         }
#     }
#     return b.reverse().toString();
# }

# V2