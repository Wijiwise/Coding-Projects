#include <stdio.h>
#include "stack.h"

void create(Stack *s)
{

    s->top = -1;  //Creates an empty stack
   
}

int is_full(Stack *s)
{
    return s->top == MAX - 1; //Returns 1 when true and 0 if false
}

int is_empty(Stack *s)
{
   return s->top == -1; // returns 1 when true and 0 if false
}

int push(Stack *s, Points elem)
{   
    int flag = 0;  //will be returned at the end
    if(is_full(s)) //checks if stack is full
    {
        printf("\nStack is full.\n");
        flag = -1; //returns -1 if full
    }
    else
    {
      s->data[++(s->top)] = elem; //pre increments the stack then adds the element
    }
    
    return flag;
}

int pop(Stack *s, Points *elem)
{ 
  int flag = 0;
  if(is_empty(s))
  {
     printf("\nStack Underflow\n");
     flag = -1;
  }
  else
  {
    *elem = s->data[(s->top)--];
  }

  return flag;
}

int top(Stack *s, Points *elem)
{  int flag = 0;
   if(is_empty(s))
   {
     printf("\nStack is empty.\n");
     flag = -1;
   }
   else{
    *elem = s->data[s->top];
   }
   return flag;
}

int next_to_top(Stack *s, Points *elem)
{
   int flag = 0;
   if(is_empty(s))
   {
     printf("\nStack is empty.\n");
     flag = -1;
   }
   else{
    *elem = s->data[s->top - 1];
   }
   return flag;
}