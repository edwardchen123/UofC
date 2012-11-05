source('ml.m');

% Parses command line arguments
arg_list = argv();
imagefile = [];
keys = [];
trainingMode = 0;
if (nargin == 2)
	trainingMode = 1;
	imagefile = arg_list{1};
	keyfile = arg_list{2};
	rawkeys = fileread(keyfile);
	keys = reshape(rawkeys, 62, 25)';
	load weights.mat
elseif (nargin == 1)
	imagefile = arg_list{1};
	load weights.mat
else
	printf('ERROR: invalid number of arguments\n');
endif

printf('Finding image features... ');
% Converts grayscale image to binary
grayscale = imread(imagefile);
q = quantile(grayscale(:), 0.20);
threshold = q*ones(size(grayscale));
binary = (grayscale < threshold);

% Removes filename from image
hasFeatureOnRow = any(binary');
edge = conv(hasFeatureOnRow, [-1 1]);
newTop = find(edge == 1);
binary = binary(newTop:rows(binary), :);
grayscale = grayscale(newTop:rows(grayscale), :);

% Gets features using provided bwlabel function
[label, num] = bwlabel(binary, 8);
printf('done. Found %d distinct features.\n', num);

% Maps features to characters
printf('Mapping features to characters...');
result = zeros(25, 60);
for i = 1:num
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
	xcol = find(unique(label(yMid, :)) == i)-1;
	xMid = floor((xMin + xMax) / 2);
	xrow = find(unique(label(:, xMid)) == i)-1;

	% Either trains OCR or OCRs input
	if (trainingMode == 1)
		weights = TrainOCR(boundboxG, boundboxB, keys(xrow, xcol), weights);
	else
		result(xrow, xcol) = OCR(boundboxG, boundboxB, weights);
	endif
endfor
printf('done.\n');
printf('Output in results.txt.\n');
save weights.mat weights
dlmwrite('results.txt', result, '', 0, 0);

