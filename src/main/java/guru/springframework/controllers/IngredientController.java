package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
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

    @GetMapping("recipe/{id}/ingredients")
    public String getIngredients(@PathVariable String id, Model model) {
        log.debug("Getting ingredient list for recipe id: "+id);
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/ingredient/list";
    }

    @GetMapping("recipe/{idR}/ingredient/{idI}/show")
    public String showIngredient(@PathVariable String idR, @PathVariable String idI, Model model) {
        log.debug("Show ingredient of id: " + idI);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(idR), Long.parseLong(idI)));

        return "recipe/ingredient/show";
    }

    @GetMapping("recipe/{idR}/ingredient/new")
    public String newIngredient(@PathVariable String idR, Model model) {

        //Make sure we have a good ID value
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.parseLong(idR));
        //TODO raise exception if null

        //Need to return back parent id for hidden form property
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.parseLong(idR));

        model.addAttribute("ingredient", ingredientCommand);

        //Init UOM
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUnitOfMeasures());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("recipe/{idR}/ingredient/{idI}/update")
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

    @GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {

        log.debug("Deleting ingredient id: "+ingredientId);

        ingredientService.deleteById(Long.parseLong(recipeId), Long.parseLong(ingredientId));

        return "redirect:/recipe/"+recipeId+"/ingredients";
    }
}
