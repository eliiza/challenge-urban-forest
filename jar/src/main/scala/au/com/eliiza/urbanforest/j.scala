package au.com.eliiza.urbanforest

import au.com.eliiza.urbanforest.{Point => SPoint}
import au.com.eliiza.urbanforest.{Loop => SLoop}
import au.com.eliiza.urbanforest.{Polygon => SPolygon}
import au.com.eliiza.urbanforest.{MultiPolygon => SMultiPolygon}
import au.com.eliiza.urbanforest.{mergeMultiPolygons => sMergeMultiPolygons}
import au.com.eliiza.urbanforest.{mayIntersect => sMayIntersect}
import au.com.eliiza.urbanforest.{multiPolygonArea => sMultiPolygonArea}
import au.com.eliiza.urbanforest.{intersectionArea => sIntersectionArea}
import java.lang.{Double => JDouble}
import java.util.{List => JList}
import scala.jdk.CollectionConverters._

object j {

  sealed trait Sequentiable[T] extends java.io.Serializable {
    def getSeq(): JList[T]
  }

  class Point(coords: JList[JDouble]) extends Sequentiable[JDouble] {
    def getSeq(): JList[JDouble] = coords
    def getCoordinates() = getSeq()

    override def equals(p: Any): Boolean =
      getSeq().containsAll(p.asInstanceOf[Point].getSeq())
  }

  class Loop(points: JList[Point]) extends Sequentiable[Point] {
    def getSeq(): JList[Point] = points
    def getPoints() = getSeq()

    override def equals(p: Any): Boolean =
      getSeq().containsAll(p.asInstanceOf[Loop].getSeq())
  }

  class Polygon(loops: JList[Loop]) extends Sequentiable[Loop] {
    def getSeq(): JList[Loop] = loops
    def getLoops() = getSeq()

    override def equals(p: Any): Boolean =
      getSeq().containsAll(p.asInstanceOf[Polygon].getSeq())
  }

  class MultiPolygon(polygons: JList[Polygon]) extends Sequentiable[Polygon] {
    def getSeq(): JList[Polygon] = polygons
    def getPolygons() = getSeq()

    override def equals(p: Any): Boolean =
      getSeq().containsAll(p.asInstanceOf[MultiPolygon].getSeq())
  }

  def mergeMultiPolygons(
      multiPolygonA: MultiPolygon,
      multiPolygonB: MultiPolygon
  ): MultiPolygon = {
    val merge = sMergeMultiPolygons(
      jMultiPolygonToSMultiPolygon(multiPolygonA),
      jMultiPolygonToSMultiPolygon(multiPolygonB)
    )
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
    for (jCoord <- jPoint.getSeq.asScala.toSeq.map(d => d.asInstanceOf[Double]))
      yield jCoord

  private def sPointToJPoint(sPoint: SPoint): Point =
    new Point(
      (for (sCoord <- sPoint) yield JDouble.valueOf(sCoord)).toList.asJava
    )

  private def jLoopToSLoop(jLoop: Loop): SLoop =
    for (jPoint <- jLoop.getSeq.asScala.toSeq) yield jPointToSPoint(jPoint)

  private def sLoopToJLoop(sLoop: SLoop): Loop =
    new Loop((for (sPoint <- sLoop) yield sPointToJPoint(sPoint)).toList.asJava)

  private def jPolygonToSPolygon(jPolygon: Polygon): SPolygon =
    for (jLoop <- jPolygon.getSeq.asScala.toSeq) yield jLoopToSLoop(jLoop)

  private def sPolygonToJPolygon(sPolygon: SPolygon): Polygon =
    new Polygon(
      (for (sLoop <- sPolygon) yield sLoopToJLoop(sLoop)).toList.asJava
    )

  private def jMultiPolygonToSMultiPolygon(
      jMultiPolygon: MultiPolygon
  ): SMultiPolygon =
    for (jPolygon <- jMultiPolygon.getSeq.asScala.toSeq)
      yield jPolygonToSPolygon(jPolygon)

  private def sMultiPolygonToJMultiPolygon(
      sMultiPolygon: SMultiPolygon
  ): MultiPolygon =
    new MultiPolygon(
      (for (sPolygon <- sMultiPolygon)
        yield sPolygonToJPolygon(sPolygon)).toList.asJava
    )

}
