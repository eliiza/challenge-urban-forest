name := "challenge-urban-forest-jar"

version := "0.0.2"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.google.common.geometry" % "s2-geometry-java" % "0.0.1" from "file://./lib/s2-geometry-java-0.0.1.jar"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
