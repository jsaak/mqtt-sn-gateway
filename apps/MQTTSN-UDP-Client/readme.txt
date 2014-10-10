This package contains the Java source code of an MQTT-SN client that uses UDP to communicate with either 
an MQTT-SN broker, or an MQTT broker via an MQTT-SN gateway. 

There are two clients defined:

* org.eclipse.paho.mqttsn.udpclient.MqttsClient.java: this is the main entry point.
* org.eclipse.paho.mqttsn.udpclient.SimpleMqttsClient.java: this class is based on the MqttsClient mentioned above 
  and provides a simpler API, e.g. no topic registration is required before first publish.
  
The directory "samples" contains two sample classes which demonstrate how the above two client libraries
could be used.
   
