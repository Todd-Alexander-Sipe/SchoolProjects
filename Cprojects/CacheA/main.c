/*
Name: Todd Sipe
Net ID: tsipe@unm.edu
*/

#include "cachelab.h"
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <getopt.h>
#include <math.h>
#include <strings.h>

/*input parameters*/
int verbose = 0;
int B;  /*calculated after b is brought in*/
int E;  /*number of lines*/
int S;  /*calculated after s is brought in*/
unsigned long long b;  /*block offset bits*/
unsigned long long s;  /*set index bits*/
unsigned long long tag;  /*calculated after b and s are brought in*/

/*printSummary variables*/
int hitCount;
int missCount;
int evictCount;

/*this just counts the hits that have happened (used to store age of every line)*/
int hitCounter = 0;

/*struct for our lines*/
typedef struct line {
  unsigned long long tag;
  unsigned long long set;
  int age;
} line;

/*function to find the oldest line in a set*/
int findOldest(line cache[S][E], unsigned long long set) {
    int least = cache[set][0].age;
    int ret = 0;
    for(int i = 0; i < E; i++) {
            if(least >= cache[set][i].age) {
                least = cache[set][i].age;
                ret = i;
            }
    }
    return ret;
}

/*function to create the mock cache*/
void createCache(line cache[S][E]) {
    line current;
    current.tag = -1;
    current.age = 0;
    for (int i = 0; i < S; i++) {
            for(int j= 0; j < E; j++) {
                cache[i][j] = current;
            }
    }
}

/*function to take in the L, S, or M commands from our trace file (does the work)*/
void simulate(line cache[S][E], unsigned long long address) {
    /*the temporary line that we are working with*/
    line current;
    /*shifting to isolate the tag and the 's' in order to find the proper set*/
    current.tag = address >> (s + b);
    current.set = (address << tag) >> (tag + b);

    int h = 0;  /*boolean for if this was a hit*/
    int emptyElement = 0;  /*boolean for if empty*/
    int emptyElementCount = 0;  /*which of the lines do we look at*/

    /*look for an empty line, look for a matching line*/
    for(int i = 0; i < E; i++) {
        if (cache[current.set][i].tag == -1) {
            emptyElement = 1;
            emptyElementCount = i;
        }
        if (cache[current.set][i].tag == current.tag) {
            hitCount++;
            h = 1;
            hitCounter++;
            cache[current.set][i].age = hitCounter;
        }
    }

    /*for misses*/
    if (h == 0 && emptyElement == 1) {
        cache[current.set][emptyElementCount].tag = current.tag;
        cache[current.set][emptyElementCount].age = hitCounter;
        missCount++;
    }
    /*for miss/evictions*/
    if(h == 0 && emptyElement == 0) {
      int line = findOldest(cache, current.set);
      cache[current.set][line].tag = current.tag;
      cache[current.set][line].age = hitCounter;
      missCount++;
      evictCount++;
    }
}

int main(int argc, char *argv[]) {
    /*file reading nonsense... (c is ridiculous)*/
    FILE *readTraceFile;
    char traceCommand;
    char *traceFile;
    char c;
    /*address is 64 bits*/
    unsigned long long address;
    int size;

    /*command line arguments reader nonsense...*/
    while( (c=getopt(argc,argv,"s:E:b:t:vh")) != -1) {
        switch(c) {
        case 's':
            s = atoi(optarg);
            break;
        case 'E':
            E = atoi(optarg);
            break;
        case 'b':
            b = atoi(optarg);
            break;
        case 't':
            traceFile = optarg;
            break;
        case 'v':
            verbose = 1;
            break;
        case 'h':
            printf("Please type the in the following format:/n");
            printf("./csim-ref -s 4 -E 1 -b 4 -t traces/yi.trace\n");
            printf("In which s, E, b are all the appropriate numbers for cache size, t is a trace file, and -v and -h for verbose and help.");
            exit(0);
        default:
            printf("Please type the correct usage.\n");
            exit(1);
        }
    }

    /*calculate B and S*/
    B = pow(2.0, b);
    S = pow(2.0, s);
    /*calculate tag (all of our addresses should be 64 bits)*/
    tag = 64 - b - s;

    /*create the 2-d array of lines, and mock up cache*/
    line cache[S][E];
    createCache(cache);

    /*MORE file reading nonsense*/
    readTraceFile = fopen(traceFile, "r");

    /*switch statements for I, L, S, and M. M does two operations, so it is called twice.*/
    if (readTraceFile != NULL) {
        while (fscanf(readTraceFile, " %c %llx,%d", &traceCommand, &address, &size) == 3) {
			switch(traceCommand) {
				case 'I':
					break;
				case 'L':
				    simulate(cache, address);
					break;
				case 'S':
				    simulate(cache, address);
					break;
				case 'M':
				    simulate(cache, address);
				    simulate(cache, address);
					break;
				default:
					break;
			}
		}
    }
    /*this method provided for us somewhere along the line in our project*/
    printSummary(hitCount, missCount, evictCount);
    return 0;
}
