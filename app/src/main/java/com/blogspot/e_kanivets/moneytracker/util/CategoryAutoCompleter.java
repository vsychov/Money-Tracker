package com.blogspot.e_kanivets.moneytracker.util;

import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class to encapsulate category autocomplete logic.
 * Created on 3/18/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryAutoCompleter {
    private List<String> categoryList;
    private CategoryController categoryController;

    public CategoryAutoCompleter(CategoryController categoryController) {
        this.categoryController = categoryController;
        categoryList = new ArrayList<>();

        for (Category category : categoryController.readFiltered()) {
            categoryList.add(category.getName());
        }
    }

    public List<String> completeByPart(String part) {
        List<String> resultList = new ArrayList<>();

        for (String category : categoryList) {
            if (category.startsWith(part)) resultList.add(category);
        }

        return resultList;
    }

    public void removeFromAutoComplete(String category) {
        categoryList.remove(category);
        categoryController.disableCategory(category);
    }
}
