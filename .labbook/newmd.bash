#!/bin/bash

name=`date -u +%y%m%d%H%M%S`Z.md
if ls *Z.md 2>/dev/null; then
    cp `ls *Z.md | tail -1` $name
else
    touch $name
fi
echo $name
