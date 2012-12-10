#pragma once

class RgbImage;

/* *
 * ImageManip
 *
 * Class encapsulating various transformations which may be performed on an image.
 * */
class ImageManip {
public:

    /* *
     * ImageManip Constructor
     * 
     * Takes a reference to the image upon which operations will be performed.
     * Operations will be performed on a copy, so the original is not modified.
     * */
    ImageManip(RgbImage const& img);

    /* *
     * Quantize
     *
     * Returns image quantized into the given number of levels.
     * */
    unsigned char* Quantize(int levels); 

    /* *
     * ChangeBrightness
     *
     * Increases or decreases the brightness of the image according to the given factor.
     * */
    unsigned char* ChangeBrightness(float s);

    /* *
     * ChangeSaturation
     *
     * Increases or decreases the saturation of the image according to the given factor.
     * */
    unsigned char* ChangeSaturation(float s);

    /* *
     * ScaleImage
     *
     * Stretches the image horizontally, doubling its width.
     * */
    unsigned char* ScaleImage();

    /* *
     * RotateImage
     *
     * Rotates the image by 45 degrees.
     * */
    unsigned char* RotateImage();

    // Returns number of rows in image
    unsigned long RowCount() const;

    // Returns number of cols in image
    unsigned long ColCount() const;

private:
    // Returns the number of pixels in the image
    unsigned long ImageElementCount() const;
    // Returns the size in bytes of each pixel
    unsigned int ImageElementSize() const;
    // Returns the number of bytes in the image
    unsigned long ImageByteCount() const;
    // Returns a pointer to a copy of the image data
    unsigned char* CopyData() const;

    RgbImage const& m_image; 
};

