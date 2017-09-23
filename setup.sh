#!/bin/bash



#if [ ! -f indonesia-latest.osm.pbf ]; then
#    echo "Downloading the OSM MAP"
#    wget http://download.geofabrik.de/asia/indonesia-latest.osm.pbf
#fi

mvn clean
mvn install dependency:copy-dependencies
mvn install

echo "setup done"
