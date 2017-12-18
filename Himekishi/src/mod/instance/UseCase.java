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
import mod.ILinePainter;;

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
	ArrayList<ILinePainter> topLines;
	ArrayList<ILinePainter> rightLines;
	ArrayList<ILinePainter> leftLines;
	ArrayList<ILinePainter> bottomLines;
	Map<Integer, ArrayList<ILinePainter>> lines;

	public UseCase(CanvasPanelHandler cph)
	{
		texts.add("New Use Case");
		reSize();
		this.setVisible(true);
		this.setLocation(0, 0);
		this.setOpaque(true);
		this.cph = cph;
		topLines = new ArrayList<ILinePainter>();
		rightLines = new ArrayList<ILinePainter>();
		leftLines = new ArrayList<ILinePainter>();
		bottomLines = new ArrayList<ILinePainter>();
		lines = new HashMap<Integer, ArrayList<ILinePainter>>();
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
		refreshPortSelect();
		System.out.println(selectPort);
		if(selectPort != -1){
			ArrayList<ILinePainter> storeLine = lines.get(selectPort);
			for (ILinePainter iLinePainter : storeLine) {
				iLinePainter.setSelect(true);
			}
			hasSelectPort = true;
			return true;
		}
		hasSelectPort = false;
		return false;
	}

	public void refreshPortSelect() {
		for (Map.Entry<Integer, ArrayList<ILinePainter>> pair : lines.entrySet()) {
			for (ILinePainter iLinePainter : pair.getValue()) {
				iLinePainter.setSelect(false);
			}
		}
	}
	
	@Override
	public void storeLine(ILinePainter line, int side) {
		if(side == 3)
			topLines.add(line);
		else if(side == 2)
			rightLines.add(line);
		else if (side == 1) 
			leftLines.add(line);
		else if (side == 0)
			bottomLines.add(line);
	}
}
