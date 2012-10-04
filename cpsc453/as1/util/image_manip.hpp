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
    void Quantize(int levels);
    void ChangeBrightness(float s);
    void ChangeSaturation(float s);
    void ScaleImage();
    void RotateImage();
private:
    RgbImage& image;
};

