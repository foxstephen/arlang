# ArLang
A very basic arithmetic language developed on top of the JVM.
This source code includes the compiler, the language
takes a single binary operation and outputs the result
i.e. the list below shows 4 different valid programs.

 - 1*9
 - 4/2
 - 100*40

# Compiling

    mvn clean package
    java -jar target/arlang-compiler.jar <program>
    
    i.e.
    java -jar  target/arlang-compiler.jar 1*9
    java -jar  target/arlang-compiler.jar 100*4
    

# Running
    
    java Arlang
