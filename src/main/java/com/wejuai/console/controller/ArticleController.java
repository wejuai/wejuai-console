package com.wejuai.console.controller;

import com.wejuai.console.controller.dto.request.AppRevokeRequest;
import com.wejuai.console.controller.dto.response.AppListInfo;
import com.wejuai.console.service.ArticleService;
import com.wejuai.console.support.WejuaiCoreClient;
import com.wejuai.dto.request.SaveArticleRequest;
import com.wejuai.dto.response.ArticleInfo;
import com.wejuai.dto.response.IdBaseResponse;
import com.wejuai.dto.response.Slice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author ZM.Wang
 */
@Api(tags = "文章管理")
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public ArticleController(ArticleService articleService, WejuaiCoreClient wejuaiCoreClient) {
        this.articleService = articleService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("文章列表")
    @GetMapping
    public Slice<AppListInfo> articles(@RequestParam(required = false, defaultValue = "") String id,
                                       @RequestParam(required = false, defaultValue = "") String userId,
                                       @RequestParam(required = false, defaultValue = "") String hobbyId,
                                       @RequestParam(required = false, defaultValue = "") Boolean del,
                                       @RequestParam(required = false, defaultValue = "")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                       @RequestParam(required = false, defaultValue = "")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                       @RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return articleService.articles(id, userId, hobbyId, del, start, end, pageable);
    }

    @ApiOperation("文章详情")
    @GetMapping("/{id}")
    public ArticleInfo getArticle(@PathVariable String id) {
        return articleService.getArticle(id);
    }

    @ApiOperation("删除文章")
    @DeleteMapping("/{id}")
    public void delOrRecoveryArticle(@PathVariable String id,
                                     @RequestParam String reason) {
        wejuaiCoreClient.deleteArticle(id, reason);
    }

    @ApiOperation("撤回发布文章")
    @PostMapping("/{id}/revoke")
    public void revokeArticle(@PathVariable String id, @RequestBody @Valid AppRevokeRequest request) {
        wejuaiCoreClient.revokeArticle(id, request.getReason());
    }

    @ApiOperation("发布系统文章")
    @PostMapping("/system")
    public IdBaseResponse saveArticle(@RequestParam(required = false, defaultValue = "") String id,
                                      @RequestBody @Valid SaveArticleRequest request) {
        return wejuaiCoreClient.saveArticle(id, request);
    }
}
