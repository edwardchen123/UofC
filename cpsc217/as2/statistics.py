#CPSC 217 Assignment 2 - Statistics
#Andrew Helwer
#10023875

import math
import sys

file = sys.stdin.readlines()

#input sorting and handling
time = []
linecount = 0
for line in file:
	linecount += 1
	if len(line.split('\t')) < 3:
		print 'One or more data fields are missing in line', linecount
		print 'Line has been discarded... continuing happily'
		print
	elif float(line.split('\t')[0]) != -1:
		time.append(float(line.split('\t')[1]))
	else:
		break

#number of data records
entries = len(time)
print entries, 'data records'

#mean
n = 0
for x in time:
	n += x
mean = float(n/entries)
print 'mean is', mean

#standard deviation
numerator = 0
for x in time:
	numerator += (mean-x)**2
sd = math.sqrt(float(numerator/entries))
print 'standard deviation is', sd

#median
time.sort()
if len(time)%2 == 0:
	a = time[(len(time)-2)/2]
	b = time[len(time)/2]
	median = float((a+b)/2)
else:
	median = time[(len(time)-1)/2]
print 'median is', median
