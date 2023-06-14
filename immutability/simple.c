int sumFirstTenSquaresHelper(int sum, int i) {
	return (i>10) ? sum : sumFirstTenSquaresHelper(sum+i*i, i+1);
}

int sumFirstTenSquares() {
	return sumFirstTenSquaresHelper(0, 1);
}

State system(State state, Event event) {
	return done(state) ? state : system(state, getEvent());
}

#include <stdio.h>
int main(int ac, char** av) {
	printf("%d\n", sumFirstTenSquares());
	return 0;
}

