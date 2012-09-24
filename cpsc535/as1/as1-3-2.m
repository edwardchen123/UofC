% CPSC 535 Assignment 1, Question 3 - Haar Wavelets
% Part 2

source("pnmread.m")
source("haar.m")

% Use function provided by course website to read in cat image
ct = pnmread("cat.pgm");
ct = double(ct);

% Level 1 Haar Decomposition
[high1a, low] = HaarDecomp(ct);
[high1b, low] = HaarDecomp(low');

% Level 2 Haar Decomposition
[high2a, low] = HaarDecomp(low');
[high2b, low] = HaarDecomp(low');

% Level 3 Haar Decomposition
[high3a, low] = HaarDecomp(low');
[high3b, low] = HaarDecomp(low');

% Display decomposed image in greyscale
colormap("gray");
image(low')

