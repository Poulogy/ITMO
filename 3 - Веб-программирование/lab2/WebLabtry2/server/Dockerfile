FROM openjdk
WORKDIR /usr/myapp
COPY lib/fastcgi-lib.jar /usr/myapp/lib/fastcgi-lib.jar
COPY lib/gson-2.13.2.jar /usr/myapp/lib/gson-2.13.2.jar
COPY server/src /usr/myapp/src
RUN javac -cp lib/fastcgi-lib.jar:lib/gson-2.13.2.jar -d bin src/App/*.java src/App/*/*.java
EXPOSE 1009
CMD ["java", "-DFCGI_PORT=1009", "-cp", "lib/fastcgi-lib.jar:lib/gson-2.13.2.jar:bin", "App.Main"]
