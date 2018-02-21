package com.smartgwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortArrow;
import com.smartgwt.client.util.PageKeyHandler;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.viewer.DetailViewer;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DataSourceDMI implements EntryPoint {
    private ListGrid boundList;
    private DynamicForm boundForm;
    private IButton saveBtn;
    private DetailViewer boundViewer;
    private IButton newBtn;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        DataSource ds = DataSource.get("supplyItemDMI");
        
		KeyIdentifier debugKey = new KeyIdentifier();
		debugKey.setCtrlKey(true);
		debugKey.setKeyName("D");

		Page.registerKey(debugKey, new PageKeyHandler() {
			public void execute(String keyName) {
				SC.showConsole();
			}
		});
        

        VStack vStack = new VStack();
        vStack.setLeft(175);
        vStack.setTop(75);
        vStack.setWidth("70%");
        vStack.setMembersMargin(20);

        Label label = new Label();
        label.setContents("<ul>" +
                "<li>click a record in the grid to view and edit that record in the form</li>" +
                "<li>click <b>New</b> to start editing a new record in the form</li>" +
                "<li>click <b>Save</b> to save changes to a new or edited record in the form</li>" +
                "<li>click <b>Clear</b> to clear all fields in the form</li>" +
                "<li>click <b>Filter</b> to filter (substring match) the grid based on form values</li>" +
                "<li>click <b>Fetch</b> to fetch records (exact match) for the grid based on form values</li>" +
                "<li>double-click a record in the grid to edit inline (press Return, or arrow/tab to another record, to save)</li>" +
                "</ul>");
        vStack.addMember(label);

        boundList = new ListGrid();
        boundList.setDataSource(ds);
        boundList.setHeight(200);
        boundList.setCanEdit(true);
        boundList.fetchData();

        boundList.addRecordClickHandler(new RecordClickHandler() {
            public void onRecordClick(RecordClickEvent event) {
                Record record = event.getRecord();
                boundForm.editRecord(record);
                saveBtn.enable();
                boundViewer.viewSelectedData(boundList);
            }
        });
        vStack.addMember(boundList);

        boundForm = new DynamicForm();
        boundForm.setDataSource(ds);
        boundForm.setNumCols(6);
        boundForm.setAutoFocus(false);
        vStack.addMember(boundForm);

        ToolStrip toolbar = new ToolStrip();
        toolbar.setMembersMargin(10);
        toolbar.setHeight(22);

        saveBtn = new IButton("Save");
        saveBtn.disable();
        saveBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundForm.saveData();
                if (!boundForm.hasErrors()) {
                    boundForm.clearValues();
                    saveBtn.disable();
                }
            }
        });
        toolbar.addMember(saveBtn);

        newBtn = new IButton("New");
        newBtn.enable();
        newBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundForm.editNewRecord();
                saveBtn.enable();
            }
        });
        toolbar.addMember(newBtn);

        IButton clearBtn = new IButton("Clear");
        clearBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundForm.clearValues();
                saveBtn.disable();
            }
        });
        toolbar.addMember(clearBtn);

        IButton filterBtn = new IButton("Filter");
        filterBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundList.filterData(boundForm.getValuesAsCriteria());
                saveBtn.disable();
            }
        });
        toolbar.addMember(filterBtn);

        IButton fetchBtn = new IButton("Fetch");
        fetchBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundList.fetchData(boundForm.getValuesAsCriteria());
                saveBtn.disable();
            }
        });
        toolbar.addMember(fetchBtn);

        vStack.addMember(toolbar);

        boundViewer = new DetailViewer();
        boundViewer.setDataSource(ds);
        vStack.addMember(boundViewer);
        
        vStack.draw();
    }
}
