package nl.avans.ivh11.BlindWalls.controller;

import nl.avans.ivh11.BlindWalls.domain.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    Statistics s = Statistics.getInstance();

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final String VIEW_HOME_STATS = "views/home/stats";

    @RequestMapping("/")
    public String index(Model model) {
        logger.info("index method was called.");

        model.addAttribute("title", "Hier de titel");
        logger.debug("returning views/home/index.");
        return "views/home/index";
    }

    @RequestMapping("/stats")
    public ModelAndView stats()
    {

        return new ModelAndView(VIEW_HOME_STATS, "stats", s);
    }

}