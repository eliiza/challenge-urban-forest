# Eliiza Urban Forest Challenge

## Intro

In this challenge you're given data about some of Melbourne's **Statistical Areas** (as defined by the ASGS - Australian
Statistical Geography Standard) and about the City of Melbourne's **urban forest**.  Statistical Areas are given in the
form of polygons representing their borders.  The urban forest is given in the form of polygons too, where they
represent the area occupied by tree canopies or bush.  In both cases, polygon vertices are longitude/latitude pairs.

More information about Statistical Areas can be found at the ASGS web site:
http://www.abs.gov.au/websitedbs/D3310114.nsf/home/Australian+Statistical+Geography+Standard+(ASGS).

## Challenge

The challenge consists in determining the greenest suburb of Melbourne, where greenest is the suburb with the highest
vegetation per area *ratio*.

Some relevant info here:
- Suburbs are related to Statistical Areas Level 2 (SA2s).
- File **melb_inner_2016.json** contains the Statistical Areas of inner Melbourne.
- Directory **melb_urban_forest_2016.txt** contains the urban forest of the City of Melbourne council.
- In **both** datasets coordinates are **longitude/latitude** pairs.
- You can check http://s2map.com to play around with plotting polygons on a real globe map (choose Statemen Terrain in
the drop-down options). This is another good one for that: http://apps.headwallphotonics.com/

## How to solve the challenge

For this challenge, **you will most probably want to use Spark/PySpark or Beam/Dataflow)**, as they provide the adequate
parallel processing you'll need to get results in a reasonable amount of time.  You can choose any language or
environment you have access to.

Some useful types and functions to help with processing polygons are also provided.  Type **Polygon** is defined
according to the GeoJson definition of polygons, *i.e.* a sequence of loops.  In a sequence of loops, the first loop
(index zero) is the polygon's outer border (perimeter), while any following loop (if present) represents holes within
the outer border.  Type **Loop** is a sequence of points and **Point** is a sequence of 2 doubles.  There is yet another
type called **MultiPolygon**, which is simply a collection of polygons.  Multi-polygons are very useful to represent
disjoint areas that make up the abstraction of a single piece of territory, for example, an island with the piece of
coast to which it belongs.

### Scala
Using jar `challenge-urban-forest-jar-assembly-1.0.0.jar`, import `au.com.eliiza.urbanforest._` and those types will be
provided like:

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

### Python
Equivalent functions are given in *Python* too!  Just import everything from `polygon_utils.py`.

### Java
Using the same jar `challenge-urban-forest-jar-assembly-1.0.0.jar`, import class `au.com.eliiza.urbanforest.j` and
everything in it (`au.com.eliiza.urbanforest.j.*`) to gain access to equivalent types and methods.

Good luck!!!

#### Licensing of the Datasets

- ASGS: https://link.fsdf.org.au/fsdf-dataset/australian-statistical-geographical-standard-boundaries
- City of Melbourne: https://data.melbourne.vic.gov.au/stories/s/data-principles/9f8u-v2fn?src=hdr
