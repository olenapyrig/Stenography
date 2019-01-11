package data;

public class Main {
    public static void main(String[] args) {

        Steganograph steganograph = new Steganograph();
        String message = "Hello Worlds";
        String path = "cat.jpg";
        steganograph.encode(message, path);
        String decodeMessage;
        decodeMessage = steganograph.decode("hidden.bmp");
        System.out.println(decodeMessage);
    }
}