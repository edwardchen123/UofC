#ifndef MODEL_H
#define MODEL_H

typedef struct Model;

Model* ModelCons(int, int);
int isLifeAt(struct Model* m, int x, int y);
void createLifeAt(struct Model* m, int x, int y);
void destroyLifeAt(struct Model* m, int x, int y);
void simulate(struct Model* m); 

#endif
