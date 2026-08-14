"""

2296. Design a Text Editor
Hard

Design a text editor with a cursor that can do the following:

Add text to where the cursor is.
Delete text from where the cursor is (simulating the backspace key).
Move the cursor either left or right.

When deleting text, only characters to the left of the cursor will be deleted. The cursor will also be moved left by that same number of characters. If the actual text is shorter than k characters, only the actual text will be deleted.

When moving the cursor, it will only move as far as it can. If it cannot move the cursor k characters to the left or right, it will move as far as it can.

Implement the TextEditor class:

TextEditor() Initializes the object with empty text.
void addText(string text) Appends text to where the cursor is. The cursor ends to the right of text.
int deleteText(int k) Deletes k characters to the left of the cursor. Returns the number of characters actually deleted.
string cursorLeft(int k) Moves the cursor to the left k times. Returns the last min(10, len) characters to the left of the cursor, where len is the number of characters to the left of the cursor.
string cursorRight(int k) Moves the cursor to the right k times. Returns the last min(10, len) characters to the left of the cursor, where len is the number of characters to the left of the cursor.


Example 1:

Input
["TextEditor", "addText", "deleteText", "addText", "cursorRight", "cursorLeft", "deleteText", "cursorLeft", "cursorRight"]
[[], ["leetcode"], [4], ["practice"], [3], [8], [10], [2], [6]]
Output
[null, null, 4, null, "etpractice", "leet", 4, "", "practi"]

Explanation
TextEditor textEditor = new TextEditor(); // The current text is "|". (The '|' character represents the cursor)
textEditor.addText("leetcode"); // The current text is "leetcode|".
textEditor.deleteText(4); // return 4
                          // The current text is "leet|".
                          // 4 characters were deleted.
textEditor.addText("practice"); // The current text is "leetpractice|".
textEditor.cursorRight(3); // return "etpractice"
                           // The current text is "leetpractice|".
                           // The cursor cannot be moved beyond the actual text and thus did not move.
                           // "etpractice" is the last 10 characters to the left of the cursor.
textEditor.cursorLeft(8); // return "leet"
                          // The current text is "leet|practice".
                          // "leet" is the last min(10, 4) = 4 characters to the left of the cursor.
textEditor.deleteText(10); // return 4
                           // The current text is "|practice".
                           // Only 4 characters were deleted.
textEditor.cursorLeft(2); // return ""
                          // The current text is "|practice".
                          // The cursor cannot be moved beyond the actual text and thus did not move.
                          // "" is the last min(10, 0) = 0 characters to the left of the cursor.
textEditor.cursorRight(6); // return "practi"
                           // The current text is "practi|cursor".
                           // "practi" is the last min(10, 6) = 6 characters to the left of the cursor.


Constraints:

1 <= text.length, k <= 40
text consists of lowercase English letters.
At most 2 * 10^4 calls in total will be made to addText, deleteText, cursorLeft, and cursorRight.

"""

# V0
# IDEA : TWO STACKS STRADDLING THE CURSOR
#
#   keep everything LEFT of the cursor in one list and everything RIGHT of it
#   in another, stored REVERSED so its top is the character just after the
#   cursor.
#
#   every operation is then a push/pop at the two tops :
#       addText     -> extend `left`
#       deleteText  -> pop from `left`
#       cursorLeft  -> move characters from `left` to `right`
#       cursorRight -> move characters from `right` to `left`
#
#   this beats a single string, where an insert or delete in the middle costs
#   O(n) per call.
#
# time = O(k) per call, space = O(total text)
class TextEditor(object):

    def __init__(self):
        self.left = []      # characters before the cursor, in order
        self.right = []     # characters after the cursor, REVERSED

    def addText(self, text):
        self.left.extend(text)

    def deleteText(self, k):
        deleted = min(k, len(self.left))
        for _ in range(deleted):
            self.left.pop()
        return deleted

    def _window(self):
        return ''.join(self.left[-10:])

    def cursorLeft(self, k):
        for _ in range(min(k, len(self.left))):
            self.right.append(self.left.pop())
        return self._window()

    def cursorRight(self, k):
        for _ in range(min(k, len(self.right))):
            self.left.append(self.right.pop())
        return self._window()


# Your TextEditor object will be instantiated and called as such:
# obj = TextEditor()
# obj.addText(text)
# param_2 = obj.deleteText(k)
# param_3 = obj.cursorLeft(k)
# param_4 = obj.cursorRight(k)
