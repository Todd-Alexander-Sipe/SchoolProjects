----------------------------HOW TO USE THE PROGRAM-----------------------------
There are three entry points for the three different programs. For ideal
execution, open them in the following order:

BankStart.java, AuctionHouseStart.java, and AgentGUI.java.

For BankStart the commandline argument should be:
1: Bank port number

For AuctionHouseStart the commandline arguments should be:
1: Bank host name
2: Bank port number
3: AuctionHouse host name (its own name)
4: AuctionHouse port number

For AgentGUI the commandline arguments should be:
1: Bank host name
2: Bank port number

Every AgentGUI and AuctionHouseStart is intended to be run on a different
computer, and multiple instances of both should be able to connect to the bank.
All Agents should also be able to connect to each auction house
(one at a time).

Once these are run, the only interactions that the user will have will be with
the GUI brought up through AgentGUI. From here you can navigate the GUI to 
connect with auction houses and make bids on items.

AGENT GUI INSTRUCTIONS:
1. Labels display information that is needed throughout the GUI.
2. Select an auction house that has an active connection with the bank from
   the drop down menu with prompt text "Auction House".
3. Click "Connect to auction house" button.
4. Select an item from the drop down menu with prompt text "Items for auction"
5. Click "Bid" button.
6. If you have enough funds, you will bid on the item.
7. Another Agent may outbid you for 25 more than the current cost.
8. You may disconnect from the auction house by clicking "Dicsonnect from 
   auction house" button.
9. When finished click "Exit" button.

--------------------------WHO DID WHAT-----------------------------------------
The project was initially set up in lobogit by Anthony Sharma.
Anthony worked on the set-up of the connections from client to server for most
aspects of the project.
Manny worked on fleshing out a lot of the details for the communications
between clients and servers, alongside Anthony.
Todd worked primarily on the GUI for the Agent.
These three also fulfilled various roles in helping each other with whatever
needed to be currently fleshed out. Many hands in many honey pots. It got a
little sticky here and there.

-------------------------KNOWN ISSUES------------------------------------------
Worked out all known bugs. Testing with jars on remote workspaces commencing as
of 5/9/2021 10:00PM