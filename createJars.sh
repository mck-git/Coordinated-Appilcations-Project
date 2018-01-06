#!/bin/bash
#Compiles Client and Server projects independentliy and creates executable jars

function mark {
    export $1="`pwd`";
}

mark rootDir

rm -rf gamejars
mkdir gamejars

mkdir tmp

cp -r ./out/production/02148_project/ ./tmp

cd tmp

touch ClientManifest.txt
echo "Manifest-Version: 1.0
Created-By: 1.7.0_06 (Oracle Corporation)
Main-Class: Client.View.ClientDisplay
" | cat > ClientManifest.txt

touch ServerManifest.txt
echo "Manifest-Version: 1.0
Created-By: 1.7.0_06 (Oracle Corporation)
Main-Class: Server.View.ServerDisplay
" | cat > ServerManifest.txt


jar cfm "$rootDir"/gamejars/Client.jar ClientManifest.txt Client com Exceptions Fields Images org Templates
jar cfm "$rootDir"/gamejars/Server.jar ServerManifest.txt Server com Exceptions Fields Images org Templates

cd "$rootDir"

rm -rf tmp
