"""

895. Maximum Frequency Stack
Hard

Design a stack-like data structure to push elements to the stack and pop the most frequent element from the stack.

Implement the FreqStack class:

FreqStack() constructs an empty frequency stack.
void push(int val) pushes an integer val onto the top of the stack.
int pop() removes and returns the most frequent element in the stack.
If there is a tie for the most frequent element, the element closest to the stack's top is removed and returned.
 

Example 1:

Input
["FreqStack", "push", "push", "push", "push", "push", "push", "pop", "pop", "pop", "pop"]
[[], [5], [7], [5], [7], [4], [5], [], [], [], []]
Output
[null, null, null, null, null, null, null, 5, 7, 5, 4]

Explanation
FreqStack freqStack = new FreqStack();
freqStack.push(5); // The stack is [5]
freqStack.push(7); // The stack is [5,7]
freqStack.push(5); // The stack is [5,7,5]
freqStack.push(7); // The stack is [5,7,5,7]
freqStack.push(4); // The stack is [5,7,5,7,4]
freqStack.push(5); // The stack is [5,7,5,7,4,5]
freqStack.pop();   // return 5, as 5 is the most frequent. The stack becomes [5,7,5,7,4].
freqStack.pop();   // return 7, as 5 and 7 is the most frequent, but 7 is closest to the top. The stack becomes [5,7,5,4].
freqStack.pop();   // return 5, as 5 is the most frequent. The stack becomes [5,7,4].
freqStack.pop();   // return 4, as 4, 5 and 7 is the most frequent, but 4 is closest to the top. The stack becomes [5,7].
 

Constraints:

0 <= val <= 109
At most 2 * 104 calls will be made to push and pop.
It is guaranteed that there will be at least one element in the stack before calling pop.

"""

# V0

# V1
# IDEA : STACK
# https://leetcode.com/problems/maximum-frequency-stack/discuss/163410/C%2B%2BJavaPython-O(1)
# IDEA : 
# Hash map freq will count the frequence of elements.
# Hash map m is a map of stack.
# If element x has n frequence, we will push x n times in m[1], m[2] .. m[n]
# maxfreq records the maximum frequence.
#
# push(x) will push x tom[++freq[x]]
# pop() will pop from the m[maxfreq]
class FreqStack(object):
    def __init__(self):
        self.freq = collections.Counter()
        self.m = collections.defaultdict(list)
        self.maxf = 0

    def push(self, x):
        freq, m = self.freq, self.m
        freq[x] += 1
        self.maxf = max(self.maxf, freq[x])
        m[freq[x]].append(x)

    def pop(self):
        freq, m, maxf = self.freq, self.m, self.maxf
        x = m[maxf].pop()
        if not m[maxf]: self.maxf = maxf - 1
        freq[x] -= 1
        return x

# V1'
# IDEA : STACK
# https://leetcode.com/problems/maximum-frequency-stack/solution/
class FreqStack(object):

    def __init__(self):
        self.freq = collections.Counter()
        self.group = collections.defaultdict(list)
        self.maxfreq = 0

    def push(self, x):
        f = self.freq[x] + 1
        self.freq[x] = f
        if f > self.maxfreq:
            self.maxfreq = f
        self.group[f].append(x)

    def pop(self):
        x = self.group[self.maxfreq].pop()
        self.freq[x] -= 1
        if not self.group[self.maxfreq]:
            self.maxfreq -= 1

        return x

# V1''
# IDEA : HEAP
# https://leetcode.com/problems/maximum-frequency-stack/discuss/1864976/Python-Heap-Solution
# IDEA : 
# The dict (self.d) keeps track of the frequency, adding when pushed and subtracting when popped, and the self.i takes care of the priority so it is sorted in order of the most recent entry in case of equal frequencies. We put the negative sign in front of the frequency/self.i when pushing to the stack as a makeshift way of turning Python's min heap into a max heap ...
class FreqStack(object):

    def __init__(self):
        self.d = defaultdict(int)
        self.stack = []
        self.i = 0
        
    def push(self, val):
        self.d[val]+=1
        self.i+=1
        heapq.heappush(self.stack, (-self.d[val], -self.i, val))
    
    def pop(self):
        cnt, location, val = heapq.heappop(self.stack)
        self.d[val]-=1
        return val

# V1'''
# https://leetcode.com/problems/maximum-frequency-stack/discuss/163565/Python-2-different-solutions
class FreqStack:

    def __init__(self):
        self.stacks = collections.defaultdict(list)
        self.freq = collections.Counter()
        self.maxFreq = 0

    def push(self, x):
        self.freq[x] += 1 
        self.maxFreq = max(self.maxFreq, self.freq[x])
        self.stacks[self.freq[x]].append(x)

    def pop(self):
        num = self.stacks[self.maxFreq].pop()
        self.freq[num] -= 1 
        if not self.stacks[self.maxFreq]: self.maxFreq -= 1
        return num

# V1''''
# IDEA : DICT
# https://leetcode.com/problems/maximum-frequency-stack/discuss/1087025/Python-dictionary-solution
class FreqStack:

    def __init__(self):
        self.max_freq = 0
        self.freq_dict = {}
        self.group_dict = {}
        
    def push(self, x: int) -> None:
        if x not in self.freq_dict:
            self.freq_dict[x] = 1
        else:
            self.freq_dict[x] += 1

        freq = self.freq_dict[x]
        if freq > self.max_freq:
            self.max_freq += 1
            self.group_dict[freq] = [x]
        else:
            self.group_dict[freq].append(x)

    def pop(self) -> int:
        top_list = self.group_dict[self.max_freq]
        res = top_list.pop()

        if not top_list:
            self.group_dict.pop(self.max_freq)
            self.max_freq -= 1
        
        self.freq_dict[res] -= 1

        return res

# V1'''''
# https://leetcode.com/problems/maximum-frequency-stack/discuss/163565/Python-2-different-solutions
class FreqStack:

    def __init__(self):
        self.stack = [] 
        self.cnt = collections.Counter()
        self.index = -1

    def push(self, x):
        self.cnt[x] += 1
        self.index += 1
        heapq.heappush(self.stack, (-self.cnt[x], -self.index, x))

    def pop(self):
        num = heapq.heappop(self.stack)[2]
        self.cnt[num] -= 1
        return num

# V1''''''
# IDEA : HEAP
# https://leetcode.com/problems/maximum-frequency-stack/discuss/344640/python-heap
from collections import Counter
import heapq

class FreqStack:

    def __init__(self):
        self.counter = Counter()
        self.heap = []
        self.idx = 0

    def push(self, x: int) -> None:
        self.counter[x] += 1
        heapq.heappush(self.heap, (-self.counter[x], -self.idx, x))
        self.idx += 1

    def pop(self) -> int:
        num = heapq.heappop(self.heap)[-1]
        self.counter[num] -= 1
        return num

# V2