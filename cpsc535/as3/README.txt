CPSC 535 Assignment 3a: Binary Image Processing
Andrew Helwer
10023875

Program use:

First compile bwlabel.cc with the mkoctfile command.
bwlabel.cc was provided with the assignment.
Run "octave as3.m filename.tif" to OCR image.
Output is put in results.txt.

Algorithm Overview:

1) Read in grayscale image file given from command line.
2) Convert grayscale image to binary image at 20% quantile threshold.
3) Crop out filename text using vertical derivative.
4) Receive list of 8-connected features from call to bwlabel().
5) Iterates through features, doing the following:
 - Isolate feature
 - Calculate bounding box of feature
 - Calculate X and Y location in grid of characters
 - Use trained algorithm to OCR isolated and bounded feature
6) Output OCRd text into results.txt.

OCR Algorithm (which doesn't work very well):

1) Train algorithm by pairing features with their known character.
2) Find average of following characteristics for features:
 - Calculate intensity of feature; brightness and numeracy of pixels
 - Calculate horizontal symmetry
 - Calculate vertical symmetry
3) To OCR, find difference between feature characteristics and average characteristics.
4) Determine symbol by minimum difference from average (this is the part that needs more work).

Observations:

- Knowing Euler characteristic would be useful, but I couldn't find the bweuler() function.
- The weakest part of the project was the statistical analysis; hypothesis testing was required, not just minimum difference to average.
- I at first attempted to use the Perceptron Learning Algorithm, but linearly separating the data in such a way that allowed for distinction between 10 different classes of data was difficult. Neural networks probably would have worked best.
- I came up with a vectorized version of the for-loop, but it used many gigabytes of memory.
- The project would have been much more difficult if the characters had not been in a simple grid pattern. Even figuring out the position in the grid was tricky.
- Still, project was a lot of fun!

