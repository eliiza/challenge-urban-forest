# Eliiza Urban Forest Challenge

## Intro

In this challenge you're given data about some of Melbourne's **Statistical Areas** (as defined by the ASGS - Australian 
Statistical Geography Standard) and about the City of Melbourne's **urban forest**.  Statistical Areas are given in the form 
of polygons representing their borders.  The urban forest is given in the form of polygons too, where they represent 
the area occupied by tree canopies or bush.  In both cases, polygon vertices are longitude/latitude pairs.

More information about Statistical Areas can be found at the ASGS web site:
http://www.abs.gov.au/websitedbs/D3310114.nsf/home/Australian+Statistical+Geography+Standard+(ASGS).

## Challenge

The challenge consists in determining the greenest suburb of Melbourne, where greenest is the suburb with the highest 
vegetation per area ratio.

Some relevant info here:
- Suburbs are related to Statistical Areas Level 2 (SA2s).
- File **melb_inner_2016.json** contains the Statistical Areas of inner Melbourne.
- Directory **melb_urban_forest_2016.txt** contains the urban forest of the City of Melbourne council.
- In **both** datasets coordinates are **longitude/latitude** pairs.
- You can check http://s2map.com to play around with plotting polygons on a real globe map.

## How to solve the challenge

For this challenge, **you must use Spark (Scala) or PySpark (Python)** in any environment you have access to.  We provide 
starting Jupyter notebooks with Spark and PySpark kernels and a Docker image to have Jupyter running locally.  Everything you 
need to solve this challenge should be available there (more details ahead), but feel free to solve it in any other Spark 
environment you like or with your favourite IDE.

Some useful types and functions to help with processing polygons are also provided.  Type **Polygon** is defined according to 
the GeoJson definition of polygons, *i.e.* a sequence of loops.  In a sequence of loops, the first loop (index zero) is the 
polygon's outer border (perimeter), while any following loop (if present) represents holes within the outer border.  Type 
**Loop** is a sequence of points and **Point** is a sequence of 2 doubles.  There is yet another type called **MultiPolygon**, 
which is simply a collection of polygons.  Multi-polygons are very useful to represent disjoint areas that make up the 
abstraction of a single piece of territory, for example, an island with the piece of coast to which it belongs.

In *Scala*, the given types are defined like the following:

    type Point        = Seq[Double]
    type Loop         = Seq[Point]
    type Polygon      = Seq[Loop]  // where the first loop is the outer border and the following ones are holes
    type MultiPolygon = Seq[Polygon]

Four functions to process multi-polygons are provided:

    // calculates the area of a multi-polygon
    def multiPolygonArea(multiPolygon: MultiPolygon): Double

    // calculates the intersection area of 2 multi-polygons
    def intersectionArea(multiPolygonA: MultiPolygon, multiPolygonB: MultiPolygon): Double

    // merges n multi-polygons
    def mergeMultiPolygons(multiPolygons: MultiPolygon*): MultiPolygon

    // a cheap way to check whether 2 multi-polygons might intersect (based on their bounding boxes)
    // NOTE: if the return is false, the multi-polygons are GUARANTEED to NOT intersect; however,
    //       if the return is true, the intersection is not guaranteed
    def mayIntersect(multiPolygonA: MultiPolygon, multiPolygonB: MultiPolygon): Boolean

If you're not using the Spark Jupyter notebook, you can use those types and functions by including
**challenge-urban-forest-jar-assembly-0.0.1.jar** into your solution and importing everything from package 
**au.com.eliiza.urbanforest**.

Equivalent functions are given in *Python* too!  If you're not using the PySpark Jupyter notebook, you can use those functions
from file **polygon_utils.py**.

## Solving the challenge with provided dockerised Jupyter notebook

As mentioned before, you can use a Jupyter notebook to solve the challenge.  A Docker image with Jupyter is provided for you, 
and it contains kernels to program in Spark (Scala) and PySpark (Python).  Follow these steps to prepare your environment:

    $ git clone https://github.com/eliiza/challenge-urban-forest.git myrepo
    $ cd myrepo
    $ docker build -t challenge .
    $ docker run -p 8888:8888 challenge
    
Once the Docker container is running, it should print to the console a URL similar to this (the token will be different):

    http://localhost:8888/?token=eab1d32b7f330f452c875467b367e4f109d39f97d647b9ac
    
which you should visit to find the Jupyter notebook.  From the file tree, select the notebook file corresponding to Spark 
(**index-spark.ipynb**) or PySpark (**index-pyspark.ipynb**), according to your preferred language.  Once you solve the 
challenge, send us back your notebook file (or your code in any other format if not using Jupyter).

Good luck!!!

#### Licensing of the Datasets

- ASGS: https://link.fsdf.org.au/fsdf-dataset/australian-statistical-geographical-standard-boundaries
- City of Melbourne: https://data.melbourne.vic.gov.au/about#principles
