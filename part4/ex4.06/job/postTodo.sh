#!/bin/sh
URL=`curl -s -I https://en.wikipedia.org/wiki/Special:Random | fgrep -i location | cut -f2 -d' ' | tr -d '\r'`
echo Todays URL is $URL
curl -s -H "Content-Type: application/json" -d "Read $URL" -X POST http://backend-svc:2347/todos

