#include<stdio.h>
struct Node
{
     int data;
     struct Node* next;

};

struct Node *head;
struct Node *tail;
//bool remove(struct Node  *elem);

int insertAfter(struct Node *elem, int data)
{
   struct Node* my;
   my->data=data;
   if(elem==head)
   {
     my->next=head;
     head=my;
     return 1;
   }
   else if (elem==tail)
   {

     tail->next=my;
     my->next=NULL;
     tail=my;
     return 1;
   }
   else
   {
     my->next=elem->next;
     elem->next=my;
     return 1;
   }
   return 0;
}

int main()
{
   struct Node elem_1,elem_2,elem_3;
   struct Node *pt;
   elem_1.data=1;
   elem_2.data=2;
   elem_3.data=3;
   elem_1.next=&elem_2;
   elem_2.next=&elem_3;
   elem_3.next=NULL;
   head=&elem_1;
   tail=&elem_3;
   int t=insertAfter(&elem_2,4);
//   printf("%d\n",t);
   pt=head;
   while(pt!=NULL)
   {
     printf("%d\n",pt->data);
     pt=pt->next;
   }
  return 0;
}

