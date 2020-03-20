package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("recipe/{id}/ingredients")
    public String getIngredients(@PathVariable String id, Model model) {
        log.debug("Getting ingredient list for recipe id: "+id);
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("recipe/{idR}/ingredient/{idI}/show")
    public String showIngredient(@PathVariable String idR, @PathVariable String idI, Model model) {
        log.debug("Show ingredient of id: " + idI);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(idR), Long.parseLong(idI)));

        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("recipe/{idR}/ingredient/{idI}/update")
    public String updateRecipeIngredient(@PathVariable String idR, @PathVariable String idI, Model model) {

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(idR), Long.parseLong(idI)));

        model.addAttribute("unitOfMeasureList",unitOfMeasureService.listAllUnitOfMeasures());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {

        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("Saved Recipe id: "+ savedCommand.getRecipeId());
        log.debug("Saved Ingredient id: " + savedCommand.getId());

        return "redirect:/recipe/"+ savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }
}
