"""

1472. Design Browser History
Medium

You have a browser of one tab where you start on the homepage and you can visit another url, get back in the history number of steps or move forward in the history number of steps.

Implement the BrowserHistory class:

BrowserHistory(string homepage) Initializes the object with the homepage of the browser.
void visit(string url) Visits url from the current page. It clears up all the forward history.
string back(int steps) Move steps back in history. If you can only return x steps in the history and steps > x, you will return only x steps. Return the current url after moving back in history at most steps.
string forward(int steps) Move steps forward in history. If you can only forward x steps in the history and steps > x, you will forward only x steps. Return the current url after forwarding in history at most steps.
 

Example:

Input:
["BrowserHistory","visit","visit","visit","back","back","forward","visit","forward","back","back"]
[["leetcode.com"],["google.com"],["facebook.com"],["youtube.com"],[1],[1],[1],["linkedin.com"],[2],[2],[7]]
Output:
[null,null,null,null,"facebook.com","google.com","facebook.com",null,"linkedin.com","google.com","leetcode.com"]

Explanation:
BrowserHistory browserHistory = new BrowserHistory("leetcode.com");
browserHistory.visit("google.com");       // You are in "leetcode.com". Visit "google.com"
browserHistory.visit("facebook.com");     // You are in "google.com". Visit "facebook.com"
browserHistory.visit("youtube.com");      // You are in "facebook.com". Visit "youtube.com"
browserHistory.back(1);                   // You are in "youtube.com", move back to "facebook.com" return "facebook.com"
browserHistory.back(1);                   // You are in "facebook.com", move back to "google.com" return "google.com"
browserHistory.forward(1);                // You are in "google.com", move forward to "facebook.com" return "facebook.com"
browserHistory.visit("linkedin.com");     // You are in "facebook.com". Visit "linkedin.com"
browserHistory.forward(2);                // You are in "linkedin.com", you cannot move forward any steps.
browserHistory.back(2);                   // You are in "linkedin.com", move back two steps to "facebook.com" then to "google.com". return "google.com"
browserHistory.back(7);                   // You are in "google.com", you can move back only one step to "leetcode.com". return "leetcode.com"
 

Constraints:

1 <= homepage.length <= 20
1 <= url.length <= 20
1 <= steps <= 100
homepage and url consist of  '.' or lower case English letters.
At most 5000 calls will be made to visit, back, and forward.

"""

# V0

# V1
# IDEA : deque, hashmap
# https://leetcode.com/problems/design-browser-history/discuss/712480/Python-sol-by-deque-w-Comment
from collections import deque
class BrowserHistory:

    def __init__(self, homepage: str):
	
        self.history = deque([homepage])
        self.cur_index = 0

    def visit(self, url: str):
        # clear all forward history
        tail_len = len(self.history)-1-self.cur_index
        while tail_len:
            self.history.pop()
            tail_len -= 1
            
        self.history.append(url)
        self.cur_index += 1
        
    def back(self, steps: int):
		# boundary check
        if steps > self.cur_index:
            self.cur_index = 0
        else:
            self.cur_index -= steps
        return self.history[self.cur_index]

    def forward(self, steps: int):
		# boundary check
        if steps >= len(self.history) - self.cur_index:
            self.cur_index = len(self.history) - 1
        else:
            self.cur_index += steps   
        return self.history[self.cur_index]


# V1'
# IDEA : LIST
# https://leetcode.com/problems/design-browser-history/discuss/1377645/Python-List-Approach
class BrowserHistory:

    def __init__(self, homepage: str):
        self.cur_page_index = 0
        self.history = [homepage]

    def visit(self, url: str):
        self.history = self.history[:self.cur_page_index + 1]
        self.history.append(url)
        self.cur_page_index = len(self.history) - 1

    def back(self, steps: int):
        self.cur_page_index = max(self.cur_page_index - steps, 0)
        return self.history[self.cur_page_index]

    def forward(self, steps: int):
        if steps + self.cur_page_index >= len(self.history):
            self.cur_page_index = len(self.history) - 1
        else:
            self.cur_page_index += steps
        return self.history[self.cur_page_index]

# V1''
# IDEA : TWO STACKS
# https://leetcode.com/problems/design-browser-history/discuss/674520/Python-Two-Stacks
class BrowserHistory:

    def __init__(self, homepage: str):
        self.back_stack = [homepage]
        self.forward_stack = []

    def visit(self, url: str):
        self.forward_stack.clear() 
        self.back_stack.append(url)

    def back(self, steps: int):
        while len(self.back_stack) >= 2 and steps > 0:
            top = self.back_stack.pop()
            self.forward_stack.append(top)
            steps -= 1
        return self.back_stack[-1]

    def forward(self, steps: int):
        while len(self.forward_stack) > 0 and steps > 0:
            top = self.forward_stack.pop()
            self.back_stack.append(top)
            steps -= 1
        return self.back_stack[-1]

# V1'''
# https://leetcode.com/problems/design-browser-history/discuss/674579/python-easy-solution
class BrowserHistory:

	def __init__(self, homepage: str):
		self.l = [homepage]
		self.idx = 0

	def visit(self, url: str):
		del self.l[self.idx + 1:]
		self.l.append(url)
		self.idx += 1

	def back(self, steps: int):
		if steps <= self.idx:
			self.idx -= steps
			return self.l[self.idx]
		else:
			self.idx = 0
			return self.l[0]

	def forward(self, steps: int):
		if self.idx + steps <= len(self.l) - 1:
			self.idx += steps            
			return self.l[self.idx]
		else:
			self.idx = len(self.l) - 1            
			return self.l[self.idx]

# V2