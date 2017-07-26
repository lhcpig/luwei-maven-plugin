package com.luwei.game.test.handler;

import com.luwei.game.handler.common.OutCommandHandler;
import com.luwei.game.handler.common.OutCommand;
import com.luwei.game.proto.Server;
import com.luwei.game.handler2.OutCommands;
import org.springframework.stereotype.Component;

/**
 * cmd: $cmd$
 * comment: $comment$
 */
@Component
@OutCommand(OutCommands.$cmdKey$)
public class $cmdKey$Handler extends OutCommandHandler<Server.$msg$> {

    @Override
    public void handle(Server.$msg$ message) {

    }

}
