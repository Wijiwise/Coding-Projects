#include <math.h>
#include "stack.h"
#include "qsort.h"

double polarAngle(Points pivot, Points p)
{ 
  return atan2(p.y - pivot.y, p.x - pivot.x);
}
//Swaps points during sorting
void swap(Points *a, Points *b) {
    Points temp = *a;
    *a = *b;
    *b = temp;
}
//Sorts polar angles
int partition(Points arr[], int low, int high, Points pivot) {
    double pivot_angle = polarAngle(pivot, arr[high]);
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (polarAngle(pivot, arr[j]) <= pivot_angle) {
            i++;
            swap(&arr[i], &arr[j]);
        }
    }
    swap(&arr[i + 1], &arr[high]);
    return i + 1;
}

void quick_sort(Points arr[], int n, Points pivot) {
    Range stack[MAX];
    int top = -1;

    // Push initial range
    stack[++top] = (Range){0, n - 1};

    while (top >= 0) {
        // Pop range
        Range current = stack[top--];
        int low = current.low;
        int high = current.high;

        if (low < high) {
            int pi = partition(arr, low, high, pivot);

            // Push subranges
            if (pi - 1 > low)
                stack[++top] = (Range){low, pi - 1};

            if (pi + 1 < high)
                stack[++top] = (Range){pi + 1, high};
        }
    }
}