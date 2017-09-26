package com.luwei.game.test.handler;

import com.luwei.game.test.common.OutCommandHandler;
import com.luwei.game.test.common.OutCommand;
import com.luwei.game.proto.Server;
import com.luwei.game.handler.OutCommands;
import org.springframework.stereotype.Component;

/**
 * cmd: $cmd$
 * comment: $comment$
 */
@OutCommand(OutCommands.$cmdKey$)
public class $cmdKey$Handler extends OutCommandHandler<Server.$msg$> {

    @Override
    public void handle(Server.$msg$ message) {

    }

}
