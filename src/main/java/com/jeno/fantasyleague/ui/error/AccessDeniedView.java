package com.jeno.fantasyleague.ui.error;

import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.jeno.fantasyleague.ui.exception.AccessDeniedException;
import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.util.AppConst;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;

import javax.servlet.http.HttpServletResponse;

@Tag("access-denied-view")
@JsModule("./src/views/errors/access-denied-view.js")
@ParentLayout(MainView.class)
@PageTitle(AppConst.TITLE_ACCESS_DENIED)
@Route("accessDenied")
@AlwaysAllow
public class AccessDeniedView extends PolymerTemplate<TemplateModel> implements HasErrorParameter<AccessDeniedException>, RouterLayout {

	@Override
	public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}
