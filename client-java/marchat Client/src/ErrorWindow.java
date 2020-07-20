import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ErrorWindow extends JDialog {

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public ErrorWindow(String errorMsg) {
		setResizable(false);
		setBounds(100, 100, 255, 182);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(OwnColors.grey_d);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel erroMessage = new JLabel(errorMsg);
			erroMessage.setFont(new Font("Tahoma", Font.PLAIN, 13));
			erroMessage.setForeground(Color.WHITE);
			erroMessage.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(erroMessage, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(OwnColors.grey_d);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
