ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.13.12"

val doobieVersion = "1.0.0-RC5"

lazy val root = (project in file(".")).settings(
  name := "cats-effect-3-quick-start",
  libraryDependencies ++= Seq(
    // "core" module - IO, IOApp, schedulers
    // This pulls in the kernel and std modules automatically.
    "org.typelevel" %% "cats-effect" % "3.3.12",
    // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
    "org.typelevel" %% "cats-effect-kernel" % "3.3.12",
    // standard "effect" library (Queues, Console, Random etc.)
    "org.typelevel" %% "cats-effect-std" % "3.3.12",
    // better monadic for compiler plugin as suggested by documentation
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    "org.tpolecat" %% "doobie-core" % doobieVersion
    ,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion
    ,
    "org.tpolecat" %% "doobie-postgres" % doobieVersion
    ,

    "org.mockito" %% "mockito-scala" % "1.17.29",
    "org.scalatest" %% "scalatest" % "3.2.17",
  )
)
