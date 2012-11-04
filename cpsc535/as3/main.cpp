#include <iostream>
#include <fstream>

int main(int argc, char* argv[]) {
	if (argc != 2) {
		std::cout << "ERROR: image file required." << std::endl;
		return -1;
	}
	char const* filename = argv[1];
	std::ifstream file (filename);
	std::cout << filename << std::endl;
	return 0;
}
