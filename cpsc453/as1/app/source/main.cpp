#include <util/rgb_image.hpp>

#ifdef __APPLE__
    #include <GLUT/glut.h>
    #include <OpenGL/gl.h>
#else
    #include <GL/glut.h>
    #include <GL/gl.h>
#endif

#include <iostream>

/* *
 * DisplayError
 *
 * Prints out error message appropriate for error code from class RgbImage.
 * */
void DisplayError(int errorCode) {
    switch (errorCode) {
        case 0:
            std::cout << "ERROR: Unkown failure." << std::endl;
            break;
        case 1:
            std::cout << "ERROR: Unable to open file." << std::endl;
            break;
        case 2:
            std::cout << "ERROR: File not recognized as 24-bit bmp." << std::endl;
            break;
        case 3:
            std::cout << "ERROR: Unable to allocate memory for image data." << std::endl;
            break;
        case 4:
            std::cout << "ERROR: End of file reached prematurely." << std::endl;
            break;
        case 5:
            std::cout << "ERROR: Unable to write data." << std::endl;
            break;
    }
}

int main(int argc, char* argv[]) {
    glutInit(&argc, argv);
    if (argc != 2) {
        std::cout << "ERROR: Expected path to .bmp file." << std::endl;
        return 0;
    }

    char const* filename = argv[1];
    RgbImage img;
    if (!img.LoadBmpFile(filename)) {
        DisplayError(img.GetErrorCode());
        return 0;
    }

    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
    glutMainLoop();
}

