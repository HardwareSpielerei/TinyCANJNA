# TinyCANJNA
Acess Tiny-CAN dongle via native API and JNA. 
TinyCANJNA
=========

[Tiny-CAN](http://www.mhs-elektronik.de/) is a dongle that plugs into any PC USB Port and gives an instant CAN connectivity. Drivers for Windows 32 bit and 64 bit are available. It can be treated by software as a standard COM Port. But communicating directly via the driver DLL allows faster communications and higher CAN bus loads. The TinyCANJNA library implements access to the native DLL using [Java Native Access (JNA)](https://github.com/twall/jna).

Features
========

* Implements essential parts of the Tiny-CAN API in plain Java.
* Provides class hierarchy hiding the C-like native interface.

Requirements
============

* Windows XP or above
* Tiny-CAN for Windows (32/64 Bit) version 3.02 or above
* jna.jar and jna-platform.jar from [Java Native Access (JNA)](https://github.com/twall/jna)

Using the Library
=================

* coming soon...

License
=======

TinyCANJNA is published under [MIT License](http://choosealicense.com/licenses/mit/).

Links
=======

A similar library for the CANUSB dongle is provided by the [CANUSBJNA](http://github.com/HardwareSpielerei/CANUSBJNA) project.
