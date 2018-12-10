# book-service
This is a microservice project using spring boot, with MongoDB as the backend storage. This book-service provides CRUD operations on domain Book via RESTFul APIs (Jason based)

I.  The Steps to run the tests for the book-service (Microservices)

1) Checkout the code from the following git reppository:

git clone https://github.com/vyao888/book-service

2) Startup the MongoDB instance locally, from a separate console window, assuming the default port "27017" using the following comman, for eample on MAC:

victoryao$ mongod

Somethijng as the following will show on the console meaning the MongoDB instance has been started successfully:

2018-12-10T15:58:58.127+0000 I CONTROL  [initandlisten] 
2018-12-10T15:58:58.180+0000 I FTDC     [initandlisten] Initializing full-time diagnostic data capture with directory '/data/db/diagnostic.data'
2018-12-10T15:58:58.181+0000 I NETWORK  [thread1] waiting for connections on port 27017
2018-12-10T15:58:59.025+0000 I FTDC     [ftdc] Unclean full-time diagnostic data capture shutdown detected, found interim file, some metrics may have been lost. OK


3) Run all the tests 
 
victoryao$ cd book-service
book-service victoryao$ mvn clean test

II. The Steps to deploy the book-service (Microservices)

1) Starts the MongoDB if it has not been started (refer to 2) above for running the tests

2) Manually insert book records to mongodb:
    from the command shell type: mongo
    > use canoo-book-service
    > paste the file content from: src/main/resources/books.js
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


3) Deploy the microservice

victoryao$ cd book-service

book-service victoryao$ mvn clean bootstrap:run

Now the book-service application should be running and ready to serve the front app


