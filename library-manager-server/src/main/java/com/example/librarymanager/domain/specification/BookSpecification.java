package com.example.librarymanager.domain.specification;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.JoinType;
import com.example.librarymanager.domain.dto.filter.Filter;
import com.example.librarymanager.domain.entity.BookDefinition;
import com.example.librarymanager.exception.BadRequestException;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.example.librarymanager.util.SpecificationsUtil.castToRequiredType;

public class BookSpecification {

    private static void validateField(String fieldName) {
        try {
            BookDefinition.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new BadRequestException(ErrorMessage.BookDefinition.ERR_INVALID_FIELD, fieldName);
        }
    }

    public static Specification<BookDefinition> createSpecification(Filter input) {
        validateField(input.getField());

        return switch (input.getOperator()) {

            case EQUALS -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(input.getField()),
                            castToRequiredType(root.get(input.getField()).getJavaType(),
                                    input.getValue()));

            case NOT_EQUALS -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.notEqual(root.get(input.getField()),
                            castToRequiredType(root.get(input.getField()).getJavaType(),
                                    input.getValue()));

            case GREATER_THAN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.gt(root.get(input.getField()),
                            (Number) castToRequiredType(
                                    root.get(input.getField()).getJavaType(),
                                    input.getValue()));

            case LESS_THAN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lt(root.get(input.getField()),
                            (Number) castToRequiredType(
                                    root.get(input.getField()).getJavaType(),
                                    input.getValue()));

            case LIKE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(input.getField()),
                            "%" + input.getValue() + "%");

            case IN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.in(root.get(input.getField()))
                            .value(castToRequiredType(
                                    root.get(input.getField()).getJavaType(),
                                    input.getValues()));
        };
    }

    public static Specification<BookDefinition> getSpecificationFromFilters(List<Filter> filters) {
        if (filters.isEmpty()) {
            return null;
        }

        Specification<BookDefinition> specification = Specification.where(createSpecification(filters.get(0)));

        for (int i = 1; i < filters.size(); i++) {
            Filter filter = filters.get(i);
            Specification<BookDefinition> nextSpecification = createSpecification(filter);

            if (filter.getJoinType() == JoinType.OR) {
                specification = specification.or(nextSpecification);
            } else {
                specification = specification.and(nextSpecification);
            }
        }
        return specification;
    }

}
