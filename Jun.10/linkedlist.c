//Given a singly-linked list, find the 2nd to last element of the list, the last element is position 0
#include<stdio.h>

struct Node
{
   int data;
   struct Node* next;
};

struct Node* find(struct Node* head)
{
    if(head==NULL) return NULL;
    
    else 
       
       {
           struct Node* tmp;
           int i;
           tmp=head;
           for(i=0;i<2;i++)  
           { 
             if (head->next) head=head->next;
             else return NULL;
           }

           while(head->next!=NULL)
           {
              head=head->next;
              tmp=tmp->next;
           }
           return tmp;
       }

}




int main()
{
   struct Node* result;
   struct Node elem1,elem2,elem3,elem4;
   elem1.data=1;
   elem2.data=2;
   elem3.data=3; 
   elem4.data=4;
   elem1.next=&elem2;
   elem2.next=&elem3;
   elem3.next=&elem4;
   elem4.next=NULL;

   result=find(&elem1);
   if(result!=NULL)  printf("%d\n",result->data);
   else printf("No result\n");


}

