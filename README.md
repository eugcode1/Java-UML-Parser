# cmpe202-UML-Parser
Java UML Parser

### Goal:
Create a program to convert Java code into uml diagram

### Libraries/API Used
 - JavaParser library: library used to parse java source code into Abstract Syntax Tree. Reference site: https://github.com/javaparser/javaparser
 - yUML: an online tool for generating UML class diagram. Reference site: https://yuml.me

### Tools Used
 - IntelliJ IDEA Community Edition: IDE for development, testing, and production of project.
 - Eclipse Neon: IDE used for exporting project as executable jar file.

### Instruction
1. Download the umlparser.jar under root directory
2. Open a terminal session and navigate to the directory where the jar is located
3. Run the command in the following format:

    example: java -jar umlparser.jar testcase1\ output1.png

    The command will take two inputs:
    1. testcase path
    2. output diagram path

### UML Limitations
1. Interface is not able to show method lists
2. Function Parameter or Function Return Type does not support plural like 'String[]', it only shows 'String' in the diagram