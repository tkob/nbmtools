package nbmtools

import java.io.InputStream
import java.io.PrintStream

class Internalize extends Command {
    override def run(
        in: InputStream, out: PrintStream, err: PrintStream, args: String*) = {
        System.err.println("TODO")
        Command.EXIT_SUCCESS
    }
}
