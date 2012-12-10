#include <util/image_manip.hpp>
#include <util/rgb_image.hpp>
#include <cstring>
#include <iostream>
#include <algorithm>
#include <cmath>

#ifdef __APPLE__
    #include <OpenGL/gl.h>
    #include <GLUT/glut.h>
#else
    #include <GL/gl.h>
    #include <GL/glut.h>
#endif

ImageManip::ImageManip(RgbImage const& img) 
    : m_image(img)
{ }

unsigned char* ImageManip::Quantize(int levels) {
    unsigned char colourTable [256];
    for (unsigned int i = 0; i < 256; ++i) {
        colourTable[i] = static_cast<unsigned char>(255*((i*(levels-1))/255)/(levels-1));
    }

    unsigned char* data = CopyData();
    for (unsigned long i = 0; i < ImageByteCount(); i += ImageElementSize()) {
        for (unsigned int j = 0; j < ImageElementSize(); ++j) {
            data[i+j] = colourTable[data[i+j]];
        }
    }
    return data;
}

unsigned char* ImageManip::ChangeBrightness(float s) {
    unsigned char* data = CopyData();
    for (unsigned long i = 0; i < ImageByteCount(); i += ImageElementSize()) {
        for (unsigned int j = 0; j < ImageElementSize(); ++j) {
            float level = static_cast<float>(data[i+j]);
            int newLevel = std::min<int>(static_cast<int>(s*level), 255);
            data[i+j] = static_cast<unsigned char>(newLevel);
        }
    }
    return data;
}

unsigned char* ImageManip::ChangeSaturation(float s) {
    unsigned char* data = CopyData();
    for (unsigned long i = 0; i < ImageByteCount(); i += ImageElementSize()) {
        float level0 = static_cast<float>(data[i]);
        float level1 = static_cast<float>(data[i+1]);
        float level2 = static_cast<float>(data[i+2]);
        float luminance = 0.3f*level0 + 0.59f*level1 + 0.11f*level2;
        for (unsigned int j = 0; j < ImageElementSize(); ++j) {
            float level = static_cast<float>(data[i+j]);
            float newLevel = (1.0f-s)*luminance + s*level;
            int discrete = std::min<int>(static_cast<int>(newLevel), 255);
            data[i+j] = static_cast<unsigned char>(discrete);
        }
    }
    return data;
}

unsigned char* ImageManip::ScaleImage() {
    unsigned char const* data = static_cast<unsigned char const*>(m_image.ImageData());
    unsigned long byteCount = ImageByteCount();
    unsigned char* scaled = new unsigned char[2*byteCount];

    unsigned long rowCount = m_image.GetNumRows();
    unsigned long colCount = m_image.GetNumCols();
    unsigned int bts = ImageElementSize();
    for (unsigned long j = 0; j < rowCount; ++j) {
        for (unsigned long i = 0; i < 2*colCount; ++i) {
            scaled[i*bts + j*(colCount*2)*bts] = data[(i/2)*bts + j*colCount*bts];
            scaled[i*bts+1 + j*(colCount*2)*bts] = data[(i/2)*bts+1 + j*colCount*bts];
            scaled[i*bts+2 + j*(colCount*2)*bts] = data[(i/2)*bts+2 + j*colCount*bts];
        }
    }
    return scaled;
}

unsigned long ImageManip::RowCount() const {
    return m_image.GetNumRows();
}

unsigned long ImageManip::ColCount() const {
    return m_image.GetNumCols();
}

unsigned char* ImageManip::RotateImage() {
    std::cout << "RotateImage operation not implemented." << std::endl;
    return 0;
}

unsigned long ImageManip::ImageElementCount() const {
    return m_image.GetNumRows()*m_image.GetNumCols();
}

unsigned int ImageManip::ImageElementSize() const {
    return 3;
}

unsigned long ImageManip::ImageByteCount() const {
    return ImageElementSize()*ImageElementCount();
}

unsigned char* ImageManip::CopyData() const {
    unsigned char const* data = static_cast<unsigned char const*>(m_image.ImageData());
    long byteCount = ImageByteCount();
    unsigned char* newData = new unsigned char[byteCount];
    std::memcpy(newData, data, byteCount);
    return newData;
}

