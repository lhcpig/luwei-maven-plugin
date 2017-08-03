package com.luwei.game.handler2;

import com.luwei.game.common.CommandContext;
import com.luwei.game.common.InCommandHandler;
import com.luwei.game.common.InCommand;
import com.luwei.game.proto.Client;
import org.springframework.stereotype.Component;

/**
 * cmd: $cmd$
 * comment: $comment$
 */
@Component
@InCommand(InCommands.$cmdKey$)
public class $cmdKey$Handler extends InCommandHandler<Client.$msg$> {

    @Override
    public void handle(CommandContext context, Client.$msg$ message) {

    }

}
