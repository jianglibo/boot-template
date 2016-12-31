package hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.repository.BarRepository;

@Controller
@RequestMapping("/bars")
public class BarController {

  @Autowired BarRepository repository;

  @RequestMapping
  public String showUsers(Model model, Pageable pageable) {
    model.addAttribute("bars", repository.findAll(pageable));
    return "bars";
  }
}