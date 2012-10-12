#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]) {
	FILE* f = fopen(argv[1], "r");
	const int dataSize = 1000;
	void* data = malloc(dataSize);
	while (!feof(f)) {
		fread(data, 1, dataSize, f);
		printf("%s", data);
	}
	free(data);
	fclose(f);
	return 0;
}
