    First, the project name is: MonteCarlo2, both folders are under this project.
    There are two source folders: junit and src.  And we need to add activemq Jars to this class path.
    Please open the ActiveMQ file at first, and without closing it, run the "MCClient.java" under activemq package under src, and then run the "MCServer.java" under activemq package under src. After several minutes, the final results will come out in the following format:
Asian Option
13300 times
Mean: 2.2553218494467115
Sigma: 5.601921241606992

European Call
70797 times
Mean: 6.37809401309024
Sigma: 12.949476009125034
 
    The numbers would change every time, but they should be within some ranges.

