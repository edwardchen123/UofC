% CPSC 535 Assignment 1, Question 3 - Haar Wavelets
% Part 1

source("haar.m");

% Perform Haar decomposition on sine function five times
data = sin(0:2*pi/(1024-1):2*pi)+randn(1,1024)/10;
[detail1, data] = HaarDecomp(data);
[detail2, data] = HaarDecomp(data);
[detail3, data] = HaarDecomp(data);
[detail4, data] = HaarDecomp(data);
[detail5, data] = HaarDecomp(data);
% Plot low frequency data with high frequency data appended
data = cat(2, data, detail1, detail2, detail3, detail4, detail5);
plot(data);

% Perform Haar composition on cosine function with high frequency data from above
data = cos(0:2*pi/(32-1):2*pi)+randn(1,32)/10;
data = HaarComp(detail5, data);
data = HaarComp(detail4, data);
data = HaarComp(detail3, data);
data = HaarComp(detail2, data);
data = HaarComp(detail1, data);
plot(data);

