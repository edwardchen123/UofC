#pragma once

#include <QWidget>
#include <QFileDialog>
#include <QErrorMessage>

#include "renderer.hpp"
#include "md2_loader_widget.hpp"

// Parent window class
class Window : public QWidget {
Q_OBJECT

public:
	Window(QWidget* parent = NULL);
signals:

public slots:

private:
	QErrorMessage* m_error;
	Renderer* m_modelWindow;
	Md2LoaderWidget* m_loader;
};

