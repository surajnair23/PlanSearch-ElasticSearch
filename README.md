# PlanSearch-ElasticSearch

RESTful Spring Boot Web Service to connect with Redis in-memory caching system to store and retreieve JSON payload using GET/POST/PUT/DELETE/PATCH. 

Implementation includes ElasticSearch and Kibana for visualization of payload data.

## Installation

1) [Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/current/windows.html)
2) [Kibana](https://www.elastic.co/guide/en/kibana/current/windows.html)
3) [Redis](https://redislabs.com/ebook/appendix-a/a-3-installing-on-windows/a-3-2-installing-redis-on-window/)
4) [Eclipse](https://www.eclipse.org/downloads/) or [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows)

## Running the program

• Run the redis-server application to get the redis server running from redis installation directory

• Run the redis-cli application to see data stored in redis
  
  Commands:
    
    ping  -> to check for server status(should recieve a PONG response)
    
    flushall -> to delete all existing data
    
    keys *   -> to see objectId's of all existing data 
    
 • Run \bin\elasticsearch.bat from Elasticsearch installation directory
    
    Hit localhost:9200 to check status
 
 • Run \bin\kibana.bat from Kibana installation directory
     
    Hit localhost:5601 access visualization
    
 • Import collection BDI Demo 3.postman_collection.json to [Postman](https://www.postman.com/downloads/)
