package mod.instance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.JPanel;

import mod.IFuncComponent;
import mod.ILinePainter;
import Define.AreaDefine;
import Pack.DragPack;
import bgWork.handler.CanvasPanelHandler;

public class DependencyLine extends JPanel implements IFuncComponent,
		ILinePainter {
	JPanel from;
	int fromSide;
	Point fp = new Point(0, 0);
	JPanel to;
	int toSide;
	Point tp = new Point(0, 0);
	int arrowSize = 6;
	int panelExtendSize = 10;
	boolean isSelect = false;
	int selectBoxSize = 5;
	CanvasPanelHandler cph;
	private double _angle;
	private int _rotateX, _rotateY;

	public DependencyLine(CanvasPanelHandler cph) {
		this.setOpaque(false);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(1, 1));
		this.cph = cph;
	}

	@Override
	public void paintComponent(Graphics g) {
		Point fpPrime;
		Point tpPrime;
		renewConnect();
		fpPrime = new Point(fp.x - this.getLocation().x, fp.y
				- this.getLocation().y);
		tpPrime = new Point(tp.x - this.getLocation().x, tp.y
				- this.getLocation().y);
		setAngle(tpPrime.x - fpPrime.x, tpPrime.y - fpPrime.y);
		double distance = Math.sqrt(Math.pow(tpPrime.x - fpPrime.x, 2)
				+ Math.pow(tpPrime.y - fpPrime.y, 2));
		int length = (int)distance / 15;
		int left = (int)distance % 15;
		System.out.println(distance);
		System.out.println(length);
		System.out.println(left);
		System.out.println(getAngle());
		g.setColor(Color.BLACK);
		Graphics2D g2 = (Graphics2D)g;
		g2.rotate(getAngle(), fpPrime.x, fpPrime.y);
		int i;
		for(i = 0;i < length; i++) {
			g2.drawLine(fpPrime.x + i * 15, fpPrime.y, 
					fpPrime.x + i * 15 + 10, fpPrime.y);
		}
		if(left > 0) {
			g2.drawLine(fpPrime.x + (length) * 15, fpPrime.y, 
					fpPrime.x + (length) * 15 + (left % 10), fpPrime.y);
		}
//		g2.drawLine(fpPrime.x, fpPrime.y, fpPrime.x + (int)distance, fpPrime.y);
		g2.rotate(-1 * getAngle(), fpPrime.x, fpPrime.y);
		paintArrow(g, tpPrime);
		if (isSelect == true) {
			paintSelect(g);
		}
	}
	
	public void setAngle(int difX, int difY) {
		_angle = Math.atan2(difY, difX);
	}
	
	public double getAngle() {
		return _angle;
	}

	@Override
	public void reSize() {
		Dimension size = new Dimension(Math.abs(fp.x - tp.x) + panelExtendSize
				* 2, Math.abs(fp.y - tp.y) + panelExtendSize * 2);
		this.setSize(size);
		this.setLocation(Math.min(fp.x, tp.x) - panelExtendSize,
				Math.min(fp.y, tp.y) - panelExtendSize);
	}

	@Override
	public void paintArrow(Graphics g, Point point) {
		int x[] = { point.x, point.x - arrowSize, point.x, point.x + arrowSize };
		int y[] = { point.y + arrowSize, point.y, point.y - arrowSize, point.y };
		Polygon polygon = new Polygon(x, y, x.length);
		g.setColor(Color.WHITE);
		g.fillPolygon(polygon);
		g.setColor(Color.BLACK);
		g.drawPolygon(polygon);
	}

	@Override
	public void setConnect(DragPack dPack) {
		Point mfp = dPack.getFrom();
		Point mtp = dPack.getTo();
		from = (JPanel) dPack.getFromObj();
		to = (JPanel) dPack.getToObj();
		fromSide = new AreaDefine().getArea(from.getLocation(), from.getSize(),
				mfp);
		toSide = new AreaDefine().getArea(to.getLocation(), to.getSize(), mtp);
		renewConnect();
		System.out.println("from side " + fromSide);
		System.out.println("to side " + toSide);
	}

	void renewConnect() {
		try {
			fp = getConnectPoint(from, fromSide);
			tp = getConnectPoint(to, toSide);
			this.reSize();
		} catch (NullPointerException e) {
			this.setVisible(false);
			cph.removeComponent(this);
		}
	}

	Point getConnectPoint(JPanel jp, int side) {
		Point temp = new Point(0, 0);
		Point jpLocation = cph.getAbsLocation(jp);
		if (side == new AreaDefine().TOP) {
			temp.x = (int) (jpLocation.x + jp.getSize().getWidth() / 2);
			temp.y = jpLocation.y;
		} else if (side == new AreaDefine().RIGHT) {
			temp.x = (int) (jpLocation.x + jp.getSize().getWidth());
			temp.y = (int) (jpLocation.y + jp.getSize().getHeight() / 2);
		} else if (side == new AreaDefine().LEFT) {
			temp.x = jpLocation.x;
			temp.y = (int) (jpLocation.y + jp.getSize().getHeight() / 2);
		} else if (side == new AreaDefine().BOTTOM) {
			temp.x = (int) (jpLocation.x + jp.getSize().getWidth() / 2);
			temp.y = (int) (jpLocation.y + jp.getSize().getHeight());
		} else {
			temp = null;
			System.err.println("getConnectPoint fail:" + side);
		}
		return temp;
	}

	@Override
	public void paintSelect(Graphics gra) {
		gra.setColor(Color.BLACK);
		gra.fillRect(fp.x, fp.y, selectBoxSize, selectBoxSize);
		gra.fillRect(tp.x, tp.y, selectBoxSize, selectBoxSize);
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
}