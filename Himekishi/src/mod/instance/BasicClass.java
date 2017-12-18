package mod.instance;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.print.attribute.standard.Sides;
import javax.swing.JPanel;

import Define.AreaDefine;
import bgWork.handler.CanvasPanelHandler;
import mod.IClassPainter;
import mod.IFuncComponent;
import mod.ILinePainter;;

public class BasicClass extends JPanel implements IFuncComponent, IClassPainter {
	Vector<String> texts = new Vector<>();
	Dimension defSize = new Dimension(150, 25);
	int maxLength = 20;
	int textShiftX = 5;
	boolean isSelect = false;
	int selectBoxSize = 5;
	CanvasPanelHandler cph;
	private boolean hasSelectPort = false;
	int selectPort = -1;
	ArrayList<ILinePainter> topLines;
	ArrayList<ILinePainter> rightLines;
	ArrayList<ILinePainter> leftLines;
	ArrayList<ILinePainter> bottomLines;
	Map<Integer, ArrayList<ILinePainter>> lines;

	public BasicClass(CanvasPanelHandler cph) {
		texts.add("New Class");
		texts.add("<empty>");
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
	public void paintComponent(Graphics g) {
		reSize();
		for (int i = 0; i < texts.size(); i++) {
			g.setColor(Color.WHITE);
			g.fillRect(0, (int) (0 + i * defSize.getHeight()),
					(int) defSize.getWidth() - 1, (int) defSize.height - 1);
			g.setColor(Color.BLACK);
			g.drawRect(0, (int) (0 + i * defSize.getHeight()),
					(int) defSize.getWidth() - 1, (int) defSize.height - 1);
			if (texts.elementAt(i).length() > maxLength) {
				g.drawString(
						texts.elementAt(i).substring(0, maxLength) + "...",
						textShiftX, (int) (0 + (i + 0.8) * defSize.getHeight()));
			} else {
				g.drawString(texts.elementAt(i), textShiftX,
						(int) (0 + (i + 0.8) * defSize.getHeight()));
			}
		}
		if (isSelect == true) {
			paintSelect(g);
		}
//		if(hasSelectPort){
//			ArrayList<ILinePainter> selectLines = lines.get(selectPort);
//			for (ILinePainter ILinePainter : selectLines) {
//				System.out.println(selectPort);
//				ILinePainter.paintSelect(g);
//			}
//		}
	}

	@Override
	public void reSize() {
		switch (texts.size()) {
		case 0:
			this.setSize(defSize);
			break;
		default:
			this.setSize(defSize.width, defSize.height * texts.size());
			break;
		}
	}

	@Override
	public void setText(String text) {
		texts.clear();
		texts.add(text);
		texts.add("<empty>");
		this.repaint();
	}

	public void addText(String text) {
		texts.add(text);
		this.repaint();
	}

	public void removeText(int index) {
		if (index < texts.size() && index >= 0) {
			texts.remove(index);
			this.repaint();
		}
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		System.out.println(isSelect);
		this.isSelect = isSelect;
	}

	@Override
	public void paintSelect(Graphics gra) {
		gra.setColor(Color.BLACK);
		gra.fillRect(this.getWidth() / 2 - selectBoxSize, 0, selectBoxSize * 2,
				selectBoxSize);
		gra.fillRect(this.getWidth() / 2 - selectBoxSize, this.getHeight()
				- selectBoxSize, selectBoxSize * 2, selectBoxSize);
		gra.fillRect(0, this.getHeight() / 2 - selectBoxSize, selectBoxSize,
				selectBoxSize * 2);
		gra.fillRect(this.getWidth() - selectBoxSize, this.getHeight() / 2
				- selectBoxSize, selectBoxSize, selectBoxSize * 2);
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
	
	public void storeLine(ILinePainter line, int side){
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
