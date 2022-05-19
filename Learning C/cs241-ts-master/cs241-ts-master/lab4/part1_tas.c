//Author: Todd Sipe
//lab 4
//Dates worked: 6/22/2020 - 6/26/2020
#include <stdio.h>
#include "lab4_tas.h"

void main(){
	
	//set the floating point values
	float x1 = 3;
	float y1 = 4;
	float x2 = 5;
	float y2 = 6;
	float x3 = 1;
	float y3 = 2;
	
	//print the values before we run the function to switch the addresses
	printf("Point 1 initially is %f, %f.\n", x1, y1);
	printf("Point 2 initially is %f, %f.\n", x2, y2);
	printf("Point 3 initially is %f, %f.\n", x3, y3);
	
	//run the function to switch point 1>3, 2>1, and 3>2
	switch_points( & x1, & x2, & x3, & y1, & y2, & y3);
	
	//print the values after switching the addresses
	printf("Point 1 after the switch is %f, %f.\n", x1, y1);
	printf("Point 2 after the switch is %f, %f.\n", x2, y2);
	printf("Point 3 after the switch is %f, %f.\n", x3, y3);
}
