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
# PQ + FREQ MAP + COOL DOWN QUEUE
# LC 621
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/task-scheduler.py
# time = O(n log n)   # n = len(s); heap ops on <=26 distinct chars
# space = O(n)
from collections import Counter, deque
import heapq


class Solution(object):
    def rearrangeString(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: str
        """

        # If k <= 1, there is effectively no distance constraint.
        # Every string is already valid.
        if k <= 1:
            return s

        # Edge case: empty string.
        if not s:
            return ""

        # Count frequency of every character.
        #
        # Example:
        # s = "aaadbbcc"
        #
        # freq =
        # {
        #   'a': 3,
        #   'b': 2,
        #   'c': 2,
        #   'd': 1
        # }
        freq = Counter(s)

        # Build a max heap.
        #
        # Python's heapq is a min heap,
        # so we store negative frequencies.
        #
        # Example:
        # [(-3,'a'), (-2,'b'), (-2,'c'), (-1,'d')]
        pq = [(-cnt, ch) for ch, cnt in freq.items()]
        heapq.heapify(pq)

        # Cooldown queue.
        #
        # Stores:
        # (remaining_count, character, release_time)
        #
        # Meaning:
        # After we use a character,
        # it cannot be used again until release_time.
        cooldown = deque()

        # Result builder.
        res = []

        # Current position / timestamp.
        #
        # Each appended character advances time by 1.
        time = 0

        # Continue while there are characters available
        # OR characters waiting inside cooldown.
        while pq or cooldown:

            # -------------------------------------------------
            # IMPORTANT:
            #
            # If heap is empty but cooldown still contains chars,
            # we are stuck.
            #
            # Example:
            #
            # s = "aaabc"
            # k = 3
            #
            # cooldown still contains 'a'
            # but there is nothing else to place.
            #
            # Therefore impossible.
            # -------------------------------------------------
            if not pq:
                return ""

            # Get the most frequent available character.
            #
            # Example:
            # (-3,'a')
            #
            # cnt = -3
            # ch  = 'a'
            cnt, ch = heapq.heappop(pq)

            # Append character to answer.
            res.append(ch)

            # We used one occurrence.
            #
            # Since count is negative:
            #
            # -3 -> -2
            # -2 -> -1
            # -1 -> 0
            cnt += 1

            # Put character into cooldown.
            #
            # release_time = current_time + k
            #
            # Example:
            #
            # time = 0
            # k = 3
            #
            # use 'a'
            #
            # cannot use again until time = 3
            cooldown.append((cnt, ch, time + k))

            # Move to next position.
            time += 1

            # -------------------------------------------------
            # Release characters whose cooldown expired.
            #
            # Example:
            #
            # cooldown front:
            # (-2, 'a', 3)
            #
            # when time becomes 3,
            # 'a' can return to heap.
            # -------------------------------------------------
            if cooldown and cooldown[0][2] == time:

                remaining_cnt, released_ch, _ = cooldown.popleft()

                # Only push back if occurrences remain.
                #
                # remaining_cnt < 0
                # means still has characters left.
                #
                # remaining_cnt == 0
                # means fully consumed.
                if remaining_cnt < 0:
                    heapq.heappush(
                        pq,
                        (remaining_cnt, released_ch)
                    )

        # Convert list -> string.
        return "".join(res)




# V0-1
# PQ + FREQ MAP + COOL DOWN QUEUE
# LC 621
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/task-scheduler.py
# time = O(n log n)   # n = len(s); heap ops on <=26 distinct chars
# space = O(n)
import heapq
from collections import Counter, deque

class Solution(object):
    def rearrangeString(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: str
        """
        if k <= 1: 
            return s  # No distance constraint or constraint is trivial
        if not s: 
            return ""
        if len(s) == 1: 
            return s if k == 0 else ""
            
        # Step 1: Build the frequency map
        freq = Counter(s)
        
        # Step 2: Build Max-Heap based on frequency
        pq = []
        for ch, count in freq.items():
            heapq.heappush(pq, (-count, ch))
            
        # Step 3: Cooldown queue and tracking variables
        # Stores tuples of (character, release_time)
        cooldown = deque()
        res = []
        time = 0
        
        # The while condition: either PQ or cooldown is NOT empty
        while pq or cooldown:
            # If empty PQ but cooldown still has locked characters -> impossible
            if not pq:
                return ""
                
            # Pop the most frequent character available
            neg_count, cur = heapq.heappop(pq)
            current_count = -neg_count
            
            # Append it to our result list and decrement its frequency
            res.append(cur)
            current_count -= 1
            freq[cur] = current_count
            
            # Put into cooldown with release time set to: time + k
            cooldown.append((cur, time + k))
            time += 1
            
            # Release cooldown characters whose time expired
            if cooldown and cooldown[0][1] == time:
                ch, release_time = cooldown.popleft()
                # Only push back into the heap if there are still remaining characters to place
                if freq[ch] > 0:
                    heapq.heappush(pq, (-freq[ch], ch))
                    
        return "".join(res)


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83039098
# https://www.cnblogs.com/fuxuemingzhu/p/15436068.html
# time = O(n log n)   # n = len(words); heap ops on <=26 distinct chars
# space = O(n)
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
# time = O(n log n)   # n = len(s); heap ops on <=26 distinct chars
# space = O(n)
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