import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

	Integer port = 8000;
	
	private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            
            registry.rebind("ConvertColor", new Grayscale());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
        System.out.println("Server 1 ligado!!!");
    }
	
	public static void main(String[] args) {
        RMIServer main = new RMIServer();
        main.startServer();
    }
	
}
