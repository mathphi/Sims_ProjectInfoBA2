package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import Controller.MouseController;

public class Window extends JFrame {
	private static final long serialVersionUID = -6602572108147389047L;
	
	private Map map = new Map();
    private Status status = new Status();
    private MessagesZone msgZone = new MessagesZone();
    private EditorPanel editorPanel = new EditorPanel();
    
    private JPanel borderPanel;
    private JButton gameMenuButton;
    private JButton editorMenuButton;

    @SuppressWarnings("serial")
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

		gameMenuButton = new JButton("Menu");
    	editorMenuButton = new JButton("Menu");
    	
    	// Disable focus to avoid the loss of focus for map, resulting in KeyListener not working
    	gameMenuButton.setFocusable(false);
    	editorMenuButton.setFocusable(false);
		
		borderPanel = new JPanel();
		borderPanel.setPreferredSize(new Dimension(300, 600));
		borderPanel.setLayout(new BorderLayout());
		
		// Layouts
        setLayout(new BorderLayout());
        add(mapView, BorderLayout.CENTER);
        add(borderPanel, BorderLayout.EAST);
       
        setVisible(true);
    }
    
    private void setupGameBorderPanel() {		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		buttonPanel.setPreferredSize(new Dimension(300, 50));
		buttonPanel.add(gameMenuButton);
    	
		borderPanel.removeAll();
		borderPanel.add(status, BorderLayout.NORTH);
		borderPanel.add(msgZone, BorderLayout.CENTER);
		borderPanel.add(buttonPanel, BorderLayout.SOUTH);
		borderPanel.revalidate();
    }
    
    private void setupEditorBorderPanel() {		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		buttonPanel.setPreferredSize(new Dimension(300, 50));
		buttonPanel.add(editorMenuButton);
    	
		borderPanel.removeAll();
		borderPanel.add(editorPanel, BorderLayout.CENTER);
		borderPanel.add(buttonPanel, BorderLayout.SOUTH);
		borderPanel.revalidate();
    }

    public void switchEditorMode() {
    	setupEditorBorderPanel();
    }
    
    public void switchGameMode() {
    	setupGameBorderPanel();
    }
    
    public void switchNeutralMode() {
		borderPanel.removeAll();
		borderPanel.revalidate();
    }

    public void addGameMenuButtonAction(ActionListener a) {
    	gameMenuButton.addActionListener(a);
    }

    public void addEditorMenuButtonAction(ActionListener a) {
    	editorMenuButton.addActionListener(a);
    }

    public void update() {
        this.map.redraw();
        
        if (this.status.isVisible())
        	this.status.redraw();
        
        if (this.editorPanel.isVisible())
        	this.editorPanel.repaint();
    }

    public void addMapKeyListener(KeyListener keyboard) {
        this.map.addKeyListener(keyboard);
    }

    public void addMapMouseListener(MouseController m) {
        this.map.addMouse(m);
    }

	public Map getMap() {
		return map;
	}

	public Status getStatus() {
		return status;
	}

	public MessagesZone getMsgZone() {
		return msgZone;
	}
	
	public EditorPanel getEditorPanel() {
		return editorPanel;
	}
}
