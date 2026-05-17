package util

import java.security.MessageDigest
import scala.collection.concurrent.TrieMap

/**
 * Computes a short MD5 fingerprint of a static asset by reading its bytes from
 * the classpath. Used to bust the browser cache via a `?v=<hash>` query string
 * appended to asset URLs.
 *
 * Results are cached for the lifetime of the JVM. In dev mode, sbt's hot reload
 * resets the cache whenever Scala code changes, and a full restart is enough to
 * pick up a fresh CSS hash. In prod, assets are immutable inside the package
 * and the cache is a pure win.
 */
object AssetFingerprint {

  private val cache = TrieMap.empty[String, String]
  private val Fallback = "0"

  def of(path: String): String =
    cache.getOrElseUpdate(path, compute(path))

  private def compute(path: String): String = {
    val resource = s"/public/${path.stripPrefix("/")}"
    Option(getClass.getResourceAsStream(resource)) match {
      case Some(stream) =>
        try {
          val bytes = stream.readAllBytes()
          MessageDigest
            .getInstance("MD5")
            .digest(bytes)
            .take(4)
            .map(b => "%02x".format(b & 0xff))
            .mkString
        } catch {
          case _: Throwable => Fallback
        } finally stream.close()
      case None => Fallback
    }
  }
}
