package com.jeno.fantasyleague.ui.main.views.admin;

import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Tag("admin-view")
@Route(value = State.StateUrlConstants.ADMIN, layout = MainView.class)
@Secured("ROLE_ADMIN")
public class AdminModule extends VerticalLayout implements RouterLayout {

	private AdminPresenter presenter;

	public AdminModule(@Autowired AdminPresenter presenter) {
		this.presenter = presenter;
		presenter.setupModule(this);
	}

}
