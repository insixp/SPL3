# assignment3
1. How to run the code:
    *Compile:
        -Server: cd server -> mvn package
        -Client: cd client -> make
    *Run:
        -Server TPC: cd server -> mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args="<PORT>"
        -Server Reactor: cd server -> mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args="<NUM_OF_THREADS> <PORT>"
        -Client: cd client -> ./bin/BGSClient <ADDRESS> <PORT>

2. Messages Example:
    1. register         - REGISTER <USERNAME> <PASSWORD> <BIRTHDAY(DD-MM-YYYY)>
    2. login            - LOGIN <USERNAME> <PASSWORD> <CAPTCHA(1/0)>
    3. logout           - LOGOUT
    4. follow           - FOLLOW <FOLLOW/UNFOLLOW(0/1)> <USERNAME>
    5. post             - POST <CONTENT>
    6. pm               - PM <USERNAME> <CONTENT>
    7. logged in stats  - LOGSTAT
    8. stats on users   - STAT <USER1>|<USER2>|<USER3>|...
    9. block            - BLOCK <USERNAME>

3. filter location:
    ${Poject_dir}/server/src/main/java/bgu/spl/net/impl/BGSServer/Database.java
    Variable name - filterWords.
