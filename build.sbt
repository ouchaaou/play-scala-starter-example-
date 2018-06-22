libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.1"


name := """play-scala-starter-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
val nexus = "http://nexus.aerowill.net"

resolvers ++= Seq(
      Resolver.url("Nexus Ivy", url(nexus + "/repository/ivy-all"))(Resolver.ivyStylePatterns),
      Resolver.url("Nexus Maven", url(nexus + "/repository/maven-all"))
)
scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.6")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.197"


// Connexion with DataBase
scalacOptions += "-Ypartial-unification" // 2.11.9+

libraryDependencies ++= Seq(

      // Start with this one
      "org.tpolecat" %% "doobie-core"      % "0.5.3",

      // And add any of these as needed
      "org.tpolecat" %% "doobie-postgres"  % "0.5.3" // Postgres driver 42.2.2 + type mappings.

)