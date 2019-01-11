package data;
import data.MyImage;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public static void main(String[] args) throws IOException {


        String text = "Hello, OLena";
        String path = "./stenography";
        String path2 = "./stenography";
        MyImage img = new MyImage(path);
        BufferedImage original = img.getImage(path);
        byte[] colors = img.get_byte_data(original);
        byte[] message = text.getBytes();
        byte[] len = img.convert_to_byte(message.length);
        img.coding(colors, len, 0);
        img.coding(colors, message, 32);

        img.setImage(original, new File(path2));
        String decoded = img.decoding(path2);
        System.out.println(decoded);
    }
    }
