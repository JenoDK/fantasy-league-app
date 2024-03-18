package com.jeno.fantasyleague.ui.error;

import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.jeno.fantasyleague.util.AppConst;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@ParentLayout(MainView.class)
@PageTitle(AppConst.TITLE_NOT_FOUND)
@Route(value = State.StateUrlConstants.PAGE_NOT_FOUND, layout = MainView.class)
@AlwaysAllow
public class CustomRouteNotFoundError extends RouteNotFoundError implements RouterLayout {

	public CustomRouteNotFoundError() {
		RouterLink link = Component.from(
				ElementFactory.createRouterLink("", "Go to the front page."),
				RouterLink.class);
		getElement().appendChild(new Text("Oops you hit a 404. ").getElement(), link.getElement());
	}

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
