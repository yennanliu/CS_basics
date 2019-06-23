# #################################################################
# # ALGORITHM DEMO : QUICK SORT 
# #################################################################

# # V0 
# # idea
# # http://bubkoo.com/2014/01/12/sort-algorithm/quick-sort/
# # https://zh.wikipedia.org/wiki/%E5%BF%AB%E9%80%9F%E6%8E%92%E5%BA%8F
# def quickSort(arr):
#     less = []
#     pivotList = []
#     more = []
#     if len(arr) <= 1:
#         return arr
#     else:
#         pivot = arr[0]      # set the first element as pivot 
#         for i in arr:
#             if i < pivot:
#                 less.append(i)
#             elif i > pivot:
#                 more.append(i)
#             else:
#                 pivotList.append(i)

#         less = quickSort(less)      # when got the 1st round output, keep running the sorting till all sorted 
#         more = quickSort(more)

#         return less + pivotList + more


# # V1 
# def quick_sort(arr, simulation=False):
#     """ Quick sort
#         Complexity: best O(n log(n)) avg O(n log(n)), worst O(N^2)
#     """
#     iteration = 0
#     if simulation:
#         print("iteration",iteration,":",*arr)
#     arr, _ = quick_sort_recur(arr, 0, len(arr) - 1, iteration, simulation)
#     return arr

# def quick_sort_recur(arr, first, last, iteration, simulation):
#     if first < last:
#         pos = partition(arr, first, last)
#         # Start our two recursive calls
#         if simulation:
#             iteration = iteration + 1
#             print("iteration",iteration,":",*arr)
            
#         _, iteration = quick_sort_recur(arr, first, pos - 1, iteration, simulation)
#         _, iteration = quick_sort_recur(arr, pos + 1, last, iteration, simulation)

#     return arr, iteration

# def partition(arr, first, last):
#     wall = first
#     for pos in range(first, last):
#         if arr[pos] < arr[last]:  # last is the pivot
#             arr[pos], arr[wall] = arr[wall], arr[pos]
#             wall += 1
#     arr[wall], arr[last] = arr[last], arr[wall]
#     return wall

# # V2 
# # https://blog.csdn.net/razor87/article/details/71155518
# def quick_sort_one_line():
#     quick_sort = lambda array: array if len(array) <= 1 else quick_sort([item for item in array[1:] if item <= array[0]]) + [array[0]] + quick_sort([item for item in array[1:] if item > array[0]])


# def quick_sort(array, left, right):
#     if left >= right:
#         return
#     low = left
#     high = right
#     key = array[low]
#     while left < right:
#         while left < right and array[right] > key:
#             right -= 1
#         array[left] = array[right]
#         while left < right and array[left] <= key:
#             left += 1
#         array[right] = array[left]
#     array[right] = key
#     quick_sort(array, low, left - 1)
#     quick_sort(array, left + 1, high)

# def quick_sort_via_partition(array, l, r):
#     def partition(array, l, r):
#     x = array[r]
#     i = l - 1
#     for j in range(l, r):
#         if array[j] <= x:
#             i += 1
#             array[i], array[j] = array[j], array[i]
#     array[i + 1], array[r] = array[r], array[i+1]
#     return i + 1

#     if l < r:
#         q = partition(array, l, r)
#         quick_sort(array, l, q - 1)
#         quick_sort(array, q + 1, r)
 

# def quick_sort_via_stack(array, l, r):
#     if l >= r:
#         return
#     stack = []
#     stack.append(l)
#     stack.append(r)
#     while stack:
#         low = stack.pop(0)
#         high = stack.pop(0)
#         if high - low <= 0:
#             continue
#         x = array[high]
#         i = low - 1
#         for j in range(low, high):
#             if array[j] <= x:
#                 i += 1
#                 array[i], array[j] = array[j], array[i]
#         array[i + 1], array[high] = array[high], array[i + 1]
#         stack.extend([low, i, i + 2, high])

# # V4 
# # https://github.com/qiwsir/algorithm/blob/master/quick_sort.md
# # METHOD 1 
# def quickSort(arr):
#     less = []
#     pivotList = []
#     more = []
#     if len(arr) <= 1:
#         return arr
#     else:
#         pivot = arr[0]      #将第一个值做为基准
#         for i in arr:
#             if i < pivot:
#                 less.append(i)
#             elif i > pivot:
#                 more.append(i)
#             else:
#                 pivotList.append(i)

#         less = quickSort(less)      #得到第一轮分组之后，继续将分组进行下去。
#         more = quickSort(more)

#         return less + pivotList + more


# # METHOD 3 
# # idea is same as above, but in different style 
# def qsort(list):
#     if not list:
#         return []
#     else:
#         pivot = list[0]
#         less = [x for x in list     if x <  pivot]
#         more = [x for x in list[1:] if x >= pivot]
#         return qsort(less) + [pivot] + qsort(more)

# # METHOD 4
# from random import *
# def qSort(a):
#     if len(a) <= 1:
#         return a
#     else:
#         q = choice(a)       #基准的选择不同于前，是从数组中任意选择一个值做为基准
#         return qSort([elem for elem in a if elem < q]) + [q] * a.count(q) + qSort([elem for elem in a if elem > q])


# # METHOD 5 
# qs = lambda xs : ( (len(xs) <= 1 and [xs]) or [ qs( [x for x in xs[1:] if x < xs[0]] ) + [xs[0]] + qs( [x for x in xs[1:] if x >= xs[0]] ) ] )[0]

# # if __name__=="__main__":
# #     a = [4, 65, 2, -31, 0, 99, 83, 782, 1]
# #     print quickSort(a)
# #     print qSort(a)

# #     print qs(a)



