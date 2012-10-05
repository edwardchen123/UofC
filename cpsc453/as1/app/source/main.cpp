#include <util/rgb_image.hpp>
#include <util/image_manip.hpp>

#ifdef __APPLE__
    #include <GLUT/glut.h>
    #include <OpenGL/gl.h>
#else
    #include <GL/glut.h>
    #include <GL/gl.h>
#endif

#include <iostream>

ImageManip* manipulator;

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

/* *
 * DrawScene
 *
 * Draws current texture to screen. Taken from code given in tutorial.
 * */
void DrawScene()
{
   glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
   glEnable(GL_TEXTURE_2D);
   glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

   glBegin(GL_QUADS);
   
   glTexCoord2f(0.0, 0.0); 
   glVertex3f(-1.0, -1.0, 0.0);
   
   glTexCoord2f(0.0, 1.0); 
   glVertex3f(-1.0, 1.0, 0.0);
   
   glTexCoord2f(1.0, 1.0); 
   glVertex3f(1.0, 1.0, 0.0);
   
   glTexCoord2f(1.0, 0.0); 
   glVertex3f(1.0, -1.0, 0.0);
   
   glEnd();

   glFlush();
   glDisable(GL_TEXTURE_2D);
}

/* *
 * ResizeWindow
 *
 * Resizes the window displaying the image appropriately.
 * Taken from code given in tutorial.
 * */
void ResizeWindow(int w, int h)
{
	float viewWidth = 1.1;
	float viewHeight = 1.1;
	glViewport(0, 0, w, h);
	h = (h==0) ? 1 : h;
	w = (w==0) ? 1 : w;
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	if ( h < w ) {
		viewWidth *= (float)w/(float)h; 
	}
	else {
		viewHeight *= (float)h/(float)w;
	}
	glOrtho( -viewWidth, viewWidth, -viewHeight, viewHeight, -1.0, 1.0 );

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}

/* *
 * Keyboard
 *
 * Exits program when ESC key is pressed.
 * Taken from code given in tutorial.
 * */
void Keyboard (unsigned char key, int x, int y)
{
	switch (key) {
		case 27:
			exit(0);
			break;
		default:
			break;
   }
}

/* *
 * FilterSelection
 *
 * Enumerated list of filter options to choose from a menu.
 * */
enum FilterSelection {
    quantize = 0,
    brighten = 1,
    saturate = 2,
    scale = 3,
    rotate = 4
};

/* *
 * FilterMenu
 *
 * Called when user selects filter from menu.
 * */
void FilterMenu(int selection) {
    unsigned char* data = 0;
    switch (selection) {
        case quantize:
            {
            std::cout << "Enter number of levels: ";
            int levels = 0;
            std::cin >> levels;
            if (1 <= levels && levels <= 255)
                data = manipulator->Quantize(levels);
            else
                std::cout << "ERROR: Levels must be between 1 and 255." << std::endl;
            break;
            }
        case brighten:
            {
            std::cout << "Enter brightness scaling factor: ";
            float s;
            std::cin >> s;
            if (0.0f <= s)
                data = manipulator->ChangeBrightness(s);
            else
                std::cout << "ERROR: Factor must be positive." << std::endl;
            break;
            }
        case saturate:
            {
            std::cout << "Enter saturation scaling factor: ";
            float s;
            std::cin >> s;
            if (0.0f <= s)
                data = manipulator->ChangeSaturation(s);
            else
                std::cout << "ERROR: Factor must be positive." << std::endl;
            break;
            }
        case scale:
            {
            data = manipulator->ScaleImage();
            break;
            }
        case rotate:
            {
            data = manipulator->RotateImage();
            break;
            }
    }
    glClearColor (0.0, 0.0, 0.0, 0.0); 
	glShadeModel(GL_FLAT); 
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_TEXTURE_2D);
    GLuint textureID;
    glBindTexture(GL_TEXTURE_2D, textureID);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    gluBuild2DMipmaps(GL_TEXTURE_2D, 3, manipulator->ColCount(), 
                        manipulator->RowCount(),
                        GL_RGB, GL_UNSIGNED_BYTE, data);
    if (data != 0) {
        delete[] data;
    }
}

/* *
 * CreateFilterMenu
 *
 * Creates and initializes the menu of filters. Opens on right click.
 * */
void CreateFilterMenu() {
    int filterMenu = glutCreateMenu(FilterMenu);
    glutSetMenu(filterMenu);
    glutAddMenuEntry("Quantize", quantize);
    glutAddMenuEntry("Brighten", brighten);
    glutAddMenuEntry("Saturate", saturate);
    glutAddMenuEntry("Scale", scale);
    glutAddMenuEntry("Rotate", rotate);
    glutAttachMenu(GLUT_RIGHT_BUTTON);
}

/* *
 * main
 *
 * Main program loop. Initializes program components.
 * */
int main(int argc, char* argv[]) {
    // Parse CL arguments
    glutInit(&argc, argv);
    if (argc != 2) {
        std::cout << "ERROR: Expected path to .bmp file." << std::endl;
        return 0;
    }
    char const* filename = argv[1];

    // Load bmp image into memory
    RgbImage img;
    if (!img.LoadBmpFile(filename)) {
        DisplayError(img.GetErrorCode());
        return 0;
    }

    // Set some initial window properties for GLUT
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(240, 240);
	glutInitWindowPosition(100, 100);
	glutCreateWindow(argv[0]);

    // Set initial properties for OpenGL 
    glClearColor (0.0, 0.0, 0.0, 0.0); 
	glShadeModel(GL_FLAT); 
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_TEXTURE_2D);

    // Registers texture
    GLuint textureID;
    glGenTextures(1, &textureID); // a bit of a hack
    glBindTexture(GL_TEXTURE_2D, textureID);

    // Move bmp image into texture memory
    gluBuild2DMipmaps(GL_TEXTURE_2D, 3, img.GetNumCols(), img.GetNumRows(),
                        GL_RGB, GL_UNSIGNED_BYTE, img.ImageData());

    // Set initial texture properties improperly
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

    // Initializes global variable manipulator
    manipulator = new ImageManip(img);
    /*
    unsigned char* data = manipulator->Quantize(10);
    gluBuild2DMipmaps(GL_TEXTURE_2D, 3, img.GetNumCols(), img.GetNumRows(),
                        GL_RGB, GL_UNSIGNED_BYTE, data);
                        */

    CreateFilterMenu();
    glutDisplayFunc(DrawScene);
    glutReshapeFunc(ResizeWindow);
    glutKeyboardFunc(Keyboard);
    glutMainLoop();
    return 0;
}

