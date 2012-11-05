% Parses command line arguments
%arg_list = argv();
arg_list = {'data00.tif'};
nargin = 1;
imagefile = [];
keys = [];
trainingMode = 0;
if (nargin == 2)
	trainingMode = 1;
	imagefile = arg_list{1};
	keyfile = arg_list{2};
	rawkeys = fileread(keyfile);
	keys = reshape(rawkeys, 60, 25)';
elseif (nargin == 1)
	imagefile = arg_list{1};
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
printf('done. Found %d features.\n', num);

printf('Mapping features to characters...\n');
% Maps features to characters
result = zeros(25, 60);
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
	xcol = find(unique(label(yMid, :)) == i)-1;
	xMid = floor((xMin + xMax) / 2);
	xrow = find(unique(label(:, xMid)) == i)-1;

	% Either trains OCR or OCRs input
	if (trainingMode == 1)
		%weights = TrainOCR(boundboxG, keys(xrow, xcol), weights);
	else
		%keys(xrow, xcol) = OCR(boundboxG, weights);
	endif
%endfor
printf('done.\n');
%mask = repmat(reshape(1:num, 1, 1, num), [rows(label) columns(label) 1]);
%isolated = repmat(label, [1 1 num]) == mask;
%clear mask;
printf('Output in results.txt.\n');

