package com.jeno.fantasyleague.ui.common.grid;

import java.util.Collection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class CustomGrid<T> extends Grid<T> {

	private CustomGridBuilder<T> builder;
	private boolean adjustHeightDynamically = true;

	public CustomGrid(CustomGridBuilder<T> builder) {
		this();
		this.builder = builder;

		setDataProvider(builder.dataProvider);
		buildColumns(builder);
		if (builder.columnOrder.length > 0) {
			setColumns(builder.columnOrder);
		}
	}

	public CustomGrid() {
		super();
		setHeightByRows(true);
	}

	@Override
	public void setItems(Collection<T> items) {
		super.setItems(items);
		if (adjustHeightDynamically) {
			setHeight((36f * (items.size() + 1)) + "px");
		}
	}

	public Collection<T> getItems() {
		return getListDataProvider().getItems();
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
					.setHeader(value.caption)
					.setId(key));
		builder.iconColumns.values().forEach(this::addIconColumn);
	}

	public Column addIconColumn(CustomGridBuilder.ColumnProvider<T, CustomGridBuilder.IconColumnValue> value) {
		Column<T> column = addColumn(new ComponentRenderer<>(t -> createIconColumnComponent(value.valueProvider.apply(t))))
				.setClassNameGenerator(item -> "icon-column")
				.setWidth("50px")
				.setHeader(value.caption);
		column.setId(value.id);
		return column;
	}

	private Component createIconColumnComponent(CustomGridBuilder.IconColumnValue iconColumnValue) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		Image icon = new Image();
		icon.setWidth("30px");
		icon.setHeight("30px");
		if (iconColumnValue.resource != null) {
			icon.setSrc(iconColumnValue.resource);
		} else if (iconColumnValue.path != null) {
			icon.setSrc(iconColumnValue.path);
		}
		if (iconColumnValue.iconClickAction != null) {
			icon.addClassName("cursor-hover-pointer");
			icon.addClickListener(ignored -> iconColumnValue.iconClickAction.perform(this));
		}
		layout.add(icon);
		return layout;
	}

	public void setAdjustHeightDynamically(boolean adjustHeightDynamically) {
		this.adjustHeightDynamically = adjustHeightDynamically;
	}
}
