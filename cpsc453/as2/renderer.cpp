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
	m_texture = NULL;
	m_texID = 0;
	m_viewDist = 10;
	setMouseTracking(true);
}

Renderer::~Renderer()
{ 
	if (m_model != NULL)
		delete m_model;
	if (m_texture != NULL) {
		glDeleteTextures(1, &m_texID);
		delete[] m_texture;
	}
}

void Renderer::RenderModel(MD2* model) {
	if (m_model != NULL)
		delete m_model;
	m_model = model;
	std::cout << "Model width: " << m_model->skin_width << std::endl;
	std::cout << "Model height: " << m_model->skin_height << std::endl;
	if (m_texture != NULL)
		delete[] m_texture;
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	updateGL();
}

void Renderer::SetTexture(unsigned char* data, int w, int h) {
	if (m_model == NULL) {
		emit(Error(tr("Must select model before texture.")));
		return;
	}
	if (!(m_model->skin_width == w && m_model->skin_height == h)) {
		emit(Error(tr("Texture size does not match current model.")));
		return;
	}
	if (m_texture != NULL)
		delete[] m_texture;
	m_texture = data;
	gluBuild2DMipmaps(GL_TEXTURE_2D, 4, w, h, GL_RGBA,
						GL_UNSIGNED_BYTE, m_texture);
	/*
	glTexImage2D(GL_TEXTURE_2D, 0, 4, w, h, 0, GL_RGBA, 
					GL_UNSIGNED_BYTE, m_texture);
					*/
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	glEnable(GL_TEXTURE_2D);
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
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    glShadeModel(GL_SMOOTH);
    glClearColor(0, 0, 0, 0);

	glGenTextures(1, &m_texID);
	glBindTexture(GL_TEXTURE_2D, m_texID);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

	glEnable(GL_DEPTH_TEST);
	glEnable(GL_RESCALE_NORMAL);
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
	glShadeModel(GL_SMOOTH);
}

void Renderer::paintGL() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);    
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
		if (m_texture != NULL) {
			int w = m_model->skin_width;
			int h = m_model->skin_height;
			glBegin(GL_TRIANGLES);
				for (int i = 0; i < m_model->num_tris; ++i) {
					triangle_t tri = m_model->tris[i];
					vec3_t* v0 = &(m_model->m_vertices[tri.index_xyz[0]]);
					vec3_t* v1 = &(m_model->m_vertices[tri.index_xyz[1]]);
					vec3_t* v2 = &(m_model->m_vertices[tri.index_xyz[2]]);
					tex_coord c0 = m_model->texs[tri.index_st[0]];
					tex_coord c1 = m_model->texs[tri.index_st[1]];
					tex_coord c2 = m_model->texs[tri.index_st[2]];
					double s0 = (double)(c0.u) / (double)w;
					double t0 = (double)(c0.v) / (double)h;
					double s1 = (double)(c1.u) / (double)w;
					double t1 = (double)(c1.v) / (double)h;
					double s2 = (double)(c2.u) / (double)w;
					double t2 = (double)(c2.v) / (double)h;
					float x0 = (*v0)[0];
					float y0 = (*v0)[1];
					float z0 = (*v0)[2];
					float x1 = (*v1)[0];
					float y1 = (*v1)[1];
					float z1 = (*v1)[2];
					float x2 = (*v2)[0];
					float y2 = (*v2)[1];
					float z2 = (*v2)[2];
					float vecVx = (x1-x0);
					float vecVy = (y1-y0);
					float vecVz = (z1-z0);
					float vecUx = (x2-x0);
					float vecUy = (y2-y0);
					float vecUz = (z2-z0);
					double nx = (vecUy*vecVz) - (vecUz*vecVy);
					double ny = (vecUz*vecVx) - (vecUx*vecVz);
					double nz = (vecUx*vecVy) - (vecUy*vecVx);
					glNormal3f(nx, ny, nz);
					glTexCoord2d(s0, t0);
					glVertex3f(x0, y0, z0);
					glTexCoord2d(s1, t1);
					glVertex3f(x1, y1, z1);
					glTexCoord2d(s2, t2);
					glVertex3f(x2, y2, z2);
				}
			glEnd();
		}
		else {
			glBegin(GL_TRIANGLES);
				for (int i = 0; i < m_model->num_tris; ++i) {
					triangle_t tri = m_model->tris[i];
					vec3_t* v0 = &(m_model->m_vertices[tri.index_xyz[0]]);
					vec3_t* v1 = &(m_model->m_vertices[tri.index_xyz[1]]);
					vec3_t* v2 = &(m_model->m_vertices[tri.index_xyz[2]]);
					glColor4f(Qt::white, Qt::white, Qt::white, 0.8);
					glVertex3f((*v0)[0], (*v0)[1], (*v0)[2]);
					glColor4f(Qt::white, Qt::white, Qt::white, 0.8);
					glVertex3f((*v1)[0], (*v1)[1], (*v1)[2]);
					glColor4f(Qt::white, Qt::white, Qt::white, 0.8);
					glVertex3f((*v2)[0], (*v2)[1], (*v2)[2]);
				}
			glEnd();
		}
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
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

    if(w>=h){
      double fc = w/(double)h;
      glFrustum( -1.0*fc, 1.0*fc, -1.0, 1.0, 2.0, 1000.0 );
    }
    else if(h>w){
      double fc = h/(double)w;
      glFrustum( -1.0, 1.0, -1.0*fc, 1.0*fc, 2.0, 1000.0 );
    }

    glMatrixMode(GL_MODELVIEW);
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

