"""

3167. Better Compression of String
Medium
🔒 (premium)

You are given a string compressed representing a compressed version of a string. The format is a character followed by its frequency. For example, "a3b1a1c2" is a compressed version of the string "aaabacc".

We seek a better compression with the following conditions:

Each character should appear only once in the compressed version.
The characters should be in alphabetical order.

Return the better compression of compressed.

Note: In the better version of compression, the order of letters may change, which is acceptable.


Example 1:

Input: compressed = "a3c9b2c1"
Output: "a3b2c10"
Explanation:
Characters "a" and "b" appear only once in the input, but "c" appears twice, once with a size of 9 and once with a size of 1.
Hence, in the resulting string, it should have a size of 10.

Example 2:

Input: compressed = "c2b3a1"
Output: "a1b3c2"
Explanation:
The input does not have any repeated characters, so the output is the same as the input but in alphabetical order.

Example 3:

Input: compressed = "a2b4c1"
Output: "a2b4c1"
Explanation:
No changes are needed to the input.


Constraints:

1 <= compressed.length <= 6 * 10^4
compressed consists only of lowercase English letters and digits.
compressed is a valid compression, i.e., each character is followed by its frequency.
Frequencies are in the range [1, 10^4] and have no leading zeroes.

"""

# V0
# IDEA : PARSE LETTER-THEN-NUMBER, ACCUMULATE, THEN EMIT IN ALPHABETICAL ORDER
#
#   the format alternates one letter and a multi-digit count, so scanning
#   with two pointers — take the letter, then run forward while the character
#   is a digit — recovers each (letter, count) pair without regex.
#
#   counts of the same letter simply add up, and the output walks 'a'..'z'
#   emitting only the letters that occurred.
#
# time = O(n), space = O(1)  (26 counters)
class Solution(object):
    def betterCompression(self, compressed):
        total = [0] * 26
        i, n = 0, len(compressed)
        while i < n:
            c = ord(compressed[i]) - 97
            i += 1
            j = i
            while j < n and compressed[j].isdigit():
                j += 1
            total[c] += int(compressed[i:j])
            i = j

        out = []
        for c in range(26):
            if total[c]:
                out.append(chr(97 + c) + str(total[c]))
        return ''.join(out)
