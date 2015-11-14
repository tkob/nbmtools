package nbmtools

import java.io.InputStream
import java.net.URI
import scala.io.BufferedSource

case class External(crc: Option[Long], urls: List[URI])

object concatWithColon {
    def apply(prefix: String, s: String): String = prefix + ":" + s
    def unapply(s: String): Option[(String, String)] = {
        val colonPos = s.indexOf(':')
        if (colonPos < 0) None
        else Some((s.substring(0, colonPos), s.substring(colonPos + 1)))
    }
}

object External {
    def from(is: InputStream): External = {
        val lines = new BufferedSource(is).getLines
        lines.foldRight(External(None, Nil)) {
            case (concatWithColon("CRC", crc), External(None, urls)) =>
                External(Some(crc.toLong), urls)
            case (concatWithColon("CRC", crc), External(Some(_), urls)) =>
                sys.error("duplicate CRC in external file")
            case (concatWithColon("URL", url), External(crc, urls)) =>
                External(crc, (new URI(url)::urls))
            case (_, external) => external
        }
    }
}