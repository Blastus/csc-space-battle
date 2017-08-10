import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
class XStarField {
    private static final int DESIRED_STAR_COUNT = 500;
    private static final int MIN_STAR_MARGIN = 5;
    private static final XColor RED_STAR = XColor.WHITE.interpolate(0.5, XColor.RED);
    private static final XColor BLUE_STAR = XColor.WHITE.interpolate(0.6, XColor.BLUE);
    private static final int PATIENCE = 100;
    private final ArrayList<XVector> positions;
    private final ArrayList<XColor> colors;

    XStarField(Dimension canvasSize) {
        this.positions = new ArrayList<>();
        this.colors = new ArrayList<>();
        XVector position = new XVector();
        IntStream.range(0, DESIRED_STAR_COUNT).forEach(a -> {
            // Try to find a position outside the margin of other stars.
            IntStream.range(0, PATIENCE).filter(b -> {
                position.random(canvasSize);
                return this.positions.stream().noneMatch(point -> position.getDistance(point) < MIN_STAR_MARGIN);
            }).findFirst().ifPresent(b -> {
                this.positions.add(position.copy());
                this.colors.add((XColor) XRandom.sChoice(RED_STAR, BLUE_STAR));
            });
        });
    }

    void draw(Graphics surface) {
        ListIterator<XVector> iterator = this.positions.listIterator();
        while (iterator.hasNext()) {
            surface.setColor(XColor.WHITE.interpolateRandom(this.colors.get(iterator.nextIndex())));
            XVector position = iterator.next();
            surface.drawRect(position.getIntX(), position.getIntY(), 0, 0);
        }
    }
}
