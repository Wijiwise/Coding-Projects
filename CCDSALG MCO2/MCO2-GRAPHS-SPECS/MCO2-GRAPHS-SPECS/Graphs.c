#ifndef GRAPHS_C
#define GRAPHS_C
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "Graphs.h"
/*
Programmer: CARLOS, LUIS GUILLER ALBERTO M.
This function will find the index of a vertex 
in an adjacency list and return that index
otherwise return -1

Graph *GDS - points to the adjacency list to be searched
char *name - points to the name of the vertex were looking for

*/
int VertexIndex(Graph *GDS, char *name){
    int i;
    for(i=0; i<GDS->nVertices; i++){
        if(strcmp(GDS->VertexNames[i], name) == 0)
        {
            return i; //Index Found
        }
    }

    return -1;//Not Found
}

/*
Programmer: CARLOS, LUIS GUILLER ALBERTO M.
This function will add a new node to a vertex 
in the adjacency list 
Graph *GDS - points to the adj list to be added to
int Vidx  - index of the vertex where the node will be added
char *label - points to the name of the node to be added

*/
void AddEdge(Graph *GDS, int Vidx, char *label){
    Node *newNode = (Node *)malloc(sizeof(Node));
    strcpy(newNode->Vertex, label);
    newNode->next = NULL;

    if (GDS->AdjList[Vidx] == NULL) {
        GDS->AdjList[Vidx] = newNode;
    } else {
        Node *temp = GDS->AdjList[Vidx];
        while (temp->next != NULL) {
            temp = temp->next;
        }
        temp->next = newNode;
    }
}
/*
Programmer: CARLOS, LUIS GUILLER ALBERTO M.
This function will read a file, get the number of vertices
create a graph using a adjacency list interpretation
and returns 1 if graph was successful with the filename as its name
otherwise 0.

const char *filename  - will be the file to read
Graph *GDS  - points to the graph to store the information

*/
int ReadInputFile(const char *filename, Graph *GDS){
    
    FILE *fp = fopen(filename, "r"); 

  if(!fp){
    printf("File %s not found.\n", filename);
    return 0;
  } 
    strcpy(GDS->SourceFileName, filename); 
    int i, j;
    fscanf(fp, "%d", &GDS->nVertices);

    GDS->AdjList = (Node**)malloc(sizeof(Node*) * GDS->nVertices);
    for (i = 0; i < GDS->nVertices; i++)
        GDS->AdjList[i] = NULL;

    // To flush newline after nVertices (in case fscanf left it)
    fgetc(fp);

    for (j = 0; j < GDS->nVertices; j++) {
        char buffer[100];
        if (!fgets(buffer, sizeof(buffer), fp)) break;
        if (strlen(buffer) < 2) { j--; continue; }

        char* token = strtok(buffer, " \n");
        if (!token) continue;

        strcpy(GDS->VertexNames[j], token);
        int currentIndex = j;

        token = strtok(NULL, " \n");
        while (token && strcmp(token, "-1") != 0) {
            AddEdge(GDS, currentIndex, token);
            token = strtok(NULL, " \n");
        }
    }

    fclose(fp);
    return 1; //Success
}

//Helper functions
//For output1 and output2

/* Programmer: RUIZ, JOSEPH BENJAMIN P.
This function compares the name of the vertices

@param - const void *a - name of the first vertex
@param - const void *b - name of the second vertex
@return - returns 0 if the vertices are of the same name
*/
int compareStr8(const void *a, const void *b) {
    return strcmp(*(const Str8*)a, *(const Str8*)b);
}

//For output1
/* Programmer: RUIZ, JOSEPH BENJAMIN P.
This function compares two edges based on their start and end vertices.

@param - const void *a - name of the first vertex
@param - const void *b - name of the second vertex
@return - returns 0 if the vertices are of the same name,
          a negative value if the first edge is less than the second,
          and a positive value if the first edge is greater than the second.
*/
int compareEdges(const void *a, const void *b) {
    const Edge *ea = (const Edge*)a;
    const Edge *eb = (const Edge*)b;

    int cmp = strcmp(ea->start, eb->start);
    if (cmp != 0) return cmp;
    return strcmp(ea->end, eb->end);
}

//Output functions

/* Programmer: RUIZ, JOSEPH BENJAMIN P.
This function outputs the vertices and edges of a graph and creates an output file "*GDS-SET.TXT".

@param - Str100 outputfile - the name of the output file to be created
@param - Graph *GDS - pointer to the graph data structure containing vertices and edges

*/
void Output1(Str100 outputfile, Graph *GDS) {
    int i, eCount = 0;
    Str8 sortedVertices[MAX_Nodes];     // Array to store and sort vertex names
    Edge edges[MAX_Nodes * MAX_Nodes];  // Array to store and sort edges
    char baseName[100];                 // Base name for the output file

    // Copy vertex names for sorting
    for (i = 0; i < GDS->nVertices; i++) {
        strcpy(sortedVertices[i], GDS->VertexNames[i]);
    }
    qsort(sortedVertices, GDS->nVertices, sizeof(Str8), compareStr8);

    // Collect edges with normalization (start < end)
    for (i = 0; i < GDS->nVertices; i++) {
        Node *curr = GDS->AdjList[i];
        while (curr != NULL) {
            int toIdx = VertexIndex(GDS, curr->Vertex);
            if (toIdx == -1) {
                curr = curr->next;
                continue;
            }

            // Normalize direction to ensure (A, B) not (B, A)
            if (strcmp(GDS->VertexNames[i], curr->Vertex) < 0) {
                strcpy(edges[eCount].start, GDS->VertexNames[i]);
                strcpy(edges[eCount].end, curr->Vertex);
                eCount++;
            }

            curr = curr->next;
        }
    }

    qsort(edges, eCount, sizeof(Edge), compareEdges);

    // Derive output filename
    char *dot = strrchr(GDS->SourceFileName, '.');
    if (dot != NULL) {
        size_t baseLen = dot - GDS->SourceFileName;
        strncpy(baseName, GDS->SourceFileName, baseLen);
        baseName[baseLen] = '\0';
        snprintf(outputfile, 100, "%s-SET.TXT", baseName);
    } else {
        strcpy(baseName, GDS->SourceFileName);
        snprintf(outputfile, 100, "%s-SET.TXT", baseName);
    }

    FILE *fp = fopen(outputfile, "w");
    if (!fp) {
        printf("Error: Could not open file %s for writing.\n", outputfile);
        return;
    }

    // Output V(G)
    fprintf(fp, "V(%s)={", baseName);
    for (i = 0; i < GDS->nVertices; i++) {
        fprintf(fp, "%s", sortedVertices[i]);
        if (i < GDS->nVertices - 1) fprintf(fp, ",");
    }
    fprintf(fp, "}\n");

    // Output E(G)
    fprintf(fp, "E(%s)={", baseName);
    for (i = 0; i < eCount; i++) {
        fprintf(fp, "(%s,%s)", edges[i].start, edges[i].end);
        if (i < eCount - 1) fprintf(fp, ",");
    }
    fprintf(fp, "}");

    fclose(fp);
}


/* Programmer: CHUA, KEION 
This function outputs the vertices its corresponding degree for each vertex and creates and output file "*GDS-DEGREE.TXT".

@param - Str100 outputfile - the name of the output file to be created
@param - Graph *GDS - pointer to the graph data structure containing vertices and edges

*/
void Output2(Str100 outputfile, Graph *GDS)
{
	
	int Degrees[MAX_Nodes] = {0}; //array to store the degrees of each vertex
	Str8 sortedVertices[MAX_Nodes]; //array to store and sort vertex names
	
	for(int i = 0; i < GDS->nVertices; i++)
	{
		Node *current = GDS->AdjList[i];
		
		while(current != NULL)
		{
			int nextIdx = VertexIndex(GDS, current->Vertex);
			
			if(nextIdx != -1)
			{
				Degrees[i]++;
			}
			current = current->next;
		}
	}
	
	for(int i = 0; i < GDS->nVertices; i++)
	{
		strcpy(sortedVertices[i],GDS->VertexNames[i]);
	}
	
	qsort(sortedVertices, GDS->nVertices, sizeof(Str8), compareStr8);
	
	
	char *dot = strrchr(GDS->SourceFileName, '.');
    if (dot != NULL) 
	{
        size_t baseLen = dot - GDS->SourceFileName;
        strncpy(outputfile, GDS->SourceFileName, baseLen);
        outputfile[baseLen] = '\0';
        strcat(outputfile, "-DEGREE.TXT");
    } 
	else 
	{
        // If no extension found, just append
        snprintf(outputfile, 100, "%s-DEGREE.TXT", GDS->SourceFileName);
    }
	
	FILE* fp = fopen(outputfile, "w");
	
	if(fp == NULL)
	{
		printf("Error, cannot open file %s.", outputfile);
		return;
	}
	//Output vertices and degrees
	for(int i = 0; i < GDS->nVertices; i++)
	{
		int index = VertexIndex(GDS, sortedVertices[i]);
		fprintf(fp, "%s %d\n", sortedVertices[i], Degrees[index]);

	}
	
	fclose(fp);
}



/*
Programmer: CARLOS, LUIS GUILLER ALBERTO M.
This function generates a file name based on the original source file name
  of the graph (stored in `GDS->SourceFileName`) by replacing or appending
  "-LIST.TXT". It then writes the graph’s adjacency list representation to 
 that file. Each vertex and its adjacent vertices are printed in the format:

 @param outputfile A string buffer where the generated output file name 
                    will be stored (should have space for at least 100 characters).
 @param GDS A pointer to the Graph data structure containing the graph 
            to be written.
 @param numVertices The number of vertices in the graph.
 

*/
void Output3(Str100 outputfile, Graph *GDS, int numVertices){
 // Derive the base name from GDS->SourceFileName
    char *dot = strrchr(GDS->SourceFileName, '.');
    if (dot != NULL) {
        size_t baseLen = dot - GDS->SourceFileName;
        strncpy(outputfile, GDS->SourceFileName, baseLen);
        outputfile[baseLen] = '\0';
        strcat(outputfile, "-LIST.TXT");
    } else {
        // If no extension found, just append
        snprintf(outputfile, 100, "%s-LIST.TXT", GDS->SourceFileName);
    }

    // Open the derived output file for writing
    FILE *fp = fopen(outputfile, "w");
    if (!fp) {
        printf("Error: Could not open file %s for writing.\n", outputfile);
        
    }

    for (int i = 0; i < numVertices; i++) {
        fprintf(fp, "%s", GDS->VertexNames[i]);

        Node *curr = GDS->AdjList[i];
        while (curr != NULL) {
            fprintf(fp, "->%s", curr->Vertex);
            curr = curr->next;
        }

        fprintf(fp, "->\\\n");
    }

    fclose(fp);
}


/*Programmer: CHUA, KEION 
 This function outputs the vertices a adjacency matrix of the graph "*GDS-MATRIX.TXT".

@param - Str100 outputfile - the name of the output file to be created
@param - Graph *GDS - pointer to the graph data structure containing vertices and edges

*/
void Output4(Str100 outputfile, Graph *GDS)
{
	int Matrix[MAX_Nodes][MAX_Nodes] = {0};// array to store the contents of the matrix
		
	for(int i = 0; i < GDS->nVertices; i++)
	{
		Node *current = GDS->AdjList[i];
		
		while(current != NULL)
		{
			int Idx = VertexIndex(GDS, current->Vertex);
			
			if(Idx != -1)
			{
				Matrix[i][Idx] = 1;
			}
			current = current->next;
		}
	}
	
	
	char *dot = strrchr(GDS->SourceFileName, '.');
    if (dot != NULL) 
	{
        size_t baseLen = dot - GDS->SourceFileName;
        strncpy(outputfile, GDS->SourceFileName, baseLen);
        outputfile[baseLen] = '\0';
        strcat(outputfile, "-MATRIX.TXT");
    } 
	else 
	{
        // If no extension found, just append
        snprintf(outputfile, 100, "%s-MATRIX.TXT", GDS->SourceFileName);
    }
	
	FILE* fp = fopen(outputfile, "w");
	
	if(fp == NULL)
	{
		printf("Error, cannot open file %s.", outputfile);
		return;
	}
	//Output matrix
	
	fprintf(fp, "        "); // 8 spaces for alignment
    for (int i = 0; i < GDS->nVertices; i++) 
	{
        fprintf(fp, "%s\t", GDS->VertexNames[i]);
    }
    
	fprintf(fp, "\n");
    
    
	for(int i = 0; i < GDS->nVertices; i++)
	{
		fprintf(fp, "%s", GDS->VertexNames[i]);
		
		for(int j = 0; j < GDS->nVertices; j++)
		{
			fprintf(fp,"\t%d", Matrix[i][j]);
		}
		fprintf(fp, "\n");
	}
	
	fclose(fp);

}

/*
This function performs a Breadth First Search (BFS) and creates an output file "*GDS-BFS.TXT".
It first checks if the entered vertex exists in the graph. If it does, it begins the BFS traversal.
It visits vertices in lexicographical order when multiple unvisited neighbors are found.

@param - Str100 outputfile  - Output filename to be generated and written to.
@param - Graph *GDS         - Pointer to the Graph Data Structure to perform BFS on.
@param - int numVertices    - Number of vertices in the graph.
@param - Str8 Vertex        - Starting vertex for the traversal.
Programmer: RUIZ, JOSEPH BENJAMIN P.
*/
void BFS(Str100 outputfile, Graph *GDS, int numVertices, int startIdx) {
        int visited[MAX_Nodes] = {0};
    int queue[MAX_Nodes], front = 0, rear = 0;
    int i;

    char baseName[100];
    strcpy(baseName, GDS->SourceFileName);
    char *dot = strrchr(baseName, '.');
    if (dot) *dot = '\0';

    snprintf(outputfile, 101, "%s-BFS.TXT", baseName);

    FILE *fp = fopen(outputfile, "w");
    if (!fp) {
        printf("Could not open %s for writing.\n", outputfile);
        return;
    }

    visited[startIdx] = 1;
    queue[rear++] = startIdx;

    while (front < rear) {
        int curr = queue[front++];
        fprintf(fp, "%s ", GDS->VertexNames[curr]);

        char *neighbors[MAX_Nodes];
        int neighborIdx[MAX_Nodes];
        int count = 0;

        Node *adj = GDS->AdjList[curr];
        while (adj != NULL) {
            int idx = VertexIndex(GDS, adj->Vertex);
            if (idx != -1 && !visited[idx]) {
                neighbors[count] = GDS->VertexNames[idx];
                neighborIdx[count] = idx;
                count++;
            }
            adj = adj->next;
        }

        for (i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (strcmp(neighbors[i], neighbors[j]) > 0) {
                    char *tmpStr = neighbors[i];
                    neighbors[i] = neighbors[j];
                    neighbors[j] = tmpStr;
                    int tmpIdx = neighborIdx[i];
                    neighborIdx[i] = neighborIdx[j];
                    neighborIdx[j] = tmpIdx;
                }
            }
        }

        for (i = 0; i < count; i++) {
            if (!visited[neighborIdx[i]]) {
                visited[neighborIdx[i]] = 1;
                queue[rear++] = neighborIdx[i];
            }
        }
    }

    fclose(fp);
}

/*
Programmer: CARLOS, LUIS GUILLER ALBERTO M.
Recursive helper function for performing DFS.
 This function visits the vertex at index `idx`, prints its name to
 the file, and recursively visits all its unvisited neighbors.

 @param idx        Index of the current vertex in the graph.
 @param GDS        Pointer to the Graph Data Structure.
 @param fp         Pointer to the output file to write traversal.
 @param visited[]  Array tracking whether a vertex has been visited.


*/
#include <string.h> // Include for strcmp

void dfsHelper(int idx, Graph *GDS, FILE *fp, int visited[]) {
    visited[idx] = 1;
    fprintf(fp, "%s ", GDS->VertexNames[idx]);

    Node *adj = GDS->AdjList[idx];
    char *neighbors[MAX_Nodes];
    int count = 0;

    // Collect neighbors
    while (adj != NULL) {
        int neighborIndex = VertexIndex(GDS, adj->Vertex);
        if (neighborIndex != -1 && !visited[neighborIndex]) {
            neighbors[count++] = GDS->VertexNames[neighborIndex];
        }
        adj = adj->next;
    }

    // Sort neighbors lexicographically
    qsort(neighbors, count, sizeof(char*), (int (*)(const void*, const void*))strcmp);

    // Visit sorted neighbors
    for (int i = 0; i < count; i++) {
        int neighborIndex = VertexIndex(GDS, neighbors[i]);
        if (!visited[neighborIndex]) {
            dfsHelper(neighborIndex, GDS, fp, visited);
        }
    }
}

/*
Programmer: CARLOS, LUIS GUILLER ALBERTO M.
This function prepares the visited array and output file, then
calls the recursive dfsHelper to perform the DFS traversal.

@param - Str100 outputfile  - outputfile to be created
@param - Grapg *GDS  - Points to the Graph Data Structure to perform DFS
@param - int numVertices - number of vertces in the graph
@param - Str8 Vertex  - Starting vertex

*/
void DFS(Str100 outputfile, Graph *GDS,int numVertices, int startIdx)
{    
    int visited[MAX_Nodes] = {0};  // Track visited vertices

    // Derive output filename: base name + "-DFS.txt"
    char baseName[100];
    strcpy(baseName, GDS->SourceFileName);
    char *dot = strrchr(baseName, '.');
    if (dot) *dot = '\0';  // Remove extension

    snprintf(outputfile, 100, "%s-DFS.txt", baseName);

    FILE *fp = fopen(outputfile, "w");
    if (!fp) {
        printf("Error: Could not open file %s for writing.\n", outputfile);
        return;
    }

        dfsHelper(startIdx, GDS, fp, visited);
    

 
    fclose(fp);
}

#endif