#pragma once

#include <QWidget>
#include <vector>
#include "md2.h"

// Utility for loading MD2 files
class Md2LoaderWidget : public QWidget {
Q_OBJECT

public:
	Md2LoaderWidget(QWidget* parent = NULL);
	~Md2LoaderWidget();
signals:
	// MD2 file is done loading
	void FinishedLoadingMd2(MD2*);
	// Error loading file
	void ErrorLoading(QString const&);
public slots:
	// Loads specified MD2 file
	void LoadMd2File(QString const&);
};

