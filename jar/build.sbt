name := "challenge-urban-forest-jar"
version := "1.0.0"
scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "io.sgr" % "s2-geometry-library-java" % "1.0.1"
)

assembly / assemblyOption := (assembly / assemblyOption).value
  .copy(includeScala = false)
