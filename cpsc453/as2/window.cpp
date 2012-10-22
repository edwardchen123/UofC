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
	QFileDialog* find = new QFileDialog();
	file->addAction("Load MD2 Model", find, SLOT(open()), Qt::CTRL+Qt::Key_O);
	menuBar->addMenu(file);
	mainLayout->setMenuBar(menuBar);

	// Creates MD2 model load and display chain
	m_modelWindow = new Renderer(this);
	mainLayout->addWidget(m_modelWindow);
	m_loader = new Md2LoaderWidget();
	// Select MD2 file to read in
	connect(find, SIGNAL(fileSelected(QString const&)), 
			m_loader, SLOT(LoadMd2File(QString const&)));
	// Error handling
	connect(m_loader, SIGNAL(ErrorLoading(QString const&)),
			m_error, SLOT(showMessage(QString const&)));
	// Render model
	connect(m_loader, SIGNAL(FinishedLoadingMd2(MD2*)),
			m_modelWindow, SLOT(RenderModel(MD2*)));

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
