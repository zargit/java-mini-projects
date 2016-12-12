package main.java;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.imageio.ImageIO;

public class MagicWand {
	
	MagicWand(){
		RGB[][] image = getImageAs2DArray("20.jpg");
		for(int i=0;i<image.length;i++){
			for(int j=0;j<image[i].length;j++){
				System.out.print(image[i][j].g+" "); // change into r,g or b to get corresponding value
			}
			System.out.println();
		}
	}
	
	public class RGB{
		// The values in r, g and b are between 0 and 255
		public int r,g,b;
		RGB(int r, int g, int b){
			this.r = r;
			this.b = b;
			this.g = g;
		}
	}

	RGB[][] getImageAs2DArray(String filename){
		BufferedImage img = null;
		RGB[][] array = null;
		try{
			img = ImageIO.read(new File(filename));
			final byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			
			int w = img.getWidth();
			int h = img.getHeight();
			boolean hasAlpha = img.getAlphaRaster()!=null;
			
			array = new RGB[h][w];
			
			int pixelLength = (hasAlpha) ? 4:3;
			
			for(int i=0, row=0, col = 0; i<pixels.length; i += pixelLength){
				int a,r,g,b;
				a = (hasAlpha) ? (((int) pixels[i] & 0xff) << 24):-16777216;
				a = a >> 24;
				b = ((int) pixels[i + pixelLength - 3] & 0xff);
				g = (((int) pixels[i + pixelLength - 2] & 0xff) << 8);
				g = g >> 8;
				r = (((int) pixels[i + pixelLength - 1] & 0xff) << 16);
				r = r >> 16;
				
				array[row][col] = new RGB(r,g,b);
				col++;
				if(col==w){
					col = 0;
					row++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return array;
	}
	
	public static void main(String[] args) {
		new MagicWand();
	}
}
