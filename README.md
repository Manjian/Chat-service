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

- **/login** This namespace will accept any new login.

```json
{
  "name": "John",
  "password": "imBadPassword"
}
```
- **/join** This namespace will run in order to join a new channel.
```json
{
  "channel": "MyChannel"
}
```
- **/leave** This namespace will automatically detect if a user is in a channel and will make him leave it.

```json 
 /* Just a empty json */
{}  
```
- **/disconnect** This namespace will close the connection between the server and the client.
```json 
 /* Just a empty json */ 
{}  
```
- **/list** This namespace will return a list of all channels.
```json 
 /* Just a empty json */
{}
```
- **/users** This namespace will return a list of users in the target channel.
```json
{
  "channel": "targetChannel"
}
```

- **/send_message** This namespace will accept a new chat message from the client.
```json
{
   "messageType" :"CLIENT",
   "text" : "Hello world", 
   "channel":  "My World",  
   "username": "John"
}
```

