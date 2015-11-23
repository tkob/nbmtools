package nbmtools

import java.io.File
import java.io.FileInputStream
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

    def this(file: File) = { this(new FileInputStream(file)) }

    private def closeExternalStream() {
        externalStream match {
            case Some(es) => es.close()
            case None => ()
        }
        externalStream = None
    }

    override def getNextEntry() = {
        closeExternalStream()
        val originalEntry = super.getNextEntry
        if (originalEntry == null) null
        else
            originalEntry.getName match {
                case appendExternal(name) => {
                        val external = External.from(this)
                        externalStream = Some(external.openStream())
                        val entry = createZipEntry(name)
                        entry
                }
                case name => originalEntry
            }
    }

    override def read(b: Array[Byte], off: Int, len: Int): Int = {
        externalStream match {
            case None => super.read(b, off, len)
            case Some(es) => es.read(b, off, len)
        }
    }

    override def close() {
        closeExternalStream()
        super.close()
    }
}
