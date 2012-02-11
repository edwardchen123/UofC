#CPSC 217 Assignment 2 - Computational Linguistics
#Andrew Helwer
#10023875

import sys

file = sys.stdin.readlines()

#input sorting and handling
d = {}
linecount = 0
wordcount = 0
for line in file:
	linecount += 1
	for word in line.split(' '):
		word = word.replace('\n','')
		wordcount += 1
		if word not in d:
			d[word] = 1
		else:
			d[word] += 1

#print words in order of magnitude of occurence
n = wordcount
while n > 0:
	for word in d:
		if d[word] == n:
			print word, d[word]
	n -= 1

#prints file info
print
print linecount, 'sentences'
print wordcount, 'words total'
print len(d), 'unique words'
