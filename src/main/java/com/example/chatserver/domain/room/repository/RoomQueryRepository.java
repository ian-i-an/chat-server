package com.example.chatserver.domain.room.repository;

import com.example.chatserver.domain.chat.QChat;
import com.example.chatserver.domain.room.dto.RoomListItem;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

import static com.example.chatserver.domain.chat.QChat.chat;
import static com.example.chatserver.domain.readstatus.QReadStatus.readStatus;
import static com.example.chatserver.domain.room.QRoom.room;


@Repository
@RequiredArgsConstructor
public class RoomQueryRepository {

    private final JPAQueryFactory queryFactory;


    public List<RoomListItem> getRoomsByUserId(Long userId) {
        QChat subChat = new QChat("subChat");

        JPQLQuery<String> lastMessageQuery = JPAExpressions
                .select(chat.content)
                .from(chat)
                .where(chat.id.eq(
                        JPAExpressions.select(subChat.id.max())
                                .from(subChat)
                                .where(subChat.room.eq(room))
                ));


        JPQLQuery<Long> unreadCountQuery = JPAExpressions
                .select(chat.count())
                .from(chat)
                .where(chat.room.eq(room)
                        .and(chat.id.gt(readStatus.lastReadChatId)));


        JPQLQuery<Instant> sortTimeQuery = JPAExpressions
                .select(chat.createdAt.max().coalesce(room.createdAt))
                .from(chat)
                .where(chat.room.eq(room));


        return queryFactory
                .select(Projections.constructor(RoomListItem.class,
                        room.id,
                        room.name,
                        lastMessageQuery,
                        unreadCountQuery
                ))
                .from(readStatus)
                .join(readStatus.room, room)
                .where(readStatus.user.id.eq(userId))
                .orderBy(new OrderSpecifier<>(Order.DESC, sortTimeQuery))
                .fetch();
    }
}
