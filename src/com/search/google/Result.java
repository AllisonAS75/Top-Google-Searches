/**
 * @author Allison Shore
 * 14/07/2020
 */

/**
 * Import Google Package
 */
package com.search.google;

/**
 * Import All Required Packages
 */
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.IOException;

//Import GUI
import javax.swing.*;

/**
 * Import JSoup Library from https://jsoup.org/
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Import Swing Hyperlink
 */
import com.swing.hyperlink.JHyperlink;

/**
 * Implement the ActionListener for JButtons
 *
 */
public class Result extends JFrame implements ActionListener {
	static JTextField textField;
	static JLabel labelQuery;
	static JButton button;

	/**
	 * Adding ArrayLists
	 */
	final static int MAX_LINKS = 5;
	static ArrayList<JHyperlink> allHyperlinks = new ArrayList<>();

	static ArrayList<String> hyperlinkText = new ArrayList<>();
	static ArrayList<String> hyperlinkSite = new ArrayList<>();

	/**
	 * Build GUI
	 */
	public Result() {
		super();
		setTitle("JSoup Demo");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		labelQuery = new JLabel("Enter Query");
		textField = new JTextField(16);

		button = new JButton("Search");
		button.addActionListener(this);

		JPanel panelOne = new JPanel();
		panelOne.add(labelQuery);
		panelOne.add(textField);
		panelOne.add(button);

		JPanel panelTwo = new JPanel();
		panelTwo.setLayout(new BoxLayout(panelTwo, BoxLayout.Y_AXIS));

		/**
		 * For Loop - For algorithm to pass through to search for the first 5 results
		 */
		for (int index = 0; index < MAX_LINKS; index++) {
			// Adding hyperlink
			JHyperlink hyperlink = new JHyperlink("");
			allHyperlinks.add(hyperlink);

			/**
			 * Adding Panels
			 * 
			 */
			panelTwo.add(hyperlink);
		}

		mainPanel.add(panelOne);
		mainPanel.add(panelTwo);

		setLayout(new FlowLayout());
		getContentPane().add(mainPanel);

		// Exit function on close
		setSize(400, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * 
	 * @param args
	 * Invoking the Main Method
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Result().setVisible(true);
				;
			}
		});
	}

	/**
	 * Clicking Submit button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		getSearchResults();

		int index = 0;
		for (JHyperlink jHyperlink : allHyperlinks) {
			jHyperlink.setText(hyperlinkText.get(index));
			jHyperlink.setURL(hyperlinkSite.get(index));

			index++;
		}
	}

	/**
	 * Get the search results
	 */
	public void getSearchResults() {
		hyperlinkText.clear();
		hyperlinkSite.clear();

		Document doc;
		String query = textField.getText();
		String url = "https://www.google.com.au/search?q=" + query + "&num=" + MAX_LINKS;

		/**
		 * Handling the block exceptions
		 */
		try {
			doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(5000).get();
			Elements links = doc.select("a[href]");

			for (Element link : links) {
				String attr1 = link.attr("href");
				String attr2 = link.attr("class");

				if (!attr2.startsWith("_Zkb") && attr1.startsWith("/url?q=")) {
					getDomainName(attr1);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 * Get domain name
	 */
	public static void getDomainName(String url) {
		String[] fullDomain = url.split("&");
		String fullSite = fullDomain[0].substring(7);

		String[] splitByDot = fullSite.split("\\.");
		String domainName = splitByDot[1].toUpperCase();

		hyperlinkText.add(domainName);
		hyperlinkSite.add(fullSite);
	}
}
