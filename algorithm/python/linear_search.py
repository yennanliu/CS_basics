#---------------------------------------------------------------
# LINEAR SEARCH
#---------------------------------------------------------------
#
# Scan every element until the target is found.
#
# Time  : Best O(1), Avg/Worst O(N)
# Space : O(1)
#
# References:
#   - https://github.com/yennanliu/Python/blob/master/searches/linear_search.py


# V0
def linear_search(sequence, target):
    """Return index of target in sequence, or None if not present.

    >>> linear_search([0, 5, 7, 10, 15], 0)
    0
    >>> linear_search([0, 5, 7, 10, 15], 15)
    4
    >>> linear_search([0, 5, 7, 10, 15], 6) is None
    True
    """
    for index, item in enumerate(sequence):
        if item == target:
            return index
    return None


if __name__ == "__main__":
    import doctest

    doctest.testmod()
    print("Success.")
