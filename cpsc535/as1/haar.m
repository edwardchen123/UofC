% Performs Haar decomposition on the given set of values
function [high, low] = HaarDecomp(pts)
    n = length(pts);

    % Calculates low frequency component
    lowMat = GetLowHaarDecompMat(n);
    low = pts * lowMat;

    % Calculates high frequency component
    highMat = GetHighHaarDecompMat(n);
    high = pts * highMat;
end
    
% Performs Haar composition with the set of high and low frequency values
function full = HaarComp(high, low)
    ptCount = length(high) + length(low);
    full = zeros(1, ptCount);
    full(1:2:ptCount-1) = low + high;
    full(2:2:ptCount) = low - high;
end

% Creates low frequency Haar decomposition matrix for a set of n values
function lowMat = GetLowHaarDecompMat(n)
    lowMat = zeros(n, n/2);
    lowMat([1:n+2:n*n/2]) = 1/2;
    lowMat([2:n+2:n*n/2]) = 1/2;
end

% Creates high frequency Haar decomposition matrix for a set of n values
function highMat = GetHighHaarDecompMat(n)
    highMat = zeros(n, n/2);
    highMat([1:n+2:n*n/2]) = 1/2;
    highMat([2:n+2:n*n/2]) = -1/2;
end

