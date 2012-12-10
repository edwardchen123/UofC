% CPSC 535 Assignment 1, Question 2 - The Koch Curve

source("koch.m");

% Apply Koch function ten times to line segment
pts = [0, 1; 0, 0];
for i = 1:10,
    pts = Koch(pts);
end

plot(pts(1, :), pts(2, :));

