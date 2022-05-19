//Author: Todd Sipe
//CS 241 - Lab 2
//Dates worked: June 8-12, 2020
#include <stdio.h>
#include <string.h>
#define MAXLINE 1000

void printReverse(char line[], int length){
	int i = 0;
	int j = length - 1;
	char temp;
	
	printf("The above line is greater than 50 characters long.\n");
	while (i < j){
		temp = line[i];
		line[i] = line[j];
		line[j] = temp;
		i++;
		j--;
	}
	printf("%s\n", line);
}

void checkFifty(char line[], int count)
{
	int i;
	if (count <= 50){
		printf("The above line is less than 50 characters long.\n");
	}
	else {
		printReverse(line, count);
	}
}

int main()
{

	int c;
	int i;
	char s[MAXLINE];
	int lim;
	lim = MAXLINE;
	int size;
	printf("Please enter as many sentences as you like, then press Ctrl + C to exit(not Ctrl + D):\n");
	do{
		memset(s, 0, sizeof(s));
		//the following for loop was used from the page 29 example
		for (i = 0; i < lim - 1 && (c = getchar()) != EOF && c != '\n'; ++i)
			s[i] = c;
		size = i;
		checkFifty(s, size);
	}while(size > 0);
	return 0;
}
