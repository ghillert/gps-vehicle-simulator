GPS Vehicle Simulator
=====================

* KML Integration
* GPSD Integration
* Generic Messaging Integration

Obtaining of Directions:

* Google Directions Api
  - Direction input (From + To address) can be provided via API
  - Direction input can be loaded from an input file
* KML file (XML)

# Running the Application

## Requirements

* Java 8
* Maven
* Access to the Google Directions Api: https://console.developers.google.com/project

Please ensure that you set your Google API key for the Google Directions API. E.g.

you can set the relevant property in the `application.yml` file:

```
gpsSimmulator:
  googleApiKey: 1234567
```

You can also provide the key via the command line:

	$ gpsSimmulator_googleApiKey=1234567 java -jar target/gps-vehicle-simulator-1.x.x.BUILD-SNAPSHOT.jar

So you can build the project e.g. using:

	$ mvn clean package -DgpsSimmulator.googleApiKey=1234567

## Execution

	$ gpsSimmulator_googleApiKey=1234567 java -jar target/gps-vehicle-simulator-1.x.x.BUILD-SNAPSHOT.jar

Start the Washington DC simulation by opening:

	http://localhost:8080/api/dc

This will create 3 vehicles driving with 40 km/h through Washington DC.

Open the `gps.kml` file in Google Earth. You should see 3 place-marks in the Washington DC area.

## REST Api

* GET http://localhost:8080/api/dc (Loads directions from JSON file and Google Directions API))
* GET http://localhost:8080/api/status
* GET http://localhost:8080/api/cancel (Cancels all vehicles)
* GET http://localhost:8080/api/start (Loads directions from KML file)
* GET http://localhost:8080/api/directions

