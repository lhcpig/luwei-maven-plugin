package com.luwei.game.handler.test;

import com.luwei.game.handler.common.CommandContext;
import com.luwei.game.handler.common.InCommandHandler;
import com.luwei.game.handler.common.InCommand;
import com.luwei.game.proto.Client;
import org.springframework.stereotype.Component;

/**
 * cmd: $cmd$
 * comment: $comment$
 */
@Component
@InCommand(InCommands.$msg$)
public class $msg$Handler extends InCommandHandler<Client.$msg$> {

    @Override
    public void handle(CommandContext context, Client.$msg$ message) {

    }

}
