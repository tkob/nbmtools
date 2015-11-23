package nbmtools

object Main {
    private val commands = Map("internalize" -> new Internalize());

    private def usage() {
        System.err.println("""usage: nbmtools <subcommand> [<args>]
                             |
                             |subcommands are:""".stripMargin)
        commands.keys.toList.sorted.foreach { key =>
            System.err.println("    " + key)
        }
    }

    def main(args: Array[String]) {
        val status: Option[Int] = for {
            cmdName <- args.headOption
            cmd <- commands.get(cmdName)
        } yield cmd.run(System.in, System.out, System.err, args.tail:_*)
        
        status match {
            case Some(status) => System.exit(status)
            case None => {
                    usage()
                    System.exit(Command.EXIT_FAILURE)
            }
        }
    }
}
