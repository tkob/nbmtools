package nbmtools

import java.io.InputStream
import java.util.zip.ZipInputStream

object appendExternal {
    def apply(s: String): String = s + ".external"
    def unapply(s: String): Option[String] = {
        if (s.endsWith(".external"))
            Some(s.stripSuffix(".external"))
        else None
    }
}

class NbmInputStream(is: InputStream) extends ZipInputStream(is) {
    private var externalStream: Option[InputStream] = None

    override def getNextEntry() = {
        val originalEntry = super.getNextEntry
        originalEntry.getName match {
            case appendExternal(name) => {
                    val external = ???
                    // val externalSteam = ??? // TODO
                    val entry = createZipEntry(name)
                    entry
            }
            case name => {
                    externalStream = None
                    originalEntry
            }
        }
    }

    override def read(b: Array[Byte], off: Int, len: Int): Int = {
        externalStream match {
            case None => super.read(b, off, len)
            case Some(es) => es.read(b, off, len)
        }
    }
}
