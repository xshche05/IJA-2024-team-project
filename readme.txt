Name of the Project: Jednoduchý 2D simulátor mobilních robotů

Team Name: Tým xsulta01

Team Members:
- Kirill Shchetiniuk (xshche05)
- Artur Sultanov (xsulta01)

Description:
This project is a simple 2D simulator for mobile robots.
It allows users to interactively add, move, and manage mobile robots in a simulated environment.
The application provides functionalities to simulate robot movements, obstacle placements,
and various robot interactions within a defined area.

How to run the project:

1. Build program and documentation:

  mvn

2. Program run:

  java -jar robot-1.0-jar-with-dependencies.jar

Final JAR archive with dependencies is located in the base directory of the project and is named robot-1.0-jar-with-dependencies.jar.
Automatically generated Javadoc documentation is located in the javadoc folder in the base directory of the project.

Additional commands:

ALL-IN-ONE command to build program, create documentation, and run the program:

  mvn && java -jar robot-1.0-jar-with-dependencies.jar

Only build program:

  mvn package

Only create the documentation:

  mvn javadoc:javadoc
