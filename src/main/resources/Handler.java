package com.luwei.game.handler;

import com.luwei.game.common.InCommandHandler;
import com.luwei.game.common.InCommand;
import com.luwei.game.proto.Client;
import com.luwei.game.service.user.User;
import org.springframework.stereotype.Component;

/**
 * cmd: $cmd$
 * comment: $comment$
 */
@Component
@InCommand(InCommands.$cmdKey$)
public class $cmdKey$Handler extends InCommandHandler<Client.$msg$> {

    @Override
    public void handle(User user, Client.$msg$ message) {

    }

}
