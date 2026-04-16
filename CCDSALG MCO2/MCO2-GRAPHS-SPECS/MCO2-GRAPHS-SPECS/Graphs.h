#ifndef GRAPHS_H
#define GRAPHS_H
#include <stdio.h>
#define MAX_Nodes 20

typedef char Str8[9];
typedef char Str100[101];

typedef struct node{
 Str8 Vertex;
 struct node *next;

}Node;

typedef struct {
    Str8 start;
    Str8 end;
} Edge;

typedef struct{
    int nVertices;
    Node** AdjList;
    Str8 VertexNames[MAX_Nodes];
    Str100 SourceFileName;
}Graph;
//Graph Functions
int VertexIndex(Graph *G, char *name);
void AddEdge(Graph *G, int Vidx, char *label);
int ReadInputFile(const char *filename, Graph *GDS);
//helper functions
//For output1
int compareStr8(const void *a, const void *b);
int compareEdges(const void *a, const void *b);
//OUTPUT1-6
void Output1(Str100 outputfile, Graph *GDS);                  //set of vertices V(G) and set of edges E(G) -SET.txt
void Output2(Str100 outputfile, Graph *GDS);				  //list of vertices with their corresponding degree -DEGREE.txt
void Output3(Str100 outputfile, Graph *GDS,int numVertices);  //adjacency List representation of the graph -LIST.txt
void Output4(Str100 outputfile, Graph *GDS);				  //adjacency matrix representation of the graph -MATRIX.txt
//BFS FUNCTION
void BFS(Str100 outputfile, Graph *GDS, int numVertices, int startIdx);  // a list of vertex IDs that correspond to a Breadth First Search (BFS) traversal sequence from a specified 
        //start vertex -BFS.txt
//DFS Functions
void dfsHelper(int idx, Graph *GDS, FILE *fp,int visited[]);
void DFS(Str100 outputfile, Graph *GDS,int numVertices, int startIdx); //a list of vertex IDs that correspond to a Depth First Search (DFS) traversal sequence from a specified 
        //start vertex (note: same start vertex as in BFS traversal) -DFS.txt
/*OUTPUT1-6
void Output2 list of vertex IDs with the corresponding degree for each vertex. -DEGREE.txt
void Output4 Adjacency Matrix representation of the graph -MATRIX.txt
*/

#endif