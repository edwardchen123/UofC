% Finds all connected features in image
grayscale = imread("data00.tif");
q = quantile(grayscale(:), 0.20);
threshold = q*ones(size(grayscale));
binary = grayscale < threshold;
[label, num] = bwlabel(binary, 8);

% Deals with each features separately
%for i = 1:num,
i = 16;
	% Isolates each feature
	mask = i*ones(size(label));
	isolated = label == mask;

	% Calculates bounding box of feature
	pixelCols = floor(find(isolated)/rows(isolated))+1;
	pixelRows = floor(find(isolated')/columns(isolated))+1;
	xMin = min(pixelCols);
	xMax = max(pixelCols);
	yMin = min(pixelRows);
	yMax = max(pixelRows);
	boundboxG = grayscale(yMin:yMax, xMin:xMax);
	boundboxB = isolated(yMin:yMax, xMin:xMax);

	% Calculates horizontal and vertical order
	yMid = floor((yMin + yMax) / 2);
	horizOrder = find(unique(label(yMid, :)) == i)-1;
	xMid = floor((xMin + xMax) / 2);
	vertOrder = find(unique(label(:, xMid)) == i)-1;

%end
%mask = repmat(reshape(1:num, 1, 1, num), [rows(label) columns(label) 1]);
%isolated = repmat(label, [1 1 num]) == mask;
%clear mask;
