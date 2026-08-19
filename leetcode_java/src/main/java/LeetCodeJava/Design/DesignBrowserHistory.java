package LeetCodeJava.Design;

// https://leetcode.com/problems/design-browser-history/

import java.util.ArrayList;
import java.util.List;

/**
 *  1472. Design Browser History
 *  Medium
 *
 *  You have a browser of one tab where you start on the homepage and you can visit
 *  another url, get back in the history number of steps or move forward in the
 *  history number of steps.
 *
 *  Implement the BrowserHistory class:
 *
 *   - BrowserHistory(String homepage) Initializes the object with the homepage of the browser.
 *   - void visit(String url) Visits url from the current page. It clears up all the forward history.
 *   - String back(int steps) Move steps back in history. If you can only return x steps in the
 *     history and steps > x, you will return only x steps. Return the current url after moving
 *     back in history at most steps.
 *   - String forward(int steps) Move steps forward in history. If you can only forward x steps
 *     and steps > x, you will forward only x steps. Return the current url.
 *
 *  Example 1:
 *
 *  Input:
 *  ["BrowserHistory","visit","visit","visit","back","back","forward","visit","forward","back","back"]
 *  [["leetcode.com"],["google.com"],["facebook.com"],["youtube.com"],[1],[1],[1],["linkedin.com"],[2],[2],[7]]
 *  Output:
 *  [null,null,null,null,"facebook.com","google.com","facebook.com",null,"linkedin.com","google.com","leetcode.com"]
 *
 *  Constraints:
 *
 *   1 <= homepage.length <= 20
 *   1 <= url.length <= 20
 *   1 <= steps <= 100
 *   homepage and url consist of '.' or lower case English letters.
 *   At most 5000 calls will be made to visit, back, and forward.
 */
public class DesignBrowserHistory {

    // V0
    // IDEA: ARRAY LIST AS HISTORY + CURSOR INDEX; visit() truncates the forward part
    /**
     * time = O(1) for back/forward, O(n) worst case for visit (truncating forward history)
     * space = O(n), n = number of visited urls
     */
    private final List<String> history;
    private int cur;

    public DesignBrowserHistory(String homepage) {
        this.history = new ArrayList<>();
        this.history.add(homepage);
        this.cur = 0;
    }

    public void visit(String url) {
        // drop everything after the current page (forward history)
        while (history.size() > cur + 1) {
            history.remove(history.size() - 1);
        }
        history.add(url);
        cur = history.size() - 1;
    }

    public String back(int steps) {
        cur = Math.max(0, cur - steps);
        return history.get(cur);
    }

    public String forward(int steps) {
        cur = Math.min(history.size() - 1, cur + steps);
        return history.get(cur);
    }
}
