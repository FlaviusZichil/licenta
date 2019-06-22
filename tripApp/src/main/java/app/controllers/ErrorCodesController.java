package app.controllers;

import java.security.Principal;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorCodesController implements ErrorController{

	@RequestMapping("/error")
	public String renderErrorPage(Model model, HttpServletRequest httpRequest, Principal principal, HttpSession session) {		

		Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		Integer statusCode = Integer.valueOf(status.toString());

		if (statusCode == HttpStatus.NOT_FOUND.value()){
			return "views/all/404ErrorPage";
		}
		if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
			return "views/all/500ErrorPage";
		}
		if (statusCode == HttpStatus.FORBIDDEN.value()){
			return "views/all/403ErrorPage";
		}
		return "views/all/500ErrorPage";
	}

	@Override
	public String getErrorPath() {
		return "views/all/500ErrorPage";
	}
}
