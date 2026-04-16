#ifndef UTILS_H
#define UTILS_H

#include "point.h"

int find_anchor(Points arr[], int n);
//for Bubble Sort
int orientation(Points p, Points q, Points r);
double distance_sq(Points a, Points b);
//for Quick Sort
double polarAngle(Points pivot, Points p);
void swap(Points *a, Points *b);
int partition(Points arr[], int low, int high, Points pivot);

#endif
