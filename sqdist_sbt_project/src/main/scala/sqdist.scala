package test


object TestSqdit {

  def main(args: Array[String]): Unit = {


    val numberRuns = args(0).toInt
    val warmUpRun = (numberRuns * 0.6).toInt
    val vectorSizes = args(1).split(",").map(_.toInt)


    import org.apache.spark.ml.linalg.{Vector, SparseVector, Vectors, DenseVector}

    /* ... new cell ... */

    def sqdistImperativ(v1: Vector, v2: Vector): Double = {

      require(v1.size == v2.size, s"Vector dimensions do not match: Dim(v1)=${v1.size} and Dim(v2)" +

        s"=${v2.size}.")

      var squaredDistance = 0.0

      (v1, v2) match {

        case (v1: SparseVector, v2: SparseVector) =>

          val v1Values = v1.values

          val v1Indices = v1.indices

          val v2Values = v2.values

          val v2Indices = v2.indices

          val nnzv1 = v1Indices.length

          val nnzv2 = v2Indices.length

    

          var kv1 = 0

          var kv2 = 0

          while (kv1 < nnzv1 || kv2 < nnzv2) {

            var score = 0.0

    

            if (kv2 >= nnzv2 || (kv1 < nnzv1 && v1Indices(kv1) < v2Indices(kv2))) {

              score = v1Values(kv1)

              kv1 += 1

            } else if (kv1 >= nnzv1 || (kv2 < nnzv2 && v2Indices(kv2) < v1Indices(kv1))) {

              score = v2Values(kv2)

              kv2 += 1

            } else {

              score = v1Values(kv1) - v2Values(kv2)

              kv1 += 1

              kv2 += 1

            }

            squaredDistance += score * score

          }

    

        case (v1: SparseVector, v2: DenseVector) =>

          squaredDistance = Vectors.sqdist(v1, v2)

    

        case (v1: DenseVector, v2: SparseVector) =>

          squaredDistance = Vectors.sqdist(v2, v1)

    

        case (DenseVector(vv1), DenseVector(vv2)) =>

          var kv = 0

          val sz = vv1.length

          while (kv < sz) {

            val score = vv1(kv) - vv2(kv)

            squaredDistance += score * score

            kv += 1

          }

        case _ =>

          throw new IllegalArgumentException("Do not support vector type " + v1.getClass +

            " and " + v2.getClass)

      }

      squaredDistance

    }

    /* ... new cell ... */

    def sqdistFunctional(v1: Vector, v2: Vector): Double = {

      require(v1.size == v2.size, s"Vector dimensions do not match: Dim(v1)=${v1.size} and Dim(v2)" +

        s"=${v2.size}.")

      var squaredDistance = 0.0

      (v1, v2) match {

        case (v1: SparseVector, v2: SparseVector) =>

          val v1Values = v1.values

          val v1Indices = v1.indices

          val v2Values = v2.values

          val v2Indices = v2.indices

          val nnzv1 = v1Indices.length

          val nnzv2 = v2Indices.length

    

          var kv1 = 0

          var kv2 = 0

          while (kv1 < nnzv1 || kv2 < nnzv2) {

            var score = 0.0

    

            if (kv2 >= nnzv2 || (kv1 < nnzv1 && v1Indices(kv1) < v2Indices(kv2))) {

              score = v1Values(kv1)

              kv1 += 1

            } else if (kv1 >= nnzv1 || (kv2 < nnzv2 && v2Indices(kv2) < v1Indices(kv1))) {

              score = v2Values(kv2)

              kv2 += 1

            } else {

              score = v1Values(kv1) - v2Values(kv2)

              kv1 += 1

              kv2 += 1

            }

            squaredDistance += score * score

          }

    

        case (v1: SparseVector, v2: DenseVector) =>

          squaredDistance = Vectors.sqdist(v1, v2)

    

        case (v1: DenseVector, v2: SparseVector) =>

          squaredDistance = Vectors.sqdist(v2, v1)

    

        case (DenseVector(vv1), DenseVector(vv2)) =>

          val sz = vv1.length

          @annotation.tailrec
          def go(d: Double, kv: Int): Double = {

            if (kv < sz) {

              val score = vv1(kv) - vv2(kv)

              go(d + score * score, kv + 1)

            }

            else d

          }

          go(0D, 0)

        case _ =>

          throw new IllegalArgumentException("Do not support vector type " + v1.getClass +

            " and " + v2.getClass)

      }

      squaredDistance

    }

    /* ... new cell ... */


    

    val times = vectorSizes.map( vSize => {

    

      var ti = 0L

      var tf = 0L

    

      (0 until numberRuns).foreach( i =>  {

    

        val dv1 = Vectors.dense(Array.fill(vSize)(scala.util.Random.nextDouble))

        val dv2 = Vectors.dense(Array.fill(vSize)(scala.util.Random.nextDouble))

    

        val t01 = System.nanoTime

        sqdistImperativ(dv1, dv2)

        val t02 = System.nanoTime

        sqdistFunctional(dv1, dv2)

        val t03 = System.nanoTime

    

        if( i >= warmUpRun ) {

          ti += t02 - t01

          tf += t03 - t02

        }

      })

    

    

      val totalTimeSorted = Array(("Imperativ", ti), ("Functional", tf)).sortBy(_._2)

      (vSize, totalTimeSorted)

    })

    

    val flatten = times.flatMap{ case (vSize, ttimes) => ttimes.map{ case (id, tab) => vSize + ", " + id + ", " + tab } }

    println(flatten.mkString("\n"))
    

  }
}
                  