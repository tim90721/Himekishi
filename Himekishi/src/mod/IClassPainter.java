package mod;

import java.awt.Graphics;

public interface IClassPainter
{
	public void setText(String text);

	public void paintSelect(Graphics gra);
	
	public void storeLine(ILinePainter line, int side);
}
