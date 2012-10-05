#pragma once

class RgbImage;

/* *
 * ImageManip
 *
 * Class encapsulating various transformations which may be performed on an image.
 * */
class ImageManip {
public:
    ImageManip(RgbImage& img);
    void Quantize(int bits);
    void ChangeBrightness(float s);
    void ChangeSaturation(float s);
    void ScaleImage();
    void RotateImage();
private:
    unsigned long ImageElementCount();
    unsigned int ImageElementSize();
    unsigned long ImageByteCount();
    unsigned char* CopyData();
    RgbImage& m_image;
};

