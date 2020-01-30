
#################################################################
# ALGORITHM DEMO : SELECTION SORT 
#################################################################

# V0
def selection_sort(list_): 
    for i in range(len(list_)-1):
        min_idx = i 
        for j in range(i+1, len(list_)):
            if list_[j] < list_[min_idx]:
                min_idx = j
        if min_idx != i:
            list_[i], list_[min_idx] = list_[min_idx], list_[i]
    return list_            

# V1
# http://jialin128.pixnet.net/blog/post/138292085-%5B%E8%B3%87%E6%96%99%E7%B5%90%E6%A7%8B%5D-%E9%81%B8%E6%93%87%E6%8E%92%E5%BA%8F%E6%B3%95%EF%BC%88selection-sort%EF%BC%89in-python  
def selection_sort(list): #from small -> big,  in-place
    for i in range(0, len(list)-1):
        min_indx = i
        for j in range(i+1, len(list)):
            if (list[min_indx] > list[j]):
                min_indx = j
        if (min_indx!=i):
            tmp = list[min_indx]
            list[min_indx] = list[i]
            list[i] = tmp
        print("pass", i + 1, ": ", list)

# V2 
# IDEA : using extra space (array)
def selection_sort(list): #extra-place
    count = 0
    sorted = []
    while(len(list) != 0):
        count = count + 1
        min_indx = list.index(min(list))
        sorted.append(list[min_indx])
        list.pop(min_indx)
        print("pass", count, ": ", sorted)
