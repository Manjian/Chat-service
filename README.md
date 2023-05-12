## Requirements
- The only requirement for the project is Java 17.

## Build

To build and run all tests just run following command in root directory of project:
```shell
./gradlew clean check
```

## Running

Run following command in root directory of project to start application:
```shell
./gradlew bootRun
```
Or you can use any code editor that supports JVM-based languages, such as [IntelliJ IDEA ](https://www.jetbrains.com/idea/) from jetbrains with Spring [plugin](https://plugins.jetbrains.com/plugin/20221-spring)


## H2 Database
To access to H2 Dashboard which been enabled
[http://localhost:8080/h2-console ](http://localhost:8080/h2-console) 
the Credentials is available in the project application.properties file
```properties
zeptolab.datasource.url=jdbc:h2:mem:ZeptoLabChatDB;
zeptolab.datasource.username=happyAdmin
zeptolab.datasource.password=whereIsThePassword
```


## Connect to socket 
Downland [Postman](https://www.postman.com/) or any Network client software who support socket connection

## How to use
<br /> The client needs to add an event listener for  ***read_message***  in order to receive all events sent from the server.

- /login 
```json
{
  "name": "John",
  "password": "imBadPassword"
}
```
- /join
```json
{
  "channel": "MyChannel"
}
```
- /leave

```json 
// Just a empty json
{}  
```
- /disconnect
```json 
// Just a empty json
{}  
```
- /list
```json 
// Just a empty json
{}
```
- /users
```json
{
  "channel": "targetChannel"
}
```

- /send_message
```json
{
   "messageType" :"CLIENT",
   "text" : "Hello world", 
   "channel":  "My World",  
   "username": "John"
}
```

