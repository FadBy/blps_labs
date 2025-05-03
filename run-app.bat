@echo off
set "JAVA_HOME=C:\java\liberica-17.0.7"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "GRADLE_USER_HOME=C:\gradle_home"
echo Using Java version:
java -version
mkdir %GRADLE_USER_HOME% 2>nul
call gradlew clean --no-daemon --gradle-user-home=%GRADLE_USER_HOME%
call gradlew bootRun --no-daemon --gradle-user-home=%GRADLE_USER_HOME% 