//Author: Todd Sipe
//Project: Lab 3 part 2
//Dates worked: June 15 - June 19, 2020
#include <stdio.h>

unsigned and(unsigned num1, unsigned num2){
	return (num1 & num2);
}


unsigned xor(unsigned num1, unsigned num2){
	return (num1 ^ num2);
}

unsigned shift(unsigned num){
	return (num << 1);
}

unsigned add(unsigned num1, unsigned num2){
	unsigned result1;
	unsigned result2;
	
	while (num2 != 0){
		result1 = and(num1, num2);
		result1 = shift(result1);
		result2 = xor(num1, num2);
		num2 = result1;
		num1 = result2;
	}
}

void main(){
	unsigned num1 = 7000;
	unsigned num2 = 5367;
	unsigned answer = and(num1, num2);
	
	printf("%u and %u sent to the 'and' function returns: %u\n", num1, num2, answer);
	answer = xor(num1, num2);
	printf("%u and %u sent to the 'xor' function returns: %u\n", num1, num2, answer);
	answer = shift(num1);
	printf("%u sent to the 'shift' function returns: %u\n", num1, answer);
	answer = add(num1, num2);
	printf("%u and %u sent to the 'add' function returns: %u\n", num1, num2, answer);
}
