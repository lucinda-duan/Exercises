#include<stdio.h>
#include<stdlib.h>

struct stack
{
   void* data;
   struct stack* next;
};

void push(struct stack** head, void* x)
{
    struct stack* ele=(struct stack*)malloc(sizeof(struct stack));
    ele->data=x;
    ele->next=*head;
    *head=ele;

}

void* pop(struct stack** head)
{
   void* ele=NULL;
   if(*head!=NULL)
   ele=(*head)->data; 

   return ele;

}

int main()
{
   struct stack* new_stack=(struct stack*)malloc(sizeof(struct stack));
   int t=3;
   push(&new_stack,(void*)&t);
   int i=*(int*)pop(&new_stack); 
   printf("%d\n",i);
   return 0;  
  // while(new_stack!=NULL)
   // { 
        
     //   printf("%d\n",*(int*)new_stack->data);
     //   new_stack=new_stack->next;

  //  }
}
