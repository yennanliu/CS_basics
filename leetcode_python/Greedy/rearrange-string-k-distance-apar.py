"""

# https://www.cnblogs.com/fuxuemingzhu/p/15436068.html
# https://tenderleo.gitbooks.io/leetcode-solutions-/content/GoogleHard/358.html

358. Rearrange String k Distance Apart

Given a non-empty string str and an integer k, rearrange the string such that the same characters are at least distance k from each other.

All input strings are given in lowercase letters. If it is not possible to rearrange the string, return an empty string "".

Example 1:

str = " ", k = 3

Result: "abcabc"

The same letters are at least distance 3 from each other.
Example 2:

str = "aaabc", k = 3

Answer: ""

It is not possible to rearrange the string.
Example 3:

str = "aaadbbcc", k = 2

Answer: "abacabcd"

Another possible answer is: "abcabcda"

The same letters are at least distance 2 from each other.

"""


# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83039098
# https://www.cnblogs.com/fuxuemingzhu/p/15436068.html
class Solution:
	def rearrangeString(self, words, k):
		_len = len(words)
		words_count = collections.Counter(words)
		que = []
		heapq.heapify(que)
		for w, v in words_count.items():
			heapq.heappush(que, (-v, w))
		res = ""
		while que:
			cnt = min(_len, k)
			used = []
			for i in range(cnt):
				if not que:
					return ""
				v, w = heapq.heappop(que)
				res += w
				if -v > 1:
					used.append((v + 1, w))
				_len -= 1
			for use in used:
				heapq.heappush(que, use)
		return res

# V1'
# https://github.com/alqamahjsr/Algorithms/blob/master/leetcode.com/python/358_Rearrange_String_k_Distance_Apart.py
from collections import Counter
import heapq
class Solution(object):
    def rearrangeString(self, s, k):
        if k == 0:
            return s
        result, priorityQueue = "", []
        charFrequencies = Counter(s)
        for key, value in charFrequencies.items():
            heapq.heappush(priorityQueue, (-value, key))
        while priorityQueue:
            tempCharHolder, currentDistance = [], 0
            while currentDistance < k:
                if priorityQueue:
                    currentDistance += 1
                    currentCharFrequency, currentChar = heapq.heappop(priorityQueue)
                    result += currentChar
                    if currentCharFrequency != -1:
                        tempCharHolder.append((currentCharFrequency + 1, currentChar))
                else:
                    if tempCharHolder:
                        return ""
                    else:
                        return result
            for item in tempCharHolder:
                heapq.heappush(priorityQueue, item)
        return result

# V1'
# https://blog.csdn.net/qq_46105170/article/details/109377535
# JAVA

# V1''
# https://tenderleo.gitbooks.io/leetcode-solutions-/content/GoogleHard/358.html
# JAVA
# public class Solution {
#     public String rearrangeString(String str, int k) {
#
#         if(k == 0) return str;
#
#         int len = str.length();
#         Map<Character, Integer> counts = new HashMap<>();
#         for(int i=0; i< len; i++){
#             char ch = str.charAt(i);
#             int n =1;
#             if(counts.containsKey(ch)){
#                 n = counts.get(ch)+1;
#             }
#             counts.put(ch, n);
#         }
#
#         PriorityQueue<Pair> pq = new PriorityQueue<>(10, new Comparator<Pair>(){
#             @Override
#             public int compare(Pair p1, Pair p2){
#                 if(p1.cnt != p2.cnt) return p2.cnt - p1.cnt;
#                 else return  p2.ch - p1.ch; // to ensure the order of the chars with same count, they should show up in same order.
#             }
#         });
#
#         for(Map.Entry<Character, Integer> entry : counts.entrySet()){
#             pq.offer(new Pair(entry.getKey(), entry.getValue()));// pick the most show-up char first.
#         }
#
#         StringBuilder sb = new StringBuilder();
#         while(!pq.isEmpty()){
#             List<Pair> tmp = new ArrayList<>();// this is avoid you pick up same char in the same k-segment.
#             int d = Math.min(k, len);
#             for(int i=0; i< d; i++){
#                 if(pq.isEmpty()) return "";
#                 Pair p = pq.poll();
#                 sb.append(p.ch);
#                 if(--p.cnt > 0) tmp.add(p);
#                 len--;
#             }
#             for(Pair p : tmp) pq.offer(p);
#
#         }
#
#         return sb.toString();
#
#     }
#
#     class Pair{
#         char ch;
#         int cnt;
#         Pair(char c, int t){
#             ch = c;
#             cnt = t;
#         }
#     };
# }

# V2