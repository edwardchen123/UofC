% Calculates the next step in the Koch fractal for the given ordered set of points.
function ptsNext = Koch(pts)
    % Calculates number of points in old and new point lists, creates new point list
    ptsCount = length(pts);
    ptsNextCount = ((ptsCount-1)*4)+1;
    ptsNext = zeros(2, ptsNextCount);

    % Copies old points over to new point list to form X0 and X1
    ptsNext(:, 1:4:ptsNextCount) = pts;

    % Calculates X2 according to formula given on assignment sheet
    ptsNext(:, 2:4:ptsNextCount) = (2/3)*pts(:, 1:ptsCount-1) + (1/3)*pts(:, 2:ptsCount);

    % Calculates X3 according to formula given on assignment sheet
    ptsNext(:, 3:4:ptsNextCount) = (1/2)*pts(:, 1:ptsCount-1) + (1/2)*pts(:, 2:ptsCount) + ...
                            (sqrt(3)/6)*(cat(1, pts(2, 1:ptsCount-1)-pts(2, 2:ptsCount), ...
                                                pts(1, 2:ptsCount)-pts(1, 1:ptsCount-1)));

    % Calculates X4 according to formula given on assignment sheet
    ptsNext(:, 4:4:ptsNextCount) = (1/3)*pts(:, 1:ptsCount-1) + (2/3)*pts(:, 2:ptsCount);

