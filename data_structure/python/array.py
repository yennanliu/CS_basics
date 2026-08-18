#---------------------------------------------------------------
# ARRAY (fixed-size, static)
#---------------------------------------------------------------
#
# A static array is a contiguous block of memory of a FIXED size.
# Python's built-in `list` is a *dynamic* array (it resizes itself);
# this file re-implements the fixed-size version so the cost of each
# operation is visible.
#
# Index math is the whole trick: element i lives at
#   base_address + i * size_of_element
# so reading/writing by index is O(1), while insert/delete have to
# SHIFT every element after the target position.
#
# Time  : get/set    O(1)
#         search     O(N)
#         insert     O(N)   (shift the tail right)
#         delete     O(N)   (shift the tail left)
# Space : O(N)
#
# References:
#   - https://github.com/OmkarPathak/Data-Structures-using-Python/blob/master/Arrays/Arrays.py


class Array:
    """Fixed-size array of `size` slots, all initialised to item_type()."""

    def __init__(self, size, item_type=int):
        self.size = size
        self.item_type = item_type
        self.items = [item_type()] * size

    def __len__(self):
        return self.size

    def __getitem__(self, index):
        return self.items[index]

    def __setitem__(self, index, value):
        self.items[index] = value

    def __str__(self):
        return " ".join(str(i) for i in self.items)

    def search(self, target):
        """Linear scan. Return the index of target, or -1 if absent."""
        for i in range(self.size):
            if self.items[i] == target:
                return i
        return -1

    def insert(self, value, position):
        """Insert value at position; the LAST element falls off the end.

        Shift right, walking BACKWARDS so we never overwrite a slot
        we still need:

            before : [1, 2, 3, 4]   insert(9, 1)
                          <-- shift
            after  : [1, 9, 2, 3]   (4 is pushed out)
        """
        if not 0 <= position < self.size:
            raise IndexError("position out of range: {}".format(position))
        for i in range(self.size - 2, position - 1, -1):
            self.items[i + 1] = self.items[i]
        self.items[position] = value

    def delete(self, position):
        """Delete the element at position; the freed tail slot is zeroed.

        Shift left, walking FORWARDS for the same reason:

            before : [1, 9, 2, 3]   delete(1)
                         shift -->
            after  : [1, 2, 3, 0]
        """
        if not 0 <= position < self.size:
            raise IndexError("position out of range: {}".format(position))
        for i in range(position, self.size - 1):
            self.items[i] = self.items[i + 1]
        self.items[self.size - 1] = self.item_type()


if __name__ == "__main__":
    arr = Array(5)
    assert len(arr) == 5
    assert str(arr) == "0 0 0 0 0"

    for i in range(5):
        arr[i] = i + 1
    assert str(arr) == "1 2 3 4 5"

    assert arr.search(3) == 2
    assert arr.search(99) == -1

    arr.insert(9, 1)                 # 5 is pushed off the end
    assert str(arr) == "1 9 2 3 4"

    arr.delete(1)                    # tail slot is reset to 0
    assert str(arr) == "1 2 3 4 0"

    print("Success.")
