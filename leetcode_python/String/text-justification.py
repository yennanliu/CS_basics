"""

68. Text Justification
Hard

Given an array of strings words and a width maxWidth, format the text such that each line
has exactly maxWidth characters and is fully (left and right) justified.

You should pack your words in a greedy approach; that is, pack as many words as you can in
each line. Pad extra spaces ' ' when necessary so that each line has exactly maxWidth characters.

Extra spaces between words should be distributed as evenly as possible. If the number of spaces
on a line does not divide evenly between words, the empty slots on the left will be assigned
more spaces than the slots on the right.

For the last line of text, it should be left-justified, and no extra space is inserted between words.

Note:

- A word is defined as a character sequence consisting of non-space characters only.
- Each word's length is guaranteed to be greater than 0 and not exceed maxWidth.
- The input array words contains at least one word.


Example 1:

Input: words = ["This", "is", "an", "example", "of", "text", "justification."], maxWidth = 16
Output:
[
   "This    is    an",
   "example  of text",
   "justification.  "
]

Example 2:

Input: words = ["What","must","be","acknowledgment","shall","be"], maxWidth = 16
Output:
[
  "What   must   be",
  "acknowledgment  ",
  "shall be        "
]
Explanation: Note that the last line is "shall be    " instead of "shall     be",
because the last line must be left-justified instead of fully-justified.
Note that the second line is also left-justified because it contains only one word.

Example 3:

Input: words = ["Science","is","what","we","understand","well","enough","to","explain",
"to","a","computer.","Art","is","everything","else","we","do"], maxWidth = 20
Output:
[
  "Science  is  what we",
  "understand      well",
  "enough to explain to",
  "a  computer.  Art is",
  "everything  else  we",
  "do                  "
]


Constraints:

1 <= words.length <= 300
1 <= words[i].length <= 20
words[i] consists of only English letters and symbols.
1 <= maxWidth <= 100
words[i].length <= maxWidth

"""

# V0
# IDEA : GREEDY line packing + space distribution
#        pack while (sum of word lens) + (min 1 space per gap) fits in maxWidth
# time = O(n * maxWidth), n = len(words)
# space = O(maxWidth) extra (excluding output)
class Solution(object):
    def fullJustify(self, words, maxWidth):
        res = []
        line = []      # words collected for the current line
        line_len = 0   # sum of the word lengths (spaces NOT counted)

        for w in words:
            # len(line) == number of gaps needed if we append `w`
            # (i.e. the minimum single-space padding between existing words)
            if line_len + len(line) + len(w) > maxWidth:
                res.append(self._justify(line, line_len, maxWidth))
                line, line_len = [], 0
            line.append(w)
            line_len += len(w)

        # last line -> left justified, single spaces, pad the tail
        last = " ".join(line)
        res.append(last + " " * (maxWidth - len(last)))
        return res

    def _justify(self, line, line_len, maxWidth):
        # single word -> left justified (no gap to spread spaces into)
        if len(line) == 1:
            return line[0] + " " * (maxWidth - line_len)

        gaps = len(line) - 1
        # base spaces per gap, plus 1 extra for the `extra` leftmost gaps
        base, extra = divmod(maxWidth - line_len, gaps)

        out = []
        for i, w in enumerate(line[:-1]):
            out.append(w)
            out.append(" " * (base + (1 if i < extra else 0)))
        out.append(line[-1])
        return "".join(out)
