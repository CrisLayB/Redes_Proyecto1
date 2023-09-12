# Chat XMPP App

Universidad del Valle de Guatemala

Computer Science Departament

Networds - Sección 10

Proyect 1

Cristian Laynez - 201281

## Proyect Description

Welcome to the Chat XMPP App, a real-time chat application built using the Smack library for Java. This application allows you to create and manage chat channels in real time using the XMPP protocol. You can log with an existing count for the server alumchat.xyz

## Proyect Execution

You can compile the code

```
javac src/App.java
```

And then run the program

```
java src/App
```

PD: Is prefered to use the maven proyect in vs code.

## Features

1. Show Users/Contacts and Their State: View a list of all users/contacts and their online/offline state.

    When you press this option automatically this will show all the options.

2. Add User to Contacts: Add new users to your contacts list to start conversations.

    In this option you will write the username that you want to add to your list.

3. View Contact Details: Get detailed information about a specific contact/user.

    If you write the username you will see all the details of the user.

4. One-on-One Communication: Initiate and engage in one-on-one real-time conversations with any user/contact.

    In first place you need to write the username of the contact that you want to chat.

    - And then you can write a message or exit the option.

5. Group Conversations: Participate in group conversations with multiple users.

    - 1. You need to write the username of the user.
    - 2. You will write the nickname that you want

    And then you can write exactly like the option 4.

6. Set Presence Message: Define a custom presence message that is displayed to your contacts.

    You just need to write your presence message and you must done.

7. Display Notifications: Receive and view notifications for new messages and events.

    This is not really an option, with the help of listeners you will recive the new information.

8. Send/Receive Files: Exchange files with other users through the chat interface.

    In the chats you can write "send-file" to send the file that you want.

9. Delete User Account: Delete your user account and associated data.

    This option will ask you if you really want to delete this user.

10. Close Session: Gracefully close the current chat session while remaining logged in.
    
11. Quit Application: Exit the application and close the current session.

## For Lab 3

Please check out this repo for see the implementation of the flooding algorithm.

https://github.com/CrisLayB/Redes_Lab03.git
