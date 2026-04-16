#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "Graphs.c"


int main()
{ 
  int retVal = 0;
  Str100 filename;
  Str100 outputfile = "";
  int idx;
  Str8 Vertex;    
  printf("Enter filename: ");
  scanf("%s", filename);

  Graph GDS;
  
  retVal = ReadInputFile(filename, &GDS);
  
  
  if(retVal != 0){
    
    Output1(outputfile, &GDS);
	  Output2(outputfile, &GDS);
    Output3(outputfile, &GDS, GDS.nVertices);
    Output4(outputfile, &GDS);
    printf("Input start vertex for the traversal: ");
    scanf("%s", Vertex);
    idx = VertexIndex(&GDS, Vertex);
	  if (idx == -1 ){
      printf("Vertex %s not found.\n", Vertex);
    } else {
      BFS(outputfile, &GDS, GDS.nVertices, idx);
      DFS(outputfile, &GDS, GDS.nVertices, idx);
    }
  } 

 return 0;
}