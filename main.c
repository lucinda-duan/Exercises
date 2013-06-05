#include<stdio.h>

int compare_max(int *p, int n)
{
int curMax, i;
if (n <= 0)
return -1;
curMax = p[0];
for (i = 1; i < n; i++) {
if (p[i] > curMax) {
curMax = p[i];
}
}
return curMax;
}

int main(void)
{
     int arr[4]={1,2,4,3};

     int k=compare_max(&arr[0],4);

     printf("The max number is %d\n",k);

     return 0;
}
