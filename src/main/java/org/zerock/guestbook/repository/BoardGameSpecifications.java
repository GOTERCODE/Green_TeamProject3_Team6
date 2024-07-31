package org.zerock.guestbook.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.zerock.guestbook.entity.BoardGame;

import java.util.ArrayList;
import java.util.List;

public class BoardGameSpecifications {

    public static Specification<BoardGame> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return builder.conjunction(); // 모든 레코드 반환
            }
            return builder.like(builder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<BoardGame> hasTags(String[] tags) {
        return (root, query, builder) -> {
            if (tags == null || tags.length == 0) {
                return builder.conjunction(); // 모든 레코드 반환
            }

            List<Predicate> tagPredicates = new ArrayList<>();
            for (String tag : tags) {
                tagPredicates.add(builder.like(builder.lower(root.get("tag")), "%" + tag.toLowerCase() + "%"));
            }

            // 모든 태그가 포함된 레코드를 찾기 위한 조건 생성
            return builder.and(tagPredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<BoardGame> sortBy(String sortOrder) {
        return (root, query, builder) -> {
            if (sortOrder == null || sortOrder.isEmpty()) {
                return builder.conjunction(); // 기본값
            }

            query.orderBy(getSortOrders(sortOrder, root, builder));
            return query.getRestriction(); // Sort가 적용되도록
        };
    }

    private static List<Order> getSortOrders(String sortOrder, Root<BoardGame> root, CriteriaBuilder builder) {
        switch (sortOrder) {
            case "scoreDesc":
                return List.of(builder.desc(root.get("score")));
            case "scoreAsc":
                return List.of(builder.asc(root.get("score")));
            case "dateAsc":
                return List.of(builder.asc(root.get("date")));
            case "dateDesc":
            default:
                return List.of(builder.desc(root.get("date")));
        }
    }
}
