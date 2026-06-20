package com.example.chatserver.domain.chatroom.repository;

import com.example.chatserver.domain.chat.QChat;
import com.example.chatserver.domain.chatroom.dto.ChatRoomListItem;
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
import static com.example.chatserver.domain.chatroom.QChatRoom.chatRoom;
import static com.example.chatserver.domain.readstatus.QReadStatus.readStatus;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;


    public List<ChatRoomListItem> getChatRoomsByUserId(Long userId) {
        QChat subChat = new QChat("subChat");

        JPQLQuery<String> lastMessageQuery = JPAExpressions
                .select(chat.content)
                .from(chat)
                .where(chat.id.eq(
                        JPAExpressions.select(subChat.id.max())
                                .from(subChat)
                                .where(subChat.chatRoom.eq(chatRoom))
                ));


        JPQLQuery<Long> unreadCountQuery = JPAExpressions
                .select(chat.count())
                .from(chat)
                .where(chat.chatRoom.eq(chatRoom)
                        .and(chat.id.gt(readStatus.lastReadChatId)));


        JPQLQuery<Instant> sortTimeQuery = JPAExpressions
                .select(chat.createdAt.max().coalesce(chatRoom.createdAt))
                .from(chat)
                .where(chat.chatRoom.eq(chatRoom));


        return queryFactory
                .select(Projections.constructor(ChatRoomListItem.class,
                        chatRoom.id,
                        chatRoom.name,
                        lastMessageQuery,
                        unreadCountQuery
                ))
                .from(readStatus)
                .join(readStatus.chatRoom, chatRoom)
                .where(readStatus.user.id.eq(userId))
                .orderBy(new OrderSpecifier<>(Order.DESC, sortTimeQuery))
                .fetch();
    }
}
