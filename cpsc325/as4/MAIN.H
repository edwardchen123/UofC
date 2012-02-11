#ifndef MAIN_H
#define MAIN_H

#include "settings.h"
#include <stdio.h>
#include <stdlib.h>
#include <pc.h>
#include <dpmi.h>
#include <go32.h>
#include <dos.h>

void populate(int model[][ROWS], FILE *fp);
void run(int model[][ROWS]);
void swapBuff();
void printHeader();
void drawBorder(char color);
void drawText(int x, int y, char text[], char color, int font);
void drawGrid(char color);
void updateModel(int model[][ROWS]);
int check(int model[][ROWS], int x, int y);
void updateSquare(int model[][ROWS], int x, int y);
void drawSquare(int x, int y, char color);
void drawHLine(int x, int y, int length, char color);
void drawVLine(int x, int y, int length, char color);
void drawPixel(int x, int y, char color);
void drawScaledPixel(int x, int y, int scale, char color);
void waitVRetrace();
void key_delete();
void key_init();
void LOCKint9();
void NewInt9();
void close_graphics();
void init_graphics();

#endif
