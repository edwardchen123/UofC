% CPSC 535 Assignment 1, Question 1 - Warming Up with Images

% Creates initial array of 100 elements according to function
a = zeros(1, 100);
a(26:50) = [1/25:1/25:1] .^ 2;
a(51:75) = fliplr(a(26:50)) * -1;

% Duplicate array to create cornsweet effect image
b = repmat(a, 50, 1);

% Display image 
% Notes: only imshow worked for me, possibly because I'm using x11 with gnuplot on mac?
imshow(b, [-1, 1]);
%caxis([-1, 1]);
%colormap("gray");
%image(b);

