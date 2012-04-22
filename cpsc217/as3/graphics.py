#CPSC 217 Assignment 3
#Andrew Helwer

#The only source I used was Prof. Aycock's website for the command /
#to change the screen size. Other than that this is all from scratch.

import random
import turtle
turtle.tracer(False)
turtle.up()

#source: http://pages.cpsc.ucalgary.ca/~aycock/217/
turtle.reset()
turtle._root.geometry('1000x1000')
turtle.reset()

#Calculates colors for turtle
def tcol(r,g,b):
	return float(r)/255.0,float(g)/255.0,float(b)/255.0

#Color scheme
bgcolor = tcol(40,40,40)
linecolor = tcol(0,0,0)
bottomcolor = tcol(90,90,90)
frontcolor = tcol(80,80,80)
rightcolor = tcol(110,110,110)
topcolor = tcol(140,140,140)
leftcolor = tcol(130,130,130)

#Background
turtle.color(bgcolor)
turtle.goto(500,500)
turtle.down()
turtle.fill(True)
turtle.goto(500,-500)
turtle.goto(-500,-500)
turtle.goto(-500,500)
turtle.goto(500,500)
turtle.fill(False)
turtle.up()

#z axis is divided into 21 planes, with the first occuring 10 units /
#into the screen, and another occuring every 1 unit after that.
#The observer is defined to be one unit away from the screen.

#Calculates apparent size of planes projected on screen, from center:
screen = []
for i in range(0, 21):
	screen.append(float(10)/float(11+i)*500)

#Each plane is composed of 20x20 squares. A cube is defined as an area /
#sandwiched between two z planes with corresponding (x,y) coordinates.
#There are therefore 8000 (20x20x20) possible cube locations in this system.
#The planes are standard (x,y) cartesian planes. The z coordinate, however, /
#increases from 1 (closest plane) to 21 (furthest plane). A cube's coordinate /
#is its bottom left vertice on the closer plane, therefore giving xE[-10,9], /
#yE[-10,9], and zE[1,20]

#Creates a dictionary in which the (x,y,z) coordinate of any cube may be /
#entered, and a list of the (x,y) coordinates of its vertices returned:

cube = {}
for z in range(1,21):
	for y in range(-10,10):
		for x in range(-10,10):
			vert = []
			#(x,y) values on close plane
			x1 = x/10.0*screen[z-1]
			x2 = (x+1)/10.0*screen[z-1]
			y1 = y/10.0*screen[z-1]
			y2 = (y+1)/10.0*screen[z-1]
			#(x,y) values on far plane
			x3 = x/10.0*screen[z]
			x4 = (x+1)/10.0*screen[z]
			y3 = y/10.0*screen[z]
			y4 = (y+1)/10.0*screen[z]
			#puts vertices in list as tuples
			vert.append((x1,y1))
			vert.append((x2,y1))
			vert.append((x2,y2))
			vert.append((x1,y2))
			vert.append((x3,y3))
			vert.append((x4,y3))
			vert.append((x4,y4))
			vert.append((x3,y4))
			#puts list in dictionary
			cube[(x,y,z)] = vert

def stars():
	starpos = []
	turtle.color('white')
	for n in range(1,5000):
		x = random.randrange(-500,500)
		y = random.randrange(-500,500)
		starpos.append((x,y))
	for star in starpos:
		turtle.up()
		turtle.goto(star)
		turtle.down()
		turtle.fill(True)
		turtle.circle(0.1)
		turtle.fill(False)
		turtle.up()

def eclipse():
	turtle.color('white')
	turtle.goto(-390,225)
	turtle.down()
	turtle.fill(True)
	turtle.circle(50)
	turtle.fill(False)
	turtle.up()
	turtle.color('black')
	turtle.goto(-388,223)
	turtle.down()
	turtle.fill(True)
	turtle.circle(50)
	turtle.fill(False)
	turtle.up()

#The monolith is not "just a rectangle." It is a monolith.
def monolith():
	x = -175 #top left
	y = 125
	turtle.color('black')
	turtle.goto(x,y)
	turtle.down()
	turtle.fill(True)
	turtle.goto(x+100,y)
	turtle.goto(x+100,y-200)
	turtle.goto(x,y-200)
	turtle.goto(x,y)
	turtle.fill(False)
	turtle.up()

def planet():
	turtle.color('black')
	turtle.goto(0,-5000)
	turtle.down()
	turtle.fill(True)
	turtle.circle(2380)
	turtle.fill(False)	
	turtle.up()

#Since cubes have different faces showing when viewed from different /
#angles, I had to make four different subsystems, one for each quadrant.
#Each subsystem has two parts - design and draw. Design is where I create /
#a list of cubes I want to draw in that quadrant (the order of this list /
#is rather important) and draw is where I send this list of cubes to /
#actually be drawn.

def quad1design():
	cubes = []

	for y in range(-8,-4): #toproot
		cubes.append(cube[4,-y,13])
	for x in range(-3,1):
		cubes.append(cube[-x,5,13])
	for y in range(-5,-2):
		cubes.append(cube[2,-y,12])
	cubes.append(cube[1,0,13])
	for y in range(-3,1): #bottomroot (1,0,12)
		cubes.append(cube[1,-y,12])
	cubes.append(cube[1,2,11])
	cubes.append(cube[0,1,12])
	for x in range(-6,-3):
		cubes.append(cube[-x,6,12])
	for z in range(-11,-8):
		cubes.append(cube[6,6,-z])
	cubes.append(cube[5,0,10])
	for y in range(-6,1): #bottomroot (5,0,9)
		cubes.append(cube[5,-y,9])
	cubes.append(cube[4,1,9])
	for z in range(-8,-6):
		cubes.append(cube[5,2,-z])
	for y in range(-5,1):
		cubes.append(cube[7,-y,8])
	for y in range(-6,-3):
		cubes.append(cube[9,-y,7])
	for x in range(-8,-6):
		cubes.append(cube[-x,5,7])
	for y in range(-8,-5): #toproot
		cubes.append(cube[9,-y,6])
	for y in range(-4,-1):
		cubes.append(cube[9,-y,6])
	for x in range(-8,-4):
		cubes.append(cube[-x,2,6])
	for x in range(-9,-7): #bottomroot (8,0,5)
		cubes.append(cube[-x,0,5])
	for z in range(-7,-4):
		cubes.append(cube[7,0,-z])

	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad1draw(TF,col,vert)

def quad2design():
	cubes = []

	for x in range(-5,-1):
		cubes.append(cube[x,6,12])
	for y in range(-5,-2):
		cubes.append(cube[-5,-y,12])
	for y in range(-8,-4): #toproot
		cubes.append(cube[-5,-y,11])
	for z in range(-13,-11):
		cubes.append(cube[-1,6,-z])
	cubes.append(cube[-1,5,13])
	for y in range(-5,-2):
		cubes.append(cube[-10,-y,11])
	for y in range(-8,-4): #toproot
		cubes.append(cube[-10,-y,10])
	for z in range(-11,-2):
		cubes.append(cube[-9,5,-z])
	for y in range(-5,1): #bottomroot (-8,0,10)
		cubes.append(cube[-8,-y,10])
	for z in range(-12,-6):
		cubes.append(cube[-5,2,-z])
	for z in range(-10,-2):
		cubes.append(cube[-10,3,-z])
	cubes.append(cube[-10,2,7])
	for x in range(-10,-3):
		cubes.append(cube[x,1,7])
	for y in range(-5,1): #bottomroot (-9,0,3)
		cubes.append(cube[-9,-y,3])
	cubes.append(cube[-4,0,8])
	cubes.append(cube[-4,0,7]) #bottomroot (-4,0,7)

	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad2draw(TF,col,vert)

def quad3design():
	cubes = []

	for y in range(-7,0): #toproot
		cubes.append(cube[-8,y,10])
	for x in range(-7,0):
		cubes.append(cube[x,-7,10])
	for z in range(-12,-6):
		for x in range(-4,0):
			cubes.append(cube[x,-4,-z])
	cubes.append(cube[-5,-3,7])
	for y in range(-3,0): #toproot
		cubes.append(cube[-4,y,7])
	cubes.append(cube[-3,-1,7])
	cubes.append(cube[-4,-2,6])
	for z in range(-4,-1):
		for y in range(-7,-4):
			for x in range(-10,-7):
				cubes.append(cube[x,y,-z])
	for y in range(-4,0):
		cubes.append(cube[-9,y,3]) #toproot

	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad3draw(TF,col,vert)

def quad4design():
	cubes = []

	for z in range(-12,-6):
		for x in range(-5,1):
			cubes.append(cube[-x,-4,-z])	
	cubes.append(cube[2,-1,12])
	for y in range(-3,0): #toproot
		cubes.append(cube[1,y,12])
	cubes.append(cube[1,-2,11])
	cubes.append(cube[0,-3,12])
	cubes.append(cube[6,-1,9])
	for y in range(-3,0): #toproot
		cubes.append(cube[5,y,9])
	cubes.append(cube[5,-2,8])
	cubes.append(cube[4,-3,9])
	for x in range(-9,1):
		cubes.append(cube[-x,-7,10])
	for z in range(-9,-5):
		cubes.append(cube[9,-7,-z])
	for z in range(-10,-7):
		cubes.append(cube[0,-7,-z])
	cubes.append(cube[5,-10,7])
	cubes.append(cube[5,-9,8])
	for x in range(-6,0):
		cubes.append(cube[-x,-9,7])
	for y in range(-9,-6):
		cubes.append(cube[0,y,7])
	for y in range(-7,0): #toproot
		cubes.append(cube[9,y,5])
	cubes.append(cube[5,-8,7])
	cubes.append(cube[5,-9,6])

	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad4draw(TF,col,vert)

def quad1top():
	cubes = []
	
	for z in range(-10,-3):
		cubes.append(cube[9,9,-z])
	for z in range(-9,-7):
		cubes.append(cube[8,9,-z])
	for z in range(-11,-5):
		cubes.append(cube[7,9,-z])
	for z in range(-15,-10):
		cubes.append(cube[6,9,-z])
	for z in range(-7,-3):
		cubes.append(cube[6,9,-z])
	for z in range(-12,-10):
		cubes.append(cube[5,9,-z])
	for z in range(-9,-3):
		cubes.append(cube[5,9,-z])
	for z in range(-13,-8):
		cubes.append(cube[4,9,-z])
	for z in range(-5,-4):
		cubes.append(cube[4,9,-z])
	for z in range(-12,-7):
		cubes.append(cube[3,9,-z])
	for z in range(-5,-1):
		cubes.append(cube[3,9,-z])
	for z in range(-17,-3):
		cubes.append(cube[2,9,-z])
	for z in range(-18,-12):
		cubes.append(cube[1,9,-z])
	for z in range(-7,-2):
		cubes.append(cube[1,9,-z])
	for z in range(-16,-11):
		cubes.append(cube[0,9,-z])
	for z in range(-5,-1):
		cubes.append(cube[0,9,-z])
	
	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad1draw(TF,col,vert)

def quad2top():
	cubes = []

	for z in range(-17,-13):
		cubes.append(cube[-10,9,-z])
	cubes.append(cube[-10,9,10])
	for z in range(-8,-5):
		cubes.append(cube[-10,9,-z])
	for z in range(-18,-4):
		cubes.append(cube[-9,9,-z])
	for z in range(-17,-5):
		cubes.append(cube[-8,9,-z])
	for z in range(-14,-6):
		cubes.append(cube[-7,9,-z])
	for z in range(-10,-8):
		cubes.append(cube[-6,9,-z])
	for z in range(-11,-8):
		cubes.append(cube[-5,9,-z])
	for z in range(-10,-9):
		cubes.append(cube[-4,9,-z])
	for z in range(-12,-5):
		cubes.append(cube[-3,9,-z])
	for z in range(-15,-3):
		cubes.append(cube[-2,9,-z])
	for z in range(-16,-11):
		cubes.append(cube[-1,9,-z])
	for z in range(-7,-2):
		cubes.append(cube[-1,9,-z])

	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad2draw(TF,col,vert)

#Rationalization for purists: in 3 dimensions, S/Z and /
#J/L pieces are the same, hence I don't need to make two.
def tetris():
	cubes = []
	for y in range(-4,0): #I piece (quad 2)
		cubes.append(cube[-3,-y,17])
	cubes.append(cube[-6,9,3])#J/L piece (quad 2)
	for z in range(-5,-2):
		cubes.append(cube[-5,9,-z])
	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad2draw(TF,col,vert)

	cubes = []
	for x in range(-4,-2): #Z/S piece (quad 3)
		cubes.append(cube[x,-8,5])
	for x in range(-5,-3):
		cubes.append(cube[x,-7,5])
	for z in range(-10,-8): #O piece (quad 3)
		for y in range(-4,-2):
			cubes.append(cube[-10,y,-z])
	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad3draw(TF,col,vert)

	cubes = []
	for y in range(-4,-1): #T piece (quad 4)
		cubes.append(cube[9,y,10])
	cubes.append(cube[8,-3,10])
	for vert in cubes:
		col = bgcolor #placeholder
		TF = True
		quad4draw(TF,col,vert)

#The algorithm by which these functions draw the cubes is quite simple.
#The first time through, the function creates a 'stamp' in the shape of /
#the cube, removing lines in the area that would otherwise show through.
#After that, the function calls itself with changed values (recursion!), /
#and draws the outline of the cube.

def quad1draw(TF,col,vert):
	turtle.goto(vert[0])
	turtle.color(col)
	turtle.down()
	#left face
	turtle.fill(TF)
	if TF==True:
		turtle.color(leftcolor)
	turtle.goto(vert[4])
	turtle.goto(vert[7])
	turtle.goto(vert[3])
	turtle.goto(vert[0])
	turtle.fill(False)
	#bottom face
	turtle.fill(TF)
	if TF==True:
		turtle.color(bottomcolor)
	turtle.goto(vert[4])
	turtle.goto(vert[5])
	turtle.goto(vert[1])
	turtle.goto(vert[0])
	turtle.fill(False)
	#front face
	turtle.fill(TF)
	if TF==True:
		turtle.color(frontcolor)
	turtle.goto(vert[1])
	turtle.goto(vert[2])
	turtle.goto(vert[3])
	turtle.goto(vert[0])
	turtle.fill(False)
	turtle.up()
	if TF == False:
		return
	col = linecolor
	TF = False
	quad1draw(TF,col,vert)

def quad2draw(TF,col,vert):
	turtle.goto(vert[1])
	turtle.color(col)
	turtle.down()
	#right face
	turtle.fill(TF)
	if TF==True:
		turtle.color(rightcolor)
	turtle.goto(vert[5])
	turtle.goto(vert[6])
	turtle.goto(vert[2])
	turtle.goto(vert[1])
	turtle.fill(False)
	#bottom face
	turtle.fill(TF)
	if TF==True:
		turtle.color(bottomcolor)
	turtle.goto(vert[5])
	turtle.goto(vert[4])
	turtle.goto(vert[0])
	turtle.goto(vert[1])
	turtle.fill(False)
	#front face
	turtle.fill(TF)
	if TF==True:
		turtle.color(frontcolor)
	turtle.goto(vert[2])
	turtle.goto(vert[3])
	turtle.goto(vert[0])
	turtle.goto(vert[1])
	turtle.fill(False)
	turtle.up()
	if TF == False:
		return
	col = linecolor
	TF = False
	quad2draw(TF,col,vert)

def quad3draw(TF,col,vert):
	turtle.goto(vert[2])
	turtle.color(col)
	turtle.down()
	#right face
	turtle.fill(TF)
	if TF==True:
		turtle.color(rightcolor)
	turtle.goto(vert[6])
	turtle.goto(vert[5])
	turtle.goto(vert[1])
	turtle.goto(vert[2])
	turtle.fill(False)
	#top face
	turtle.fill(TF)
	if TF==True:
		turtle.color(topcolor)
	turtle.goto(vert[6])
	turtle.goto(vert[7])
	turtle.goto(vert[3])
	turtle.goto(vert[2])
	turtle.fill(False)
	#front face
	turtle.fill(TF)
	if TF==True:
		turtle.color(frontcolor)
	turtle.goto(vert[3])
	turtle.goto(vert[0])
	turtle.goto(vert[1])
	turtle.goto(vert[2])
	turtle.fill(False)
	turtle.up()
	if TF == False:
		return
	col = linecolor
	TF = False
	quad3draw(TF,col,vert)

def quad4draw(TF,col,vert):
	turtle.goto(vert[3])
	turtle.color(col)
	turtle.down()
	#left face
	turtle.fill(TF)
	if TF==True:
		turtle.color(leftcolor)
	turtle.goto(vert[7])
	turtle.goto(vert[4])
	turtle.goto(vert[0])
	turtle.goto(vert[3])
	turtle.fill(False)
	#top face
	turtle.fill(TF)
	if TF==True:
		turtle.color(topcolor)
	turtle.goto(vert[7])
	turtle.goto(vert[6])
	turtle.goto(vert[2])
	turtle.goto(vert[3])
	turtle.fill(False)
	#front face
	turtle.fill(TF)
	if TF==True:
		turtle.color(frontcolor)
	turtle.goto(vert[0])
	turtle.goto(vert[1])
	turtle.goto(vert[2])
	turtle.goto(vert[3])
	turtle.fill(False)
	turtle.up()
	if TF == False:
		return
	col = linecolor
	TF = False
	quad4draw(TF,col,vert)

def main():
	stars() #creates stars
	eclipse() #object 1 (eclipsed star)
	monolith() #object 2 (monolith [2001: A Space Odyssey])
	planet() #object 3 (dark planet)
	quad1top() #object 4 (ceiling in quad 1)
	quad2top() #object 5 (ceiling in quad 2)
	quad1design() #object 6 (misc. cubes in quad 1)
	quad2design() #object 7 (misc. cubes in quad 2)
	quad3design() #object 8 (misc. cubes in quad 3)
	quad4design() #object 9 (misc. cubes in quad 4)
	tetris() #object 10 (tetris pieces)

main()

raw_input()
