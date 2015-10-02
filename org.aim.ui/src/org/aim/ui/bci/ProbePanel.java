package org.aim.ui.bci;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.aim.ui.components.ItemListPanel;
import org.aim.ui.manager.ClientManager;

/**
 * Panel to define probes.
 * 
 * @author Marius Oehler
 *
 */
public class ProbePanel extends JPanel {
	/**  */
	private static final long serialVersionUID = 1L;

	private final ItemListPanel panel;

	private Map<String, Set<String>> probeMapping;

	/**
	 * Constructor.
	 */
	public ProbePanel() {
		setBorder(new TitledBorder(null, "Probes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		panel = new ItemListPanel();
		final GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.fill = GridBagConstraints.BOTH;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 0;
		add(panel, gbcPanel);

		if (ClientManager.instance().isConnected()) {
			probeMapping = ClientManager.instance().getProbeMapping();
			panel.setPredefinedValues(ClientManager.instance().getProbes());
		}
	}

	/**
	 * Returns an array of the specified probes.
	 * 
	 * @return an array with strings, representing probes
	 */
	public String[] getProbes() {
		return panel.getValues().toArray(new String[0]);
	}

	/**
	 * Sets the given probes into the inputfields.
	 * 
	 * @param probes
	 *            - the probes to set
	 */
	public void setProbes(final String[] probes) {
		for (final String probe : probes) {
			panel.addItem(probe);
		}
	}

	/**
	 * Fitlers the probes. Show only probes which are consistent with the
	 * selected scope.
	 * 
	 * @param selectedScope
	 *            scope that is selected
	 * @param traceScope
	 *            scope is trace scope
	 */
	public void filterProbes(final String selectedScope, final boolean traceScope) {
		final List<String> filtered = new ArrayList<String>();
		for (final String probe : probeMapping.keySet()) {
			if (probeMapping.get(probe).contains(selectedScope)) {
				filtered.add(probe);
			}
		}

		panel.setPredefinedValues(filtered, true);
	}
	
	private String abbreviateString(final String scopeName) {
		final int lastIndexOf = scopeName.lastIndexOf('.');
		if (lastIndexOf >= 0) {
			return scopeName.substring(lastIndexOf + 1);
		}
		return scopeName;
	}
	
	public void filterProbes(final String scope) {
//		if (rawEntity == null) {
//			return;
//		}
//		scpPanel.getSelectedScope();
//
//		List<String> filtered = new ArrayList<String>();
//		for (String probe : rawEntity.getProbes()) {
//
//		}
//
//		probePanel.setProbes(filtered.toArray(new String[0]));
	}
}
