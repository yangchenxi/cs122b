#!bin/bash

wget http://infolab.stanford.edu/pub/movies/mains243.xml

wget http://infolab.stanford.edu/pub/movies/actors63.xml

wget http://infolab.stanford.edu/pub/movies/casts124.xml

mvn clean install

mvn exec:java -Dexec.mainClass="com.cs122b.project.Main" -Dexec.cleanupDaemonThreads=false


