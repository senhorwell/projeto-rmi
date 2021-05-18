import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

	Integer port = 8000;
	Integer port2 = 8001;
	
	private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            Registry registry2 = LocateRegistry.createRegistry(port2);
            
            registry.rebind("ConvertColor", new Grayscale());
            registry2.rebind("ConvertColor", new Grayscale());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
        System.out.println("Server ligado!!!");
    }
	
	public static void main(String[] args) {
        RMIServer main = new RMIServer();
        main.startServer();
    }
	
}