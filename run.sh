#!/bin/sh
clear
rm -R out
java -classpath classes:lib/postgresql-9.2-1002.jdbc4.jar:. main.Main
