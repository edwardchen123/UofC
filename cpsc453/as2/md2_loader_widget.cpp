#include "md2_loader_widget.hpp"
#include <iostream>

Md2LoaderWidget::Md2LoaderWidget(QWidget* parent) 
	: QWidget(parent)
{ }

Md2LoaderWidget::~Md2LoaderWidget()
{ }

void Md2LoaderWidget::LoadMd2File(QString const& fileName) {
	char const* cFileName = fileName.toStdString().c_str();
	MD2* model = new MD2();
	bool success = model->LoadModel(cFileName);
	if (success) {
		emit(FinishedLoadingMd2(model));
	}
	else {
		delete model;
		emit(ErrorLoading(tr("Unable to load selected MD2 file")));
	}
}

