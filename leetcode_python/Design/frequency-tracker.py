"""

2671. Frequency Tracker
Medium

Design a data structure that keeps track of the values in it and answers some queries regarding their frequencies.

Implement the FrequencyTracker class.

FrequencyTracker(): Initializes the FrequencyTracker object with an empty array initially.
void add(int number): Adds number to the data structure.
void deleteOne(int number): Deletes one occurrence of number from the data structure. The data structure may not contain number, and in this case nothing is deleted.
bool hasFrequency(int frequency): Returns true if there is a number in the data structure that occurs frequency number of times, otherwise, it returns false.


Example 1:

Input
["FrequencyTracker", "add", "add", "hasFrequency"]
[[], [3], [3], [2]]
Output
[null, null, null, true]

Explanation
FrequencyTracker frequencyTracker = new FrequencyTracker();
frequencyTracker.add(3); // The data structure now contains [3]
frequencyTracker.add(3); // The data structure now contains [3, 3]
frequencyTracker.hasFrequency(2); // Returns true, because 3 occurs twice

Example 2:

Input
["FrequencyTracker", "add", "deleteOne", "hasFrequency"]
[[], [1], [1], [1]]
Output
[null, null, null, false]

Explanation
FrequencyTracker frequencyTracker = new FrequencyTracker();
frequencyTracker.add(1); // The data structure now contains [1]
frequencyTracker.deleteOne(1); // The data structure becomes empty []
frequencyTracker.hasFrequency(1); // Returns false, because the data structure is empty

Example 3:

Input
["FrequencyTracker", "hasFrequency", "add", "hasFrequency"]
[[], [2], [3], [1]]
Output
[null, false, null, true]

Explanation
FrequencyTracker frequencyTracker = new FrequencyTracker();
frequencyTracker.hasFrequency(2); // Returns false, because the data structure is empty
frequencyTracker.add(3); // The data structure now contains [3]
frequencyTracker.hasFrequency(1); // Returns true, because 3 occurs once


Constraints:

1 <= number <= 10^5
1 <= frequency <= 10^5
At most, 2 * 10^5 calls will be made to add, deleteOne, and hasFrequency in total.

"""

# V0
# IDEA : TWO COUNTERS -- "count of each number" + "count of each count"
#
#   hasFrequency asks "does ANY number occur exactly f times?". Scanning the
#   counts per query would be O(distinct) and too slow for 2*10^5 calls, so
#   we keep a SECOND level of counting:
#
#     cnt[x] = how many times number x is currently stored
#     freq[c] = how many DISTINCT numbers currently have cnt == c
#
#   every mutation of cnt[x] is mirrored into freq as a move:
#     freq[old] -= 1 ; cnt[x] = new ; freq[new] += 1
#   so freq stays consistent, and hasFrequency(f) is just freq[f] > 0.
#
#   NOTE : the decrement of freq[old] must happen BEFORE cnt[x] changes and
#          the increment AFTER -- doing both against the new value silently
#          leaves the old bucket over-counted, and hasFrequency then lies.
#
#   NOTE : deleteOne on an absent number is a no-op; guard with cnt[x] > 0.
#          Without the guard cnt would go negative and freq[0] would be
#          polluted.
#
#   NOTE : freq[0] is written to (it tracks numbers back down at zero) but
#          is never queried, since frequency >= 1 per the constraints.
#
#   NOTE : a plain dict with .get(...) default is used instead of a 10^5
#          array so the space follows the number of DISTINCT values seen.
#
# time = O(1) per operation, space = O(n) with n = #distinct numbers added
class FrequencyTracker(object):

    def __init__(self):
        self.cnt = {}
        self.freq = {}

    def add(self, number):
        c = self.cnt.get(number, 0)
        if c:
            self.freq[c] = self.freq.get(c, 0) - 1
        self.cnt[number] = c + 1
        self.freq[c + 1] = self.freq.get(c + 1, 0) + 1

    def deleteOne(self, number):
        c = self.cnt.get(number, 0)
        if c == 0:
            return
        self.freq[c] = self.freq.get(c, 0) - 1
        self.cnt[number] = c - 1
        if c - 1 > 0:
            self.freq[c - 1] = self.freq.get(c - 1, 0) + 1

    def hasFrequency(self, frequency):
        return self.freq.get(frequency, 0) > 0


# Your FrequencyTracker object will be instantiated and called as such:
# obj = FrequencyTracker()
# obj.add(number)
# obj.deleteOne(number)
# param_3 = obj.hasFrequency(frequency)
