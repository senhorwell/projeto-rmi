import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer2 {
	
	Integer port2 = 8001;
	
	private void startServer(){
        try {
            Registry registry2 = LocateRegistry.createRegistry(port2);
            
            registry2.rebind("ConvertColor", new Grayscale());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
        System.out.println("Server 2 ligado!!!");
    }
	
	public static void main(String[] args) {
        RMIServer2 main = new RMIServer2();
        main.startServer();
    }

}
