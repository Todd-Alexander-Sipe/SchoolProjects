//Author Todd Sipe
//lab 4
//Dates worked: 6/22/2020 - 6/26/2020

//function accepting all six values to switch them around
void switch_points(float *x1, float *x2, float *x3, float *y1, float *y2, float *y3){
	
	//temporary variable to store for our swapping process
	float temp;
	
	//first we swap points 1 and 3
	temp = *x1;
	*x1 = *x3;
	*x3 = temp;
	temp = *y1;
	*y1 = *y3;
	*y3 = temp;
	
	//then we swap points 2 and 3
	temp = *x3;
	*x3 = *x2;
	*x2 = temp;
	temp = *y3;
	*y3 = *y2;
	*y2 = temp;
}
