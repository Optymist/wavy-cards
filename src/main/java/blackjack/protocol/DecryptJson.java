// todo
// takes json string and crafts objects from it 
// action objects 

package blackjack.protocol;

import blackjack.actions.BlackJackAction;
import blackjack.player.Player;
import blackjack.protocol.Exceptions.InvalidAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * DecryptJson
 */
public class DecryptJson {

    /**
     * This method takes a client's turnResponse and gets the chosen action.
     *
     * @param turnResponse The response from the client
     * @param player       The player linked to the client.
     * @return The Chosen BlackJackAction.
     * @throws InvalidAction If a non-valid action was chosen.
     */
    public static BlackJackAction getChosenAction(
            String turnResponse, Player player) throws InvalidAction {
        // player is parsed in so that we can call `performAction` to validate 
        // the action, should probably be renamed to something like `validateAction`

        return null;
    }

}
