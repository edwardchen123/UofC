#pragma once

#include <QGLWidget>
#include <QWheelEvent>
#include <QMouseEvent>
#include <QKeyEvent>
#include "md2.h"

// Main model view window
class Renderer : public QGLWidget {
Q_OBJECT
public:
	Renderer(QWidget* parent = NULL);
	~Renderer();
signals:
	void Error(QString const&);
public slots:
	// Accepts pointer to MD2 data and renders it
	void RenderModel(MD2*);
	// Sets texture for current model
	void SetTexture(unsigned char*, int, int);	
	// Sets shading type
	void SetShading(int);
protected:
	void initializedGL();
	void paintGL();
	void resizeGL(int w, int h);
	// Zooms in and out
	void wheelEvent(QWheelEvent* event);
	// Click and drag to rotate
	void mouseMoveEvent(QMouseEvent* event);
private:
	MD2* m_model;
	unsigned char* m_texture;
	GLuint m_texID;
	int m_viewDist;
	float m_theta;
	float m_phi;
	bool m_dragging;
	QPoint m_lastMousePos;
};

