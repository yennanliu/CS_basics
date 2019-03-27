#  http://kuanghy.github.io/2016/06/14/python-bisect

def binary_search_recursion(lst, value, low, high):
    if high < low:
        return None
    mid = (low + high) / 2
    if lst[mid] > value:
        return binary_search_recursion(lst, value, low, mid-1)
    elif lst[mid] < value:
        return binary_search_recursion(lst, value, mid+1, high)
    else:
        return mid

def binary_search_loop(lst,value):
    low, high = 0, len(lst)-1
    while low <= high:
        mid = (low + high) / 2
        if lst[mid] < value:
            low = mid + 1
        elif lst[mid] > value:
            high = mid - 1
        else:
            return mid
    return None