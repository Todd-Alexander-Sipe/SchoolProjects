//Author: Todd Sipe
//Project: Lab 3 part 1
//Dates worked: June 15 - June 19, 2020
# include <stdio.h>
void mask(unsigned given){
	unsigned maskOp = 255;
	unsigned answer;
	unsigned maskArray[4];
	int i;
	
	for (i = 0; i < 4; i++){
		maskArray[i] = given & maskOp;
		maskOp = maskOp << 8;
	}
	answer = maskArray[0] | maskArray[1] | maskArray[2] | maskArray[3];
	printf("This is the returned unsigned int after it was run through the function mask: %u\n", answer);
}

void main(){
	unsigned firstNum = 789363;
	
	printf("This is the unsigned int that has been sent to the function mask: %u\n", firstNum);
	mask(firstNum);
}
