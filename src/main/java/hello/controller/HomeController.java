package hello.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@RequestMapping
	public String home(Model model, HttpServletRequest req) {
		model.addAttribute("templateName", "home");
		model.addAttribute("fragmentName", "home");
		return "index";
	}
	

	@RequestMapping(path = "/page/{template}")
	public String others(@PathVariable("template") String whichBlock, Model model) {
		model.addAttribute("templateName", whichBlock);
		model.addAttribute("fragmentName", whichBlock);
		return "index";
	}
}
