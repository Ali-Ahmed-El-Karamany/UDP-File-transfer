# UDP File Transfer

A simple **UDP file transfer** project implemented in Java.  
This project allows sending text files over the network using **UDP Sockets**, 
by splitting long data into smaller packet-sized chunks and reconstructing them at the receiver.


## Project Overview

This project implements a file transfer over UDP.
It demonstrates:

- UDP socket programming in Java
- File I/O operations
- Modular design using classes and packages
- Graceful resource handling (sockets, file writers, scanners)
- line separators (`"\n"`) and an end-of-file indicator (`"EOF"`) so the receiver can reconstruct lines and know when the transfer is complete.


## Architecture

The project uses a simple, modular architecture separated into two packages:

```
src/
├── sender/                     // Sender-side classes
│   ├── FileSender.java         // Orchestrator: uses FileReaderHelper + UDPSender
│   ├── UDPSender.java          // Handles UDP socket and packet sending
│   └── FileReaderHelper.java   // Reads a local file line-by-line
└── receiver/                   // Receiver-side classes
|   ├── FileReceiver.java       // Entry point for receiver
|   └── UDPReceiver.java        // Handles UDP socket receiving & file writing
|
└── data/
    ├── sample.txt                  # Example file to send
    └── output.txt                  # File that receiver writes to
```


## Features

- Send a text file line-by-line over UDP.
- Automatic splitting of long lines into multiple UDP packets.
- Line boundary preservation `\n` indicator.
- Clean termination using an `EOF` marker.
- Modular OOP design for clarity and reuse.
- Safe resource cleanup.


## How to Run the Project


1. Compile all source files:
   ```bash
   cd UDPFileTransfer/src
   javac sender/*.java receiver/*.java
   ```

2. Start the receiver:
   ```bash
   # Create or overwrite data/output.txt and listen on port 5000
   java receiver.FileReceiver ../data/output.txt 5000
   ```

3. Start the sender:
   ```bash
   java sender.FileSender ../data/sample.txt localhost 5000
   ```
