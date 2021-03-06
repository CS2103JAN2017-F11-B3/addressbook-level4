package seedu.task.commons.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.Objects;

import seedu.task.ui.Theme;

/**
 * A Serializable class that contains the GUI settings.
 */
public class GuiSettings implements Serializable {

    private static final double DEFAULT_HEIGHT = 600;
    private static final double DEFAULT_WIDTH = 740;

    private Double windowWidth;
    private Double windowHeight;
    private Point windowCoordinates;

    private String styleSheet;
    private String lastLoadedYTomorrow;

    public GuiSettings() {
        this.windowWidth = DEFAULT_WIDTH;
        this.windowHeight = DEFAULT_HEIGHT;
        this.windowCoordinates = null; // null represent no coordinates
        this.styleSheet = Theme.DEFAULT_STYLESHEET;
        this.lastLoadedYTomorrow = null;
    }

    public GuiSettings(
            Double windowWidth,
            Double windowHeight,
            int xPosition,
            int yPosition,
            String styleSheet,
            String lastLoadedYTomorrow) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowCoordinates = new Point(xPosition, yPosition);
        this.styleSheet = styleSheet;
        this.lastLoadedYTomorrow = lastLoadedYTomorrow;
    }

    public Double getWindowWidth() {
        return windowWidth;
    }

    public Double getWindowHeight() {
        return windowHeight;
    }

    public Point getWindowCoordinates() {
        return windowCoordinates;
    }

    //@@author A0163848R
    /**
     * Gets the stylesheet stored in GuiSettings
     * @return String path to stylesheet
     */
    public String getStyleSheet() {
        return styleSheet;
    }

    /**
     * Gets the last loaded Y Tomorrow task database
     * @return Path to last loaded task database
     */
    public String getLastLoadedYTomorrow() {
        return lastLoadedYTomorrow;
    }

    /**
     * Stores the stylesheet path
     * @param path to store
     */
    public void setStyleSheet(String path) {
        styleSheet = path;
    }

    /**
     * Store the YTomorrow task database path
     * @param path to store
     */
    public void setLastLoadedYTomorrow(String path) {
        lastLoadedYTomorrow = path;
    }
    //@@author

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuiSettings)) { //this handles null as well.
            return false;
        }

        GuiSettings o = (GuiSettings) other;

        return Objects.equals(windowWidth, o.windowWidth)
                && Objects.equals(windowHeight, o.windowHeight)
                && Objects.equals(windowCoordinates.x, o.windowCoordinates.x)
                && Objects.equals(windowCoordinates.y, o.windowCoordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowWidth, windowHeight, windowCoordinates);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Width : " + windowWidth + "\n");
        sb.append("Height : " + windowHeight + "\n");
        sb.append("Position : " + windowCoordinates + "\n");
        sb.append("Style : " + styleSheet);
        return sb.toString();
    }
}
