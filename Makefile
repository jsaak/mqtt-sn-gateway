all:
	mkdir -p /home/admin/bin/mqttsn-gateway/
	cd apps/MQTTSN-Gateway/src/org/eclipse/paho/mqttsn/gateway && javac -sourcepath ./ -d /home/admin/bin/mqttsn-gateway/ ./**/**/**/**/**/*.java ; cd ../../../../../../../..
