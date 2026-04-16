// qsort.h
#ifndef QSORT_H
#define QSORT_H
#include "point.h"
#define MAX 32768

typedef struct {
    int low;
    int high;
} Range;


//Quick Sort Functions
double polarAngle(Points pivot, Points p);
void swap(Points *a, Points *b);
int partition(Points arr[], int low, int high, Points pivot);
void quick_sort(Points arr[], int n, Points pivot);
#endif