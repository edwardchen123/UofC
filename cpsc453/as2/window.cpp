#include "window.hpp"
#include "md2_loader_widget.hpp"
#include <QtGui>

Window::Window(QWidget* parent)
	: QWidget(parent)
{
	setWindowTitle(tr("CPSC 453 Assignment 2"));
	QVBoxLayout* mainLayout = new QVBoxLayout();

	// Error dialog box
	m_error = new QErrorMessage();

	// Creates file menu
	QMenuBar* menuBar = new QMenuBar();
	QMenu* file = new QMenu(tr("&File"), this);
	file->addAction("Exit", qApp, SLOT(quit()), Qt::CTRL+Qt::Key_Q);
	QFileDialog* findModel = new QFileDialog();
	file->addAction("Load MD2 Model", findModel, SLOT(open()), Qt::CTRL+Qt::Key_O);
	QFileDialog* findTex = new QFileDialog();
	file->addAction("Load Texture", findTex, SLOT(open()), Qt::CTRL+Qt::Key_T);
	menuBar->addMenu(file);
	mainLayout->setMenuBar(menuBar);

	m_modelWindow = new Renderer(this);
	connect(m_modelWindow, SIGNAL(Error(QString const&)),
			m_error, SLOT(showMessage(QString const&)));
	mainLayout->addWidget(m_modelWindow);

	// Creates MD2 model load chain
	m_modLoader = new Md2LoaderWidget();
	// Select MD2 file to read in
	connect(findModel, SIGNAL(fileSelected(QString const&)), 
			m_modLoader, SLOT(LoadMd2File(QString const&)));
	// Error handling
	connect(m_modLoader, SIGNAL(ErrorLoading(QString const&)),
			m_error, SLOT(showMessage(QString const&)));
	// Render model
	connect(m_modLoader, SIGNAL(FinishedLoadingMd2(MD2*)),
			m_modelWindow, SLOT(RenderModel(MD2*)));

	// Creates texture load and display chain
	m_texLoader = new TexLoaderWidget();
	// Select MD2 file to read in
	connect(findTex, SIGNAL(fileSelected(QString const&)), 
			m_texLoader, SLOT(LoadTexFile(QString const&)));
	// Error handling
	connect(m_texLoader, SIGNAL(ErrorLoading(QString const&)),
			m_error, SLOT(showMessage(QString const&)));
	// Add texture to model
	connect(m_texLoader, 
			SIGNAL(FinishedLoadingTex(unsigned char*, int, int)),
			m_modelWindow, 
			SLOT(SetTexture(unsigned char*, int, int)));

	// Creates shading selection drop-down menu
	QComboBox* viewMode = new QComboBox();
	viewMode->insertItem(1, tr("Smooth shading"));
	viewMode->insertItem(2, tr("Flat shading"));
	viewMode->insertItem(3, tr("Wireframe"));
	viewMode->insertItem(3, tr("Point cloud"));
	mainLayout->addWidget(viewMode);
	connect(viewMode, SIGNAL(activated(int)),
			m_modelWindow, SLOT(SetShading(int)));

	// Sets main layout
	setLayout(mainLayout);
}

