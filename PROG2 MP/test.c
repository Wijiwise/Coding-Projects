 #include <stdio.h>
 #include <string.h>
 #include <stdlib.h>
 #include <time.h>
 #include "helper.h"
 #include "helper.c"

 int main() 
 { 

  PlayerTag players[0];
  int numPlayers = 0;
  SavePlayers("abc.txt", players, &numPlayers);

 return  0; 
 } 