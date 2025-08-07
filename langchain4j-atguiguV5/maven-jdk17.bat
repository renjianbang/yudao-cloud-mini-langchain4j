@echo off
set MAVEN_HOME=D:\Environment\apache-maven-3.8.1
set JDK17_HOME=D:\Environment\JDK17
set PROJECT_DIR=%cd%

%JDK17_HOME%\bin\java -cp "%MAVEN_HOME%\boot\plexus-classworlds-2.6.0.jar" "-Dclassworlds.conf=%MAVEN_HOME%\bin\m2.conf" "-Dmaven.home=%MAVEN_HOME%" "-Dlibrary.jansi.path=%MAVEN_HOME%\lib\jansi-native" "-Dmaven.multiModuleProjectDirectory=%PROJECT_DIR%" org.codehaus.plexus.classworlds.launcher.Launcher %*