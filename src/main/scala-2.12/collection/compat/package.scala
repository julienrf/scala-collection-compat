package scala.collection

import scala.collection.generic._
import scala.collection.mutable.Builder
import scala.reflect.ClassTag

/** The collection compatibility API */
package object compat {
  import scala.collection.compat_impl._

  implicit def genericCompanionToCBF[A, CC[X] <: GenTraversable[X]](fact: GenericCompanion[CC]): CanBuildFrom[Nothing, A, CC[A]] =
    simpleCBF(fact.newBuilder[A])

  implicit def arrayCompanionToCBF[A : ClassTag](fact: Array.type): CanBuildFrom[Nothing, A, Array[A]] =
    simpleCBF(Array.newBuilder[A])

  implicit def mapFactoryToCBF[K, V, CC[A, B] <: Map[A, B] with MapLike[A, B, CC[A, B]]](fact: MapFactory[CC]): CanBuildFrom[Nothing, (K, V), CC[K, V]] =
    simpleCBF(fact.newBuilder[K, V])

  implicit def immutableBitSetFactoryToCBF(fact: BitSetFactory[immutable.BitSet]): CanBuildFrom[Nothing, Int, ImmutableBitSetCC[Int]] =
    simpleCBF(fact.newBuilder)

  implicit def mutableBitSetFactoryToCBF(fact: BitSetFactory[mutable.BitSet]): CanBuildFrom[Nothing, Int, MutableBitSetCC[Int]] =
    simpleCBF(fact.newBuilder)

  implicit class IterableFactoryExtensionMethods[CC[X] <: GenTraversable[X]](private val fact: GenericCompanion[CC]) {
    def from[A](source: TraversableOnce[A]): CC[A] = fact.apply(source.toSeq: _*)
  }

  implicit class MapFactoryExtensionMethods[CC[A, B] <: Map[A, B] with MapLike[A, B, CC[A, B]]](private val fact: MapFactory[CC]) {
    def from[K, V](source: TraversableOnce[(K, V)]): CC[K, V] = fact.apply(source.toSeq: _*)
  }

  implicit class BitSetFactoryExtensionMethods[C <: scala.collection.BitSet with scala.collection.BitSetLike[C]](private val fact: BitSetFactory[C]) {
    def fromSpecific(source: TraversableOnce[Int]): C = fact.apply(source.toSeq: _*)
  }

  // This really belongs into scala.collection but there's already a package object in scala-library so we can't add to it
  type IterableOnce[+X] = TraversableOnce[X]
}
