package io.codeswarm.boids.neighbor

import io.codeswarm.boids.metrics.SpatialMetrics
import io.codeswarm.boids.model.{Boid, WorldConfig}

import scala.collection.mutable

/** Uniform-grid spatial index for local boid neighborhood queries.
  *
  * @param cellSize
  *   side length of one spatial cell in world units
  * @param metricsEnabled
  *   whether candidate/query counters are collected
  */
final class UniformGridNeighborSearch(cellSize: Double, metricsEnabled: Boolean = false) extends NeighborSearch {

  /** Java-friendly constructor with diagnostics disabled. */
  def this(cellSize: Double) = this(cellSize, false)

  require(cellSize > 0.0 && java.lang.Double.isFinite(cellSize), "cellSize must be finite and positive")

  /** Builds an immutable cell map for one flock snapshot. */
  override def index(flock: IndexedSeq[Boid], world: WorldConfig): NeighborIndex = {
    val buckets = mutable.HashMap.empty[Cell, mutable.ArrayBuffer[Boid]]
    flock.foreach { boid =>
      val cell = Cell.from(boid, cellSize)
      buckets.getOrElseUpdate(cell, mutable.ArrayBuffer.empty) += boid
    }
    val immutableBuckets = buckets.iterator.map { case (cell, boids) => cell -> boids.toVector }.toMap
    if (metricsEnabled) new InstrumentedUniformGridNeighborIndex(immutableBuckets, cellSize)
    else new UniformGridNeighborIndex(immutableBuckets, cellSize)
  }
}

/** Basic grid query implementation used by performance-sensitive paths. */
private class UniformGridNeighborIndex(protected val cells: Map[Cell, Vector[Boid]], protected val cellSize: Double) extends NeighborIndex {

  /** Queries all intersecting cells and applies exact Euclidean-distance filtering. */
  override def neighborsOf(boid: Boid, radius: Double): IndexedSeq[Boid] =
    query(boid, radius)._1

  /** Performs a query and also returns the number of inspected candidate boids. */
  protected def query(boid: Boid, radius: Double): (IndexedSeq[Boid], Int) = {
    require(radius > 0.0 && java.lang.Double.isFinite(radius), "radius must be finite and positive")
    val center = Cell.from(boid, cellSize)
    val extent = math.ceil(radius / cellSize).toInt
    val radiusSquared = radius * radius
    val result = Vector.newBuilder[Boid]
    var candidates = 0

    var cellY = center.y - extent
    while (cellY <= center.y + extent) {
      var cellX = center.x - extent
      while (cellX <= center.x + extent) {
        cells.get(Cell(cellX, cellY)).foreach { bucket =>
          candidates += bucket.size
          bucket.foreach { other =>
            if (other.id != boid.id) {
              val distanceSquared = boid.position.distanceSquaredTo(other.position)
              if (distanceSquared > 0.0 && distanceSquared < radiusSquared) result += other
            }
          }
        }
        cellX += 1
      }
      cellY += 1
    }
    (result.result().sortBy(_.id.value), candidates)
  }
}

/** Instrumented grid index used by the desktop observability path. */
private final class InstrumentedUniformGridNeighborIndex(cells: Map[Cell, Vector[Boid]], cellSize: Double)
    extends UniformGridNeighborIndex(cells, cellSize)
    with InstrumentedNeighborIndex {
  private var queryCount = 0L
  private var candidateCount = 0L
  private var neighborCount = 0L

  /** Executes one query while accumulating candidate and accepted-neighbor counters. */
  override def neighborsOf(boid: Boid, radius: Double): IndexedSeq[Boid] = {
    val (neighbors, candidates) = query(boid, radius)
    queryCount += 1L
    candidateCount += candidates.toLong
    neighborCount += neighbors.size.toLong
    neighbors
  }

  /** Returns occupancy and query metrics accumulated by this index. */
  override def spatialMetrics: SpatialMetrics = {
    val occupancies = cells.valuesIterator.map(_.size).toVector
    val occupied = occupancies.size
    val averageOccupancy = if (occupied == 0) 0.0 else occupancies.sum.toDouble / occupied.toDouble
    val maxOccupancy = occupancies.maxOption.getOrElse(0)
    val averageCandidates = if (queryCount == 0L) 0.0 else candidateCount.toDouble / queryCount.toDouble
    val averageNeighbors = if (queryCount == 0L) 0.0 else neighborCount.toDouble / queryCount.toDouble
    SpatialMetrics(occupied, averageOccupancy, maxOccupancy, averageCandidates, averageNeighbors)
  }
}

/** Integer coordinates of one uniform-grid cell. */
private final case class Cell(x: Int, y: Int)

/** Cell coordinate helpers. */
private object Cell {

  /** Maps a boid position to its containing cell. */
  def from(boid: Boid, cellSize: Double): Cell =
    Cell(math.floor(boid.position.x / cellSize).toInt, math.floor(boid.position.y / cellSize).toInt)
}
