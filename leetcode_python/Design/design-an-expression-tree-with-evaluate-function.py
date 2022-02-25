"""

1628. Design an Expression Tree With Evaluate Function
Medium

Given the postfix tokens of an arithmetic expression, build and return the binary expression tree that represents this expression.

Postfix notation is a notation for writing arithmetic expressions in which the operands (numbers) appear before their operators. For example, the postfix tokens of the expression 4*(5-(7+2)) are represented in the array postfix = ["4","5","7","2","+","-","*"].

The class Node is an interface you should use to implement the binary expression tree. The returned tree will be tested using the evaluate function, which is supposed to evaluate the tree's value. You should not remove the Node class; however, you can modify it as you wish, and you can define other classes to implement it if needed.

A binary expression tree is a kind of binary tree used to represent arithmetic expressions. Each node of a binary expression tree has either zero or two children. Leaf nodes (nodes with 0 children) correspond to operands (numbers), and internal nodes (nodes with two children) correspond to the operators '+' (addition), '-' (subtraction), '*' (multiplication), and '/' (division).

It's guaranteed that no subtree will yield a value that exceeds 109 in absolute value, and all the operations are valid (i.e., no division by zero).

Follow up: Could you design the expression tree such that it is more modular? For example, is your design able to support additional operators without making changes to your existing evaluate implementation?

 

Example 1:


Input: s = ["3","4","+","2","*","7","/"]
Output: 2
Explanation: this expression evaluates to the above binary tree with expression ((3+4)*2)/7) = 14/7 = 2.
Example 2:


Input: s = ["4","5","2","7","+","-","*"]
Output: -16
Explanation: this expression evaluates to the above binary tree with expression 4*(5-(2+7)) = 4*(-4) = -16.
 

Constraints:

1 <= s.length < 100
s.length is odd.
s consists of numbers and the characters '+', '-', '*', and '/'.
If s[i] is a number, its integer representation is no more than 105.
It is guaranteed that s is a valid expression.
The absolute value of the result and intermediate values will not exceed 109.
It is guaranteed that no expression will include division by zero.

"""

# V0

# V1
# https://blog.csdn.net/qq_46105170/article/details/109441459

# V1'
# https://leetcode.com/problems/design-an-expression-tree-with-evaluate-function/discuss/1669374/python
import abc 
from abc import ABC, abstractmethod 
class Node(ABC):
    @abstractmethod
    # define your fields here
    def evaluate(self) -> int:
        pass

   
class eNode(Node):
    def __init__(self, val = None):
        self.val = val
        self.left = None
        self.right = None
        
    def evaluate(self):
        alu = {'*': lambda x, y: x * y, '-': lambda x, y: x - y, '+': lambda x, y: x + y, '/': lambda x, y: x // y}
        def eval(node):
            if node.val in alu:
                return alu[node.val](eval(node.left), eval(node.right))
            return int(node.val)
        return eval(self)


class TreeBuilder(object):
    def buildTree(self, postfix: List[str]) -> 'Node':
        def search(node):
            node.val = postfix[self.i]
            if not node.val.isdigit():
                self.i -= 1
                node.right = search(eNode())
                self.i -= 1
                node.left = search(eNode())
            return node
        
        self.i = len(postfix) - 1
        return search(eNode())

# V1''
# https://leetcode.com/problems/design-an-expression-tree-with-evaluate-function/discuss/1286682/Python
import abc 
from abc import ABC, abstractmethod 
"""
This is the interface for the expression tree Node.
You should not remove it, and you can define some classes to implement it.
"""

class Node(ABC):
    @abstractmethod
    def evaluate(self) -> int:
        pass

"""    
This is the TreeBuilder class.
You can treat it as the driver code that takes the postinfix input
and returns the expression tree represnting it as a Node.
"""
class TreeNode(Node):
    def __init__(self, val=None, left=None, right=None):
        self.val = val
        self.left = left 
        self.right = right
        self.ops = { 
            "+": operator.add, 
            "-": operator.sub,
            "*": operator.mul, 
            "/": operator.floordiv
        }
        
    def evaluate(self) -> int:
        if self.val not in self.ops:
            return int(self.val) 
        return self.ops[self.val] (self.left.evaluate(), self.right.evaluate())

class TreeBuilder(object):
    def buildTree(self, postfix: List[str]) -> 'Node':
        stack = []
        for item in postfix:
            if item in "+-*/":
                r = stack.pop()
                l = stack.pop()
                node = TreeNode(item)
                node.left = l
                node.right = r
                stack.append(node)
            else:
                stack.append(TreeNode(item))
        return stack[0]

# V1'''
# https://leetcode.com/problems/design-an-expression-tree-with-evaluate-function/discuss/1095682/Python-solution
import abc 
from abc import ABC, abstractmethod 
"""
This is the interface for the expression tree Node.
You should not remove it, and you can define some classes to implement it.
"""

class Node(ABC):
    @abstractmethod
    # define your fields here
    def evaluate(self) -> int:
        pass

# TreeNode is inheriting from the abstract class, Node.
class TreeNode(Node):
    def __init__(self, val, l = None, r = None):
        self.value = val
        self.left = l
        self.right = r
    
    def evaluate(self):
        if not self.left and not self.right:
		# leaf node => value is an integer
            return self.value
        
        if self.value == '+':
            return self.left.evaluate() + self.right.evaluate()
        elif self.value == '-':
            return self.left.evaluate() - self.right.evaluate()
        elif self.value == '*':
            return self.left.evaluate() * self.right.evaluate()
        elif self.value == '/':
            return self.left.evaluate() // self.right.evaluate()


"""    
This is the TreeBuilder class.
You can treat it as the driver code that takes the postinfix input
and returns the expression tree represnting it as a Node.
"""

class TreeBuilder(object):
    def buildTree(self, postfix: List[str]) -> 'Node':
        stk = []
        for ch in postfix:
            if ch.isdigit():
                stk.append(TreeNode(int(ch)))
            else:
                r = stk.pop()
                l = stk.pop()
                stk.append(TreeNode(ch, l, r))
        
		# The top of the stack contains the root of the expression tree.
        return stk[-1]
		
"""
Your TreeBuilder object will be instantiated and called as such:
obj = TreeBuilder();
expTree = obj.buildTree(postfix);
ans = expTree.evaluate();
"""

# V2