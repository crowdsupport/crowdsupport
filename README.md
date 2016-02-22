[![Build Status](https://travis-ci.org/crowdsupport/crowdsupport.svg?branch=master)](https://travis-ci.org/crowdsupport/crowdsupport)

Crowdsupport
============
Crowdsupport has been created by Markus Möslinger during his BSc. Computing (Hons.) degree at University of Central Lancashire / United Kingdom.

Idea
----
The global situation led to a lot of people depending on the help of volunteers: starting in September 2015, hundreds of refugees arrived daily on Austrian and German train stations, while people were waiting for them, supplying them with food, water and other items. Currently needed donations were published through various social media platforms like Twitter and Facebook. This had the advantage of a big user base, but came at the cost of clarity. For people wanting to bring donations, it was hard to see what’s needed right now – often, too many items were brought, so that the donation storage was overflowing with water bottles, but although food or toothbrushes would have been more important on this day. The goal of this project is to build a platform designed for coordinating donations. A group of people can request a site for their place in a city, and post new donation requests on it. Users then can see how much more is needed and comment on the request, promising a certain amount of items, until the enough donations are received. 

Build
=====
To compile and start Crowdsupport, you have to have a [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) installed.

Download repository
-------------------
The easiest way to do this is with git:

    git clone --depth 1 https://github.com/crowdsupport/crowdsupport.git

This will download the newest commit on the `master` branch, which represents the latest stable version.
If you want to have all commits, just use `git clone` without the `--depth 1` option.

If you don't have git installed on your system, you can just download the repository [as a ZIP file](https://github.com/crowdsupport/crowdsupport/archive/master.zip).

Start
-----
As soon as you have the project anywhere on your computer, you can build and start the application.

The easiest way to do this? If you're on Windows, just click on `demo.cmd`, which is in the same directory as this README is! This will download all necessary files, compile the project and start the server. As soon as you think the command window froze (and as soon as you can read something like "Started CrowdsupportApplication..."), you can open your browser and go to [localhost:8080](http://localhost:8080).

Welcome!

Because you've started the application this way, you've two user profiles available per default:

* admin (Password: adminadmin)
* user (Password: user)

Troubleshooting
===============

* Do you have the correct version of Java in your `$PATH`? Executing `java -version` in the command line should tell you something about java version 1.8. Or higher, if you're from the future.
* When you are trying to build the project (i.e. by starting `demo.cmd`, make sure you've [`$JAVA_HOME`](http://lmgtfy.com/?q=java_home) correctly set, pointing to your JDK installation.
* Is your port 8080 free?

Simply create a [new issue](https://github.com/crowdsupport/crowdsupport/issues) :)