import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.imageio.ImageIO;

class ThreadWorker extends Thread {
	private String worker;
	private int index;

	ThreadWorker(String server, int idx) {
		this.worker = server;
		this.index = idx;
		
		System.out.println("Conectando a " + worker);
	}
	
	String pathNewFile = "img\\grayImage\\";
	String imagePath = "img";
	Integer port = 8000;
	public void run() {
        try {			
            Registry registry = LocateRegistry.getRegistry(worker, port);					
            GrayscaleInterface message = (GrayscaleInterface) registry.lookup("ConvertColor");
            File path = new File(imagePath);
            String[] files = path.list();
            int numOfFiles = files.length;
            byte[] images;
            
            for (int i = index - 1; i < numOfFiles; i+=2) {
            	String fileIndex = files[i];
            	File sendFile = new File(imagePath + "/" + fileIndex);
            	
            	String fileName = fileIndex.replaceFirst("[.][^.]+$", "");
            	String fileExtension = fileIndex.substring(fileIndex.lastIndexOf('.')+1);
            	BufferedImage img = ImageIO.read(sendFile);
            	BufferedImage half1 = img.getSubimage(0, 0,(img.getWidth()/2), img.getHeight());	
            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            	ImageIO.write(half1, fileExtension, baos);
            	images = new byte[baos.size()];
            	images = baos.toByteArray();
            	
            	int fileNumber = i + 1;
            	
            	// Converte pra Grayscale
            	byte[] imageOut = message.Convert(images, fileNumber, fileName, fileExtension);
    			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageOut));
    			File destinationPath = new File(pathNewFile + fileName + "_grayscale." + fileExtension);
    			ImageIO.write(image, fileExtension, destinationPath);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
    }

}

public class RMIClient {
	
    public static void main(String[] args) {
        String[] worker = {"127.0.0.1", "127.0.0.1"};
        
        long Inicio = System.currentTimeMillis();
        
        for (int i = 0; i < 2; i++) {
        	ThreadWorker t1 = new ThreadWorker(worker[i%2], i + 1);
        	t1.run();
        }
        
        long Fim = System.currentTimeMillis();
		long Total = (Fim - Inicio) / 1000;
		
		System.out.println("Tempo: " + Total + " segundos");
    }
    
}
