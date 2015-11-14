package nbmtools

import java.io.ByteArrayInputStream
import nbmtools.ZipUtils.Entry
import org.apache.commons.io.IOUtils

sealed trait ZipEntry

case class DirEntry(
    name: String, time: Long, comment: Option[String], extra: Option[Seq[Byte]])
    extends ZipEntry {
        def this(name: String, time: Long) = this(name, time, None, None)
        def this(name: String) = this(name, 0L, None, None)
    }

case class FileEntry(
    name: String, content: Seq[Byte], time: Long, comment: Option[String], extra: Option[Seq[Byte]])
    extends ZipEntry {
        def this(name: String, content: Array[Byte], time: Long) =
            this(name, content, time, None, None)
        def this(name: String, content: Array[Byte]) =
            this(name, content, 0L, None, None)

        def getZipEntry(): java.util.zip.ZipEntry = {
            val zipEntry = new java.util.zip.ZipEntry(name)
            zipEntry.setTime(time)
            zipEntry.setComment(comment.getOrElse(null))
            zipEntry.setExtra(extra.map(_.toArray).getOrElse(null))
            zipEntry
        }

        def getInputStream() = new ByteArrayInputStream(content.toArray)
    }

object ZipEntry {
    def of(entry: Entry): ZipEntry = {
        entry match {
            case (entry: ZipUtils.DirEntry) => {
                    val zipEntry = entry.getZipEntry
                    val name = zipEntry.getName
                    val time = zipEntry.getTime
                    val comment = zipEntry.getComment
                    val extra: Seq[Byte] = zipEntry.getExtra
                    DirEntry(name, time, Option(comment), Option(extra))
            }
            case (entry: ZipUtils.FileEntry) => {
                    val zipEntry = entry.getZipEntry
                    val name = zipEntry.getName
                    val time = zipEntry.getTime
                    val comment = zipEntry.getComment
                    val extra: Seq[Byte] = zipEntry.getExtra
                    val content: Seq[Byte] =
                        IOUtils.toByteArray(entry.getInputStream)
                    FileEntry(name, content, time, Option(comment), Option(extra))
            }
        }
    }
}