import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
	public BufferedImage run(Integer port, Integer half) {
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
            	if (half == 1) {
            	BufferedImage halfImg = img.getSubimage(0, 0,(img.getWidth()/2), img.getHeight());
            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            	ImageIO.write(halfImg, fileExtension, baos);
            	images = new byte[baos.size()];
            	images = baos.toByteArray();
            	}
            	else {
            		BufferedImage halfImg = img.getSubimage((img.getWidth()/2), 0,(img.getWidth()/2), img.getHeight());
            		ByteArrayOutputStream baos = new ByteArrayOutputStream();
                	ImageIO.write(halfImg, fileExtension, baos);
                	images = new byte[baos.size()];
                	images = baos.toByteArray();
            	}
            	
            	int fileNumber = i + 1;
            	
            	// Converte pra Grayscale
            	byte[] imageOut = message.Convert(images, fileNumber, fileName, fileExtension);
    			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageOut));
    			
    			File destinationPath = new File(pathNewFile + fileName + "_grayscale" + half +"." + fileExtension);
    			ImageIO.write(image, fileExtension, destinationPath);
    			return image;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " Error in thread 1");
        }
		return null;
    }

}

public class RMIClient {
	
    public static void main(String[] args) throws IOException {
        long Inicio = System.currentTimeMillis();
        Integer port1 = 8000;
        Integer port2 = 8001;
        BufferedImage half1, half2;
        BufferedImage half3;
        Integer w, h;
        
        ThreadWorker t1 = new ThreadWorker("127.0.0.1", 1);
        ThreadWorker t2 = new ThreadWorker("127.0.0.2", 1);
        half1 = t1.run(port1, 1);
        half2 = t2.run(port2, 2);
        
        w = 2*half1.getWidth() +1;
        h = half1.getHeight();
        BufferedImage combined = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combined.createGraphics();
        g.drawImage(half1, 0, 0, null);
        g.drawImage(half2, half1.getWidth(), 0, null);

        g.dispose();
        ImageIO.write(combined, "jpg", new File("img\\grayImage\\combined.jpg"));
        
        long Fim = System.currentTimeMillis();
		long Total = (Fim - Inicio) / 1000;
		
		System.out.println("Tempo: " + Total + " segundos");
    }
    
}