package com.awsome.shop.product.facade.http.controller;

import com.awsome.shop.product.application.api.dto.category.CategoryDTO;
import com.awsome.shop.product.application.api.dto.category.request.*;
import com.awsome.shop.product.application.api.service.category.CategoryApplicationService;
import com.awsome.shop.product.facade.http.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Category", description = "分类管理")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryApplicationService categoryApplicationService;

    @Operation(summary = "分类列表（公开）")
    @PostMapping("/public/category/list")
    public Result<List<CategoryDTO>> list() {
        return Result.success(categoryApplicationService.listAll());
    }

    @Operation(summary = "创建分类")
    @PostMapping("/category/create")
    public Result<CategoryDTO> create(@RequestBody @Valid CreateCategoryRequest request) {
        return Result.success(categoryApplicationService.create(request));
    }

    @Operation(summary = "编辑分类")
    @PostMapping("/category/update")
    public Result<CategoryDTO> update(@RequestBody @Valid UpdateCategoryRequest request) {
        return Result.success(categoryApplicationService.update(request));
    }

    @Operation(summary = "删除分类")
    @PostMapping("/category/delete")
    public Result<Void> delete(@RequestBody @Valid DeleteCategoryRequest request) {
        categoryApplicationService.delete(request);
        return Result.success();
    }
}
