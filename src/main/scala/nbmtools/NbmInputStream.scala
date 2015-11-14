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
    override def getNextEntry() = {
        val originalEntry = super.getNextEntry
        originalEntry.getName match {
            case appendExternal(name) => {
                    // TODO
                    originalEntry
            }
            case name => originalEntry
        }
    }
}
