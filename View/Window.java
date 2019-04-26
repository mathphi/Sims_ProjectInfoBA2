package View;

import Model.GameObject;
import Model.Person;
import Tools.Size;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Controller.Mouse;

public class Window extends JFrame {
    private Map map = new Map();
    private Status status = new Status();

    public Window(String title) {
    	super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1250, 800);
        setBackground(Color.gray);
        
        // Configure the map's scrolling
        JScrollPane mapView = new JScrollPane(map);
        map.setAutoscrolls(false);
        mapView.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        mapView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mapView.setWheelScrollingEnabled(false);

        // Disable map scrolling by keyboard arrow
		mapView.getActionMap().put("unitScrollRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		mapView.getActionMap().put("unitScrollDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		mapView.getActionMap().put("unitScrollLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		mapView.getActionMap().put("unitScrollUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		// Layouts
        setLayout(new BorderLayout());
        add(mapView, BorderLayout.CENTER);
        add(status, BorderLayout.EAST);
       
        setVisible(true);
    }

    public void setGameObjects(ArrayList<GameObject> objects) {
        this.map.setObjects(objects);
        this.map.redraw();
    }

    public void update() {
        this.map.redraw();
        this.status.redraw();
    }

    public void setKeyListener(KeyListener keyboard) {
        this.map.addKeyListener(keyboard);
    }

    public void setMouseListener(Mouse m) {
        this.map.addMouse(m);
    }

	public Map getMap() {
		return map;
	}
	
	public Status getStatus() {
		return status;
	}
}
