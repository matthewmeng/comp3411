
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Action.java \
	ActionKnowledge.java \
	Agent.java \
	Direction.java \
	Environment.java \
	Explorer.java \
	HeuristicDistance.java \
	HeuristicManhattanDistance.java \
	KnowledgeBase.java \
	MapExpander.java \
	Path.java \
	PathFinder.java \
	PathFinderAStar.java \
	PathFinderThroughWater.java \
	Position.java \
	Stepping.java \
	StoneStrategy.java \
	TileBaseMap.java \
	ToolKnowledge.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
