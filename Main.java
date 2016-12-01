import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

//��applet��֧���̣߳���Ҫʵ��Runnable�ӿ�
public class Main extends Applet implements Runnable {
	private Thread thread = null; // applet֧�ֵ��߳�

	private boolean no_thunder = true; // û������ı�־����

	private boolean thunder = true; // ������ı�־����

	private int[] light;

	private int[] b1;

	private int[] b2;

	private Color whiteSky = new Color(0, 0, 65), yellowSky = new Color(144, 40, 40);// ��ɫ���硢��ɫ����

	private Image buffer, image;

	private String delay = "3";

	public void init() {
		// ��ʼ��applet
		String imageName = getParameter("Image1");
		image = getImage(getCodeBase(), imageName);
		light = new int[getSize().height];
		b1 = new int[getSize().height];
		b2 = new int[getSize().height];
		buffer = this.createImage(getSize().width, getSize().height);
	}

	public void paint(Graphics g) {
		int i, thr;
		if (no_thunder) // û������
		{
			g.setColor(Color.black); // �豳��ɫΪ��ɫ
			g.fillRect(0, 0, getSize().width, getSize().height); // ��䱳��ɫ
			g.drawImage(image, 0, 0, this); // ���City.gif
		} else // ������
		{
			if (thunder) // ��ɫ����
				g.setColor(whiteSky); // �豳��ɫΪ��ɫ
			else
				// ��ɫ����
				g.setColor(yellowSky); // �豳��ɫΪ��ɫ
			g.fillRect(0, 0, getSize().width, getSize().height); // ��䱳��ɫ
			// �������ͼ��
			thr = (int) (0.8F * getSize().height);
			for (i = 1; i < getSize().height; i++) {
				if (i < thr) { // ���������Χ�Ļ�ɫ����
					g.setColor(Color.darkGray);
					g.drawRect(light[i] - 4, i, 3, 1);
					g.drawRect(light[i] + 2, i, 3, 1);
					g.setColor(Color.gray);
					g.drawRect(light[i] - 1, i, 1, 1);
					g.drawRect(light[i] + 1, i, 1, 1);
				}
				if (thunder) { // ��ɫ����
					g.setColor(Color.white);
				} else
					// ��ɫ����
					g.setColor(Color.yellow);
				g.drawLine(light[i], i, light[i - 1], i - 1);
				if (b1[i] >= 0) { // �����������
					g.drawLine(b1[i], i, b1[i - 1], i - 1);
				}
				if (b2[i] >= 0) { // �����������
					g.drawLine(b2[i], i, b2[i - 1], i - 1);
				}
			}
			// �������ͼ��city.gif
			g.drawImage(image, 0, 0, this);
			thunder = !thunder;
		}
	}

	void drawBuffer() {
		// ����paint()����
		Graphics g;
		g = buffer.getGraphics();
		paint(g);
	}

	public void start() {
		// ����applet�������������߳�
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	void createThunder() {
		// ���������������������
		int i;
		int bs1, bs2; // ��ʼλ�õ�����
		int be1, be2; // ����λ�õ�����
		light[0] = (int) (Math.random() * getSize().width); // �������������ֵ�λ��
		b1[0] = light[0];
		b2[0] = light[0];
		bs1 = (int) (Math.random() * getSize().height) + 1;
		bs2 = (int) (Math.random() * getSize().height) + 1;
		be1 = bs1 + (int) (0.5 * Math.random() * getSize().height) + 1;
		be2 = bs2 + (int) (0.5 * Math.random() * getSize().height) + 1;
		for (i = 1; i < getSize().height; i++) {
			light[i] = light[i - 1] + ((Math.random() > 0.5) ? 1 : -1);
			b1[i] = light[i];
			b2[i] = light[i];
		}
		for (i = bs1; i < getSize().height; i++) {
			b1[i] = b1[i - 1] + ((Math.random() > 0.5) ? 2 : -2);
		}
		for (i = bs2; i < getSize().height; i++) {
			b2[i] = b2[i - 1] + ((Math.random() > 0.5) ? 2 : -2);
		}
		for (i = be1; i < getSize().height; i++) {
			b1[i] = -1;
		}
		for (i = be2; i < getSize().height; i++) {
			b2[i] = -1;
		}
	}

	public void run() {
		// ��������

		Graphics g;
		while (true) {
			try {
				// ���ͼ��
				drawBuffer();
				g = this.getGraphics();
				g.drawImage(buffer, 0, 0, this);
				// �߳����ߣ�ʱ���������
				Thread.sleep((int) (Integer.parseInt(delay) * 1000 * Math.random()));
				// �������Ǳ�����λ
				no_thunder = false;
				// ��������
				createThunder();
				// ���ͼ��
				drawBuffer();
				g = this.getGraphics();
				g.drawImage(buffer, 0, 0, this);
				// �߳�����1��
				Thread.sleep(1000);
				// �������Ǳ�����λ
				no_thunder = true;
			} catch (InterruptedException e) {
				stop();// �����쳣��ֹͣ�߳�
			}
		}
	}
}