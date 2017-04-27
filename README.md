# cmpe202-UML-Parser
Java UML Parser

### Goal:
Create a program to convert Java code into uml diagram

### Libraries/API Used
Java Parser: https://github.com/javaparser/javaparser<br/>
yUML: https://yuml.me/

### Instruction
I will export jar file.
The 281 partner will take the jar to run on AWS and export in the webpage

The command will take two inputs:
1. testcase path
2. source diagram path
example: umlparser testcase1 output1

### Development Process
1. I use java parser library to parse java source code into compilation unit lists.
2. For each compilation unit, I am able to tell its property(eg: function, class, variable)
3.

### UML Limitations
1. Interface is not able to show method lists
2. Function Parameter or Function Return Type does not support plural like 'String[]', it only shows 'String' in the diagram