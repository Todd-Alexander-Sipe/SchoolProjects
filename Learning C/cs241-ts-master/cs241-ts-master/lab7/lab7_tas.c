//author: Todd Sipe
//Lab 7
//Dates worked: July 13 2020 - July 17 2020
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

#define LINE 1024

// Modified struct to be set-up for a linked list implementation
typedef struct country_capital {
	char country[100];
	char capital[100];
	float cap_lat;
	float cap_long;
	char code[3]; // Make code at least 3 characters long to avoid weird bug
	char continent[50];
	struct country_capital *next_pointer;
} cc;//END linked list implementation

// This function will delete the appropriate countries and return head, which is a pointer to the first struct in the list
cc * delete_some(cc *head, cc *temp) {
	
	// code from example
	while (temp != NULL && ((temp->cap_lat > 0)||(temp->cap_long > 0)) ) {
		head = temp->next_pointer;
		free(temp);
		temp = head;
	}//END while
	
	cc *previous;
	while (temp != NULL) {
		while (temp != NULL && ((temp->cap_lat < 0)&&(temp->cap_long < 0)) ) {
			previous = temp;
			temp = temp->next_pointer;
		}//END while
	
		// We need to check for NULL in case the inner while loop iterated to the end of the list
		if (temp != NULL) {
			previous->next_pointer = temp->next_pointer;
			free(temp);
			temp = previous->next_pointer;
		}//END if
	}//END while
	
	return head;
	
}//END function delete_some

void main() {
	
	char *this_token;
	cc *head;
	cc *temp;
	cc *current;

	FILE *f;
	f = fopen("country-capitals.csv", "r");

	if (f == NULL) {
		printf("Try again \n");
		return;
	}//END if

	char my_string[LINE];
	int counter = 0;
	int token_count;

	while(fgets(my_string, LINE, f) != NULL) {

		current  = (cc *) malloc(sizeof(cc));
		this_token = strtok(my_string, ",");
		token_count = 0;

		while (this_token != NULL) {
			if (token_count== 0) {
				strcpy(current->country, this_token);
			} else if (token_count == 1) {
				strcpy(current->capital, this_token);
			} else if (token_count == 2) {
				current->cap_lat = (float) atof(this_token);
			} else if (token_count == 3) {
				current->cap_long = (float) atof(this_token);
			} else if (token_count == 4) {
				strcpy(current->code, this_token);
			} else {
				strcpy(current->continent, this_token);
			}//END if/else statements
			
			token_count = token_count + 1; 
			this_token = strtok(NULL, ",");
		}//END while

		if (counter == 0) {
			head = current;
			temp = current;
		} else {
			temp->next_pointer = current;
			temp = current;
		}//END if/else

		counter = counter + 1;
		
	} // end outer while loop

	temp->next_pointer = NULL;
	temp = head;
	
	printf("\n");
	printf("Before delete_some:\n");
	while(temp != NULL) {
		if (temp->next_pointer != NULL) {
			printf("%s, ", temp->country);
			temp = temp->next_pointer;

		} else {
			printf("%s", temp->country);
			temp = temp->next_pointer;
		}//END if/else
	}//END while
	
	temp = head;
	
	head = delete_some(head, temp);
	
	temp = head;
	
	printf("\n");
	printf("\n");
	printf("After delete_some:\n");
	while(temp != NULL) {
		if (temp->next_pointer != NULL) {
			printf("%s, ", temp->country);
			temp = temp->next_pointer;

		} else {
			printf("%s", temp->country);
			temp = temp->next_pointer;
		}//END if/else
	}//END while
	
	printf("\n\n");
	
}//END function main
