# Project: Three-Tiered Data System with Reminder Service





## Project Statement

This project aims to develop a scalable, three-tiered data system that allows users to set and receive reminders through a basic form website interface. The service is designed to process and store reminders and notify users via SMS or Email, depending on their preference and schedule. I have aimed to build a highly-scalable backend system which can accomodate a large number of reminder requests.

### _Note to Professor_
> Please note that the SMS functionality cannot be tested as we are using a `free tier` account of the Vonage API for sending messages. For message testing, a phone number must be registered with the API. Currently, only my number is registered with Vonage, so you will not be able to test this functionality. You can find its code in `ReminderListener` module

## Project Components

- `ReminderUI`: The front-end module developed using Swing. It provides the user interface for setting reminders and interacts with the backend through REST API.
- `ReminderRegister`: The backend module using Spring Boot, H2 database, and JPA for CRUD operations. It includes Spring Security with Bcrypt for password management and two threads for handling reminders
  - Thread 1: Polls the database every 5 minutes for reminders due in the next 5 minutes and stores them in a local cache.
  - Thread 2: Checks the local cache every minute for expired reminders and posts them to Kafka.
- `ReminderListener`: A Spring Boot service that listens to the Kafka topic continuously and sends out messages or emails to recipients based on the reminder's configuration.

## Dependencies

- Java Version: 8
- Maven

## How To Run

For convenience and easy testing, pre-compiled JAR files for each module have been created and are stored in the `JARS` folder.
Below are the commands to run the JAR files for each module :

- `java -jar ReminderRegister-0.0.1-SNAPSHOT.jar`
- `java -jar ReminderUI-0.0.1-SNAPSHOT.jar`
- `java -jar ReminderListener-0.0.1-SNAPSHOT.jar`

### _Note to Professor_
> If you wish to create the JAR files on your own, navigate to each module's directory and run `mvn clean install`. 
After the build process completes, you will find the JAR file in the `target` directory of that module. 
You can then run the above commands after changing your directory to `target`:
>- `cd target`
>- `java -jar ReminderUI.jar`

> Repeat the process for each module (`ReminderRegister` and `ReminderListener`) to create and run their respective JAR files.




## Advanced Topics Used & How They Are Used

- **Swing**: Used in `ReminderUI` for creating the graphical user interface.
- **Spring Security**: Implemented for authentication in the ReminderRegister.
- **JWT (JSON Web Tokens)**: Utilized for session management.
- **Spring Boot**: Forms the backbone of all three modules, simplifying the bootstrapping and development of the applications.
- **H2 Database**: An in-memory database used in `ReminderRegister` for storing reminder information.
- **Messaging with Confluent Kafka**: Employed for communication between `ReminderRegister` and `ReminderListener`.
- **Multi-threading**: In `ReminderRegister`, multi-threading is used for efficient task processing, handling database polling, and cache management.
- **Maven**: Build Tool

## Project Structure
```shell
.
├── JARS
│   ├── ReminderListener-0.0.1-SNAPSHOT.jar
│   ├── ReminderRegister-0.0.1-SNAPSHOT.jar
│   ├── ReminderUI-0.0.1-SNAPSHOT.jar
│   
├── README.md
├── ReminderListener
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── nyu
│                       └── java
│                           └── reminderlistener
│                               ├── ReminderListenerApplication.java
│                               ├── config
│                               │   └── AppConfig.java
│                               ├── model
│                               │   └── Reminder.java
│                               └── service
│                                   ├── EmailService.java
│                                   ├── KafkaConsumerService.java
│                                   └── SmsService.java
├── ReminderRegister
|   └── src
|       └── main
|           └── java
|               └── org
|                   └── nyu
|                       └── java
|                           └── project
|                               └── reminderregister
|                                   ├── ReminderRegisterApplication.java
|                                   ├── config
|                                   |   └── AppConfig.java
|                                   ├── controller
|                                   │   ├── AuthController.java
|                                   |   └── ReminderController.java
|                                   ├── entity
|                                   │   ├── ERole.java
|                                   │   ├── ReminderEntity.java
|                                   │   ├── Role.java
|                                   |   └── User.java
|                                   ├── model
|                                   │   ├── request
|                                   │   │   ├── EmailVerificationRequest.java
|                                   │   │   ├── LoginRequest.java
|                                   │   │   ├── ReminderCreationRequest.java
|                                   │   │   ├── ReminderRetrivalRequest.java
|                                   │   │   ├── ReminderUpdateRequest.java
|                                   |   │   └── SignupRequest.java
|                                   |   └── response
|                                   │       ├── MessageResponse.java
|                                   │       ├── ReminderResponse.java
|                                   |       └── UserInfoResponse.java
|                                   ├── repository
|                                   │   ├── ReminderRepository.java
|                                   │   ├── RoleRepository.java
|                                   |   └── UserRepository.java
|                                   ├── security
|                                   │   ├── WebSecurityConfig.java
|                                   │   ├── jwt
|                                   │   │   ├── AuthEntryPointJwt.java
|                                   │   │   ├── AuthTokenFilter.java
|                                   |   │   └── JwtUtils.java
|                                   │   ├── model
|                                   │   │   ├── JwtResponse.java
|                                   |   │   └── UserDetailsImpl.java
|                                   |   └── services
|                                   |       └── UserDetailsServiceImpl.java
|                                   └── service
|                                       ├── ReminderPollingService.java
|                                       ├── ReminderProcessor.java
|                                       └── UserService.java
└── ReminderUI
    └── src
        └── main
            └── java
                └── com
                    └── nyu
                        └── java
                            └── reminderui
                                ├── ReminderUiApplication.java
                                ├── client
                                │   ├── AuthenticateClient.java
                                │   └── ReminderClient.java
                                ├── config
                                │   ├── AuthEndpoints.java
                                │   ├── PropertiesConfig.java
                                │   ├── SpringBeans.java
                                │   ├── TestEndpoints.java
                                │   └── TokenStorage.java
                                ├── exceptions
                                │   ├── AuthenticationException.java
                                │   ├── EmailNotVerifiedException.java
                                │   ├── NoRemindersFoundException.java
                                │   ├── ReminderCreationException.java
                                │   ├── ReminderDeleteException.java
                                │   ├── ReminderUpdateException.java
                                │   └── UserNotFoundException.java
                                ├── frames
                                │   ├── CreateReminderPopup.java
                                │   ├── LogInFrame.java
                                │   ├── MainFrame.java
                                │   ├── Mode.java
                                │   ├── ReminderCreationCallback.java
                                │   ├── ReminderPanels.java
                                │   ├── SignUpFrame.java
                                │   ├── SwitchFrameEvent.java
                                │   └── WelcomeFrame.java
                                ├── model
                                │   ├── request
                                │   │   ├── EmailVerificationRequest.java
                                │   │   ├── LoginRequest.java
                                │   │   ├── ReminderCreationRequest.java
                                │   │   └── SignUpRequest.java
                                │   └── response
                                │       ├── JwtValidationResponse.java
                                │       ├── LogInResponse.java
                                │       ├── Reminder.java
                                │       └── ReminderList.java
                                └── utility
                                    ├── EmailValidator.java
                                    └── PhoneNumberValidator.java
```
