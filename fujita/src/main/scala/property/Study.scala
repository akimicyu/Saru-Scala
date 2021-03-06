package property

import fpinscala.state.{State, RNG}


/**
  * 8章 プロパティベースのテスト
  */
object Study {

  def main(args:Array[String]) = {
    val c = (new TestProp && new TestProp).check
    println(c)
  }

  def sum(ints:List[Int]):Int = {
    ints.foldLeft(0)((s, elem) => elem + s)
  }
}

class TestProp extends Prop {
  def check:Boolean = {
    println("checking TestProp!!")
    true
  }
}


/**
  * テスト用値のジェネレータ
  * @param sample
  * @tparam A
  */
case class Gen[A](sample:State[RNG, A])

object Gen {
  def choose(start:Int, stopExclusive:Int):Gen[Int] = {
    Gen(State(RNG.nonNegativeInt).map(n => start + n % (stopExclusive - start)))
  }

  def choose2(start: Int, stopExclusive: Int): Gen[Int] = {
    Gen(State(rng => RNG.nonNegativeInt(rng) match {
      case (n, rng2) => (start + n % (stopExclusive-start), rng2)
      }))
  }

  def unit[A](a: => A): Gen[A] = {
    Gen(State.unit(a))
  }

  def boolean: Gen[Boolean] = {
    Gen(State(RNG.boolean))
  }

  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] = {
    Gen(State.sequence(List.fill(n)(g.sample)))
  }
}



trait Prop {
  def check:Boolean

  def &&(p:Prop):Prop = new Prop {
    def check = p.check && Prop.this.check
  }
}











