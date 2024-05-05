.PHONY: all compile doc run clean

# Default target executed
all: compile doc

# Compile the project and package it into a jar file
compile:
	mvn package

# Generate Javadoc
doc:
	mvn javadoc:javadoc

# Run the application
run:
	java -jar ./target/robot-1.0.jar

# Clean the project
clean:
	mvn clean
