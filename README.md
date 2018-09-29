# Eliiza Urban Forest Challenge

Launch in Jupyter with Scala Spark: [![Binder](https://mybinder.org/badge.svg)](https://mybinder.org/v2/gh/eliiza/challenge-urban-forest/master?filepath=index-spark.ipynb) 
Launch in Jupyter with PySpark: [![Binder](https://mybinder.org/badge.svg)](https://mybinder.org/v2/gh/eliiza/challenge-urban-forest/master?filepath=index-pyspark.ipynb) 


## Overview

In this challenge you're given data about some of Melbourne's **Statistical Areas** (as defined by ASGS - Australian 
Statistical Geography Standard) and about the City of Melbourne's **urban forest**.  Statistical Areas are given in the form 
of polygons representing their borders.  The urban forest is given in the form of polygons too, where each polygon represents 
the area occupied by a tree canopy or bush.  In both cases, polygon vertices are **longitude/latitude** pairs.

More information about Statistical Areas can be found at the ASGS web site:
http://www.abs.gov.au/websitedbs/D3310114.nsf/home/Australian+Statistical+Geography+Standard+(ASGS).

For this challenge, you can use Spark (Scala) in a Jupyter notebook.  Everything you need to perform this challenge should be 
available, but feel free to install additional packages should you need them.  Some useful types and functions to help with 
processing polygons are also provided.

Type **Polygon** is defined according to the GeoJson definition of polygons, *i.e.* a sequence of loops.  In a sequence of
loops, the first loop (index zero) is the polygon's outer shape (perimeter), while any following loop (if present) represents 
holes within the outer shape.  Type **Loop** is a sequence of points and **Point** is a sequence of 2 doubles.

    type Point   = Seq[Double]
    type Loop    = Seq[Point]
    type Polygon = Seq[Loop] // where the first loop is the outer shape and the following ones are holes

Four functions to process polygons are provided:

    // calculates the area of a polygon
    def polygonArea(polygon: Polygon): Double

    // calculates the intersection area of 2 polygons
    def intersectionArea(polygonA: Polygon, polygonB: Polygon): Double

    // merges n polygons
    def mergePolygons(polygons: Polygon*): Polygon

    // a cheap way to check whether 2 polygons might intersect (based on their bounding boxes)
    // NOTE: if the return is false, the polygons are GUARANTEED to NOT intersect; however,
    //       if the return is true, the same is not guaranteed
    def mayIntersect(polygonA: Polygon, polygonB: Polygon): Boolean

You can check http://s2map.com to play around with polygons.

## Challenge

Determine the greenest suburb of Melbourne.  Some relevant info:
- Suburbs are related to Statistical Areas Level 2 (SA2s).
- File *melb_inner_2016.json* contains the Statistical Areas of inner Melbourne.
- File *melb_urban_forest_2016.txt* contains the urban forest of the City of Melbourne council.

#### Access and Licensing of the Datasets

- ASGS: https://link.fsdf.org.au/fsdf-dataset/australian-statistical-geographical-standard-boundaries
- City of Melbourne: https://data.melbourne.vic.gov.au/about#principles
