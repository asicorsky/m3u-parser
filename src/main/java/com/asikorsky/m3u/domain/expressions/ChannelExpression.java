package com.asikorsky.m3u.domain.expressions;

import com.asikorsky.m3u.domain.entity.QChannelEntity;
import com.asikorsky.m3u.dto.ChannelFilter;
import com.asikorsky.m3u.dto.ChannelStatus;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChannelExpression {

	private final QChannelEntity meta;
	private final ChannelFilter filter;

	public ChannelExpression(ChannelFilter filter) {

		meta = QChannelEntity.channelEntity;
		this.filter = filter;
	}

	public Predicate predicate() {

		var predicates = new ArrayList<BooleanExpression>();
		String name = filter.getName();
		if (Objects.nonNull(name) && !name.isBlank()) {
			BooleanExpression predicate = meta.name.containsIgnoreCase(name);
			predicates.add(predicate);
		}
		var playlistIds = filter.getPlaylistIds();
		if (Objects.nonNull(playlistIds) && !playlistIds.isEmpty()) {
			BooleanExpression predicate = meta.categories.any().playlist.id.in(playlistIds);
			predicates.add(predicate);
		}

		var categoryIds = filter.getCategoryIds();
		if (Objects.nonNull(categoryIds) && !categoryIds.isEmpty()) {
			BooleanExpression predicate = meta.categories.any().id.in(categoryIds);
			predicates.add(predicate);
		}

		var statuses = filter.getStatuses();
		if (Objects.nonNull(statuses) && !statuses.isEmpty()) {
			var texts = statuses.stream().map(ChannelStatus::text).collect(Collectors.toList());
			BooleanExpression predicate = meta.status.in(texts);
			predicates.add(predicate);
		}

		BooleanExpression expression = Expressions.asBoolean(true).isTrue();
		for (BooleanExpression predicate : predicates) {
			expression = expression.and(predicate);
		}
		return expression;
	}

	public OrderSpecifier<?>[] orders() {

		return new OrderSpecifier<?>[] {new OrderSpecifier<>(Order.ASC, meta.name)};
	}
}
