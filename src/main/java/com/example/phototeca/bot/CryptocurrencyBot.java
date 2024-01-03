package com.example.phototeca.bot;

import com.example.phototeca.constants.Constants;
import com.example.phototeca.facade.subscriber.SubscriberFacade;
import com.example.phototeca.service.bot.SubscriberResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class CryptocurrencyBot extends AbilityBot {

    private final SubscriberResponseHandler subscriberResponseHandler;

    @Autowired
    public CryptocurrencyBot(Environment env, SubscriberFacade subscriberFacade) {
        super(env.getProperty("botToken"), "cryptocurrencyBot");
        subscriberResponseHandler = new SubscriberResponseHandler(silent, subscriberFacade, db);
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> subscriberResponseHandler.replyToStart(ctx.chatId()))
                .build();
    }

    public Reply replyToMessage() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) ->
                subscriberResponseHandler.replyToMessage(getChatId(upd), upd.getMessage());
        return Reply.of(action, Flag.TEXT, upd -> subscriberResponseHandler.userIsActive(getChatId(upd)));
    }

}
