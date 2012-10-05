#include <util/image_manip.hpp>
#include <util/rgb_image.hpp>
#include <cstring>

#ifdef __APPLE__
    #include <OpenGL/gl.h>
    #include <GLUT/glut.h>
#else
    #include <GL/gl.h>
    #include <GL/glut.h>
#endif

ImageManip::ImageManip(RgbImage& img) 
    : m_image(img)
{ }

void ImageManip::Quantize(int bits) {
    char* data = CopyData();
    for (unsigned long i = 0; i < ImageByteCount(); i += ImageElementSize()) {
        for (unsigned int j = 0; j < ImageElementSize(); ++j) {
            data[i+j] >>= (8-bits); 
            data[i+j] <<= (8-bits);
        }
    }
    glTexImage2D(GL_TEXTURE_2D, 0, 3, m_image.GetNumCols(), m_image.GetNumRows(), 0,
                    GL_RGB, GL_UNSIGNED_BYTE, data);
    delete[] data;
}

void ImageManip::ChangeBrightness(float s) {
    char* data = CopyData();
    for (unsigned long i = 0; i < ImageByteCount(); i += ImageElementSize()) {
        for (unsigned int j = 0; j < ImageElementSize(); ++j) {
            float d = static_cast<float>(data[i+j]);
            data[i+j] = static_cast<char>(d*s);
        }
    }
    glTexImage2D(GL_TEXTURE_2D, 0, 3, m_image.GetNumCols(), m_image.GetNumRows(), 0,
                    GL_RGB, GL_UNSIGNED_BYTE, data);
    delete[] data;
}

void ImageManip::ChangeSaturation(float s) {

}

void ImageManip::ScaleImage() {

}

void ImageManip::RotateImage() {

}

unsigned long ImageManip::ImageElementCount() {
    return m_image.GetNumRows()*m_image.GetNumCols();
}

unsigned int ImageManip::ImageElementSize() {
    return 4;
}

unsigned long ImageManip::ImageByteCount() {
    return ImageElementSize()*ImageElementCount();
}

char* ImageManip::CopyData() {
    char const* data = static_cast<char const*>(m_image.ImageData());
    long byteCount = ImageByteCount();
    char* newData = new char[byteCount];
    std::memcpy(newData, data, byteCount);
    return newData;
}

