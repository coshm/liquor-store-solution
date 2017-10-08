package com.learning.liquorstore.phasetwo.commandhandler;

public interface CommandHandler {

    String BACK_COMMAND = "back";

    /**
     * Handle the command chosen by the user.
     * @return success of the operation.
     */
    boolean handleCommand();

}
