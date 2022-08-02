package au.com.eliiza.urbanforest

import au.com.eliiza.urbanforest.{Point => SPoint}
import au.com.eliiza.urbanforest.{Loop => SLoop}
import au.com.eliiza.urbanforest.{Polygon => SPolygon}
import au.com.eliiza.urbanforest.{MultiPolygon => SMultiPolygon}
import au.com.eliiza.urbanforest.{mergeMultiPolygons => sMergeMultiPolygons}
import au.com.eliiza.urbanforest.{mayIntersect => sMayIntersect}
import au.com.eliiza.urbanforest.{multiPolygonArea => sMultiPolygonArea}
import au.com.eliiza.urbanforest.{intersectionArea => sIntersectionArea}
import java.util.{List => JList}
import scala.collection.JavaConversions._

object j {

  sealed trait Sequentiable[T] {
    def getSeq(): JList[T]
  }

  case class Point(coords: JList[Double]) extends Sequentiable[Double] {
    def getSeq(): JList[Double] = coords
  }

  case class Loop(points: JList[Point]) extends Sequentiable[Point] {
    def getSeq(): JList[Point] = points
  }

  case class Polygon(loops: JList[Loop]) extends Sequentiable[Loop] {
    def getSeq(): JList[Loop] = loops
  }

  case class MultiPolygon(polygons: JList[Polygon])
      extends Sequentiable[Polygon] {
    def getSeq(): JList[Polygon] = polygons
  }

  def mergeMultiPolygons(multiPolygons: MultiPolygon*): MultiPolygon = {
    val sMultiPolygons: Seq[SMultiPolygon] =
      for (jMultiPolygon <- multiPolygons)
        yield jMultiPolygonToSMultiPolygon(jMultiPolygon)
    val merge = sMergeMultiPolygons(sMultiPolygons: _*)
    sMultiPolygonToJMultiPolygon(merge)
  }

  def mayIntersect(
      multiPolygonA: MultiPolygon,
      multiPolygonB: MultiPolygon
  ): Boolean =
    sMayIntersect(
      jMultiPolygonToSMultiPolygon(multiPolygonA),
      jMultiPolygonToSMultiPolygon(multiPolygonB)
    )

  def multiPolygonArea(multiPolygon: MultiPolygon): Double =
    sMultiPolygonArea(jMultiPolygonToSMultiPolygon(multiPolygon))

  def intersectionArea(
      multiPolygonA: MultiPolygon,
      multiPolygonB: MultiPolygon
  ): Double =
    sIntersectionArea(
      jMultiPolygonToSMultiPolygon(multiPolygonA),
      jMultiPolygonToSMultiPolygon(multiPolygonB)
    )

  private def jPointToSPoint(jPoint: Point): SPoint =
    for (jCoord <- jPoint.getSeq) yield jCoord

  private def sPointToJPoint(sPoint: SPoint): Point =
    Point((for (sCoord <- sPoint) yield sCoord).toList)

  private def jLoopToSLoop(jLoop: Loop): SLoop =
    for (jPoint <- jLoop.getSeq) yield jPointToSPoint(jPoint)

  private def sLoopToJLoop(sLoop: SLoop): Loop =
    Loop(for (sPoint <- sLoop) yield sPointToJPoint(sPoint))

  private def jPolygonToSPolygon(jPolygon: Polygon): SPolygon =
    for (jLoop <- jPolygon.getSeq) yield jLoopToSLoop(jLoop)

  private def sPolygonToJPolygon(sPolygon: SPolygon): Polygon =
    Polygon(for (sLoop <- sPolygon) yield sLoopToJLoop(sLoop))

  private def jMultiPolygonToSMultiPolygon(
      jMultiPolygon: MultiPolygon
  ): SMultiPolygon =
    for (jPolygon <- jMultiPolygon.getSeq) yield jPolygonToSPolygon(jPolygon)

  private def sMultiPolygonToJMultiPolygon(
      sMultiPolygon: SMultiPolygon
  ): MultiPolygon =
    MultiPolygon(
      for (sPolygon <- sMultiPolygon) yield sPolygonToJPolygon(sPolygon)
    )

}
