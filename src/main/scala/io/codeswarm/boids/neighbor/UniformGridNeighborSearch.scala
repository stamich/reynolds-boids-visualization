package io.codeswarm.boids.neighbor

import io.codeswarm.boids.model.{Boid, WorldConfig}

import scala.collection.mutable

/** Uniform-grid spatial index for local boid neighborhood queries.
  *
  * The world is partitioned into square cells with side `cellSize`. Building the grid is O(n), while a query normally examines only the cells intersecting the
  * requested radius instead of scanning the complete flock. An exact squared Euclidean-distance filter is still applied, so grid cells only reduce the
  * candidate set and never approximate the Reynolds neighborhood.
  *
  * Milestone 0.3 deliberately preserves the Euclidean distance semantics of 0.2. Position wrapping remains a boundary policy; opposite edges are therefore not
  * treated as adjacent during neighbor lookup yet.
  *
  * @param cellSize
  *   side length of one spatial cell in world units
  */
final class UniformGridNeighborSearch(cellSize: Double) extends NeighborSearch {
  require(cellSize > 0.0 && java.lang.Double.isFinite(cellSize), "cellSize must be finite and positive")

  /** Builds an immutable cell map for one flock snapshot. */
  override def index(flock: IndexedSeq[Boid], world: WorldConfig): NeighborIndex = {
    val buckets = mutable.HashMap.empty[Cell, mutable.ArrayBuffer[Boid]]
    flock.foreach { boid =>
      val cell = Cell.from(boid, cellSize)
      buckets.getOrElseUpdate(cell, mutable.ArrayBuffer.empty) += boid
    }
    val immutableBuckets = buckets.iterator.map { case (cell, boids) => cell -> boids.toVector }.toMap
    new UniformGridNeighborIndex(immutableBuckets, cellSize)
  }
}

/** Internal grid query implementation created once per simulation tick. */
private final class UniformGridNeighborIndex(cells: Map[Cell, Vector[Boid]], cellSize: Double) extends NeighborIndex {

  /** Queries all cells intersecting the radius and then performs exact distance filtering. */
  override def neighborsOf(boid: Boid, radius: Double): IndexedSeq[Boid] = {
    require(radius > 0.0 && java.lang.Double.isFinite(radius), "radius must be finite and positive")
    val center = Cell.from(boid, cellSize)
    val extent = math.ceil(radius / cellSize).toInt
    val radiusSquared = radius * radius
    val result = Vector.newBuilder[Boid]

    var cellY = center.y - extent
    while (cellY <= center.y + extent) {
      var cellX = center.x - extent
      while (cellX <= center.x + extent) {
        cells.get(Cell(cellX, cellY)).foreach { bucket =>
          bucket.foreach { other =>
            if (other.id != boid.id) {
              val distanceSquared = boid.position.distanceSquaredTo(other.position)
              if (distanceSquared > 0.0 && distanceSquared < radiusSquared) {
                result += other
              }
            }
          }
        }
        cellX += 1
      }
      cellY += 1
    }

    result.result().sortBy(_.id.value)
  }
}

/** Integer coordinates of one uniform-grid cell. */
private final case class Cell(x: Int, y: Int)

/** Cell coordinate helpers. */
private object Cell {

  /** Maps a boid position to its containing cell. */
  def from(boid: Boid, cellSize: Double): Cell =
    Cell(
      math.floor(boid.position.x / cellSize).toInt,
      math.floor(boid.position.y / cellSize).toInt
    )
}
