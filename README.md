webcast-utility
===============

Webcast Utility to simplify operations

Usage
==========
``` java
WebcastUtility [-p <arg>]
 -p,--properties <arg>        webcasting config file
```



Example properties file
==========
``` java
// --------------- WEBCASTING CONFIG FILE ------------------//
// This file contains configuration for ffmpeg webcasting
// ---------------------------------------------------------//
// Author : Steve McAdams
// Email  : smcadams86@gmail.com
// ---------------------------------------------------------//
ffmpeg_location=C:/users/dave/ffmpeg/bin/ffmpeg.exe
ffmpeg_command=-re -i "C:/test.mp3" -g 60 -strict experimental -acodec aac -ab 48k -ar 22050 -ac 2  -f flv "rtmp://server/app/stream"

```

Building
==========
``` java
mvn assembly:single
```

