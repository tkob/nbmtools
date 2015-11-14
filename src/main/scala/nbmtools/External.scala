package nbmtools

import java.net.URI

case class External(crc: Option[Long], urls: List[URI])

object concatWithColon {
    def apply(prefix: String, s: String): String = prefix + ":" + s
    def unapply(s: String): Option[(String, String)] = {
        val colonPos = s.indexOf(':')
        if (colonPos < 0) None
        else Some((s.substring(0, colonPos), s.substring(colonPos + 1)))
    }
}