# Simple BlackJack Game
I just wanted to create a fun, text-based BlackJack game that you can play against the dealer in the terminal.
--> Add more here

## Rules
First 2 decks of cards are initialized and shuffled.
Then a card is dealt to you, as the player, then one to the dealer and then another to you and the dealer's second card
is kept hidden. You will not know the value of this card until you have finished the round. 
Now each card has a value assigned to it: 
- A = 1
- 2 to 10 = the number on the card
- J, Q & K = 10

The goal of the game is to get the closest to 21 as you can without busting (going over 21) and to get a higher overall 
card value than the dealer does. Each value of each card in your hand is added together to create this total card value.

So after you have looked at your cards and see what overall value you currently have, you can choose to either STAND,
HIT or SPLIT (can only be done in special cases).

### Hit:
    If you choose to hit, you will receive the next card of the deck and will have to add it to the current value of
    your hand. This can result in busting, and you will immediately lose the round.

### Stand:
    If you choose to stand, you stay with the current value of your hand, and it is compared to the dealer's final hand 
    value to determine if you win or lose the round.

### Split:
    This is a special case where, when you have 2 cards of the same value as your starting cards, you can choose to 
    separate them and play them as two separate hands. When playing with chips (a representation of money in casino
    games) you will be required to pay the same amount as what you paid to play in the beginning. After splitting, you
    will be play each hand one at a time until you bust or decide to stand. Then your remaining hand(s) will be 
    compared to the dealer's.

If your hand(s) bust(s) then you instantly lose to the dealer. If not the dealer will reveal his face down card to show
his starting value. If it is 17 or above the dealer will not draw another card and that initial value will be compared 
to your own cards' value, otherwise the dealer draws cards until he has 17 or above.

### You Lose If:
    The dealer ends up with a higher value than you have or if you bust.

### You Win If:
    The dealer ends up with a lower value than you have or if the dealer busts.

### You Push If:
    The dealer gets the same value as you (considered a draw).

### BlackJack!
    When your first 2 cards dealt are an ace and a card with the value of 10. This means that you've hit 21 on the first
    cards and is considered a BlackJack. If the dealer does not also get a BlackJack then you get paid 1.5 times your 
    bet, otherwise you and the dealer will push, and you'll get your bet back.



