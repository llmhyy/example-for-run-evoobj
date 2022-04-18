# File Format
Given a compiled Jar file, the user need to set up the folder conforming to the EvoSuite format. 
Specifically, given a jar file with name `jarname.jar`, we need to

1. Create a folder with name `index_jarname` (e.g., 0_jhotdraw) to contain the jar file, and
2. Create a folder with name `evosuite-files` in which a `evosuite-properties` file is created to specifiy the runtime classpath
```
CP=lib/*; (use : if you are using linux) commons.jar
PROJECT_PREFIX=
inheritance_file=evosuite-files/inheritance.xml.gz
log.level=warn
```
3. Specify the folder path in `example.Example.main(String[] args)` via
```
SFConfiguration.sfBenchmarkFolder = "D:\\sf100";
```
where the "D:\\sf100" is the directory of `index_jarname` folder.
