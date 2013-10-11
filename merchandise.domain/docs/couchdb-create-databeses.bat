REM create database for opportunities
curl -vX PUT  http://localhost:5984/opportunities

REM create database for commercial subjects
curl -vX PUT  http://localhost:5984/subjects

REM show existing databases
curl -vX GET  http://localhost:5984/_all_dbs