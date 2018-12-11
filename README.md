# book-service
A microservice project using Spring Boot, with MongoDB as the backend storage.

The book-service provides CRUD operations on the domain Book via RESTFul APIs (Jason based)

# Checking out and Building, Running Tests, and the Service Deployment

To check out the project and build from source, do the following:

    git clone https://github.com/vyao888/book-service
    cd book-service
    mvn compile

The Java SE 8 is recommended to build the project.

The Steps to run the tests for the book-service (Microservices)

Startup the MongoDB instance locally, from a separate console window, assuming the default port "27017" using the following comman, for eample on MAC:

    victoryao$ mongod

    Somethijng as the following will show on the console meaning the MongoDB instance has been started successfully:

    2018-12-10T15:58:58.127+0000 I CONTROL  [initandlisten]
    2018-12-10T15:58:58.180+0000 I FTDC     [initandlisten] Initializing full-time diagnostic data capture with directory '/data/db/diagnostic.data'
    2018-12-10T15:58:58.181+0000 I NETWORK  [thread1] waiting for connections on port 27017
    2018-12-10T15:58:59.025+0000 I FTDC     [ftdc] Unclean full-time diagnostic data capture shutdown detected, found interim file, some metrics may have been lost. OK


Run all the tests


    book-service victoryao$ mvn clean test


The Steps to deploy the book-service

Starts the MongoDB if it has not been started (refer to above)

Manually insert book records to mongodb from the command shell:

    victoryao$ mongo

    > use canoo-book-service

    > paste the file content from: src/main/resources/books.js:

    db.books.insert(
    [
        { "_id" : "1128820655", "title" : "Effective Java", "authors" : [ "Joshua Bloch" ], "description" : "The 3rd Edition", "publishDate" : ISODate("2017-10-22T23:00:00Z"), "price" : "27.44"},
        { "_id" : "1839855250", "title" : "Effective Java", "authors" : [ "Joshua Bloch" ], "description" : "The 2rd Edition", "publishDate" : ISODate("2012-10-22T23:00:00Z"), "price" : "37.44"},
        { "_id" : "1758600849", "title" : "Effective Java", "authors" : [ "Joshua Bloch" ], "description" : "The 1st Edition", "publishDate" : ISODate("2008-10-22T23:00:00Z"), "price" : "47.44"},
        { "_id" : "1801996137", "title" : "Java 8 in Action", "authors" : [ "Raoul-Gabriel Urma", "Mario Fusco", "Alan Mycroft"], "description" : "Lambdas, Streams, and functional-style programming", "publishDate" : ISODate("2014-08-28T23:00:00Z"), "price" : "43.2"},
        { "_id" : "1683908380", "title" : "Thinking in Java", "authors" : [ "Bruce Eckel" ], "description" : "The 1st Edition", "publishDate" : ISODate("2006-02-10T23:00:00Z"), "price" : "42.76"},
        { "_id" : "1858615848", "title" : "Thinking in Java", "authors" : [ "Bruce Eckel" ], "description" : "One-off", "publishDate" : ISODate("2017-10-22T23:00:00Z"), "price" : "17.44"},
        { "_id" : "1873537007", "title" : "Head First Java", "authors" : [ "Kathy Sierra", "Bert Bates" ], "description" : "The 2rd Edition", "publishDate" : ISODate("2005-02-19T23:00:00Z"), "price" : "21.63"},
        { "_id" : "1346169336", "title" : "Java Concurrency in Practice", "authors" : [ "Brian Goetz" ], "description" : "JDK Concurrency Czar, Sun Microsystems", "publishDate" : ISODate("2006-05-09T23:00:00Z"), "price" : "31.99"},
        { "_id" : "1621573012", "title" : "Java For Dummies", "authors" : [ "Barry A. Burd" ], "description" : "The top-selling beginning Java book", "publishDate" : ISODate("2011-07-15T23:00:00Z"), "price" : "12.20"}
    ]
    );

    > hit return (9 book records should be inserted into the database. Something as the following will get displayed):

    BulkWriteResult({
    	"writeErrors" : [ ],
    	"writeConcernErrors" : [ ],
    	"nInserted" : 9,
    	"nUpserted" : 0,
    	"nMatched" : 0,
    	"nModified" : 0,
    	"nRemoved" : 0,
    	"upserted" : [ ]
    })


Deploy the microservices

    book-service victoryao$ mvn clean spring-boot:run

    Now the book-service application should be running and ready to serve the RESTFul requests

Check if the the deployment is Ok or Not by running the following url in your browser:

    http://localhost:8888/books

    you will see all books inserted into db "canoo-book-service" previously will get displayed as json collection:

    [{"isbn":"1128820655","title":"Effective Java","authors":["Joshua Bloch"],"description":"The 3rd Edition","publishDate":"2017-10-23","price":27.44},{"isbn":"1839855250","title":"Effective Java","authors":["Joshua Bloch"],"description":"The 2rd Edition","publishDate":"2012-10-23","price":37.44},{"isbn":"1758600849","title":"Effective Java","authors":["Joshua Bloch"],"description":"The 1st Edition","publishDate":"2008-10-23","price":47.44},{"isbn":"1801996137","title":"Java 8 in Action","authors":["Raoul-Gabriel Urma","Mario Fusco","Alan Mycroft"],"description":"Lambdas, Streams, and functional-style programming","publishDate":"2014-08-29","price":43.2},{"isbn":"1683908380","title":"Thinking in Java","authors":["Bruce Eckel"],"description":"The 1st Edition","publishDate":"2006-02-10","price":42.76},{"isbn":"1858615848","title":"Thinking in Java","authors":["Bruce Eckel"],"description":"One-off","publishDate":"2017-10-23","price":17.44},{"isbn":"1873537007","title":"Head First Java","authors":["Kathy Sierra","Bert Bates"],"description":"The 2rd Edition","publishDate":"2005-02-19","price":21.63},{"isbn":"1346169336","title":"Java Concurrency in Practice","authors":["Brian Goetz"],"description":"JDK Concurrency Czar, Sun Microsystems","publishDate":"2006-05-10","price":31.99},{"isbn":"1621573012","title":"Java For Dummies","authors":["Barry A. Burd"],"description":"The top-selling beginning Java book","publishDate":"2011-07-16","price":12.20}]






