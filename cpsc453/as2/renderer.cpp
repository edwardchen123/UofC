#include "renderer.hpp"
#include "math.h"
#include "main.hpp"
#include <limits.h>
#include <algorithm>
#include <iostream>

Renderer::Renderer(QWidget* parent)
	: QGLWidget(parent)
{ 
	m_model = NULL;
	m_viewDist = 10;
	setMouseTracking(true);
}

Renderer::~Renderer()
{ }

void Renderer::RenderModel(MD2* model) {
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	m_model = model;
	updateGL();
}

void Renderer::SetShading(int index) {
	if (index == 0) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
	else if (index == 1) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
	else if (index == 2) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}
	else if (index == 3) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
	}
	updateGL();
}

void Renderer::initializedGL() {
    glHint( GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST );
    glShadeModel( GL_SMOOTH );
    glClearColor(0, 0, 0, 0);
}

void Renderer::paintGL() {
    glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );    
    glLoadIdentity();

	// Sets camera m_viewDist away from origin
    gluLookAt(m_viewDist, 0.0, 0.0,
              0.0, 0.0, 0.0,  
              0.0, 0.0, 1.0);

	// Displays model in correct rotation
	if (m_model != NULL) {
		glPushMatrix();
		glRotated(m_theta, 0.0, 0.0, 1.0);
		glRotated(m_phi, 0.0, 1.0, 0.0);
		glBegin(GL_TRIANGLES);
			for (int i = 0; i < m_model->num_tris; ++i) {
				triangle_t tri = m_model->tris[i];
				vec3_t* v0 = &(m_model->m_vertices[tri.index_xyz[0]]);
				vec3_t* v1 = &(m_model->m_vertices[tri.index_xyz[1]]);
				vec3_t* v2 = &(m_model->m_vertices[tri.index_xyz[2]]);
				//tex_coord t0 = m_model->texs[tri.index_st[0]];
				//tex_coord t1 = m_model->texs[tri.index_st[1]];
				//tex_coord t2 = m_model->texs[tri.index_st[2]];
				glColor4f(Qt::white, Qt::white, Qt::white, 0.8);
				glVertex3f((*v0)[0], (*v0)[1], (*v0)[2]);
				glColor4f(Qt::white, Qt::white, Qt::white, 0.8);
				glVertex3f((*v1)[0], (*v1)[1], (*v1)[2]);
				glColor4f(Qt::white, Qt::white, Qt::white, 0.8);
				glVertex3f((*v2)[0], (*v2)[1], (*v2)[2]);
			}
		glEnd();
		glPopMatrix();
	}
	else {
		glPushMatrix();
		renderText(0, 0, 0, tr("Nothing here"));
		glPopMatrix();
	}
    
    glFlush();
}

void Renderer::resizeGL(int w, int h) {
    glViewport( 0, 0, w, h );
    glMatrixMode( GL_PROJECTION );
    glLoadIdentity();

    if(w>=h){
      double fc = w/(double)h;
      glFrustum( -1.0*fc, 1.0*fc, -1.0, 1.0, 2.0, 1000.0 );
    }
    else if(h>w){
      double fc = h/(double)w;
      glFrustum( -1.0, 1.0, -1.0*fc, 1.0*fc, 2.0, 1000.0 );
    }

    glMatrixMode( GL_MODELVIEW );
    glLoadIdentity();
}

void Renderer::wheelEvent(QWheelEvent* event) {
	int delta = event->delta();
	if (delta > 0)
		m_viewDist = std::max(2, m_viewDist-2);
	else
		m_viewDist += 2;
	updateGL();
}

void Renderer::mouseMoveEvent(QMouseEvent* event) {
	if (m_dragging) {
		if (event->buttons() & Qt::LeftButton) {
			QPoint currentMousePos = event->globalPos();
			int dx = currentMousePos.x()-m_lastMousePos.x();
			int dy = currentMousePos.y()-m_lastMousePos.y();
			m_theta += (static_cast<float>(dx)/static_cast<float>(WIN_W))*360;
			m_phi += static_cast<float>(dy)/static_cast<float>(WIN_H)*360;
			m_lastMousePos.setX(currentMousePos.x());
			m_lastMousePos.setY(currentMousePos.y());
			updateGL();
		}
		else
			m_dragging = false;
	}
	else {
		m_dragging = true;
		m_lastMousePos = event->globalPos();
	}
}

