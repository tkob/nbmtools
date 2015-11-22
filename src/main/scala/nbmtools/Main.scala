package nbmtools

object Main {
    private val commands = Map("internalize" -> new Internalize());

    def main(args: Array[String]) {
        val status: Option[Int] = for {
            cmdName <- args.headOption
            cmd <- commands.get(cmdName)
        } yield cmd.run(System.in, System.out, System.err, args:_*)
        
        System.exit(status.getOrElse(Command.EXIT_FAILURE))
    }
}
