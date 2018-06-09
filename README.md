![Logo](./OLSystem.png)

## About OLSystem

OLSystem - it's cross-platform system for conducting olympiads, tests, 
control works and individual solutions of tasks on programming. It' adapted for different devices
(mobiles, tablets, laptops and computers). Work with the system takes
place in the web browser. At the moment system is supporting only C, C++, Java and Python languages, 
but another languages will be added soon. Compiler installation directories are taken from the registry 
(windows-version) or from the path variable (linux-version). Also *OLSystem* supports 
administrator room, in which administrator can:
- change basic information about system(title, description and much more); 
- add, remove and change tasks;
- change programming language for passing olympiad or test;
- change time for the that is given for the passage olympiad or test;
- add, remove and change groups or classes for grouping participants;
- remove participants;
- observe for the results of all participants.

This system consists of four main modules:

- [GUI - Vue.js](./GUI)
- [Database - MySQL](./Database)
- [Program for checking tasks - C++](./ProgChecker)
- [The whole system that unites all - Java](./OLSystem)

Each module is a separate unit. Thanks to this, independently of other modules, it is possible to change 
a certain module or completely replace it with another one.

## What you need to build OLSystem
For build *OLSystem* on your computer, you will need to have the following
applications:
- Git
- JDK/JRE
- TomCat
- Maven
- MySQL
- CMake

For Windows, you have to download and install [Git](https://git-scm.com/download), 
[JDK/JRE](http://www.oracle.com/technetwork/java/index-jsp-138363.html), 
[TomCat](https://tomcat.apache.org/download-80), 
[Maven](http://maven.apache.org/download.cgi),
[MySQL](https://dev.mysql.com/downloads/installer/) and [CMake](https://cmake.org/download/). After installation you
need to add system variables for JDK/JRE and Maven. 

OS X users should install Homebrew. Once Homebrew is installed, run:
    
    brew install git
    brew install java
    brew install tomcat
    brew install maven
    brew install mysql
    brew install cmake

Linux/BSD users should use their appropriate package managers to install:

    apt-get install git
    apt-get install default-jre
    apt-get install default-jdk
    apt-get install tomcat8
    apt-get install maven
    apt-get install mysql-server
    apt-get install cmake

## How to build OLSystem
Clone a copy of the main OLSystem git repo by running:

    git clone https://github.com/VladimirBalun/OLSystem.git

Enter the OLSystem directory and run the build script on Windows:

    build.bat

Enter the OLSystem directory and run the build script on OS X or Linux/BSD:

    ./build.sh
    
If you don't have installing programming languages(compilers) on your computer, you can 
run script and choose the ones you need programming languages(C, C++, Python and Java 
must be already installed after building OLSystem) for passing olympiad. On Linux/BSD:

    ./languages.sh

Script for Windows will be added later...

____
Documentation will be supplemented over time, but if you have any questions, please 
contact: vladimirbalun@yandex.ru

