package nbmtools

import java.io.Closeable
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.PrintStream
import java.net.URL
import org.apache.commons.io.IOUtils
import scala.util.Failure
import scala.util.Success
import scala.util.Try

class Internalize extends Command {
    def openIn(s: String) =
        Try(new URL(s)) match {
            case Success(url) => url.openStream
            case Failure(_) => new FileInputStream(s)
        }

    private def using[T <: Closeable, U](resource: T) ( block: T => U ): U =
        try { block(resource) } finally { resource.close }

    override def run(
        in: InputStream, out: PrintStream, err: PrintStream, args: String*): Int = {
        val names: Try[(String, String)] = for {
            fromName <- Try(args(0))
            toName <- Try(args(1))
        } yield (fromName, toName) 
        names match {
            case Failure(_) =>
                err.println("too few arguments")
                Command.EXIT_FAILURE
            case Success((fromName, toName)) => {
                using(new NbmInputStream(openIn(fromName))) { fromStream =>
                using(new FileOutputStream(toName)) { toStream =>
                    IOUtils.copy(fromStream, toStream)
                }}
                Command.EXIT_SUCCESS
            }
        }
    }
}