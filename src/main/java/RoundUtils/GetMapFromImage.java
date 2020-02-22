package RoundUtils;

import org.bukkit.Material;

import java.awt.image.BufferedImage;

public class GetMapFromImage {

    private static int[][] getRGBGrid(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();

        int[][] RGBGrid = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = img.getRGB(j, i);
                RGBGrid[j][i] = pixel;
            }
        }
        return RGBGrid;
    }

    public static Material[][] makeWoolPattern(BufferedImage img) {
        int height = img.getHeight();
        int width =  img.getWidth();
        Material[][] pattern = new Material[height][width];

        int[][] imgGrid = GetMapFromImage.getRGBGrid(img);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = (imgGrid[j][i] >> 16) & 0xff;
                int green = (imgGrid[j][i] >> 8) & 0xff;
                int blue = (imgGrid[j][i]) & 0xff;

                if (red == 255 && green == 255 && blue == 255)
                    pattern[j][i] = Material.WHITE_TERRACOTTA;
                else if (red == 255 && green == 165 && blue == 0)
                    pattern[j][i] = Material.ORANGE_TERRACOTTA;
                else if (red == 255 && green == 0 && blue == 255)
                    pattern[j][i] = Material.MAGENTA_TERRACOTTA;
                else if (red == 173 && green == 216 && blue == 230)
                    pattern[j][i] = Material.LIGHT_BLUE_TERRACOTTA;
                else if (red == 255 && green == 255 && blue == 0)
                    pattern[j][i] = Material.YELLOW_TERRACOTTA;
                else if (red == 0 && green == 255 && blue == 0)
                    pattern[j][i] = Material.LIME_TERRACOTTA;
                else if (red == 255 && green == 192 && blue == 203)
                    pattern[j][i] = Material.PINK_TERRACOTTA;
                else if (red == 128 && green == 128 && blue == 128)
                    pattern[j][i] = Material.GRAY_TERRACOTTA;
                else if (red == 211 && green == 211 && blue == 211)
                    pattern[j][i] = Material.LIGHT_GRAY_TERRACOTTA;
                else if (red == 0 && green == 255 && blue == 255)
                    pattern[j][i] = Material.CYAN_TERRACOTTA;
                else if (red == 128 && green == 0 && blue == 128)
                    pattern[j][i] = Material.PURPLE_TERRACOTTA;
                else if (red == 0 && green == 0 && blue == 255)
                    pattern[j][i] = Material.BLUE_TERRACOTTA;
                else if (red == 165 && green == 42 && blue == 42)
                    pattern[j][i] = Material.BROWN_TERRACOTTA;
                else if (red == 0 && green == 128 && blue == 0)
                    pattern[j][i] = Material.GREEN_TERRACOTTA;
                else if (red == 255 && green == 0 && blue == 0)
                    pattern[j][i] = Material.RED_TERRACOTTA;
                else if (red == 0 && green == 0 && blue == 0)
                    pattern[j][i] = Material.BLACK_TERRACOTTA;
                else
                    pattern[j][i] = Material.BEDROCK;

                System.out.println("RED " + red + " GREEN " + green + " BLUE " + blue);
            }
        }
        return pattern;
    }
}
