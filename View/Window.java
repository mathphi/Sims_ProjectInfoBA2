package View;

import Model.GameObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Controller.Mouse;

public class Window extends JFrame {
	private static final long serialVersionUID = -6602572108147389047L;
	
	private Map map = new Map();
    private Status status = new Status();
    
    JButton menuButton;

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
		
		menuButton = new JButton("Menu");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(300, 50));
		buttonPanel.add(menuButton);
		
		JPanel borderPanel = new JPanel();
		borderPanel.setPreferredSize(new Dimension(300, 600));
		borderPanel.setLayout(new BorderLayout());
		borderPanel.add(status, BorderLayout.NORTH);
		//TODO: add messages panel here
		borderPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Layouts
        setLayout(new BorderLayout());
        add(mapView, BorderLayout.CENTER);
        add(borderPanel, BorderLayout.EAST);
       
        setVisible(true);
    }
    
    public void addMenuButtonAction(ActionListener a) {
    	menuButton.addActionListener(a);
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
