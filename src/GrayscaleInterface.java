import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GrayscaleInterface extends Remote {
	byte[] Convert(byte[] imageByteArray, int fileNumber, String fileName, String fileExtension) throws RemoteException, IOException;
}
