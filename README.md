huedistribution
===============

huedistribution is a simple mapreduce-based algorithm to calculate distribution of Hue (color axis) values provided in a large data file. Data file is composed of lines each of which correspond to data obtained from segments of a range of images. This data can be collected using [idext](https://github.com/cetinkaya/idext), Image Data Extraction Tool.

huedistribution is designed to be run on Hadoop. 


Usage
-----

To build first set environment variable HADOOP_HOME for example with 

```sh

export HADOOP_HOME=$HOME/hadoop


```

then run shell script build.sh provided with huedistribution. 

To calculate distribution of HUE values huedistribution over Hadoop, you can use 

```sh

$HADOOP_HOME/bin/hadoop jar huedistribution.jar ac.ahmet.analysis.HueDistribution examples/input examples/output 

```

