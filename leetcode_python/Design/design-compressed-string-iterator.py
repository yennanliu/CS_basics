"""

604. Design Compressed String Iterator
Easy

Design and implement a data structure for a compressed string iterator.
The given compressed string will be in the form of each letter followed by a positive
integer representing the number of this letter existing in the original uncompressed string.

Implement the StringIterator class:

- next() Returns the next character if the original string still has uncompressed
  characters, otherwise returns a white space.
- hasNext() Returns true if there is any letter needs to be uncompressed in the
  original string, otherwise returns false.

Example 1:

Input
["StringIterator", "next", "next", "next", "next", "next", "next", "hasNext", "next", "hasNext"]
[["L1e2t1C1o1d1e1"], [], [], [], [], [], [], [], [], []]
Output
[null, "L", "e", "e", "t", "C", "o", true, "d", true]

Explanation
StringIterator stringIterator = new StringIterator("L1e2t1C1o1d1e1");
stringIterator.next();    // return "L"
stringIterator.next();    // return "e"
stringIterator.next();    // return "e"
stringIterator.next();    // return "t"
stringIterator.next();    // return "C"
stringIterator.next();    // return "o"
stringIterator.hasNext(); // return True
stringIterator.next();    // return "d"
stringIterator.hasNext(); // return True

Constraints:

1 <= compressedString.length <= 1000
compressedString consists of lower-case an upper-case English letters and digits.
The number of a single character repetitions in compressedString is in the range [1, 10^9]
At most 100 calls will be made to next and hasNext.

"""

# V0
# IDEA : PARSE ONCE INTO (char, count) PAIRS + POINTER
#
#   Never expand the string (a count can be up to 10^9).
#   Keep a pointer `p` to the current (char, count) pair and decrement the
#   count lazily on each next() call.
#
# time = O(n) for __init__ (n = len(compressedString)), O(1) for next / hasNext
# space = O(n)
class StringIterator(object):
    def __init__(self, compressedString):
        """
        :type compressedString: str
        """
        self.data = []  # list of [char, remaining_count]
        self.p = 0

        n = len(compressedString)
        i = 0
        while i < n:
            c = compressedString[i]
            i += 1
            # collect the whole digit run following the letter
            j = i
            while j < n and compressedString[j].isdigit():
                j += 1
            self.data.append([c, int(compressedString[i:j])])
            i = j

    def next(self):
        """
        :rtype: str
        """
        if not self.hasNext():
            return ' '
        c = self.data[self.p][0]
        self.data[self.p][1] -= 1
        # current group exhausted -> move to next group
        if self.data[self.p][1] == 0:
            self.p += 1
        return c

    def hasNext(self):
        """
        :rtype: bool
        """
        return self.p < len(self.data)


# Your StringIterator object will be instantiated and called as such:
# obj = StringIterator(compressedString)
# param_1 = obj.next()
# param_2 = obj.hasNext()
