#include "tex_loader_widget.hpp"
#include "BMP.h"
#include "pcx.h"
#include <iostream>

TexLoaderWidget::TexLoaderWidget(QWidget* parent)
	:	QWidget(parent)
{ }

TexLoaderWidget::~TexLoaderWidget()
{ }

void TexLoaderWidget::LoadTexFile(QString const& fileName) {
	char const* cFileName = fileName.toStdString().c_str();
	if (fileName.endsWith(tr("bmp"), Qt::CaseInsensitive)) {
		BMPImg img;
		int error = img.Load(cFileName);
		if (error == 1) {
			unsigned char* data = img.GetImg();
			int width = img.GetWidth();
			int height = img.GetHeight();
			std::cout << "Texture width: " << width << std::endl;
			std::cout << "Texture height: " << height << std::endl;
			emit(FinishedLoadingTex(data, width, height));
		}
		else
			emit(ErrorLoading(tr("Could not load given .bmp file")));
	}
	else if (fileName.endsWith(tr("pcx"), Qt::CaseInsensitive)) {
		unsigned char* data = NULL;
		int width = 0;
		int height = 0;
		int error = LoadFilePCX(cFileName, &data, &width, &height, true);
		std::cout << "Texture width: " << width << std::endl;
		std::cout << "Texture height: " << height << std::endl;
		if (error == 1)
			emit(FinishedLoadingTex(data, width, height));
		else
			emit(ErrorLoading(tr("Could not load given .pcx file")));
	}
	else {
		emit(ErrorLoading(tr(".bmp or .pcx required for texture file")));
	}
}

