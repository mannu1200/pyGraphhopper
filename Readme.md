# pyGraphhopper 

A utility which allows you to use openstreetmap using [graphhopper](https://github.com/graphhopper/graphhopper) in python.
It uses py4j to bridge between java and python.

### Prerequisites

1. Java
2. Python 
3. py4j python module globally
4. The pbf file of open street map for the locality you want to work on. (e.g. http://download.geofabrik.de/asia/indonesia-latest.osm.pbf)

### Installing

1. Get the pbf file desired location. e.g.
```
wget http://download.geofabrik.de/asia/indonesia-latest.osm.pbf
```

2. Install the java dependencies

```
mvn clean
mvn install dependency:copy-dependencies
mvn install
```

3. Start the java gateway server:

```
java -cp ./target/route_distance-1.0-SNAPSHOT.jar:./target/dependency/* pygraphhopper.Gateway
```

4. You can call the APIs from python now.

```
from py4j.java_gateway import JavaGateway
#Creating bridge to Java gateway!
gateway = JavaGateway()
routeCalculator = gateway.entry_point.getRCObject("osm_work_dir", "car", "indonesia-latest.osm.pbf")
routeCalculator.getDistance(orig_lat, orig_lon, dest_lat, dest_lon, "car")
```


## APIs:

1. `getRouteDistance: routeCalculator.getDistance`
    
    This function returns the actual route distance between two geolocations given the type of compute (walk/car)

2. `processFiles: routeCalculator.mainHelper`

    To calculate the route distance for a set of geolocations in a csv.

## TODO:
Add more APIs provided by graphhopper, only calculate route distance is being provided right now.

## Contribution 
Be the part of this `revolutionary world changing project` :P . Pick something from TODO list and raise PR. :)

Earn as many thanks as possible.

## License

This project is licensed under the MIT License.

## Acknowledgments

* [Graphhopper](https://github.com/graphhopper/graphhopper)