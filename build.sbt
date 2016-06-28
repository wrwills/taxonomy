lazy val root = (project in file(".")).
  settings(
    name := "taxonomy",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

libraryDependencies += "org.typelevel" %% "cats" % "0.4.1"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.4"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.4.4"
