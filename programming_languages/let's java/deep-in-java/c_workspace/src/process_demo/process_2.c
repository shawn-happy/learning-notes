#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main()
{
    fork();
    fork();
    fork();
    printf("hello world!\n");
    return 0;
}
/*
output: 
hello world!
hello world!
hello world!                                                                                                                                                
hello world!
hello world!
hello world!
hello world!
hello world!
 */