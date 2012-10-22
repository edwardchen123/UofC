#include "main.hpp"
#include <QApplication>
#include "window.hpp"

int main(int argc, char* argv[]) {
	QApplication parent(argc, argv);
	Window w;
	w.resize(WIN_W, WIN_H);
	w.show();
	return parent.exec();
}

