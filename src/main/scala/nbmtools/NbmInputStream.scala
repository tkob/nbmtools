package nbmtools

import java.io.InputStream
import java.util.zip.ZipInputStream

class NbmInputStream(is: InputStream) extends ZipInputStream(is) {
    override def getNextEntry() = {
        val originalEntry = super.getNextEntry
        if (originalEntry.getName.endsWith(".external")) {
            // TODO
            originalEntry
        } else originalEntry
    }
}
