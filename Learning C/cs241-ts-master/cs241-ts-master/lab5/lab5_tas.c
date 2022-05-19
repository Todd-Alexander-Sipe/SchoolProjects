//Author: Todd Sipe
//Lab 5
//Dates worked: 6/29/2020 - 7/3/2020
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

#define TOTAL 196 // This number refers to the total number of countries in the csv file
#define LINE_SIZE 1024 // Max line size variable

struct cc {
	char country[100];
	char capital[100];
	float cap_lat;
	float cap_long;
	char code[3]; // Make code at least 3 characters long to avoid weird bug
	char continent[50];
};

void northern_most_capital(struct cc data[TOTAL]) {
	float current;
	float highest = 0;
	char cap[100];
	
	for(int i = 0; i < TOTAL; i++){
		current = data[i].cap_lat;
		if (current > highest){
			highest = current;
			strcpy(cap, data[i].capital);
		}//END if
	}//END for
	printf("The Northern-most capital is %s at %f latitude.\n", cap, highest);
} //END function nothern_most_capital

void southern_most_capital(struct cc data[TOTAL]) {
	float current;
	float lowest = 0;
	char cap[100];
	
	for(int i = 0; i < TOTAL; i++){
		current = data[i].cap_lat;
		if (current < lowest){
			lowest = current;
			strcpy(cap, data[i].capital);
		}//END if
	}//END for
	printf("The Southern-most capital is %s at %f latitude.\n", cap, lowest);
} //END function southern_most_capital

void closest_to_zero_long(struct cc data[TOTAL]) {
	float current;
	float smallestValue = 50;
	char cap[100];
	
	for(int i = 0; i < TOTAL; i++){
		current = fabs(data[i].cap_long);
		if (current < smallestValue){
			smallestValue = current;
			strcpy(cap, data[i].capital);
		}//END if
	}//END for
	printf("The capital that is closest to 0 longitude is %s at %f longitude.\n", cap, smallestValue);
} //END function closest_to_zero_long


void begins_with_k(struct cc data[TOTAL]) {
	char cap[100];
	char ctry[100];
	
	for(int i = 0; i < TOTAL; i++){
		strcpy(cap, data[i].capital);
		if (cap[0] == 'K')
			printf("%s has a capital that starts with 'K'\n", strcpy(ctry, data[i].country));
	}//END for
} // end begins_with_k


void shared_name(struct cc data[TOTAL]) {
	char cap[100];
	char *sharedNameToken;
	int compare;
	
	for(int i = 0; i < TOTAL; i++){
		strcpy(cap, data[i].capital);
		sharedNameToken = strtok(cap, " ");
		while(sharedNameToken != NULL){
			compare = strcmp(sharedNameToken, data[i].country);
			if(compare == 0){
				printf("The country %s has a capital that shares it's name: %s.\n", data[i].country, data[i].capital);
			}//END if
			sharedNameToken = strtok(NULL, " ");
		}//END while
	}//END for
} //END function shared_name


void main() {

	struct cc array[TOTAL];
	
	char *this_token;

	FILE *f;
	f = fopen("country-capitals.csv", "r");

	if (f == NULL) {
		printf("Try again \n");
		return;
	}//END if

	char my_string[LINE_SIZE];
	int token_counter; // count for token
	int position = 0; //array position

	while(fgets(my_string, LINE_SIZE, f) != NULL) {
		token_counter = 0;

		this_token = strtok(my_string, ",");

		while (this_token != NULL) {
	
			if (token_counter == 0) {
				strcpy(array[position].country, this_token);
			} else if (token_counter == 1) {
				strcpy(array[position].capital, this_token);
			} else if (token_counter == 2) {
				array[position].cap_lat = (float) atof(this_token);
			} else if (token_counter == 3) {
				array[position].cap_long = (float) atof(this_token);
			} else if (token_counter == 4) {
				strcpy(array[position].code, this_token);
			} else {
				strcpy(array[position].continent, this_token);
			}//END if/else statements


			token_counter = token_counter + 1;
			
			this_token = strtok(NULL, ",");
		}//END while
		
		position = position + 1;
	}//END while

	northern_most_capital(array);
	southern_most_capital(array);
	closest_to_zero_long(array);
	begins_with_k(array);
	shared_name(array);
}//END function main
