dir = "ca-ws" "ws-handlers" "transporter-ws" "transporter-ws-cli" "broker-ws" "broker-ws-cli"
cln = mvn -q clean
ins = mvn clean install -DskipTests

ALL:
	for i in $(dir); do (cd $$i; $(ins) > ../$$i.ins.log 2>&1; cat ../$$i.ins.log | grep ERROR; printf "\n>>> $${i} installed"; rm ../$$i.ins.log) & done

clean:
	for i in $(dir) "ca-ws-cli"; do (cd $$i; $(cln); printf "\n>>> $${i} cleaned") & done
