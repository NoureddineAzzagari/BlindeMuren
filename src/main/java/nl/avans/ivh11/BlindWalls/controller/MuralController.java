package nl.avans.ivh11.BlindWalls.controller;

import nl.avans.ivh11.BlindWalls.crosscutting.MyExecutionTime;
import nl.avans.ivh11.BlindWalls.domain.mural.Mural;
import nl.avans.ivh11.BlindWalls.domain.mural.SearchStrategy;
import nl.avans.ivh11.BlindWalls.repository.MuralRepository;
import nl.avans.ivh11.BlindWalls.services.interfaces.IMuralService;
import nl.avans.ivh11.BlindWalls.viewModel.MuralViewModel;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@CrossOrigin
@RequestMapping(value = "/mural")
public class MuralController {

    private final Logger logger = LoggerFactory.getLogger(MuralController.class);
    private ArrayList<Mural> murals = new ArrayList<>();

    // Views constants
    private final String VIEW_LIST_MURALS = "views/mural/list";
    private final String VIEW_CREATE_MURAL = "views/mural/create";
    private final String VIEW_READ_MURAL = "views/mural/read";
    private final String VIEW_EDIT_MURAL = "views/mural/edit";

    @Autowired
    private final MuralRepository muralRepository;
    private final IMuralService muralService;

    // Constructor with Dependency Injection
    public MuralController(IMuralService muralService, MuralRepository muralRepository) {
        this.muralService = muralService;
        this.muralRepository = muralRepository;
        getMuralsFromDB();
    }

    @MyExecutionTime
    @GetMapping
    public String listMurals(
            @RequestParam(value="category", required=false, defaultValue="all") String category,
            @RequestParam(value="size", required=false, defaultValue="10") String size,
            Model model) {

        logger.debug("listMurals called.");
        Iterable<Mural> murals = muralRepository.findAll();

        model.addAttribute("category", category);
        model.addAttribute("size", size);
        model.addAttribute("murals", murals);
        return VIEW_LIST_MURALS;
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Mural mural) {
        return new ModelAndView(VIEW_READ_MURAL, "mural", mural);
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    public String showCreateMuralForm(final Mural mural, final ModelMap model) {
        logger.debug("showCreateMuralForm");
        return VIEW_CREATE_MURAL;
    }

    @RequestMapping(value="/new", method = RequestMethod.POST)
    public ModelAndView validateAndSaveMural(
            @Valid Mural mural,
            final BindingResult bindingResult,
            RedirectAttributes redirect) {

        Mural.MuralBuilder muralBuilder = new Mural.MuralBuilder(mural.getName(), mural.getDescription());
        if (mural.getArtistName() != null) {
            muralBuilder.artistName(mural.getArtistName());
        }

        Mural m = muralBuilder.build();
        muralService.addMural(m);

        logger.debug("validateAndSaveMural - adding mural " + mural.getName());
        if (bindingResult.hasErrors()) {
            logger.debug("validateAndSaveMural - not added, bindingResult.hasErrors");
            return new ModelAndView(VIEW_CREATE_MURAL, "formErrors", bindingResult.getAllErrors());
        }

        Iterable<Mural>  murals = muralService.getAllMurals();
        return new ModelAndView(VIEW_LIST_MURALS, "murals", murals);



//
//        //
//        // ToDo: volgende acties naar de servicelaag verplaatsen.
//        //
//
//        mural = this.muralRepository.save(mural);
//
////        redirect.addFlashAttribute("globalMessage", "Successfully created a new message");
////        return new ModelAndView("redirect:/mural/{mural.id}", "mural.id", mural.getId());
//
//        murals = (ArrayList<Mural>) this.muralRepository.findAll();
//        return new ModelAndView(VIEW_LIST_MURALS, "murals", murals);
    }

    @GetMapping(value = "{id}/edit")
    public ModelAndView editMuralForm(@PathVariable("id") Mural mural) {
        logger.debug("edit" + mural.getName() + mural.getDescription());

        return new ModelAndView(VIEW_EDIT_MURAL, "mural", mural);
    }

    @RequestMapping(value="/edit", method = RequestMethod.POST)
    public ModelAndView validateAndEditMural(
            @Valid Mural mural,
            final BindingResult bindingResult,
            RedirectAttributes redirect) {
        Mural.MuralBuilder muralBuilder = new Mural.MuralBuilder(mural.getName(), mural.getDescription());
        if (mural.getArtistName() != null) {
            muralBuilder.artistName(mural.getArtistName());
        }

        Mural existingMural = muralService.getMural(mural.getId());
        existingMural.setArtistName(mural.getArtistName());
        existingMural.setDescription(mural.getDescription());
        existingMural.setName(mural.getName());
        muralService.saveEditedMural(existingMural);

        logger.debug("validateAndSaveMural - adding mural " + mural.getName());
        if (bindingResult.hasErrors()) {
            logger.debug("validateAndSaveMural - not added, bindingResult.hasErrors");
            return new ModelAndView(VIEW_CREATE_MURAL, "formErrors", bindingResult.getAllErrors());
        }

        Iterable<Mural>  murals = muralService.getAllMurals();
        return new ModelAndView(VIEW_LIST_MURALS, "murals", murals);
    }

    // ToDo: Dit moet uitgewerkt worden
    @RequestMapping(value = "mural/search")
    public ModelAndView searchMural(@Valid Mural mural) {

        String name = mural.getName();


        return null;
    }

    @RequestMapping("")
    public MuralViewModel getMuralsFromDB() {
        if (!muralService.getMuralsFromDB()) {
            try {
                muralService.getAllMuralsFromAPI();
            } catch(JSONException e) {

            }
        }
        return null;
    }

}
