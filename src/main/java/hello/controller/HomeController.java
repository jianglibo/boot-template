package hello.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hello.util.SecurityUtil;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@RequestMapping
	public String home(@RequestParam(name="tplName", required=false, defaultValue="index") String tplName, @RequestParam(name="frgTpl", required=false, defaultValue="home") String frgTpl,@RequestParam(name="frgName", required=false, defaultValue = "home") String frgName,Model model, HttpServletRequest req) {
		model.addAttribute("frgTpl", frgTpl);
		model.addAttribute("frgName", "home");
		SecurityUtil.addUserToModel(model);
		return tplName;
	}
}
