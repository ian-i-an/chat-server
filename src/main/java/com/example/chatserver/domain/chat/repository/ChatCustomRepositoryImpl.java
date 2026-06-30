package com.example.chatserver.domain.chat.repository;

import com.example.chatserver.domain.chat.Chat;
import com.example.chatserver.domain.chat.QChat;
import com.example.chatserver.domain.chat.dto.request.ChatCursorCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


import static com.example.chatserver.domain.chat.QChat.chat;

@Repository
@RequiredArgsConstructor
public class ChatCustomRepositoryImpl implements ChatCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Chat> getChatsByCursor(Long chatRoomId, ChatCursorCondition chatCursorCondition) {
        QChat replyToChat = new QChat("replyToChat");

        return queryFactory
                .select(chat)
                .from(chat)
                .leftJoin(chat.replyTo, replyToChat).fetchJoin()
                .where(
                        chat.room.id.eq(chatRoomId),
                        cursorCondition(chatCursorCondition.cursor())
                )
                .orderBy(chat.id.desc())
                .limit(chatCursorCondition.limit() + 1).fetch();
    }

    private BooleanExpression cursorCondition(Long cursor) {
        if (cursor == null)
            return null;
        return chat.id.lt(cursor);
    }
}
