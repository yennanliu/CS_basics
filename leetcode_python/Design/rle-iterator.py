"""

900. RLE Iterator
Medium

We can use run-length encoding (i.e., RLE) to encode a sequence of integers.
In a run-length encoded array of even length encoding (0-indexed), for all even i,
encoding[i] tells us the number of times that the non-negative integer value
encoding[i + 1] is repeated in the sequence.

For example, the sequence arr = [8,8,8,5,5] can be encoded to be
encoding = [3,8,2,5]. encoding = [3,8,0,9,2,5] and encoding = [2,8,1,8,2,5]
are also valid RLE of arr.

Given a run-length encoded array, design an iterator that iterates through it.

Implement the RLEIterator class:

- RLEIterator(int[] encoded) Initializes the object with the encoded array encoded.
- int next(int n) Exhausts the next n elements and returns the last element
  exhausted in this way. If there is no element left to exhaust, return -1 instead.

Example 1:

Input
["RLEIterator", "next", "next", "next", "next"]
[[[3, 8, 0, 9, 2, 5]], [2], [1], [1], [2]]
Output
[null, 8, 8, 5, -1]

Explanation
RLEIterator rLEIterator = new RLEIterator([3, 8, 0, 9, 2, 5]);
// This maps to the sequence [8,8,8,5,5].
rLEIterator.next(2); // exhausts 2 terms of the sequence, returning [8,8].
                     // The remaining sequence is now [8, 5, 5].
rLEIterator.next(1); // exhausts 1 term of the sequence, returning [8].
                     // The remaining sequence is now [5, 5].
rLEIterator.next(1); // exhausts 1 term of the sequence, returning [5].
                     // The remaining sequence is now [5].
rLEIterator.next(2); // exhausts 2 terms, but only 1 term is left so it returns -1.
                     // Note that the last term is exhausted,
                     // but the sequence is now empty.

Constraints:

2 <= encoding.length <= 1000
encoding.length is even.
0 <= encoding[i] <= 10^9
1 <= n <= 10^9
At most 1000 calls will be made to next.

"""

# V0
# IDEA : POINTER OVER THE ENCODING + LAZY COUNT DECREMENT
#
#   never expand the sequence : a single run can be 10^9 long.
#   keep a pointer `p` at the current (count, value) pair and eat from that
#   pair's count.  when the pair is used up, move on and keep eating.
#   running out of pairs means the sequence is exhausted -> -1.
#
# time = O(m) for __init__, O(1) amortised per next (each pair is walked
#        past at most once over the whole run of calls)
# space = O(m)
class RLEIterator(object):
    def __init__(self, encoding):
        """
        :type encoding: List[int]
        """
        self.data = list(encoding)   # [count, value, count, value, ...]
        self.p = 0

    def next(self, n):
        """
        :type n: int
        :rtype: int
        """
        while self.p < len(self.data):
            if self.data[self.p] >= n:
                self.data[self.p] -= n
                return self.data[self.p + 1]
            n -= self.data[self.p]
            self.data[self.p] = 0
            self.p += 2
        return -1


# V0-1
# IDEA : PREFIX SUMS OF THE COUNTS + BINARY SEARCH ON THE ABSOLUTE POSITION
#
#   build ends[i] = count[0] + ... + count[i], i.e. the 1-based index of the
#   LAST element of run i.  track how many elements have been consumed in
#   total; the element just eaten is the one at position `consumed`, so the
#   run holding it is the first run with ends[i] >= consumed.
#
#   bisect_left is what makes empty runs (count 0) fall out for free : they
#   share the previous run's end, and the earliest matching index wins.
#   unlike V0 this never mutates the input and supports random access.
#
# time = O(m) for __init__, O(log m) per next
# space = O(m)
import bisect
class RLEIterator(object):
    def __init__(self, encoding):
        """
        :type encoding: List[int]
        """
        self.vals = list(encoding[1::2])
        self.ends = []
        total = 0
        for count in encoding[0::2]:
            total += count
            self.ends.append(total)
        self.total = total
        self.consumed = 0

    def next(self, n):
        """
        :type n: int
        :rtype: int
        """
        self.consumed += n
        if self.consumed > self.total:
            return -1
        return self.vals[bisect.bisect_left(self.ends, self.consumed)]


# V0-2
# IDEA : BRUTE FORCE — RE-WALK THE RUNS FROM THE START ON EVERY CALL
#
#   keep nothing but a running total of consumed elements, then re-scan the
#   encoding from run 0 and subtract counts until the remaining offset falls
#   inside a run.  no prefix table, no mutation, no pointer state.
#
#   with m <= 1000 pairs and <= 1000 calls this is only 10^6 steps, and it is
#   the version to fall back on if the in-place bookkeeping of V0 feels
#   error-prone under interview pressure.
#
# time = O(1) for __init__, O(m) per next
# space = O(1) extra
class RLEIterator(object):
    def __init__(self, encoding):
        """
        :type encoding: List[int]
        """
        self.enc = encoding
        self.consumed = 0

    def next(self, n):
        """
        :type n: int
        :rtype: int
        """
        self.consumed += n
        left = self.consumed
        for i in range(0, len(self.enc), 2):
            if left <= self.enc[i]:
                return self.enc[i + 1]
            left -= self.enc[i]
        return -1


# V1

# V2
