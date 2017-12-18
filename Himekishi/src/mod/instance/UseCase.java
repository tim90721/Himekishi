package mod.instance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;

import Define.AreaDefine;
import bgWork.handler.CanvasPanelHandler;
import mod.IClassPainter;
import mod.IFuncComponent;

public class UseCase extends JPanel implements IFuncComponent, IClassPainter
{
	Vector <String>		texts			= new Vector <>();
	Dimension			defSize			= new Dimension(150, 40);
	int					maxLength		= 20;
	boolean				isSelect		= false;
	int					selectBoxSize	= 5;
	CanvasPanelHandler	cph;
	private int selectPort = -1;
	private boolean hasSelectPort = false;
	ArrayList<IBasicLine> topLines;
	ArrayList<IBasicLine> rightLines;
	ArrayList<IBasicLine> leftLines;
	ArrayList<IBasicLine> bottomLines;
	Map<Integer, ArrayList<IBasicLine>> lines;

	public UseCase(CanvasPanelHandler cph)
	{
		texts.add("New Use Case");
		reSize();
		this.setVisible(true);
		this.setLocation(0, 0);
		this.setOpaque(true);
		this.cph = cph;
		topLines = new ArrayList<IBasicLine>();
		rightLines = new ArrayList<IBasicLine>();
		leftLines = new ArrayList<IBasicLine>();
		bottomLines = new ArrayList<IBasicLine>();
		lines = new HashMap<Integer, ArrayList<IBasicLine>>();
		lines.put(3, topLines);
		lines.put(2, rightLines);
		lines.put(1, leftLines);
		lines.put(0, bottomLines);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		reSize();
		for (int i = 0; i < texts.size(); i ++)
		{
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, defSize.width, defSize.height);
			g.setColor(Color.BLACK);
			g.drawOval(0, 0, defSize.width - 1, defSize.height - 1);
			if (texts.elementAt(i).length() > maxLength)
			{
				g.drawString(texts.elementAt(i).substring(0, maxLength) + "...",
						3, (int) (0 + (i + 0.8) * defSize.getHeight()));
			}
			else
			{
				g.drawString(texts.elementAt(i), (int) (defSize.width * 0.2),
						(int) (defSize.getHeight()) / 2);
			}
		}
		if (isSelect == true)
		{
			paintSelect(g);
		}
	}

	public boolean isSelect()
	{
		return isSelect;
	}

	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}

	@Override
	public void reSize()
	{
		switch (texts.size())
		{
			case 0:
				this.setSize(defSize);
				break;
			default:
				this.setSize(defSize.width, defSize.height);
				break;
		}
	}

	@Override
	public void setText(String text)
	{
		texts.clear();
		texts.add(text);
		this.repaint();
	}

	@Override
	public void paintSelect(Graphics gra)
	{
		gra.setColor(Color.BLACK);
		gra.fillRect(this.getWidth() / 2 - selectBoxSize, 0, selectBoxSize * 2,
				selectBoxSize);
		gra.fillRect(this.getWidth() / 2 - selectBoxSize,
				this.getHeight() - selectBoxSize, selectBoxSize * 2,
				selectBoxSize);
		gra.fillRect(0, this.getHeight() / 2 - selectBoxSize, selectBoxSize,
				selectBoxSize * 2);
		gra.fillRect(this.getWidth() - selectBoxSize,
				this.getHeight() / 2 - selectBoxSize, selectBoxSize,
				selectBoxSize * 2);
	}
	
	public boolean isSelectPort(Point clickPoint) {
		Point jpLocation = cph.getAbsLocation(this);
//		if (x == jpLocation.x + this.getSize().getWidth() / 2
//				&& y == jpLocation.y) {
//			selectPort = 3;
//			hasSelectPort = true;
//			return true;
//		} else if (x == jpLocation.x + getSize().getWidth()
//				&& y == jpLocation.y + getSize().getHeight() / 2) {
//			selectPort = 2;
//			hasSelectPort = true;
//			return true;
//		} else if (x == jpLocation.x
//				&& y == jpLocation.y + getSize().getHeight() / 2) {
//			selectPort = 1;
//			hasSelectPort = true;
//			return true;
//		} else if (x == jpLocation.x + getSize().getWidth() / 2
//				&& y == jpLocation.y + getSize().getHeight()) {
//			selectPort = 0;
//			hasSelectPort = true;
//			return true;
//		}
//		System.out.println("bbb");
//		selectPort = -1;
//		hasSelectPort = false;
//		return false;
		selectPort = new AreaDefine().getArea(this.getLocation(), this.getSize(), clickPoint);
		if(selectPort != -1){
			hasSelectPort = false;
			return false;
		}
		hasSelectPort = true;
		return true;
	}
}
