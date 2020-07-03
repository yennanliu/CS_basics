private static void MergeSort(Comparable[] a, Comparable[] aux, int lo, int mid, int hi)
{
  assert isSorted(a, lo, mid);
  assert isSorted(a, mid+1, hi);

  for (int k = lo; k <= hi; k++)
    aux[k] = a[k];

  int i = lo; j = mid + 1;
  for (int k = lo; k <= hi; k++)
  {
    if (i > mid)                      a[k] = aux[j++];
    else if (j > hi)                  a[k] = aux[i++];
    else if (less(aux[j], aux[i]))    a[k] = aux[j++];
    else                              a[k] = aux[i++];
  }

  assert isSorted(a, lo, hi)
}