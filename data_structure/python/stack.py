#---------------------------------------------------------------
# STACK
#---------------------------------------------------------------
#
# LIFO (Last In, First Out) container: the last item pushed is the
# first one popped.
#
#     push(3) ->  |   |      pop() ->  |   |
#                 | 3 |                |   |
#                 | 2 |                | 2 |
#                 | 1 |                | 1 |
#                 +---+                +---+
#
# Backed by a Python list, so push/pop touch only the END of the list
# and are therefore O(1) (amortised).
#
# Time  : push / pop / peek / is_empty / size -> O(1)
# Space : O(N)
#
# Typical uses: undo history, DFS, matching brackets, monotonic stack.


class Stack:
    """LIFO stack with an optional capacity limit."""

    def __init__(self, limit=10):
        self.stack = []
        self.limit = limit

    def __len__(self):
        return len(self.stack)

    def __str__(self):
        # printed bottom -> top
        return " ".join(str(i) for i in self.stack)

    def is_empty(self):
        return not self.stack

    def is_full(self):
        return len(self.stack) >= self.limit

    def push(self, data):
        """Add on top. Raises when the stack is full (overflow)."""
        if self.is_full():
            raise OverflowError("stack overflow (limit={})".format(self.limit))
        self.stack.append(data)

    def pop(self):
        """Remove and return the top item. Raises when empty (underflow)."""
        if self.is_empty():
            raise IndexError("pop from an empty stack")
        return self.stack.pop()

    def peek(self):
        """Return the top item WITHOUT removing it."""
        if self.is_empty():
            raise IndexError("peek at an empty stack")
        return self.stack[-1]

    def size(self):
        return len(self.stack)


if __name__ == "__main__":
    s = Stack(limit=3)
    assert s.is_empty()

    s.push(1)
    s.push(2)
    s.push(3)
    assert str(s) == "1 2 3"
    assert s.size() == 3

    # last in, first out
    assert s.peek() == 3
    assert s.pop() == 3
    assert s.pop() == 2
    assert str(s) == "1"

    s.push(2)
    s.push(3)
    try:
        s.push(4)                    # limit is 3 -> overflow
        raise AssertionError("expected OverflowError")
    except OverflowError:
        pass

    s.pop(); s.pop(); s.pop()
    try:
        s.pop()                      # empty -> underflow
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    print("Success.")
