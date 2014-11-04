osgi_basic
==========
1. Install Apache Karaf

2. Build this project. "mvn clean install"

3. Copy the following jars into $KARAF_HOME/deploy  
    log4j-1.2.17.jar  
    jdo-api-3.1-rc1.jar  
    h2-1.3.168.jar  
    datanucleus-core-{version}.jar  
    datanucleus-api-jdo-{version}.jar  
    datanucleus-rdbms-{version}.jar  
    datanucleus-samples-osgi-jdo-4.0.jar   (the jar from this project, present under "target")


4. Start it ($KARAF_HOME/bin/karaf).
    The jars above will then be automatically installed/started. The Activator in this bundle will be started.


5. If you do a "list" in the karaf console you will now get something like

    karaf@root> list

    START LEVEL 100 , List Threshold: 50  
    ID | State    | Lvl | Version        | Name                                   
    ------------------------------------------------------------------------------  
    64 | Active   |  80 | 1.3.168        | H2 Database Engine                     
    65 | Active   |  80 | 1.2.17         | Apache Log4j                           
    66 | Active   |  80 | 4.0.4.SNAPSHOT | DataNucleus Core                       
    67 | Active   |  80 | 3.1.0.rc1      | JDO API                                
    68 | Active   |  80 | 4.0.4.SNAPSHOT | DataNucleus JDO API plugin             
    69 | Active   |  80 | 4.0            | DataNucleus Samples : OSGi demo for JDO  
    70 | Active   |  80 | 4.0.5.SNAPSHOT | DataNucleus RDBMS plugin


