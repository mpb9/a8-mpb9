package a8;


import java.awt.Color; 

public interface Spot {

	int getSpotX();
	int getSpotY();
	SpotBoard getBoard();

	boolean isEmpty();
	void setSpot();
	void clearSpot();
	default void toggleSpot() {
		if (isEmpty()) {
			setSpot();
		} else {
			clearSpot();
		}
	}

	boolean isHighlighted();
	void highlightSpot();
	void unhighlightSpot();
	default void toggleHighlight() {
		if (isHighlighted()) {
			unhighlightSpot();
		} else {
			highlightSpot();
		}
	}

	void setBackground(Color c);
	Color getBackground();
	void setSpotColor(Color c);
	Color getSpotColor();
	void setHighlight(Color c);
	Color getHighlight();

	void addSpotListener(SpotListener l);
	void removeSpotListener(SpotListener l);

	default String getCoordString() {
		return "(" + getSpotX() + ", " + getSpotY() + ")";
	}
}