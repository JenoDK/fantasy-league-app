package com.jeno.fantasyleague.ui.error;

import javax.servlet.http.HttpServletResponse;

import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.util.AppConst;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.router.RouterLink;

@ParentLayout(MainView.class)
@PageTitle(AppConst.TITLE_NOT_FOUND)
@JsModule("./styles/shared-styles.js")
public class CustomRouteNotFoundError extends RouteNotFoundError {

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
