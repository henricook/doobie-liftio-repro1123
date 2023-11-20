package com.example

import cats.effect.{IO, IOApp, LiftIO}
import doobie.{ConnectionIO, FC, Transactor, WeakAsync}
import doobie.implicits._
import doobie.util.transactor.Strategy
import org.mockito.ArgumentMatchers.any
import org.mockito.MockitoSugar

import java.sql.Connection

object Main extends IOApp.Simple with MockitoSugar {

  // Create a mock transactor with Scalatest
  lazy val dummyRecoveringTransactor: Transactor[IO] = {
    val mockConnection = mock[Connection]
    doNothing.when(mockConnection).setClientInfo(any[String], any[String])
    doNothing.when(mockConnection).setTransactionIsolation(any[Int])

    val s: Strategy = doobie.util.transactor.Strategy(FC.unit, FC.unit, FC.unit, FC.unit)

      Transactor
        .fromConnection[IO](mockConnection, logHandler = None)
        .copy(strategy0 = s)
  }

  // Works, produces all three printlns
  // def run: IO[Unit] = WeakAsync.liftIO[ConnectionIO].use(x => printWithLift(x).transact(dummyRecoveringTransactor))

  // Also works (now the .flatten is added)
  def run: IO[Unit] = {
    (for {
      (result, cancelHook) <- WeakAsync.liftIO[ConnectionIO].allocated
    } yield {
      printWithLift(result).transact(dummyRecoveringTransactor)
    }).flatten
  }

  // This is your new "main"!
  def printWithLift(liftCIO: LiftIO[ConnectionIO]): ConnectionIO[Unit] = for {
    _ <- WeakAsync[ConnectionIO].pure(println("Hi there!"))
    - <- liftCIO.liftIO(IO.pure(println("Hi I'm in the middle!")))
    _ <- WeakAsync[ConnectionIO].pure(println("Bye there!"))
  } yield ()
}
