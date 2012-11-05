% Calculates weights for this particular image
function vars = ExtractInfo(imageG, imageB)
	% x1 is intensity
	data = imageG;
	space = size(data(:))(1);
	x1 = sum(data(:)) / space;
	% x2 is horizontal symmetry
	x2 = 10*sum((data - fliplr(data))(:)) / space;
	% x3 is vertical symmetry
	x3 = 10*sum((data - flipud(data))(:)) / space;
	vars = [x1 x2 x3];
endfunction

% Trains weights to recognize characters
function weights = TrainOCR(imageG, imageB, symbol, weights)
	vars = ExtractInfo(imageG);	
	idx = symbol-47;
	weights(idx, :, 1) += vars;
	weights(idx, :, 2) += [1 1 1];
endfunction

% OCRs given character image
function symbol = OCR(imageG, imageB, weights)
	vars = ExtractInfo(imageG);
	vals = repmat(vars, 10, 1);
	avg = (weights(:, :, 1) ./ weights(:, :, 2));
	diff = sum(abs(vals - avg)');
	[m, mix] = min(diff);
	symbol = mix-1;
endfunction

