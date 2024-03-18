package com.jeno.fantasyleague.ui.common.grid;

import com.google.common.collect.Sets;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CustomGrid<T> extends Grid<T> {

	private CustomGridBuilder<T> builder;
	private boolean adjustHeightDynamically = true;

	public CustomGrid(CustomGridBuilder<T> builder) {
		this();
		this.builder = builder;

		setDataProvider(builder.dataProvider);
		buildColumns(builder);
		if (builder.columnOrder.length > 0) {
			List<Column<T>> columns = Arrays.stream(builder.columnOrder)
					.map(this::getColumnByKey)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			columns.addAll(getColumns().stream()
					.filter(c -> !Sets.newHashSet(builder.columnOrder).contains(c.getKey()))
					.collect(Collectors.toList()));
			setColumnOrder(columns);
		}
	}

	public CustomGrid() {
		super();
		setHeightByRows(true);
		addThemeVariants(
				GridVariant.LUMO_NO_BORDER,
				GridVariant.LUMO_NO_ROW_BORDERS,
				GridVariant.LUMO_ROW_STRIPES);

	}

	@Override
	protected <C extends Column<T>> C addColumn(Renderer<T> renderer, BiFunction<Renderer<T>, String, C> columnFactory) {
		return super.addColumn(renderer, columnFactory);
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
					.setKey(key)
					.setId(key));
		builder.iconColumns.values().forEach(this::addIconColumn);
	}

	public Column addIconColumn(CustomGridBuilder.ColumnProvider<T, CustomGridBuilder.IconColumnValue> value) {
		Column<T> column = addColumn(new ComponentRenderer<>(t -> createIconColumnComponent(value.valueProvider.apply(t))))
				.setClassNameGenerator(item -> "icon-column")
				.setFlexGrow(0)
				.setHeader(value.caption);
		column.setId(value.id);
		column.setKey(value.id);
		return column;
	}

	private Component createIconColumnComponent(CustomGridBuilder.IconColumnValue iconColumnValue) {
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
		return icon;
	}

	public void setAdjustHeightDynamically(boolean adjustHeightDynamically) {
		this.adjustHeightDynamically = adjustHeightDynamically;
	}
}
