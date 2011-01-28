package com.example.crazybiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import dao.DBactions;

public class SearchItem extends VerticalLayout {
	private Table table;
	private Label selected;
	
	public SearchItem() {
		selected = new Label("");
		table = new Table("Results");
		
		table.setSelectable(true);
        table.setMultiSelect(false);
        table.setImmediate(true); // react at once when something is selected
        // turn on column reordering and collapsing
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);
        // set column headers
        table.setColumnHeaders(new String[] { "Brand", "Model", "Status" });
        // Populate from db ???
        /*
        ResultSet results = UserDAO.getSearchResults("...query...");
        try {
			while(results.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
        
        // Action handler
        table.addListener(new Table.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
               // in multiselect mode, a Set of itemIds is returned,
                // in singleselect mode the itemId is returned directly
                Set<?> value = (Set<?>) event.getProperty().getValue();
                if (null == value || value.size() == 0) {
                    selected.setValue("No selection");
                } else {
                    selected.setValue("Selected: " + table.getValue());
                }
			}
		});
	}
}
