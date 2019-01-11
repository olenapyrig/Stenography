package data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import lombok.SneakyThrows;


    public class Steganograph {

        @SneakyThrows
        public void encode(String messageToHide, String imagePath) {
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            hideMessage(messageToHide, bufferedImage);
            save(bufferedImage);
        }

        @SneakyThrows
        private void save(BufferedImage bufferedImage) {
            ImageIO.write(bufferedImage, "bmp", new File("hidden.bmp"));
        }

        @SneakyThrows
        private void hideMessage(String messageStr, BufferedImage bufImage) {
            byte[] message = messageStr.getBytes();
            int messageLength = message.length;
            int width = bufImage.getWidth();
            int height = bufImage.getHeight();
            int rgb = bufImage.getRGB(0, 0);
            rgb = rgb & 0xff00ffff;
            rgb = rgb | messageLength << 16;
            bufImage.setRGB(0, 0, rgb);
            int currentChar = 0;
            for (int i = 0; i < width; i++) {
                if (currentChar == messageLength) break;
                for (int j = 0; j < height; j++) {
                    if (i == 0 && j == 0) continue;
                    if (currentChar == messageLength) break;
                    rgb = bufImage.getRGB(i, j);
                    int a = (rgb >> 24) & 0xff;
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    r = ((r >> 3 & 0xff) << 3 & 0xff) | ((message[currentChar] >> 5) & 0xff);
                    g = ((g >> 3 & 0xff) << 3 & 0xff) | ((message[currentChar] >> 2) & 0xff);
                    b = ((b >> 2 & 0xff) << 2 & 0xff) | (message[currentChar]);
                    rgb = (a << 24) | (r << 16) | (g << 8) | b;
                    bufImage.setRGB(i, j, rgb);
                    currentChar++;
                }
            }
        }

        @SneakyThrows
        public String decode(String imagePath) {
            BufferedImage bufImage = ImageIO.read(new File(imagePath));
            char[] message = showMessage(bufImage);
            StringBuilder strBuild = new StringBuilder();
            for (char aMessage : message) {
                strBuild.append(aMessage);
            }
            return strBuild.toString();
        }

        private char[] showMessage(BufferedImage bufImage) {
            int width = bufImage.getWidth();
            int height = bufImage.getHeight();
            int rgb = bufImage.getRGB(0, 0);
            int messageSize = (rgb & 0x00ff0000) >>> 16;
            char[] message = new char[messageSize];
            int countChar = 0;
            for (int i = 0; i < width; i++) {
                if (countChar == messageSize) break;
                for (int j = 0; j < height; j++) {
                    if (i == 0 && j == 0) continue;
                    if (countChar == messageSize) break;
                    rgb = bufImage.getRGB(i, j);
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    r = (r << 5) & 0xE0;
                    g = (g << 2) & 0x1C;
                    b &= 3;
                    int currentChar = r | g | b;
                    message[countChar] = (char) currentChar;
                    countChar++;
                }
            }
            return message;
        }

    }

