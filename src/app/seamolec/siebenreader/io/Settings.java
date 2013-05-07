package app.seamolec.siebenreader.io;

public class Settings {
	private int fontSize;
	private int bgColor;
	
	public Settings(){
		fontSize = 3;
		bgColor = 0xff0000ff;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}
	
	
}
