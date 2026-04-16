#include <stdio.h>
#include <string.h>
#include "stack.h"
#include "qsort.h"
#include "graham2.c"

typedef char String30[31];

int main()
{
  FILE *fp;
  FILE *out_fp;
  int n, i, j;
  String30 Filename;
  strcpy(Filename,"INPUT");

  do{
   printf("Enter a number from 1-10: ");
   scanf("%d", &j);

  }while(j<1 || j>10);

  sprintf(Filename + strlen(Filename), "%d.txt", j);

  //Open file
  fp = fopen(Filename, "r");
  if(fp == NULL)
  {
     printf("File does not exist\n");
     return 1;
  }

  //Reading the file
  if(fscanf(fp, "%d", &n) != 1 || n > MAX)
  {
     fprintf(stderr, "Error, beyond MAX points\n");
     fclose(fp);
     return 1;
  }
  Points points[MAX];

  for(i=0; i<n; i++)
  {
     if(fscanf(fp, "%lf %lf", &points[i].x, &points[i].y) != 2)
     {
          fprintf(stderr, "Error reading point %d\n", i+1);
          fclose(fp);
          return 1;
     }
  }

  fclose(fp);
  //Create output file
  String30 output;
  
  sprintf(output, "OUTPUT%d.txt", j);
  out_fp = fopen(output, "w");
  if(out_fp == NULL)
  {
   printf("Error creating output file\n");
   return 1;
  }
  //Call Graham Scan
  graham_scan(points, n, out_fp);
  //Close file 
  fclose(out_fp);
    return 0;
}