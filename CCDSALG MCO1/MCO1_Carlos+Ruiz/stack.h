// stack.h
#ifndef STACK_H
#define STACK_H
#include "point.h"
#define MAX 32768


typedef struct {
    Points data[MAX];
    int top;
} Stack;

// Stack operations
void create(Stack *s);// Creates an stack
int is_full(Stack *s);// Checks if stack is full
int is_empty(Stack *s);// Checks if stack is empty
int push(Stack *s, Points elem);// Adds to the top of the stack
int pop(Stack *s, Points *elem);// removes the element on top of the stack
int top(Stack *s, Points *elem);// Checks what elem is on top
int next_to_top(Stack *s, Points *elem);// Checks what elem is below the top

#endif