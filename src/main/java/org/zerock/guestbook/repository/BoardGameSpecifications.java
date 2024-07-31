package org.zerock.guestbook.repository;

import org.springframework.data.jpa.domain.Specification;
import org.zerock.guestbook.entity.BoardGame;

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

            // 모든 태그를 포함하는 레코드를 찾기 위한 조건 생성
            Specification<BoardGame> spec = (root1, query1, builder1) -> builder1.conjunction();
            for (String tag : tags) {
                Specification<BoardGame> currentTagSpec = (root1, query1, builder1) ->
                        builder1.like(builder1.lower(root1.get("tag")), "%" + tag.toLowerCase() + "%");
                spec = spec.and(currentTagSpec); // AND 조건 추가
            }
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<BoardGame> sortBy(String sortOrder) {
        return (root, query, builder) -> {
            if (sortOrder.equals("scoreDesc")) {
                query.orderBy(builder.desc(root.get("scoreSum")));
            } else if (sortOrder.equals("scoreAsc")) {
                query.orderBy(builder.asc(root.get("scoreSum")));
            } else if (sortOrder.equals("dateAsc")) {
                query.orderBy(builder.asc(root.get("date")));
            } else {
                query.orderBy(builder.desc(root.get("date")));
            }
            return builder.conjunction(); // Sorting은 쿼리에서 직접 처리
        };
    }
}
