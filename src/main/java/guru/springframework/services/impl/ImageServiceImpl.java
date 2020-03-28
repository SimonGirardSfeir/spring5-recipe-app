package guru.springframework.services.impl;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(long recipeId, MultipartFile file) {

        Recipe recipe = recipeRepository.findById(recipeId).get();

        try {
            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;

            for(byte b: file.getBytes()) {
                byteObjects[i++] = b ;
            }

            recipe.setImage(byteObjects);

            recipeRepository.save(recipe);
        } catch (IOException e) {
            log.error("Error occured", e);

            e.printStackTrace();
        }

        log.debug("Received a File");
    }
}
