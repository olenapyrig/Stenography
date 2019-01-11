package data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MyImage {
    public String path;

    public MyImage(String path) throws IOException {
        this.path = path;
    }

    public BufferedImage getImage(String path) throws IOException {
        File file = new File(path);
        BufferedImage image = ImageIO.read(file);
        BufferedImage picture = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = picture.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return picture;
    }

    public byte[] get_byte_data(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }


    public  byte[] convert_to_byte(int i) {
        byte byte3 = (byte) ((i & 0xFF000000) >>> 24);
        byte byte2 = (byte) ((i & 0x00FF0000) >>> 16);
        byte byte1 = (byte) ((i & 0x0000FF00) >>> 8);
        byte byte0 = (byte) ((i & 0x000000FF));
        return (new byte[]{byte3, byte2, byte1, byte0});
    }


    public byte[] coding(byte[] image, byte[] addition, int bias) {
        for (int i = 0; i < addition.length; i++) {
            int add = addition[i];
            for (int bit = 7; bit >= 0; bit--, bias++) {
                int b = (add >>> bit) & 1;
                image[bias] = (byte) ((image[bias] & 0xFE) | b);
            }
        }
        return image;
    }

    public void setImage(BufferedImage image, File save) throws IOException {
        save.delete();
        ImageIO.write(image, "png", save);
    }

    public String decoding(String path2) throws IOException {
        BufferedImage image = getImage(path2);
        byte[] byte_img = get_byte_data(image);
        int length = 0;
        int bias = 32;
        for (int i = 0; i < 32; i++) {
            length = (length << 1) | (byte_img[i] & 1);
        }

        byte[] text = new byte[length];
        for (int b = 0; b < text.length; b++) {
            for (int i = 0; i < 8; i++, bias++) {
                text[b] = (byte) ((text[b] << 1) | (byte_img[bias] & 1));
            }
        }
        return (new String(text));
    }

}
