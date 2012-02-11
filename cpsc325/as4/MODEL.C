#include "model.h"
#define ROWS 5
#define COLUMNS 5

typedef struct Model {
	char* grid;
	int (*isLifeAt)(struct Model*, int, int);
	void (*createLifeAt)(struct Model*, int, int);
	void (*destroyLifeAt)(struct Model*, int, int);
	void (*simulate)(struct Model*);
} Model;

Model* ModelCons(int x, int y) {
	Model* m;
	char* newGrid[ROWS][COLUMNS] = {0};
	m->grid = newGrid;
	m->isLifeAt = isLifeAt;
	m->createLifeAt = createLifeAt;
	m->destroyLifeAt = destroyLifeAt;
	m->simulate = simulate;
	return m;
}

int isLifeAt(struct Model* m, int x, int y) {
	return (m->grid[x][y]);
}

void createLifeAt(struct Model* m, int x, int y) {
	(m->grid[x][y]) = 1;
}

void destroyLifeAt(struct Model* m, int x, int y) {
	(m->grid[x][y]) = 0;
}

void simulate(struct Model* m) {
}

int main() {
	return 0;
}

