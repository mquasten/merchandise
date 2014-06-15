#!/bin/bash
if [ "$1" == "-f" ]
then
  curl -vX POST -H "Content-Type:application/json" -d@petstore-data.json  http://127.0.0.1:5984/petstore/_bulk_docs
  exit $?
fi
echo ""
echo "Achtung: Die Daten werden ggf. auch  mehrfach eingefuegt, -f  zur Bestaetigung benutzen."
exit 1
