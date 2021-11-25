#!/bin/bash
#echo "$@" >> ./testgithelperargs.log
if [[ "$1" =~ ^[U,u]sername.* ]]; then
  echo "${GIT_USER}"
elif [[ "$1" =~ ^[P,p]assword.* ]]; then 
  echo "${GIT_PASSWORD}"
fi
