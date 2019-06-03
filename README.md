# Hourse traveler service

## Build instructions
Clone this repository: ``git clone https://github.com/Adramalek/travaler.git``
Compile project and download its dependencies: ``mvn clean package``
And start server: ``mvn spring-boot:run``

## Usage instructions
One can use curl to create a journey for our hourse and get its moves count.  
Rest service: ``curl http://localhost:8080/rest/count?width={width}&height={height}&start={start}&end={end}``  
Servlet service: ``curl http://localhost:8080/servlet/count?width={width}&height={height}&start={start}&end={end}``  
Parameters ``width`` and ``height`` should be positive whole integer. They represent a field which our hourse roams.  
Parameters ``start`` and ``end`` should be a string representing coordinates in excel like style ("A1"). They represent start and end points of hourse'e journey.