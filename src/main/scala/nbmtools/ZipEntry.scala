package nbmtools

import nbmtools.ZipUtils.Entry
import org.apache.commons.io.IOUtils

sealed trait ZipEntry

case class DirEntry(
    name: String, time: Long, comment: Option[String], extra: Option[Seq[Byte]])
    extends ZipEntry

case class FileEntry(
    name: String, content: Seq[Byte], time: Long, comment: Option[String], extra: Option[Seq[Byte]])
    extends ZipEntry

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