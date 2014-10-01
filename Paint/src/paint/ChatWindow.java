package paint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class ChatWindow extends JPanel {

	JList messageList;
	JTextField sendText;
	JButton sendButton;
	
	public ChatWindow(){
		this.setSize(new Dimension(270, 600));
		JPanel panel = new JPanel(new BorderLayout());
		this.add(panel, "chat");
		
			this.messageList = new JList();
			JScrollPane scrollPane = new JScrollPane(this.messageList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setPreferredSize(new Dimension(250, 460));		
			panel.add(scrollPane, BorderLayout.CENTER);
			
			this.messageList.setModel(new DefaultListModel());
			DefaultListSelectionModel disableSelections = new DefaultListSelectionModel() {
				public void setSelectionInterval (int index0, int index1) {
					//override!!!
				}
			};
			
		this.messageList.setSelectionModel(disableSelections);{
				JPanel bottomPanel = new JPanel(new BorderLayout());
				bottomPanel.setPreferredSize(new Dimension(270, 50));
				
				bottomPanel.add(this.sendText = new JTextField(), BorderLayout.CENTER);
				
				JButton sendButton = new JButton("Send");
				sendButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String message = ChatWindow.this.sendText.getText();
						if(!message.equals("")){
							ChatMessage packet = new ChatMessage();
							packet.message = message;
							packet.name = PaintWindow.name;
						
							Paint.network.client.sendUDP(packet);
							ChatWindow.this.sendText.setText("");
							addMessage(packet.name + " : " + message);
						}
					}
				});
				bottomPanel.add(sendButton, BorderLayout.SOUTH);
				
				panel.add(bottomPanel, BorderLayout.SOUTH);
			}

	}
	
	public void addMessage (final String message) {
				DefaultListModel model = (DefaultListModel)ChatWindow.this.messageList.getModel();
				model.addElement(message);
				ChatWindow.this.messageList.ensureIndexIsVisible(model.size() - 1);
	}
}
