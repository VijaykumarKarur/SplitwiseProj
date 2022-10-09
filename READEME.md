# Splitwise - Expense Sharing Application

## Requirements
><li>Users can register and update their profiles</li>
><li>A user's profile should contain at least their name, phone number and password</li>
><li>Users can participate in expenses with other users</li>
><li>Users can participate in groups.</li>
><li>To add an expense, a user must specify either the group, or the other users involved in the expense, along with who paid what and who owes what. They must also specify a description for the expense.</li>
><li>A user can see their total owed amount.</li>
><li>A user can see a history of the expenses they're involved in</li>
><li>A user can see a history of the expenses made in a group that they're participating in</li>
><li>Users shouldn't be able to query about groups they are not a member of</li>
><li>Only the user who has created a group can add/remove members to the group</li>
><li>Users can request a settle-up. The application should show a list of transactions, which when executed will ensure that the user no longer owes or receives money from any other user. Note that this need not settle-up any other users.</li>
><li>Users can request a settle-up for any group they're participating in. The application should show a list of transactions, which when executed, will ensure that everyone participating in the group is settled up (owes a net of 0 Rs). Note that will only deal with the expenses made inside that group. Expenses outside the group need not be settled. </li>
><li>When settling a group, we should try to minimize the number of transactions that the group members should make to settle up.</li>

## Input Format
> Register -name vinsmokesanji -phone 003 -password namisswwaann</b></li>
>> User is registering with the username "vinsmokesanji", phone "003" and password as "namisswwaann"

> UpdateProfile -uid 1 -password robinchwan
>> User with id 1 is updating their profile password to "robinchwan"

> AddGroup -admin 1 -name roommates
>> User with id 1 is creating a group titled "roommates"

> AddMember -admin 1 -gid g1 -uid 2
>> User with id 1 is adding User 2 as a member of the group "roommates" (which has the id 1)

> MyTotal -uid 1
>> User with id 1 is asking to see the total amount they owe/receive.

> History -uid 1
>> User with id 1 is asking to see their history of transactions (whether added by themselves or someone
else)

> Groups -uid 1
>> User with id 1 is asking to see the groups that they're a member of

> SettleUp -uid 1
>> User with id 1 asking to see the list of transactions they should perform to settle up.

> SettleUpGroup -uid 1 -gid 1
>> User with id 1 is asking to see the list of transactions that need to be performed by members of group with id 1 to completely settle up the group.

> GroupExpense -createdBy 1 -gid 1 -paidBy iPay -amounts 1000 -split Equal -desc Wifi Bill 
>> User with id 1 is adding an expense in the group 1. 
>> User 1 paid 1000 Rs.
>> Each user of group 1 owes an equal amount (the exact amount will depend on the number of users in group 1. Say group 1 has 5 users, then the amount owed by each will be 200Rs).

> Expense -createdBy 1 -uid 2,3,4 -paidBy iPay -amounts 1000 -split Equal -desc Last night dinner
>> User 1 is adding an expense with users 2, 3 and 4.
>> User 1 paid 1000 Rs.
>> Each user owes an equal amount.

> Expense -createdBy 1 -uid 2,3 -paidBy iPay -amounts 1000 -split Percent -splits 20,30,50 -desc House rent
>> User 1 is adding an expense with users 2 and 3.
>> User 1 paid 1000 Rs.
>> User 1 owes 20% (200Rs), 2 owes 30% (300Rs) and 3 owes 50% (500Rs).

> Expense -createdBy -1 -uid 2,3,4 -paidBy iPay -amounts 1000 -split Ratio -splits 1,2,3,4 -desc Goa trip
>> User 1 is adding an expense with users 2, 3 and 4.
>> User 1 paid 1000 Rs.
>> User 1 owes 100Rs (1 part), 2 owes 200Rs (2 parts), 3 owes 300Rs (3 parts) and 4 owes 400Rs (4
parts).

> Expense -createdBy 1 -uid 2,3 -paidBy iPay -amounts 1000 -split Exact -splits 100,300,600 -desc Groceries
>> User 1 is adding an expense with users 2 and 3.
>> User 1 paid 1000 Rs.
>> User 1 owes 100Rs, 2 owes 300Rs and 3 owes 600Rs.

> Expense -createdBy 1 -uid 2,3 -piadBy MultiPay -amounts 100,300,200 -split Equal -desc Lunch at office
>> User 1 is adding an expense with users 2 and 3.
>> User 1 paid 100 Rs, 2 paid 300Rs and 3 paid 200Rs.
>> Each user owes an equal amount of 200Rs.

> Expense -createdBy 1 -uid 2,3 -paidBy MultiPay -amounts 500,300,200 -split Percent -splits 20,30,50 -desc Netflix subscription
>> User 1 is adding an expense with users 2 and 3.
>> User 1 paid 500 Rs, 2 paid 300Rs and 3 paid 200Rs.
>> User 1 owes 20% (200Rs), 2 owes 30% (300Rs) and 3 owes 50% (500Rs).

## Additional Information
> Register 
>> -name parameter is used to specify the name of User.</br>
>> -phone parameter is used to specify the phone number of User.</br>
>> -password parameter is used to specify the password.</br>

> UpdateProfile
>> -uid parameter is mandatory specifying the User Id whose profile needs to be updated.</br>
>> -name parameter is used to specify the name of User.</br>
>> -phone parameter is used to specify the phone number of User.</br>
>> -password parameter is used to specify the password.</br>

> AddGroup
>> -admin parameter is used to specify User Id creating the group.</br>
>> -name parameter is used to specify the name of the group being created.</br>

> AddMember
>> -admin parameter is used to specify admin User Id of the group.</br>
>> -gid parameter is used to specify the Group Id.</br>
>> -uid parameter is used to specify the User Id of member being added.</br>

> MyTotal
>> -uid parameter is used to specify the User Id.</br>

> History
>> -uid parameter is used to specify the User Id.</br>

> Groups
>> -uid parameter is used to specify the User Id.</br>

> SettleUp
>> -uid parameter is used to specify the User Id.</br>

> SettleUpGroup
>> -uid parameter is used to specify the User Id.</br>
>> -gid parameter is used to specify the Group Id.</br>

> Expense
>> -createdBy parameter is used to specify the User Id creating the expense record.</br>
>> -uid parameter is used to specify User Id of user(s) part of the expense separated by comma.</br>
>> -paidBy parameter with value iPAY or MultiPay is used to specify if the amount was paid by Self or Multiple Members part of the expense.</br>
>> -amounts parameter is used to specify amount(s) paid by self or multiple members separated by comma.</br>
>> -split parameter with value EXACT, EQUAL, PERCENT or RATIO  specify the way amount needs to be split among the members.</br>
>> -splits parameter specify the values separated by comma used to determine the amount owed by users based on the split strategy.</br>
>> -desc parameter is used to provide description of the expense.</br>

> GroupExpense
>> -createdBy parameter is used to specify the User Id creating the expense record.</br>
>> -gid parameter is used to specify the Group Id whose members would be part of the expense.</br>
>> -paidBy parameter with value iPAY or MultiPay is used to specify if the amount was paid by Self or Multiple Members part of the expense.</br>
>> -amounts parameter is used to specify amount(s) paid by self or multiple members separated by comma. The amounts are mapped to Users based on ascending order of their User Id.</br>
>> -split parameter with value EXACT, EQUAL, PERCENT or RATIO  specify the way amount needs to be split among the members.</br>
>> -splits parameter specify the values separated by comma used to determine the amount owed by users based on the split strategy.</br>
>> -desc parameter is used to provide description of the expense.</br>
