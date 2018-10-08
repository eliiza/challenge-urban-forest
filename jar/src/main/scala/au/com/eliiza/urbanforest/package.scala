package au.com.eliiza

import com.google.common.geometry.S2LatLng.{latitude, longitude}
import com.google.common.geometry._

import scala.collection.JavaConversions._

package object urbanforest {

  type Point = Seq[Double]
  type Loop = Seq[Point]
  type Polygon = Seq[Loop] // where the first loop is the outer shape and the following ones are holes
  type MultiPolygon = Seq[Polygon]

  private[eliiza] def toS2MultiPolygon(multiPolygon: MultiPolygon): S2Polygon = {
    def toS2Point(point: Point): S2Point = S2LatLng.fromDegrees(point(1), point(0)).toPoint
    def toS2Loop(loop: Loop): S2Loop = new S2Loop(loop.map(toS2Point))
    def toS2Polygon(polygon: Polygon): S2Polygon = {
      val builder = new S2PolygonBuilder()
      polygon.map(toS2Loop).foreach(builder.addLoop)
      builder.assemblePolygon()
    }

    val builder = new S2PolygonBuilder()
    multiPolygon.foreach(polygon => builder.addPolygon(toS2Polygon(polygon)))
    builder.assemblePolygon()
  }

  private[eliiza] def toMultiPolygon(multiPolygon: S2Polygon*): MultiPolygon = {
    def toPoint(point: S2Point): Point = Seq(longitude(point).degrees(), latitude(point).degrees())
    def toLoop(loop: S2Loop): Loop = for (i <- 0 until loop.numVertices()) yield toPoint(loop.vertex(i))
    def toPolygon(polygon: S2Polygon): Polygon =
      for (i <- 0 until polygon.numLoops()) yield toLoop(polygon.loop(i))

    multiPolygon.map(toPolygon)
  }

  def mergeMultiPolygons(multiPolygons: MultiPolygon*): MultiPolygon = {
    val builder = new S2PolygonBuilder()
    multiPolygons
      .map(toS2MultiPolygon)
      .foreach(builder.addPolygon)
    toMultiPolygon(builder.assemblePolygon())
  }

  def mayIntersect(multiPolygonA: MultiPolygon, multiPolygonB: MultiPolygon): Boolean = {
    def bounds(multiPolygon: MultiPolygon) = {
      val box = toS2MultiPolygon(multiPolygon).getRectBound
      (
        box.lngHi.degrees, // maxX
        box.lngLo.degrees, // minX
        box.latHi.degrees, // maxY
        box.latLo.degrees // minY
      )
    }

    val (a_maxX, a_minX, a_maxY, a_minY) = bounds(multiPolygonA)
    val (b_maxX, b_minX, b_maxY, b_minY) = bounds(multiPolygonB)

    a_minY <= b_maxY &&
      a_maxX >= b_minX &&
      a_maxY >= b_minY &&
      a_minX <= b_maxX
  }

  def multiPolygonArea(multiPolygon: MultiPolygon): Double = toS2MultiPolygon(multiPolygon).getArea

  def intersectionArea(multiPolygonA: MultiPolygon, multiPolygonB: MultiPolygon): Double = {
    val intersection = new S2Polygon()
    intersection.initToIntersection(toS2MultiPolygon(multiPolygonA), toS2MultiPolygon(multiPolygonB))
    intersection.getArea
  }

}
