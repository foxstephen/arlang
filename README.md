# ArLang
There is an associated blog post for this code which explains 
much of what it is about, find [here](https://foxstephen.net/a-language-on-the-jvm).

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
    
    e.g.
    java -jar  target/arlang-compiler.jar 1*9
    java -jar  target/arlang-compiler.jar 100*4
    

# Running
    
    java Arlang
