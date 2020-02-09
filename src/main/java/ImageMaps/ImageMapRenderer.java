package ImageMaps;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageMapRenderer extends MapRenderer {
    private BufferedImage image = null;

    private boolean first = true;

    public ImageMapRenderer(BufferedImage image, int x1, int y1, double scale) {
        recalculateInput(image, x1, y1, scale);
    }

    public void recalculateInput(BufferedImage input, int x1, int y1, double scale) {
        int x2 = 128;
        int y2 = 128;
        if (x1 > input.getWidth() * scale + 0.001D || y1 > input.getHeight() * scale + 0.001D)
            return;
        if ((x1 + x2) >= input.getWidth() * scale)
            x2 = (int) (input.getWidth() * scale) - x1;
        if ((y1 + y2) >= input.getHeight() * scale)
            y2 = (int) (input.getHeight() * scale) - y1;
        this.image = input.getSubimage((int) (x1 / scale), (int) (y1 / scale), (int) (x2 / scale), (int) (y2 / scale));
        if (scale != 1.0D) {
            BufferedImage resized = new BufferedImage(128, 128, input.getType());
            AffineTransform at = new AffineTransform();
            at.scale(scale, scale);
            AffineTransformOp scaleOp = new AffineTransformOp(at, 2);
            this.image = scaleOp.filter(this.image, resized);
        }
        this.first = true;
    }

    public void render(MapView view, MapCanvas canvas, Player player) {
        if (this.image != null && this.first) {
            canvas.drawImage(0, 0, this.image);
            this.first = false;
        }
    }
}
