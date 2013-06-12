#include<stdio.h>

struct Node
{
  struct Node *prev;
  struct Node *next;
  struct Node *child;
  int data;
};
void diff_level(struct Node *current)
{
   // while(current)
 // {
    if (current->child!=NULL)
      {
             current->child->prev->next=NULL;
             current->child->prev=NULL;
             diff_level(current->child);

      }
  //  current=current->next;
 // }
}

void unflatten(struct Node *head, struct Node **tail)
{
   struct Node *tmp, *current=head;
   while(current)
   {
     diff_level(current);
     current=current->next;

   }
     for(tmp=head;tmp->next;tmp=tmp->next);

     *tail=tmp;
}


int main()
{
  struct Node *head, *tail,*tmp;
  struct Node elem1,elem2,elem3,elem4,elem5,elem6,elem7,elem8,elem9,elem10,elem11;
  elem1.data=5;elem1.next=&elem2;elem1.prev=NULL;elem1.child=&elem6;
  elem2.data=33;elem2.next=&elem3;elem2.prev=&elem1;elem2.child=NULL;
  elem3.data=17;elem3.next=&elem4;elem3.prev=&elem2;elem3.child=NULL;
  elem4.data=2;elem4.next=&elem5;elem4.prev=&elem3;elem4.child=&elem9;
  elem5.data=1;elem5.next=&elem6;elem5.prev=&elem4;elem5.child=NULL;
  elem6.data=6;elem6.next=&elem7;elem6.prev=&elem5;elem6.child=NULL;
  elem7.data=25;elem7.next=&elem8;elem7.prev=&elem6;elem7.child=&elem11;
  elem8.data=6;elem8.next=&elem9;elem8.prev=&elem7;elem8.child=NULL;
  elem9.data=2;elem9.next=&elem10;elem9.prev=&elem8;elem9.child=NULL;
  elem10.data=7;elem10.next=&elem11;elem10.prev=&elem9;elem10.child=NULL;
  elem11.data=8;elem11.next=NULL;elem11.prev=&elem10;elem11.child=NULL;
  head=&elem1; tail=&elem11;
  unflatten(head,&tail);
  tmp=head;
  while(tmp)
   { printf("%d\n",tmp->data);
     tmp=tmp->next;
   }

}
