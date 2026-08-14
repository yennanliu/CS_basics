"""

1678. Goal Parser Interpretation
Easy

You own a Goal Parser that can interpret a string command. The command consists of an alphabet of
"G", "()" and/or "(al)" in some order. The Goal Parser will interpret "G" as the string "G", "()" as
the string "o", and "(al)" as the string "al". The interpreted strings are then concatenated in the
original order.

Given the string command, return the Goal Parser's interpretation of command.


Example 1:

Input: command = "G()(al)"
Output: "Goal"
Explanation: The Goal Parser interprets the command as follows:
G -> G
() -> o
(al) -> al
The final concatenated result is "Goal".

Example 2:

Input: command = "G()()()()(al)"
Output: "Gooooal"

Example 3:

Input: command = "(al)G(al)()()G"
Output: "alGalooG"


Constraints:

1 <= command.length <= 100
command consists of "G", "()", and/or "(al)" in some order.

"""

# V0
# IDEA : SINGLE PASS PARSE (the token is decided by the char after '(')
#
#   only three tokens exist and they are self-delimiting:
#     'G'    -> "G"     (1 char)
#     "()"   -> "o"     (2 chars)
#     "(al)" -> "al"    (4 chars)
#   so on seeing '(' just peek at the next char: ')' means "o", 'a' means "al".
#
#   NOTE : chained str.replace also works, but only because "()" can never be
#          created by an earlier substitution -- the explicit scan is safer.
#
# time = O(n), space = O(n)
class Solution(object):
    def interpret(self, command):
        res = []
        i = 0
        n = len(command)
        while i < n:
            if command[i] == "G":
                res.append("G")
                i += 1
            elif command[i + 1] == ")":
                res.append("o")
                i += 2
            else:
                res.append("al")
                i += 4
        return "".join(res)
