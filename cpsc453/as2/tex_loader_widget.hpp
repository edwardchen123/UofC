#pragma once

#include <QWidget>

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

