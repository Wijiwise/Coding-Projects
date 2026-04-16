#ifndef GRAHAM2_H
#define GRAHAM2_H
#define MAX 32768


//Graham's Scan Functions
int orientation(Points a, Points b, Points c);
void graham_scan(Points point[], int n, FILE *output);
int find_anchor(Points points[], int n);

#endif