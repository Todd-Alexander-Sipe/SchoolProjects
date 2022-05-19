//Author: Todd Sipe
//CS 241 - Lab 2
//Dates worked: June 8-12, 2020
#include <stdio.h>
#include <string.h>
#define MAXLINE 1000

void countAndVowel(char line[], int count)
{
	int i;
	int vowels;
	vowels = 0;
	printf("The number of characters in the above line is %d.\n", count);
	for (i = 0; i < count; i++){
		if (line[i] == 'a'|| line[i] == 'e'|| line[i] == 'i'|| line[i] == 'o'|| line[i] == 'u'|| line[i] == 'A'|| line[i] == 'E'|| line[i] == 'I'|| line[i] == 'O'|| line[i] == 'U')
			vowels++;
	}
	printf("The number of vowels in the above line is %d.\n", vowels);
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
		countAndVowel(s, size);
	}while(size > 0);
	return 0;
}
