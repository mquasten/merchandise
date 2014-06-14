#!/bin/bash
cat qualityByArtist.json | dos2unix | tr '\n' ' ' | tr '\t' ' '  > tmp.json
curl -X PUT http://localhost:5984/petstore/_design/qualityByArtist -d @tmp.json
cat   pricePerUnit.json | dos2unix| tr '\n' ' '| tr '\t' ' '  > tmp.json
curl -X  PUT http://localhost:5984/petstore/_design/pricePerUnit -d @tmp.json
rm tmp.json
echo  "=============================================================================="
echo "*** nicole with platinium quality shoud be shown ***"  
curl -X GET "http://localhost:5984/petstore/_design/qualityByArtist/_view/QualityByArtist?key=\"nicole\""
echo  "=============================================================================="
echo "*** price matrix for  platinium escorts dependend from order quantity ***"  
curl -X GET "http://localhost:5984/petstore/_design/pricePerUnit/_view/PricePerUnit?key=\{\"quality\":\"platinium\",\"unit\":\"date\"\}"
echo  "=============================================================================="
echo "*** pice  for 5 dates   with a platinium escort ***"  
curl -X GET  "http://localhost:5984/petstore/_design/pricePerUnit/_list/quantityFilter/PricePerUnit?key=\{\"quality\":\"platinium\",\"unit\":\"date\"\}&quantity=5"
echo  "=============================================================================="