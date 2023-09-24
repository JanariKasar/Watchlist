# Watchlist instructions

## Running application
Application runs on port 8080 and uses in memory database for sanctioned members. By default, sanctions list is empty. To run the 
application with sample data `test` profile can be used which adds sample data in `SampleDataConfig.java`. Test profile can be 
enabled by adding `spring.profiles.active=test` to application properties or adding command line parameter `-Dspring.profiles.active=test`.

## Finding a sanctioned person by name

Querying for a match returns an object with sanctioned person and match confidence from 0-100 if a match is found. If no match is found reponse is blank 200.
By default, match threshold is set to 30 which means confidence less than 30 is not considered a positive match.

#### Example:
```
curl http://localhost:8080/sanctions/match/Osama%20Bin%20Laden
```

## Managing sanctions list
Project uses Spring Data REST to allow querying and modifying sanctioned members on `/sanctions` endpoint.

### Sample queries

#### Listing all sanctioned members
```
curl http://localhost:8080/sanctions
```
#### Query person by id 1
```
curl http://localhost:8080/sanctions/1
```
#### Adding person:
```
curl -X POST -H "Content-Type: application/json" -d "{\"firstName\":\"Osama\",\"lastName\":\"Bin Laden\"}" http://localhost:8080/sanctions
```
#### Updating person with id 1:
```
curl -X PUT -H "Content-Type: application/json" -d "{\"firstName\":\"Osama\",\"lastName\":\"Bin Laden\",\"endDate\": \"2023-09-24T20:00:00\"}" http://localhost:8080/sanctions/1
```
#### Deleting a person with id 1
```
curl -X DELETE http://localhost:8080/sanctions/1
```
