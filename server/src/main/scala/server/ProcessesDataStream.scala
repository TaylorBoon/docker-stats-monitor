package server

import java.io.InputStream

import cats.effect.{ Blocker, ContextShift, Sync }
import fs2.Stream

object ProcessesDataStream {

  def stream[F[_]: Sync: ContextShift](fis: F[InputStream], blocker: Blocker): Stream[F, ProcessesData] =
    RawDataStream
      .stream(fis, blocker)
      .evalMap(Processes.parseCSV(_))
      .fold(ProcessesData.empty)((map, ps) => map + (ps.id -> ps))
}
