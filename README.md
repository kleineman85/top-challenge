# TOP Challenge
This is an API that makes it possible to do a text search for books by using the Google Books API.

## Installation
Clone the repository and run mvn clean install from the root directory. 
You can make use of Maven Wrapper (in the root directory) if you don't have Maven installed.
#### On Unix
./mvnw clean install (on Unix)
#### On Windows
mvnw.cmd clean install

## Usage
### Run the application
Go to the target directory and run the application.
#### On Unix
java -jar top-challenge-0.0.1-SNAPSHOT.jar
#### On Windows
java top-challenge-0.0.1-SNAPSHOT.jar

### Use the TOP Challenge API
Open a browser and go to http://localhost:8080/api/v1/books?q=mySearchString
replace 'mySearchString' for any (url encoded) string you want to search on.

#### filter on language
Use the langRestrict query parameter to filter on language, this should match the two-letter ISO-639-1 format.
i.e. http://localhost:8080/api/v1/books?q=mySearchString&langRestrict=nl
