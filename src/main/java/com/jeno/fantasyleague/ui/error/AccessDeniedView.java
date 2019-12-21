package com.jeno.fantasyleague.ui.error;

import javax.servlet.http.HttpServletResponse;

import com.jeno.fantasyleague.ui.exception.AccessDeniedException;
import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.util.AppConst;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("access-denied-view")
@JsModule("./src/views/errors/access-denied-view.js")
@ParentLayout(MainView.class)
@PageTitle(AppConst.TITLE_ACCESS_DENIED)
@Route
public class AccessDeniedView extends PolymerTemplate<TemplateModel> implements HasErrorParameter<AccessDeniedException> {

	@Override
	public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}
