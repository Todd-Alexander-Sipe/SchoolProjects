//Author: Todd Sipe
//Project: Lab 3 part 3
//Dates worked: June 15 - June 19, 2020
#include <stdio.h>
#include <stdbool.h>

bool pattern(unsigned num){
	unsigned temp = num % 2;
	
	num = num / 2;
	while (num > 0){
		unsigned current = num % 2;
		if (current == temp)
			return false;
		temp = current;
		num = num / 2;
	}
	return true;
}

void main(){
	unsigned entry = 2863311531;
	
	if (pattern(entry))
		printf("Alternating pattern.\n");
	else
		printf("Not alternating pattern.\n");
}
