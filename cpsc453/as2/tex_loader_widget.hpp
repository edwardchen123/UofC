#pragma once

#include <QWidget>

// Utility for loading textures
class TexLoaderWidget : public QWidget {
Q_OBJECT

public:
	TexLoaderWidget(QWidget* parent = NULL);
	~TexLoaderWidget();
signals:
	void FinishedLoadingTex(unsigned char*, int, int);
	void ErrorLoading(QString const&);
public slots:
	void LoadTexFile(QString const&);
};

