"""

1702. Maximum Binary String After Change
Medium

You are given a binary string binary consisting of only 0's or 1's. You can apply each of the
following operations any number of times:

Operation 1: If the number contains the substring "00", you can replace it with "10".
  For example, "00010" -> "10010"
Operation 2: If the number contains the substring "10", you can replace it with "01".
  For example, "00010" -> "00001"

Return the maximum binary string you can obtain after any number of operations. Binary string x is
greater than binary string y if x's decimal representation is greater than y's decimal
representation.


Example 1:

Input: binary = "000110"
Output: "111011"
Explanation: A valid transformation sequence can be:
"000110" -> "000101"
"000101" -> "100101"
"100101" -> "110101"
"110101" -> "110011"
"110011" -> "111011"

Example 2:

Input: binary = "01"
Output: "01"
Explanation: "01" cannot be transformed any further.


Constraints:

1 <= binary.length <= 10^5
binary consist of '0' and '1'.

"""

# V0
# IDEA : GREEDY / INVARIANT (op 2 gathers the zeros, op 1 then eats all but one)
#
#   observations:
#     - the leading block of 1's is frozen: no operation can touch it.
#     - op 2 ("10" -> "01") lets any zero slide LEFT past a one, so all the zeros
#       can be packed into one contiguous run right after that leading block.
#     - op 1 ("00" -> "10") then turns a run of z zeros into z-1 ones + one zero.
#
#   so the answer is always all 1's with exactly ONE zero (unless there was no
#   zero at all), and the zero sits at index
#     (index of the first 0) + (number of zeros AFTER that first 0)
#   which is as far right as it can be pushed.
#
#   NOTE : a string with no '0' is already maximal -> return it untouched.
#
# time = O(n), space = O(n)
class Solution(object):
    def maximumBinaryString(self, binary):
        n = len(binary)
        first = binary.find("0")
        if first == -1:
            return binary
        zeros_after = binary.count("0", first + 1)
        pos = first + zeros_after
        return "1" * pos + "0" + "1" * (n - pos - 1)
