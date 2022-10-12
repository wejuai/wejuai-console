package com.wejuai.console.service;

import com.endofmaster.commons.util.DateUtil;
import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.controller.dto.response.AppListInfo;
import com.wejuai.console.repository.mysql.ArticleRepository;
import com.wejuai.dto.response.ArticleInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mysql.Article;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ZM.Wang
 */
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Slice<AppListInfo> articles(String id, String userId, String hobbyId, Boolean del, LocalDate start, LocalDate end, Pageable pageable) {
        if (StringUtils.isNotBlank(id)) {
            Article article = articleRepository.findById(id).orElse(null);
            List<AppListInfo> articles = article == null ? Collections.emptyList() : Collections.singletonList(new AppListInfo(article));
            return new Slice<>(articles, 0, 10, article == null ? 0 : 1);
        }
        Specification<Article> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (StringUtils.isNotBlank(hobbyId)) {
                predicates.add(cb.equal(root.get("hobby").get("id"), hobbyId));
            }
            if (del != null) {
                predicates.add(cb.equal(root.get("del"), del));
                predicates.add(cb.equal(root.get("authorDel"), del));
            }
            if (start != null) {
                predicates.add(cb.greaterThan(root.get("createdAt"), DateUtil.getAnyDayStart(start)));
            }
            if (end != null) {
                predicates.add(cb.lessThan(root.get("createdAt"), DateUtil.getAnyDayEnd(end)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<AppListInfo> appListInfos = articleRepository.findAll(specification, pageable).map(AppListInfo::new);
        return new Slice<>(appListInfos);
    }

    public ArticleInfo getArticle(String id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该文章: " + id));
        return new ArticleInfo(article, true);
    }

}
