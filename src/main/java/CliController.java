public class CliController {
    private static CliController cliController;
    private SocketPostman socketPostman;

    private CliController(SocketPostman socketPostman){
        this.socketPostman = socketPostman;
    }
}
