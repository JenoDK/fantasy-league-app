package com.jeno.fantasyleague.ui.common.grid;

import com.google.common.collect.Maps;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.Resource;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

public class CustomGridBuilder<T> {

	protected final DataProvider<T, ?> dataProvider;
	protected final Function<T, ?> keyExtractor;
	protected Map<String, ColumnProvider<T, String>> textColumns = Maps.newHashMap();
	protected Map<String, ColumnProvider<T, IconColumnValue>> iconColumns = Maps.newHashMap();
	protected String[] columnOrder = new String[0];

	public CustomGridBuilder(DataProvider<T, ?> dataProvider, Function<T, ?> keyExtractor) {
		this.dataProvider = dataProvider;
		this.keyExtractor = keyExtractor;
	}

	public CustomGridBuilder withColumnOrder(String...columnIds) {
		this.columnOrder = columnIds;
		return this;
	}

	public CustomGridBuilder withTextColumn(ColumnProvider<T, String> columnProvider) {
		this.textColumns.put(columnProvider.id, columnProvider);
		return this;
	}

	public CustomGridBuilder withIconColumn(ColumnProvider<T, IconColumnValue> columnProvider) {
		this.iconColumns.put(columnProvider.id, columnProvider);
		return this;
	}

	public CustomGrid<T> build() {
		return new CustomGrid<>(this);
	}

	public interface GridAction {

		void perform(CustomGrid grid);

	}

	public static class IconColumnValue {

		protected final Resource resource;
		protected GridAction iconClickAction;

		public IconColumnValue(Resource resource, GridAction iconClickAction) {
			this.resource = resource;
			this.iconClickAction = iconClickAction;
		}

		public IconColumnValue(Resource resource) {
			this.resource = resource;
		}
	}

	public static class ColumnProvider<T, V> {

		protected final String id;
		protected final ValueProvider<T, V> valueProvider;
		protected final String caption;

		public ColumnProvider(String id, ValueProvider<T, V> valueProvider, String caption) {
			this.id = id;
			this.valueProvider = valueProvider;
			this.caption = caption;
		}

	}
}
