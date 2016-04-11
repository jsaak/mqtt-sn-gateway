# MQTT-SN Gateway

## What it is
```
This is a working MQTT-SN <-> MQTT gateway.
The code is taken from the Eclipse Paho project.
The reason is, that no one seems to know about this, nor use it.
I fixed some bugs.
On embedded devices the logging should be turned off.
Otherwise the MQTT-SN gateway is working pretty well.

I did not try the MQTT-SN c or java client
```

## How to compile
```
   cd apps/MQTTSN-Gateway/src/org/eclipse/paho/mqttsn/gateway && javac -sourcepath ./ -d target/path/ ./**/**/**/**/**/*.java && cd ../../../../../../../..
```
there should be a more elegant way, if you know one, let me know, and i will change this

## Licensing
see [notice.html](https://github.com/jsaak/mqtt-sn-gateway/blob/master/notice.html)

# original readme: Eclipse Paho MQTT-SN reference code and sample applications

## Reporting bugs

Please report bugs in [Eclipse Bugzilla](http://bugs.eclipse.org/bugs/) for the Paho project.

## More information

Discussion of the Paho clients takes place on the [Eclipse paho-dev mailing list](https://dev.eclipse.org/mailman/listinfo/paho-dev).

General questions about the MQTT protocol are discussed in the [MQTT Google Group](https://groups.google.com/forum/?hl=en-US&fromgroups#!forum/mqtt).

There is much more information available via the [MQTT community site](http://mqtt.org).
