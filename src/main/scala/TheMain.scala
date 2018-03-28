package almikey.effect

import cats.effect.IO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object TheMain extends App {

  //we can call runAsync on an async enabled IO
  var IAmAnAsyncEnabledIO: IO[String] = IO.async { iAmACallBackFn =>
    def mySync =
      Future.successful("father forgive me i've been an asyncful man")
    mySync.onComplete {
      //if things went ok with my async function call
      //the iAmACallBackFn with the result
      case Success(a) => {
        iAmACallBackFn(Right(a))
        a
      }
      case Failure(e) => {
        iAmACallBackFn(Left(e))
        e.getMessage
      }
    }
  }

  //this is the callback function that will be passed in as iAmACallBackFn
  def theCallBackFn(n: Either[Throwable, String]): Unit = {
    n match {
      //if i get a Right i print hooray
      case Right(a) => println("hooray")
      //if i get a throwable i say boo, though i should probably do something the throwable message
      case Left(e) => println("boo")
    }
  }

  //so we have defined the async enabled IO which is impure
  //meaning it pulls values out of thin air or something beyond
  //our control like a db which can fail.
  //in our case we have a hard coded Future.successful in it
  //now we run it and feed it the call back we want it to use to tell us
  //if things are ok
//IAmAnAsyncEnabledIO.unsafeRunAsync(theCallBackFn)
  var iob = IO("i is a string")
  var ioc = for {
    a <- IAmAnAsyncEnabledIO
    b<-iob
  } yield a

 println(ioc.unsafeRunSync())

  IO().unsafeRunSync()
}
