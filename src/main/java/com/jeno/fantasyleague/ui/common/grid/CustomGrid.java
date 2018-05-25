package com.jeno.fantasyleague.ui.common.grid;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.renderers.ComponentRenderer;

public class CustomGrid<T> extends Grid<T> {

	private CustomGridBuilder<T> builder;
	private List<T> listDataproviderItems = Lists.newArrayList();
	private boolean adjustHeightDynamically = true;

	public CustomGrid(CustomGridBuilder<T> builder) {
		this();
		this.builder = builder;

		setDataProvider(builder.dataProvider);
		buildColumns(builder);
		if (builder.columnOrder.length > 0) {
			setColumnOrder(builder.columnOrder);
		}
	}

	public CustomGrid() {
		super();
		setRowHeight(36);
		setHeightByRows(5);
		setHeightMode(HeightMode.ROW);
	}

	@Override
	public void setItems(Collection<T> items) {
		super.setItems(items);
		this.listDataproviderItems = Lists.newArrayList(items);
		if (adjustHeightDynamically) {
			setHeight(36f * (items.size() + 1), Unit.PIXELS);
		}
	}

	public List<T> getItems() {
		return listDataproviderItems;
	}

	public void removeItem(T itemToRemove) {
		ListDataProvider<T> listDataProvider = getListDataProvider();
		if (containsItem(itemToRemove, listDataProvider)) {
			listDataProvider.getItems().remove(itemToRemove);
			listDataProvider.refreshAll();
		}
	}

	public void addItem(T itemToAdd) {
		ListDataProvider<T> listDataProvider = getListDataProvider();
		if (!containsItem(itemToAdd, listDataProvider)) {
			listDataProvider.getItems().add(itemToAdd);
			listDataProvider.refreshAll();
		}
	}

	private boolean containsItem(T itemToAdd, ListDataProvider<T> listDataProvider) {
		return listDataProvider.getItems().stream()
				.anyMatch(item -> builder.keyExtractor.apply(item).equals(builder.keyExtractor.apply(itemToAdd)));
	}

	private ListDataProvider<T> getListDataProvider() {
		if (!(getDataProvider() instanceof ListDataProvider)) {
			throw new UnsupportedOperationException("Can only be used on a grid with a ListDataProvider");
		}
		return (ListDataProvider<T>) getDataProvider();
	}

	private void buildColumns(CustomGridBuilder<T> builder) {
		builder.textColumns.forEach((key, value) ->
			addColumn(value.valueProvider)
					.setId(key)
					.setCaption(value.caption));
		builder.iconColumns.values().forEach(this::addIconColumn);
	}

	public Column<T, Component> addIconColumn(CustomGridBuilder.ColumnProvider<T, CustomGridBuilder.IconColumnValue> value) {
		return addColumn(t -> createIconColumnComponent(value.valueProvider.apply(t)), new ComponentRenderer())
				.setId(value.id)
				.setStyleGenerator(item -> "icon-column")
				.setWidth(50)
				.setCaption(value.caption);
	}

	private Component createIconColumnComponent(CustomGridBuilder.IconColumnValue iconColumnValue) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		Image icon = new Image();
		icon.setWidth(30f, Unit.PIXELS);
		icon.setHeight(30f, Unit.PIXELS);
		icon.setSource(iconColumnValue.resource);
		if (iconColumnValue.iconClickAction != null) {
			icon.addStyleName("cursor-hover-pointer");
			icon.addClickListener(ignored -> iconColumnValue.iconClickAction.perform(this));
		}
		layout.addComponents(icon);
		layout.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
		return layout;
	}

	public void setAdjustHeightDynamically(boolean adjustHeightDynamically) {
		this.adjustHeightDynamically = adjustHeightDynamically;
	}
}
