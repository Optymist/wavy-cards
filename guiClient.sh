#! bin/bash

mvn clean compile exec:java -Dexec.mainClass=blackjack.Client.Gui.GuiClient

