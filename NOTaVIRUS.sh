#!/bin/bash

# Functions
ok() { echo -e '\e[32m'$1'\e[m'; } # Green

EXPECTED_ARGS=2
E_BADARGS=65
MYSQL=`which mysql`
 
Q1="CREATE DATABASE IF NOT EXISTS PokeputzDB;"
Q2="GRANT ALL ON PokeputzDB.* TO '$1'@'localhost' IDENTIFIED BY '$2';"
Q3="FLUSH PRIVILEGES;"
SQL="${Q1}${Q2}${Q3}"
 
if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: $0 db_user db_pass"
  exit $E_BADARGS
fi
 
echo -e "Need admin privileges to create a base user for the new database"
echo -e "Creating base 'PokeputzDB' with user '$1' and password '$2'"

$MYSQL -e "$SQL"

$MYSQL -u $1 -p$2 < Database.sql

