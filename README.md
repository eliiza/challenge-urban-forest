# Eliiza Urban Forest Challenge

## Overview

In this challenge you're given data about some of Melbourne's **Statistical Areas** (as defined by the ASGS - Australian 
Statistical Geography Standard) and about the City of Melbourne's **urban forest**.  Statistical Areas are given in the form 
of polygons representing their borders.  The urban forest is given in the form of polygons too, where they represent 
the area occupied by tree canopies or bush.  In both cases, polygon vertices are **longitude/latitude** pairs.

More information about Statistical Areas can be found at the ASGS web site:
http://www.abs.gov.au/websitedbs/D3310114.nsf/home/Australian+Statistical+Geography+Standard+(ASGS).

For this challenge, you can use Spark (Scala) or PySpark (Python) in a Jupyter notebook.  Everything you need to perform this 
challenge should be available, but feel free to install additional packages should you need them.  Some useful types and 
functions to help with processing polygons are also provided.

Type **Polygon** is defined according to the GeoJson definition of polygons, *i.e.* a sequence of loops.  In a sequence of
loops, the first loop (index zero) is the polygon's outer border (perimeter), while any following loop (if present) represents 
holes within the outer border.  Type **Loop** is a sequence of points and **Point** is a sequence of 2 doubles.  There is yet 
another type called **MultiPolygon**, which is simply a collection of type Polygon.  MultiPolygon's are very useful to 
represent disjoint areas that make up a single piece of territory, for example, an island with the piece of coast to which it
belongs.  In *Scala*, these types are:

    type Point        = Seq[Double]
    type Loop         = Seq[Point]
    type Polygon      = Seq[Loop]  // where the first loop is the outer shape and the following ones are holes
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
    //       if the return is true, the same is not guaranteed
    def mayIntersect(multiPolygonA: MultiPolygon, multiPolygonB: MultiPolygon): Boolean

Equivalent functions are given in *Python* too!  You can check http://s2map.com to play around with plotting polygons.

## Challenge

Determine the greenest suburb of Melbourne.  Some relevant info:
- Suburbs are related to Statistical Areas Level 2 (SA2s).
- File *melb_inner_2016.json* contains the Statistical Areas of inner Melbourne.
- File *melb_urban_forest_2016.txt* contains the urban forest of the City of Melbourne council.
- In **both** datasets, borders are given in multi-polygons and coordinates are **longitude/latitude** pairs.

## Where to solve the challenge

As mentioned before, you can use a Jupyter notebook to solve the challenge.  A Docker image with Jupyter is provided for you, 
and it contains kernels to program in Spark (Scala) and PySpark (Python).  Follow these steps to prepare your environment:

    $ git clone https://github.com/eliiza/challenge-urban-forest.git myrepo
    $ cd myrepo
    $ docker build -t challenge .
    $ docker run -p 8888:8888 challenge
    
Once the docker container is running, it should print a URL similar to this (the token will be different):

    http://localhost:8888/?token=eab1d32b7f330f452c875467b367e4f109d39f97d647b9ac
    
which you should visit to find the Jupyter notebook. From the file tree, select the notebook file corresponding to Spark or 
PySpark conform your preference. Good luck!!!

#### Access and Licensing of the Datasets

- ASGS: https://link.fsdf.org.au/fsdf-dataset/australian-statistical-geographical-standard-boundaries
- City of Melbourne: https://data.melbourne.vic.gov.au/about#principles
