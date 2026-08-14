r"""

1410. HTML Entity Parser
Medium

HTML entity parser is the parser that takes HTML code as input and replace all
the entities of the special characters by the characters itself.

The special characters and their entities for HTML are:

Quotation Mark: the entity is &quot; and symbol character is ".
Single Quote Mark: the entity is &apos; and symbol character is '.
Ampersand: the entity is &amp; and symbol character is &.
Greater Than Sign: the entity is &gt; and symbol character is >.
Less Than Sign: the entity is &lt; and symbol character is <.
Slash: the entity is &frasl; and symbol character is /.

Given the input text string to the HTML parser, you have to implement the entity
parser.

Return the text after replacing the entities by the special characters.


Example 1:

Input: text = "&amp; is an HTML entity but &ambassador; is not."
Output: "& is an HTML entity but &ambassador; is not."
Explanation: The parser will replace the &amp; entity by &

Example 2:

Input: text = "and I quote: &quot;...&quot;"
Output: "and I quote: \"...\""


Constraints:

1 <= text.length <= 10^5
The string may contain any possible characters out of all the 256 ASCII
characters.

"""

# V0
# IDEA : HASH TABLE + LEFT-TO-RIGHT SCAN
#
#   Scan once. At each index try every entity length (2..7); the first match
#   wins and we jump past it, so a produced '&' is never re-parsed
#   (e.g. "&amp;quot;" -> "&quot;", NOT '"').
#
# time = O(n * L), L = 7 (longest entity)
# space = O(n)
class Solution(object):
    def entityParser(self, text):
        d = {
            "&quot;": '"',
            "&apos;": "'",
            "&amp;": "&",
            "&gt;": ">",
            "&lt;": "<",
            "&frasl;": "/",
        }
        res = []
        i = 0
        n = len(text)
        while i < n:
            matched = False
            for l in range(2, 8):
                piece = text[i:i + l]
                if piece in d:
                    res.append(d[piece])
                    i += l
                    matched = True
                    break
            if not matched:
                res.append(text[i])
                i += 1
        return "".join(res)

# V1
# IDEA : SEQUENTIAL str.replace, with &amp; LAST
#
#   The other 5 entities never produce a '&', so they can never create a new
#   entity. Doing &amp; last means the '&' it produces is not rescanned,
#   which is exactly the required semantics.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def entityParser(self, text):
        for entity, ch in (
            ("&quot;", '"'),
            ("&apos;", "'"),
            ("&gt;", ">"),
            ("&lt;", "<"),
            ("&frasl;", "/"),
        ):
            text = text.replace(entity, ch)
        return text.replace("&amp;", "&")
