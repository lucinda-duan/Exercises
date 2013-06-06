#include<stdio.h>
#include<stdlib.h>

struct Node{
    
        int data;
        struct Node* next;
};

struct Node* find_delete(struct Node* head, int num)
{
    struct Node* tmp1=head;
    struct Node* tmp2=head;
    if(head->data==num)   //Special:when first element to be deleted
    {
      tmp1=head->next;
    }
  


    while(head!=NULL && head->data!=num)
   {
       tmp2=head;
       head=head->next;
 
   }
   if(head==NULL) printf("Not found!\n");
   if(head->data==num)
   {
     tmp2->next=head->next;
     
   } 
  
  printf("After deletion:\n");
  if(tmp1==NULL) 
   printf("No elements!\n");
   while(tmp1!=NULL)
       {
         printf("%d\n",tmp1->data);
         tmp1=tmp1->next;
       } 
    
}
void insert_front(struct Node** pt, int x)
{   
    int i,n;
    for(i=0;i<x;i++)
   {
    struct Node* A=(struct Node*)malloc(sizeof(struct Node));
    printf("insert:\n");
    scanf("%d",&n);
    A->data=n;
    A->next=*pt;
    *pt=A;
   }
}
int main()
{
      struct Node* p1=NULL;
      int x,y;
      printf("Insert how many elements:\n");
      scanf("%d",&x);
      insert_front(&p1,x);
      struct Node* p2=p1;
      printf("New list:\n");
      while(p1!=NULL)
       {
         printf("%d\n",p1->data);
         p1=p1->next;
       }
     if(p2!=NULL)
      {
       printf("Find an element to delete:\n");
       scanf("%d",&y);
       find_delete(p2,y);
      }  
      
      return 0;
}
