package LeetCodeJava.Design;

// https://leetcode.com/problems/design-a-text-editor/

/**
 *  2296. Design a Text Editor
 *  Hard
 *
 *  Design a text editor with a cursor that can do the following:
 *   - Add text to where the cursor is.
 *   - Delete text from where the cursor is (simulating the backspace key).
 *   - Move the cursor either left or right.
 *
 *  When deleting text, only characters to the left of the cursor will be deleted. The
 *  cursor will also be moved left by that same number of characters. If the actual text
 *  is shorter than k characters, only the actual text will be deleted.
 *
 *  When moving the cursor, it will only move as far as it can.
 *
 *  Implement the TextEditor class:
 *
 *   - TextEditor() Initializes the object with empty text.
 *   - void addText(String text) Appends text to where the cursor is. The cursor ends to
 *     the right of text.
 *   - int deleteText(int k) Deletes k characters to the left of the cursor. Returns the
 *     number of characters actually deleted.
 *   - String cursorLeft(int k) Moves the cursor to the left k times. Returns the last
 *     min(10, len) characters to the left of the cursor, where len is the number of
 *     characters to the left of the cursor.
 *   - String cursorRight(int k) Moves the cursor to the right k times. Returns the last
 *     min(10, len) characters to the left of the cursor.
 *
 *  Example 1:
 *    Input
 *      ["TextEditor","addText","deleteText","addText","cursorRight","cursorLeft",
 *       "deleteText","cursorLeft","cursorRight"]
 *      [[],["leetcode"],[4],["practice"],[3],[8],[10],[2],[6]]
 *    Output
 *      [null,null,4,null,"etpractice","leet",4,"","practi"]
 *    Explanation
 *      addText("leetcode")  -> "leetcode|"
 *      deleteText(4)        -> return 4, text is "leet|"
 *      addText("practice")  -> "leetpractice|"
 *      cursorRight(3)       -> "etpractice" (cursor cannot move past the end)
 *      cursorLeft(8)        -> "leet",  text is "leet|practice"
 *      deleteText(10)       -> return 4, text is "|practice"
 *      cursorLeft(2)        -> ""
 *      cursorRight(6)       -> "practi"
 *
 *  Constraints:
 *    1 <= text.length, k <= 40
 *    text consists of lowercase English letters.
 *    At most 2 * 10^4 calls in total will be made to addText, deleteText, cursorLeft,
 *    and cursorRight.
 */
public class DesignATextEditor {

    // V0
    // IDEA: TWO STACKS STRADDLING THE CURSOR
    //
    //   keep everything LEFT of the cursor in one stack and everything RIGHT of it
    //   in another, stored REVERSED so its top is the character just after the cursor.
    //
    //   every operation is then a push/pop at the two tops:
    //       addText     -> push onto `left`
    //       deleteText  -> pop from `left`
    //       cursorLeft  -> move characters from `left` to `right`
    //       cursorRight -> move characters from `right` to `left`
    //
    //   this beats a single StringBuilder, where an insert or delete in the middle
    //   costs O(n) per call.
    /**
     * time = O(K) per call (K = the call's own k / text length)
     * space = O(total text)
     */
    private final StringBuilder left = new StringBuilder();   // before the cursor, in order
    private final StringBuilder right = new StringBuilder();  // after the cursor, REVERSED

    public DesignATextEditor() {
    }

    public void addText(String text) {
        this.left.append(text);
    }

    public int deleteText(int k) {
        int deleted = Math.min(k, this.left.length());
        this.left.setLength(this.left.length() - deleted);
        return deleted;
    }

    private String window() {
        int len = this.left.length();
        return this.left.substring(Math.max(0, len - 10), len);
    }

    public String cursorLeft(int k) {
        int move = Math.min(k, this.left.length());
        for (int i = 0; i < move; i++) {
            int last = this.left.length() - 1;
            this.right.append(this.left.charAt(last));
            this.left.setLength(last);
        }
        return window();
    }

    public String cursorRight(int k) {
        int move = Math.min(k, this.right.length());
        for (int i = 0; i < move; i++) {
            int last = this.right.length() - 1;
            this.left.append(this.right.charAt(last));
            this.right.setLength(last);
        }
        return window();
    }
}
