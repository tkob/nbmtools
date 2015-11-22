package nbmtools

import java.io.ByteArrayOutputStream
import java.io.PrintStream

class StubPrintStream extends PrintStream(new ByteArrayOutputStream()) {
    override def toString() = {
        out.asInstanceOf[ByteArrayOutputStream].toString("UTF-8")
    }
}
