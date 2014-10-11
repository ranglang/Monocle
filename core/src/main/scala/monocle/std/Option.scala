package monocle.std

import monocle.function._
import monocle.{ SimplePrism, Prism, Iso }
import scalaz.{Maybe, -\/, \/-}

object option extends OptionInstances

trait OptionFunctions {

  def some[A, B]: Prism[Option[A], Option[B], A, B] =
    Prism[Option[A], Option[B], A, B](_.map(\/-(_)) getOrElse -\/(None))(Some.apply)

  def none[A]: SimplePrism[Option[A], Unit] =
    SimplePrism[Option[A], Unit](opt => if (opt == None) Maybe.just(()) else Maybe.empty)(_ => None)

  def someIso[A, B]: Iso[Some[A], Some[B], A, B] =
    Iso[Some[A], Some[B], A, B](_.get)(Some(_))

}

trait OptionInstances extends OptionFunctions {

  implicit def optionEmpty[A]: Empty[Option[A]] = new Empty[Option[A]] {
    def empty = none
  }

  implicit def optEach[A]: Each[Option[A], A] = new Each[Option[A], A] {
    def each = some.asTraversal
  }

  implicit val noneEmpty: Empty[None.type] = new Empty[None.type] {
    def empty = SimplePrism[None.type , Unit](_ => Maybe.Just(()))(_ => None)
  }

  implicit def someEach[A]: Each[Some[A], A] = new Each[Some[A], A] {
    def each = someIso.asTraversal
  }

}

