CPSC 453 Assignment 2
Andrew Helwer
10023875

To compile and run the program, run:
$ qmake -project "QT+=opengl"
$ qmake -makefile
$ make

Code used from tutorials or given with assignment:
- md2.cpp and md2.h for loading md2 files
- BMP.h and pcx.h/pcx.cpp for loading .pcx and .bmp texture files
- initializedGL() function in Renderer.cpp
- resizedGL() function in Renderer.cpp

No notable data structures or algorithms were used in this assignment.

To use the program, run:
$ ./dirname
where dirname is the name of the directory in which the project was compiled. 

You can then select an .md2 file to load either by clicking File > Load MD2 Model or with ctrl+o. 
The program will load and display the model in wireframe, or display an error if it is unable. 
Once the model is loaded, select a .bmp or .pcx texture file by clicking File > Load Texture or with ctrl+t.
You can select which shading method to use with the drop-down menu. 

You can use your mouse to manipulate the model shown in the viewer. 
Scroll up with the scroll wheel to zoom in, and scroll down with the scroll wheel to zoom out. 
Click and drag in the view screen to rotate the model.

Exit the program by clicking File > Exit or with ctrl+q.

No bonuses have been implemented.

