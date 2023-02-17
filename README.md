# Despg Extension using GraphHopper for Route Calculation

## [Despg](https://gitlab.com/lobequadrat/despg)
Discrete Event Simulation Playground<br>
<br>
[This project is licensed under the Eclipse Public License 2.0.](https://gitlab.com/lobequadrat/despg/-/blob/main/LICENSE)<br>
<br>
Website: [despg.dev](https://despg.dev)<br>
<br>
[Was ist despg?](https://despg.dev/was-ist-despg/)

### get started
[onboarding](https://despg.dev/erste-schritte/)

## [GraphHopper](https://github.com/graphhopper/graphhopper)
GraphHopper is a fast and memory-efficient routing engine released under Apache License 2.0. It can be used as a <br>
Java library or standalone web server to calculate the distance, time, turn-by-turn instructions and many road <br> 
attributes for a route between two or more points. Beyond this "A-to-B" routing it supports "snap to road", Isochrone <br>
calculation, mobile navigation and more. GraphHopper uses OpenStreetMap and GTFS data by default and it can <br>
import other data sources too.

## Installation
- Make sure to use at least Java 8 and higher.
- Follow the Link https://download.geofabrik.de/europe/germany-latest.osm.pbf to download the newest osm.pbf File of your Germany<br>
- Replace the File Path inside the Class Routing.java to your Local File Path.
````
hopper.setOSMFile("" + "your_local_file_path.osm.pbf");
````
- Replace the Cache File Path inside the Class Routing.java to your Local Cache File Path.
```
hopper.setGraphHopperLocation("your_cache_path");
```
 
### VM - Arguments
- Use at least 5Gb RAM
- Disclaimer, when first executing it may take a while until the Map is fully loaded.
```java
-Xmx5g -Xms5g
```

### Library
- Follow the Link to download the [GraphHopper Jar](https://github.com/graphhopper/graphhopper/releases/download/6.0/graphhopper-web-6.0.jar) 
- Create a new User Library inside your Java Project
- Add the User Library to your Java Project


## Server - Setup
- Disclaimer, when first executing it may take a while until the Map is fully loaded.
- Download the [config.yml](despg/src/despgutils/config.yml).
- Change the Parameter datareader.file to your_local_file_path.osm.pbf.
```
graphhopper:
 
  datareader.file: your_local_file_path.osm.pbf
  graph.location: germany-gh
```
- To Start the Server use the following Command
```
java -Xmx5g -Xms5g -jar your_local_file_path_of_the_graphhopper.jar server your_local_file_path_of_the_config.yml
```
